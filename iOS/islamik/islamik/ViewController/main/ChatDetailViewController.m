//
//  ChatDetailsViewController.m
//  Bmbrella
//
//  Created by Vitaly's Team on 10/31/17.
//  Copyright © 2017 BrainyApps. All rights reserved.
//

#import "ChatDetailViewController.h"
#import "MessageModel.h"
#import "IQDropDownTextField.h"
#import <MobileCoreServices/MobileCoreServices.h>

#import <IQAudioRecorderController/IQAudioRecorderViewController.h>
#import <IQAudioRecorderController/IQAudioCropperViewController.h>
#import <MediaPlayer/MediaPlayer.h>

static ChatDetailViewController *_sharedViewController = nil;

@interface ChatDetailViewController ()<UIImagePickerControllerDelegate, UINavigationControllerDelegate, IQAudioRecorderViewControllerDelegate,IQAudioCropperViewControllerDelegate>
{
    PFUser *me;
    
    NSMutableArray *messages;
    JSQMessagesBubbleImage *outgoingBubbleImageData;
    JSQMessagesBubbleImage *incomingBubbleImageData;
    
    BOOL isLoading;
    
    BOOL isCamera;
    BOOL isPhoto;
}
@end

@implementation ChatDetailViewController
@synthesize toUser;

- (void)viewDidLoad {
    [super viewDidLoad];
    me = [PFUser currentUser];
    
    isCamera = NO;
    isPhoto = NO;
    
    _sharedViewController = self;
    self.inputToolbar.contentView.textView.pasteDelegate = self;
    self.inputToolbar.contentView.rightBarButtonItem.enabled = YES;
    [self.inputToolbar setBackgroundColor:[UIColor colorWithRed:14/255.f green:97/255.f blue:41/255.f alpha:1]];
    self.inputToolbar.contentView.textView.placeHolder = @"Enter Message";
    [self.inputToolbar.contentView setBackgroundColor:[UIColor colorWithRed:14/255.f green:97/255.f blue:41/255.f alpha:1]];
    self.inputToolbar.contentView.textView.textColor = [UIColor blackColor];
    self.inputToolbar.contentView.textView.tintColor = [UIColor darkGrayColor];
    [self.inputToolbar.contentView.textView setBackgroundColor:[UIColor whiteColor]];
    [self.inputToolbar.contentView.leftBarButtonContainerView setBackgroundColor:[UIColor clearColor]];
    
    JSQMessagesBubbleImageFactory *bubbleFactory = [JSQMessagesBubbleImageFactory new];
    outgoingBubbleImageData = [bubbleFactory outgoingMessagesBubbleImageWithColor:[UIColor jsq_messageBubbleBlueColor]];
    incomingBubbleImageData = [bubbleFactory incomingMessagesBubbleImageWithColor:[UIColor grayColor]];
    
    messages = [NSMutableArray new];
    isLoading = NO;
    
    /**  hide avatars **/
    //    self.collectionView.collectionViewLayout.incomingAvatarViewSize = CGSizeZero;
    //    self.collectionView.collectionViewLayout.outgoingAvatarViewSize = CGSizeZero;
    
    [self.view setBackgroundColor:[UIColor whiteColor]];
    
    self.showLoadEarlierMessagesHeader = NO;
    [JSQMessagesCollectionViewCell registerMenuAction:@selector(delete:)];
    
    /**
     *  Customize your toolbar buttons
     *
     *  self.inputToolbar.contentView.leftBarButtonItem = custom button or nil to remove
     *  self.inputToolbar.contentView.rightBarButtonItem = custom button or nil to remove
     */
    
    /** Set a maximum height for the input toolbar **/
    self.inputToolbar.maximumHeight = 150;
    self.senderId = @"";
    self.senderDisplayName = @"";
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(refreshMessage) name:kChatReceiveNotificationUsers object:nil];
    
    if (toUser){
        [self refreshUI];
    } else {
        
    }
}

- (void) viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    [AppStateManager sharedInstance].chatRoomId = @"";
    _sharedViewController = nil;
}

+ (ChatDetailViewController *)getInstance{
    return _sharedViewController;
}

- (void) setRoom:(PFObject *) room User:(PFUser *) user {
    self.toUser = user;
    self.bookObj = room;
    [self refreshUI];
}

- (void)refreshUI {
    [AppStateManager sharedInstance].chatRoomId = self.bookObj.objectId;
    self.senderId = me.objectId;
    NSString *name = @"";
    name = [NSString stringWithFormat:@"%@ %@", me[PARSE_FIRSTNAME], me[PARSE_LASTSTNAME]];
    if (name.length > 0)
        self.senderDisplayName = name;
    else
        self.senderDisplayName = me.username;
    
    [self loadMessages];
}

- (NSString *) getDisplayName:(PFUser *)user {
    NSString *result = @"";
    NSString *name = @"";
    name = [NSString stringWithFormat:@"%@ %@", user[PARSE_FIRSTNAME], user[PARSE_LASTSTNAME]];
    if (name.length > 0)
        result = name;
    else
        result = user.username;
    return result;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) refreshMessage {
    [self loadMessages];
}

- (void)loadMessages {
    if (!isLoading) {
        isLoading = true;
        MessageModel *message_last = messages.lastObject;
        
        PFQuery *query = [PFQuery queryWithClassName:PARSE_TABLE_CHAT];
        [query whereKey:PARSE_BOOK_OBJ equalTo:self.bookObj];
        if (message_last != nil) {
            [query whereKey:PARSE_FIELD_CREATED_AT greaterThan:message_last.date];
        }
        PFUser * roomSender = self.bookObj[PARSE_SENDER];
        
        [query orderByDescending:PARSE_FIELD_CREATED_AT];
        [query includeKey:PARSE_SENDER];
        [query setLimit:100];
        [query findObjectsInBackgroundWithBlock:^(NSArray * _Nullable objects, NSError * _Nullable error) {
            if (!error && objects.count > 0) {
                self.automaticallyScrollsToMostRecentMessage = NO;
                for (int i = objects.count - 1; i>=0; i--) {
                    [self addMessage:objects[i]];
                }
                [self finishReceivingMessage];
                [self scrollToBottomAnimated:NO];
                self.automaticallyScrollsToMostRecentMessage = YES;
            }
            isLoading = NO;
        }];
    }
}

- (void)addMessage:(PFObject *)object {
    PFUser *sender = object[PARSE_SENDER]; // me
    NSString *senderId = sender.objectId;
    
    if (![senderId isEqualToString:me.objectId] && ![senderId isEqualToString:toUser.objectId]){
        return;
    }
    
    NSString *fileVideo = object[PARSE_VIDEO];
    PFFileObject *filePhoto = object[PARSE_PHOTO];
    PFFileObject *fileVoice = object[PARSE_VOICE_FILE];
    
    
    if (!filePhoto && !fileVoice && [fileVideo  isEqual: @""])
    {
        NSString *chatText = object[PARSE_MESSAGE];
        MessageModel *message = [[MessageModel alloc] initWithSenderId:senderId senderDisplayName:[self getDisplayName:sender] date:object.createdAt text:chatText];
        message.objectId = object.objectId;
        [messages addObject:message];
    }
    
    else if (filePhoto)
    {
        JSQPhotoMediaItem *mediaItem = [[JSQPhotoMediaItem alloc] initWithImage:nil];
        
        mediaItem.appliesMediaViewMaskAsOutgoing = [senderId isEqualToString:me.objectId];
        
        MessageModel *photoMsg = [[MessageModel alloc] initWithSenderId:senderId senderDisplayName:[self getDisplayName:sender] date:object.createdAt media:mediaItem];
        photoMsg.objectId = object.objectId;
        [filePhoto getDataInBackgroundWithBlock:^(NSData * _Nullable data, NSError * _Nullable error) {
            if (!error) {
                mediaItem.image = [UIImage imageWithData:data];
                photoMsg.image = mediaItem.image;
                [self.collectionView reloadData];
            }
        }];
        
        [messages addObject:photoMsg];
    }
    
    else if (fileVoice)
    {
        JSQPhotoMediaItem *mediaItem = [[JSQPhotoMediaItem alloc] initWithImage:[UIImage imageNamed:@"img_voice"]];
        
        mediaItem.appliesMediaViewMaskAsOutgoing = [senderId isEqualToString:me.objectId];
        
        MessageModel *voiceMsg = [[MessageModel alloc] initWithSenderId:senderId senderDisplayName:[self getDisplayName:sender] date:object.createdAt media:mediaItem];
        voiceMsg.objectId = object.objectId;
        voiceMsg.image = [UIImage imageNamed:@"img_voice"];
        
        [messages addObject:voiceMsg];
    }
    else if (![fileVideo  isEqual: @""])
    {
        JSQVideoMediaItem *mediaItem = [[JSQVideoMediaItem alloc] initWithFileURL:[NSURL URLWithString:fileVideo] isReadyToPlay:YES];
        
        mediaItem.appliesMediaViewMaskAsOutgoing = [senderId isEqualToString:me.objectId];
        
        MessageModel *videoMsg = [[MessageModel alloc] initWithSenderId:senderId senderDisplayName:[self getDisplayName:sender] date:object.createdAt media:mediaItem];
        videoMsg.objectId = object.objectId;
        mediaItem.fileURL = [NSURL URLWithString:fileVideo];
        videoMsg.video = fileVideo;
        videoMsg.image = [UIImage imageNamed:@"img_video"];
        [messages addObject:videoMsg];
    }
    
    int count = [messages count];
}

/// Helper methods
- (BOOL)inComing:(JSQMessage *)message {
    BOOL isOutGoing = [message.senderId isEqualToString:me.objectId];
    return !isOutGoing;
}

- (BOOL)outGoing:(JSQMessage *)message {
    BOOL isOutGoing = [message.senderId isEqualToString:me.objectId];
    return isOutGoing;
}

#pragma mark - JSQMessagesViewController method overrides

- (void)didPressSendButton:(UIButton *)button
           withMessageText:(NSString *)text
                  senderId:(NSString *)senderId
         senderDisplayName:(NSString *)senderDisplayName
                      date:(NSDate *)date
{
    text = [Util trim:text];
    UITextView *textView = self.inputToolbar.contentView.textView;
    textView.text = text;
    if (toUser){
        if (text.length == 0) {
            [Util showAlertTitle:self title:@"Error" message:@"Please input message."];
            return;
        }
        if (text.length > 1000) {
            //            text = [text substringToIndex:1000];
            [Util showAlertTitle:self title:@"Error" message:@"Message is too long"];
            return;
        }
        [self sendMessage:text videoName:@"" videoPath:@"" photo:nil audio:nil];
    } else {
        [Util showAlertTitle:self title:@"Error" message:@"There is no receiver."];
    }
}

- (void) uploadVideo:(NSURL*) videoUrl {
    NSString *videoName = [Util convertDateTimeToString:[NSDate date]];
    NSData *videoData = [NSData dataWithContentsOfURL:videoUrl];
    FIRStorage *storage = [FIRStorage storage];
    FIRStorageReference *storageRef = [storage reference];
    NSString *storagePath = [NSString stringWithFormat:@"file/%@.mp4", videoName];
    FIRStorageReference *fileRef = [storageRef child:storagePath];
    
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    FIRStorageUploadTask *uploadTask = [fileRef putData:videoData
                                                 metadata:nil
                                               completion:^(FIRStorageMetadata *metadata,
                                                            NSError *error) {
      if (error != nil) {
          [SVProgressHUD dismiss];
          [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
      } else {
          [fileRef downloadURLWithCompletion:^(NSURL * _Nullable URL, NSError * _Nullable error) {
              if (error != nil) {
                  [SVProgressHUD dismiss];
                  [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
              } else {
                  [self sendMessage:@"" videoName:videoName videoPath:URL.absoluteString photo:nil audio:nil];
              }
        }];
      }
    }];
}

- (void) uploadAudio:(NSURL*) videoUrl {
    [self sendMessage:@"" videoName:nil videoPath:nil photo:nil audio:videoUrl];
 }

- (void)sendMessage:(NSString *)text videoName:(NSString *)videoName videoPath:(NSString *)videoPath photo:(PFFileObject *)photo audio:(NSURL *)audioPath{
    if (!self.bookObj || !toUser){
        [Util showAlertTitle:self title:@"Error" message:@"Please select recipient first."];
        return;
    }
    NSString *shortMsg = @"";
    PFObject *object = [PFObject objectWithClassName:PARSE_TABLE_CHAT];
    object[PARSE_BOOK_OBJ] = self.bookObj;
    object[PARSE_SENDER] = me;
    object[PARSE_RECEIVER] = toUser;
    
    if (text) {
        self.inputToolbar.contentView.textView.text = @"";
        object[PARSE_MESSAGE] = text;
        
        shortMsg = [NSString stringWithFormat:@"'%@'", text];
        if (text.length > 20) {
            shortMsg = [NSString stringWithFormat:@"'%@...'", [text substringToIndex:20]];
        }
    }
    
    if (photo) {
        object[PARSE_PHOTO] = photo;
        shortMsg = @"with photo";
    }
    
    if (videoPath != nil){
        object[PARSE_VIDEO] = videoPath;
        object[PARSE_VIDEO_NAME] = videoName;
        shortMsg = @"with video";
    }
    
    if (audioPath != nil){
        NSData *videoData = [NSData dataWithContentsOfURL:audioPath];
        object[PARSE_VOICE_FILE] = [PFFileObject fileObjectWithName:@"rec.m4a" data:videoData];
        shortMsg = @"with photo";
    }
    
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    
    [object saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        [SVProgressHUD dismiss];
        if (succeeded) {
            [self loadMessages];
        }
    }];
    
    [self finishSendingMessage];
}

- (void)sendPushNotification:(NSString *)msg {
    BOOL isEnableChatNotify = YES;
    if (isEnableChatNotify) {
        NSString *name = [NSString stringWithFormat:@"%@ %@", me[PARSE_FIRSTNAME], me[PARSE_LASTSTNAME]];
        NSString *pushMsg = [NSString stringWithFormat:@"%@ sent a message %@", name, msg];
        NSDictionary *data = @{
                               @"alert" : pushMsg,
                               @"badge" : @"Increment",
                               @"sound" : @"cheering.caf",
                               @"email" : toUser.username,
                               @"type"  : [NSNumber numberWithInt:PUSH_TYPE_CHAT],
                               @"data"  : self.bookObj.objectId
                               };
        
        [PFCloud callFunctionInBackground:@"SendPush" withParameters:data block:^(id object, NSError *err) {
            if (err) {
                NSLog(@"Fail APNS: %@", @"SendChat");
            } else {
                NSLog(@"Success APNS: %@", @"SendChat");
            }
        }];
    }
}

- (void)didPressAccessoryButton:(UIButton *)sender
{
    [self.inputToolbar.contentView.textView resignFirstResponder];
    
    UIAlertController *actionsheet = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    [actionsheet addAction:[UIAlertAction actionWithTitle:@"Take Photo" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        [self onTakePhoto:nil];
    }]];
    [actionsheet addAction:[UIAlertAction actionWithTitle:@"Choose from Gallery" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        [self onChooseGallery:nil];
    }]];
    [actionsheet addAction:[UIAlertAction actionWithTitle:@"Record" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        [self onRecordVoice:nil];
    }]];
    
    [actionsheet addAction:[UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:^(UIAlertAction *action){
        [self dismissViewControllerAnimated:YES completion:nil];
    }]];
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone) {
        [self presentViewController:actionsheet animated:YES completion:nil];
    } else {
        UIPopoverController *popup = [[UIPopoverController alloc] initWithContentViewController:actionsheet];
        [popup presentPopoverFromRect:CGRectMake(self.view.frame.size.width/3, self.view.frame.size.height/2, 0, 0)inView:self.view permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
    }
}

- (void)onChooseGallery:(id)sender {
    if (![Util isPhotoAvaileble]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Photo"];
        return;
    }
    isPhoto = YES;
    isCamera = NO;
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
    imagePickerController.delegate = self;
    imagePickerController.sourceType =  UIImagePickerControllerSourceTypePhotoLibrary;
    imagePickerController.mediaTypes = [NSArray arrayWithObjects:(NSString *)kUTTypeImage, nil];
    [self presentViewController:imagePickerController animated:YES completion:nil];
}
#pragma mark - record voice
- (void) onRecordVoice:(id)sender{
    
    IQAudioRecorderViewController *controller = [[IQAudioRecorderViewController alloc] init];
    controller.delegate = self;
    controller.allowCropping = false;
    controller.normalTintColor = UIColor.blueColor;
    controller.highlightedTintColor = UIColor.redColor;
//    [self presentBlurredAudioRecorderViewControllerAnimated:controller];
    controller.barStyle = UIBarStyleDefault;
    controller.title = @"Recording";
    [self presentAudioRecorderViewControllerAnimated:controller];
}
#pragma mark - audioRecorderController delegate
-(void)audioRecorderController:(IQAudioRecorderViewController *)controller didFinishWithAudioAtPath:(NSString *)filePath
{
    NSString * audioFilePath = filePath;
    [controller dismissViewControllerAnimated:YES completion:^{
        [self uploadAudio:[[NSURL alloc] initFileURLWithPath:audioFilePath]];
    }];
}

-(void)audioRecorderControllerDidCancel:(IQAudioRecorderViewController *)controller
{
    [controller dismissViewControllerAnimated:YES completion:nil];
}

- (void)onTakeVideo:(id)sender {
    isPhoto = YES;
    isCamera = NO;
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc] init];
    imagePickerController.delegate = self;
    imagePickerController.sourceType =  UIImagePickerControllerSourceTypeCamera;
    imagePickerController.mediaTypes = [NSArray arrayWithObjects:(NSString *)kUTTypeMovie, nil];
    [self.parentViewController presentViewController:imagePickerController animated:YES completion:nil];
}

- (void)onTakePhoto:(id)sender {
    if (![Util isCameraAvailable]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Camera"];
        return;
    }
    isCamera = YES;
    isPhoto = NO;
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
    imagePickerController.delegate = self;
    imagePickerController.sourceType =  UIImagePickerControllerSourceTypeCamera;
    [self presentViewController:imagePickerController animated:YES completion:nil];
}

- (void) imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<NSString *,id> *)info{
    [picker dismissViewControllerAnimated:YES completion:nil];
    if (isCamera &&![Util isCameraAvailable]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Camera"];
        return;
    }
    if (isPhoto && ![Util isPhotoAvaileble]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Photo"];
        return;
    }
    
    NSString *type = info[UIImagePickerControllerMediaType];
    UIImage *image = (UIImage *)[info valueForKey:UIImagePickerControllerOriginalImage];
    image = [Util getUploadingImageFromImage:image];
    NSData *data = UIImageJPEGRepresentation(image, 0.8);
    [self sendMessage:@"" videoName:@"" videoPath:@"" photo:[PFFileObject fileObjectWithData:data] audio:nil];
}

- (void)actionSheet:(UIActionSheet *)actionSheet didDismissWithButtonIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == actionSheet.cancelButtonIndex) {
        [self.inputToolbar.contentView.textView becomeFirstResponder];
        return;
    }
    [self finishSendingMessageAnimated:YES];
}



#pragma mark - JSQMessages CollectionView DataSource

- (id<JSQMessageData>)collectionView:(JSQMessagesCollectionView *)collectionView messageDataForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return [messages objectAtIndex:indexPath.item];
}

- (void)collectionView:(JSQMessagesCollectionView *)collectionView didDeleteMessageAtIndexPath:(NSIndexPath *)indexPath{}

- (id<JSQMessageBubbleImageDataSource>)collectionView:(JSQMessagesCollectionView *)collectionView messageBubbleImageDataForItemAtIndexPath:(NSIndexPath *)indexPath
{
    JSQMessage *message = [messages objectAtIndex:indexPath.item];
    
    if ([message.senderId isEqualToString:self.senderId]) {
        return outgoingBubbleImageData;
    }
    
    return incomingBubbleImageData;
}

- (id<JSQMessageAvatarImageDataSource>)collectionView:(JSQMessagesCollectionView *)collectionView avatarImageDataForItemAtIndexPath:(NSIndexPath *)indexPath
{
    JSQMessage *message = [messages objectAtIndex:indexPath.item];
    
    if ([message.senderId isEqualToString:self.senderId]) {
        return nil;
    }
    else {
        return nil;
    }
    
    // can add avatar image
    return nil;
}


- (NSAttributedString *)collectionView:(JSQMessagesCollectionView *)collectionView attributedTextForCellTopLabelAtIndexPath:(NSIndexPath *)indexPath
{
    /**
     *  This logic should be consistent with what you return from `heightForCellTopLabelAtIndexPath:`
     *  The other label text delegate methods should follow a similar pattern.
     *
     *  Show a timestamp for every 3rd message
     */
    if (indexPath.item % 3 == 0) {
        JSQMessage *message = [messages objectAtIndex:indexPath.item];
        return [[JSQMessagesTimestampFormatter sharedFormatter] attributedTimestampForDate:message.date];
    }
    
    return nil;
}

- (NSAttributedString *)collectionView:(JSQMessagesCollectionView *)collectionView attributedTextForMessageBubbleTopLabelAtIndexPath:(NSIndexPath *)indexPath
{
    JSQMessage *message = [messages objectAtIndex:indexPath.item];
    
    /**
     *  iOS7-style sender name labels
     */
    if ([message.senderId isEqualToString:self.senderId]) {
        return nil;
    }
    
    if (indexPath.item - 1 > 0) {
        JSQMessage *previousMessage = [messages objectAtIndex:indexPath.item - 1];
        if ([[previousMessage senderId] isEqualToString:message.senderId]) {
            return nil;
        }
    }
    
    /**
     *  Don't specify attributes to use the defaults.
     */
    return [[NSAttributedString alloc] initWithString:message.senderDisplayName];
}

- (NSAttributedString *)collectionView:(JSQMessagesCollectionView *)collectionView attributedTextForCellBottomLabelAtIndexPath:(NSIndexPath *)indexPath
{
    return nil;
}



#pragma mark - UICollectionView DataSource

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return [messages count];
}

- (UICollectionViewCell *)collectionView:(JSQMessagesCollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    /**
     *  Override point for customizing cells
     */
    JSQMessagesCollectionViewCell *cell = (JSQMessagesCollectionViewCell *)[super collectionView:collectionView cellForItemAtIndexPath:indexPath];
    
    JSQMessage *msg = [messages objectAtIndex:indexPath.item];
    
    if (!msg.isMediaMessage) {
        
        if ([msg.senderId isEqualToString:self.senderId]) {
            cell.textView.textColor = [UIColor whiteColor];
            PFFileObject *avatarFile = me[PARSE_AVATAR];
            [cell.avatarImageView sd_setImageWithURL:[NSURL URLWithString:avatarFile.url] placeholderImage:[UIImage imageNamed:@"default_profile"]];
        }
        else {
            cell.textView.textColor = [UIColor whiteColor];
            PFFileObject *avatarFile = toUser[PARSE_AVATAR];
            [cell.avatarImageView sd_setImageWithURL:[NSURL URLWithString:avatarFile.url] placeholderImage:[UIImage imageNamed:@"default_profile"]];
        }
        
        cell.textView.linkTextAttributes = @{ NSForegroundColorAttributeName : cell.textView.textColor,
                                              NSUnderlineStyleAttributeName : @(NSUnderlineStyleSingle | NSUnderlinePatternSolid) };
    }else{
        if ([msg.senderId isEqualToString:self.senderId]) {
            PFFileObject *avatarFile = me[PARSE_AVATAR];
            [cell.avatarImageView sd_setImageWithURL:[NSURL URLWithString:avatarFile.url] placeholderImage:[UIImage imageNamed:@"default_profile"]];
        }else{
            PFFileObject *avatarFile = toUser[PARSE_AVATAR];
            [cell.avatarImageView sd_setImageWithURL:[NSURL URLWithString:avatarFile.url] placeholderImage:[UIImage imageNamed:@"default_profile"]];
        }
        cell.textView.text = @"Media Message";
        UIImageView * imageView = [[UIImageView alloc] initWithFrame:cell.mediaView.bounds];
        [cell.mediaView addSubview:imageView];
        if([msg isKindOfClass:MessageModel.class]){
            MessageModel * mmodel = (MessageModel*)msg;
            if(mmodel.image != nil){
                [imageView setImage:mmodel.image];
            }
        }
        [cell setMediaView:imageView];
    }
    
    return cell;
}
- (JSQMessagesBubbleImage *) setupOutgoingBubble {
   JSQMessagesBubbleImageFactory *bubbleImageFactory = [[JSQMessagesBubbleImageFactory alloc] init];
   JSQMessagesBubbleImage * imgBubble = [bubbleImageFactory outgoingMessagesBubbleImageWithColor:[UIColor blueColor]];//jsq_messageBubbleBlueColor return imgBubble;
   return imgBubble;
}

- (JSQMessagesBubbleImage *) setupIncomingBubble {
   JSQMessagesBubbleImageFactory *bubbleImageFactory = [[JSQMessagesBubbleImageFactory alloc] init];
   JSQMessagesBubbleImage * imgBubble = [bubbleImageFactory outgoingMessagesBubbleImageWithColor:[UIColor greenColor]];//jsq_messageBubbleBlueColor return imgBubble;
   return imgBubble;
}


#pragma mark - UICollectionView Delegate

#pragma mark - Custom menu items

- (BOOL)collectionView:(UICollectionView *)collectionView canPerformAction:(SEL)action forItemAtIndexPath:(NSIndexPath *)indexPath withSender:(id)sender
{
    if (action == @selector(customAction:)) {
        return YES;
    }
    
    return [super collectionView:collectionView canPerformAction:action forItemAtIndexPath:indexPath withSender:sender];
}

- (void)collectionView:(UICollectionView *)collectionView performAction:(SEL)action forItemAtIndexPath:(NSIndexPath *)indexPath withSender:(id)sender
{
    if (action == @selector(customAction:)) {
        [self customAction:sender];
        return;
    }
    
    [super collectionView:collectionView performAction:action forItemAtIndexPath:indexPath withSender:sender];
}

- (void)customAction:(id)sender
{
    NSLog(@"Custom action received! Sender: %@", sender);
    
    [[[UIAlertView alloc] initWithTitle:@"Custom Action"
                                message:nil
                               delegate:nil
                      cancelButtonTitle:@"OK"
                      otherButtonTitles:nil]
     show];
}



#pragma mark - JSQMessages collection view flow layout delegate

#pragma mark - Adjusting cell label heights

- (CGFloat)collectionView:(JSQMessagesCollectionView *)collectionView
                   layout:(JSQMessagesCollectionViewFlowLayout *)collectionViewLayout heightForCellTopLabelAtIndexPath:(NSIndexPath *)indexPath
{
    /**
     *  Each label in a cell has a `height` delegate method that corresponds to its text dataSource method
     */
    
    /**
     *  This logic should be consistent with what you return from `attributedTextForCellTopLabelAtIndexPath:`
     *  The other label height delegate methods should follow similarly
     *
     *  Show a timestamp for every 3rd message
     */
    if (indexPath.item % 3 == 0) {
        return kJSQMessagesCollectionViewCellLabelHeightDefault;
    }
    
    return 0.0f;
}

- (CGFloat)collectionView:(JSQMessagesCollectionView *)collectionView
                   layout:(JSQMessagesCollectionViewFlowLayout *)collectionViewLayout heightForMessageBubbleTopLabelAtIndexPath:(NSIndexPath *)indexPath
{
    /**
     *  iOS7-style sender name labels
     */
    JSQMessage *currentMessage = [messages objectAtIndex:indexPath.item];
    if ([[currentMessage senderId] isEqualToString:self.senderId]) {
        return 0.0f;
    }
    
    if (indexPath.item - 1 > 0) {
        JSQMessage *previousMessage = [messages objectAtIndex:indexPath.item - 1];
        if ([[previousMessage senderId] isEqualToString:[currentMessage senderId]]) {
            return 0.0f;
        }
    }
    
    return kJSQMessagesCollectionViewCellLabelHeightDefault;
}

- (CGFloat)collectionView:(JSQMessagesCollectionView *)collectionView
                   layout:(JSQMessagesCollectionViewFlowLayout *)collectionViewLayout heightForCellBottomLabelAtIndexPath:(NSIndexPath *)indexPath
{
    return 0.0f;
}

#pragma mark - Responding to collection view tap events

- (void)collectionView:(JSQMessagesCollectionView *)collectionView
                header:(JSQMessagesLoadEarlierHeaderView *)headerView didTapLoadEarlierMessagesButton:(UIButton *)sender
{
    NSLog(@"Load earlier messages!");
    [self.inputToolbar.contentView.textView resignFirstResponder];
}

- (void)collectionView:(JSQMessagesCollectionView *)collectionView didTapAvatarImageView:(UIImageView *)avatarImageView atIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"Tapped avatar!");
    [self.inputToolbar.contentView.textView resignFirstResponder];
}

- (void)collectionView:(JSQMessagesCollectionView *)collectionView didTapMessageBubbleAtIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"Tapped message bubble!");
    [self.inputToolbar.contentView.textView resignFirstResponder];
    
    JSQMessage *msg = [messages objectAtIndex:indexPath.item];
    if (msg.isMediaMessage){
        MessageModel *model = [messages objectAtIndex:indexPath.row];
//        if (model.image){
//            MediaViewController * vc = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"MediaViewController"];
//            vc.image = model.image;
//            [self.navigationController pushViewController:vc animated:YES];
//        }
    }
}

- (void)collectionView:(JSQMessagesCollectionView *)collectionView didTapCellAtIndexPath:(NSIndexPath *)indexPath touchLocation:(CGPoint)touchLocation
{
    NSLog(@"Tapped cell at %@!", NSStringFromCGPoint(touchLocation));
    [self.inputToolbar.contentView.textView resignFirstResponder];
}

#pragma mark - JSQMessagesComposerTextViewPasteDelegate methods


- (BOOL)composerTextView:(JSQMessagesComposerTextView *)textView shouldPasteWithSender:(id)sender
{
    if ([UIPasteboard generalPasteboard].image) {
        // If there's an image in the pasteboard, construct a media item with that image and `send` it.
        JSQPhotoMediaItem *item = [[JSQPhotoMediaItem alloc] initWithImage:[UIPasteboard generalPasteboard].image];
        JSQMessage *message = [[JSQMessage alloc] initWithSenderId:self.senderId
                                                 senderDisplayName:self.senderDisplayName
                                                              date:[NSDate date]
                                                             media:item];
        [messages addObject:message];
        [self finishSendingMessage];
        return NO;
    }
    return YES;
}

- (void) textFieldDidEndEditing:(UITextField *)textField
{
    NSString *username = textField.text;
    if (![username isEmail]){
        return;
    }
    PFQuery *query = [PFUser query];
    [query whereKey:PARSE_USER_NAME notEqualTo:textField.text];
    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    [query getFirstObjectInBackgroundWithBlock:^(PFObject *object, NSError *error){
        [MBProgressHUD hideHUDForView:self.view animated:YES];
        if (!error && object){
            //            self.other = (PFUser *)object;
            //            [self refreshUI];
        }
    }];
}

#pragma  alertview Delegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    NSInteger tag = alertView.tag;
    //    if (tag == TAG_ERROR) {
    //        [self.navigationController popViewControllerAnimated:YES];
    //    }
}
@end
