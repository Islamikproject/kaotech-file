//
//  GaugeViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/26/21.
//  Copyright © 2021 Ales Gabrysz. All rights reserved.
//

#import "GaugeViewController.h"
#import <MobileCoreServices/MobileCoreServices.h>
#import <AVKit/AVKit.h>

@interface GaugeViewController ()<IQDropDownTextFieldDelegate, UINavigationControllerDelegate, UIImagePickerControllerDelegate>{
    BOOL isCamera;
    BOOL isGallery;
    BOOL hasPhoto;
    BOOL hasVideo;
    NSURL *videoUrl;
}
@property (weak, nonatomic) IBOutlet UIButton *btnDelete;
@property (weak, nonatomic) IBOutlet UITextView *edtDescription;
@property (weak, nonatomic) IBOutlet UITextField *edtWebLink;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *edtTextColor;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *edtTextSize;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *edtTextFont;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *edtBackgroundColor;
@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;
@property (weak, nonatomic) IBOutlet UIView *videoView;
@property (nonatomic, retain) AVPlayer *player;
@property (nonatomic, retain) AVPlayerLayer *playerLayer;

@end

@implementation GaugeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.edtDescription.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.edtDescription.layer.borderWidth = 1.f;
    [self initialize];
}

- (void) initialize {
    hasVideo = NO;
    hasPhoto = NO;
    self.btnDelete.hidden = YES;
    self.edtTextColor.itemList = STRING_COLOR;
    self.edtTextColor.isOptionalDropDown = YES;
    self.edtTextColor.delegate = self;
    
    self.edtTextSize.itemList = ARRAY_STRING_SIZE;
    self.edtTextSize.isOptionalDropDown = YES;
    self.edtTextSize.delegate = self;
    
    self.edtTextFont.itemList = ARRAY_FONT;
    self.edtTextFont.isOptionalDropDown = YES;
    self.edtTextFont.delegate = self;
    
    self.edtBackgroundColor.itemList = STRING_COLOR;
    self.edtBackgroundColor.isOptionalDropDown = YES;
    self.edtBackgroundColor.delegate = self;
    
    self.edtDescription.text = @"";
    self.edtWebLink.text = @"";
    self.edtTextColor.selectedRow = 2;
    self.edtTextSize.selectedRow = 0;
    self.edtTextFont.selectedRow = 0;
    self.edtBackgroundColor.selectedRow = 1;
    
    
    if (self.mGaugeObj != nil) {
        self.btnDelete.hidden = NO;
        self.edtWebLink.text = self.mGaugeObj[PARSE_WEB_LINK];
        NSString *description = self.mGaugeObj[PARSE_DESCRIPTION];
        self.edtDescription.text = description;
        if (description.length > 0) {
            int bgColor = [self.mGaugeObj[PARSE_BG_COLOR] intValue];
            int textColor = [self.mGaugeObj[PARSE_TEXT_COLOR] intValue];
            int textFont = [self.mGaugeObj[PARSE_TEXT_FONT] intValue];
            int textSize = [self.mGaugeObj[PARSE_TEXT_SIZE] intValue];
            self.edtTextColor.selectedRow = textColor;
            self.edtTextSize.selectedRow = textSize;
            self.edtTextFont.selectedRow = textFont;
            self.edtBackgroundColor.selectedRow = bgColor;
            [self.edtDescription setFont:[UIFont fontWithName:ARRAY_FONT[textFont] size:textSize + 10]];
            [self.edtDescription setTextColor:ARRAY_COLOR[textColor]];
            [self.edtDescription setBackgroundColor:ARRAY_COLOR[bgColor]];
        }
        
        PFFileObject *photoFile = self.mGaugeObj[PARSE_PHOTO];
        [self.imgPhoto sd_setImageWithURL:[NSURL URLWithString:photoFile.url] placeholderImage:[UIImage imageNamed:@"default_image_bg"]];
        NSString *video = self.mGaugeObj[PARSE_VIDEO];
        if (video.length > 0) {
            NSURL *videoURL = [NSURL URLWithString:video];
            self.player = [AVPlayer playerWithURL:videoURL];
            [self.player addObserver:self forKeyPath:@"rate" options:0 context:nil];
            self.playerLayer = [AVPlayerLayer playerLayerWithPlayer:self.player];
            self.playerLayer.frame = self.videoView.bounds;
            self.playerLayer.needsDisplayOnBoundsChange = true;
            [self.videoView.layer addSublayer:self.playerLayer];
            [self.player play];
        }
    }
}
- (void) viewWillLayoutSubviews
{
    [super viewWillLayoutSubviews];
    self.playerLayer.frame = self.videoView.bounds;
}
- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context {
    if ([keyPath isEqualToString:@"rate"]) {
        if ([self.player rate]) {
            NSLog(@"[self changeToPause];  // This changes the button to Pause");
        } else {
            NSLog(@"[self changeToPlay];  // This changes the button to Play");
        }
    }
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onDeleteClick:(id)sender {
    NSString *msg = @"Are you sure you want to delete this gauge?";
    SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
    alert.customViewColor = MAIN_COLOR;
    alert.horizontalButtons = YES;
    [alert addButton:@"No" actionBlock:^(void) {
    }];
    [alert addButton:@"Yes" actionBlock:^(void) {
        [SVProgressHUD showWithStatus:@"Please wait..." maskType:SVProgressHUDMaskTypeGradient];
        [self.mGaugeObj deleteInBackgroundWithBlock:^(BOOL success, NSError* error){
            [SVProgressHUD dismiss];
            if (error) {
                [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
            } else {
                [self onBack:nil];
            }
        }];
    }];
    [alert showQuestion:@"Delete" subTitle:msg closeButtonTitle:nil duration:0.0f];
}

- (IBAction)onPhotoClick:(id)sender {
    UIAlertController *actionsheet = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    [actionsheet addAction:[UIAlertAction actionWithTitle:@"Take Photo" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        [self onTakePhoto:nil];
    }]];
    [actionsheet addAction:[UIAlertAction actionWithTitle:@"Choose from Gallery" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        [self onChoosePhoto:nil];
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
- (IBAction)onVideoClick:(id)sender {
    UIAlertController *actionsheet = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    [actionsheet addAction:[UIAlertAction actionWithTitle:@"Take a new video" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        if (![Util isCameraAvailable]){
            [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Camera"];
            return;
        }
        hasVideo = NO;
        UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
        imagePickerController.delegate = self;
        imagePickerController.sourceType =  UIImagePickerControllerSourceTypeCamera;
        imagePickerController.mediaTypes = [NSArray arrayWithObjects:(NSString *)kUTTypeMovie, nil];
        [self presentViewController:imagePickerController animated:YES completion:nil];
    }]];
    [actionsheet addAction:[UIAlertAction actionWithTitle:@"Select video from gallery" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
        hasVideo = NO;
        UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
        imagePickerController.delegate = self;
        imagePickerController.sourceType =  UIImagePickerControllerSourceTypePhotoLibrary;
        imagePickerController.mediaTypes = [NSArray arrayWithObjects:(NSString *)kUTTypeMovie, nil];
        [self presentViewController:imagePickerController animated:YES completion:nil];
    }]];
    [actionsheet addAction:[UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:^(UIAlertAction *action){
        [self dismissViewControllerAnimated:YES completion:nil];
    }]];
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone) {
        [self presentViewController:actionsheet animated:YES completion:nil];
    } else {
        // Change Rect to position Popover
        UIPopoverController *popup = [[UIPopoverController alloc] initWithContentViewController:actionsheet];
        [popup presentPopoverFromRect:CGRectMake(self.view.frame.size.width/2, self.view.frame.size.height/4, 0, 0)inView:self.view permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
    }
}
- (IBAction)onSaveClick:(id)sender {
    if ([self isValid]) {
        [self uploadVideo];
    }
}
- (BOOL) isValid {
    NSString *weblink = [Util trim:_edtWebLink.text];
    NSString *description = [Util trim:_edtDescription.text];
    if (weblink.length == 0 && description.length == 0 && !hasPhoto && !hasVideo) {
        [Util showAlertTitle:self title:@"Error" message:@"Please enter gauge."];
        return NO;
    }
    if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return NO;
    }
    return YES;
}

- (void) uploadVideo {
    if (hasVideo) {
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
                      if (self.mGaugeObj == nil) {
                          [self create:videoName videoPath:URL.absoluteString];
                      } else {
                          [self save:videoName videoPath:URL.absoluteString];
                      }
                  }
            }];
          }
        }];
    } else {
        if (self.mGaugeObj == nil) {
            [self create:@"" videoPath:@""];
        } else {
            [self save:@"" videoPath:@""];
        }
    }
}
- (void) create:(NSString *)videoName videoPath:(NSString *)videoPath {
    NSString *weblink = [Util trim:_edtWebLink.text];
    NSString *description = [Util trim:_edtDescription.text];
    PFObject *object = [PFObject objectWithClassName:PARSE_TABLE_GAUGE];
    object[PARSE_OWNER] = [PFUser currentUser];
    object[PARSE_WEB_LINK] = weblink;
    object[PARSE_DESCRIPTION] = description;
    object[PARSE_VIDEO] = videoPath;
    object[PARSE_VIDEO_NAME] = videoName;
    int textColor = (int)[self.edtTextColor selectedRow];
    int textSize = (int)[self.edtTextSize selectedRow];
    int textFont = (int)[self.edtTextFont selectedRow];
    int backgroundColor = (int)[self.edtBackgroundColor selectedRow];
    object[PARSE_TEXT_COLOR] = [NSNumber numberWithInt:textColor];
    object[PARSE_TEXT_SIZE] = [NSNumber numberWithInt:textSize];
    object[PARSE_TEXT_FONT] = [NSNumber numberWithInt:textFont];
    object[PARSE_BG_COLOR] = [NSNumber numberWithInt:backgroundColor];
    if (hasPhoto){
        UIImage *profileImage = [Util getUploadingUserImageFromImage:_imgPhoto.image];
        NSData *imageData = UIImageJPEGRepresentation(profileImage, 0.8);
        object[PARSE_PHOTO] = [PFFileObject fileObjectWithData:imageData];
    }
    [SVProgressHUD showWithStatus:@"Please wait..." maskType:SVProgressHUDMaskTypeGradient];
    [object saveInBackgroundWithBlock:^(BOOL success, NSError* error){
        [SVProgressHUD dismiss];
        if (error) {
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        } else {
            [self onBack:nil];
        }
    }];
}

- (void) save:(NSString *)videoName videoPath:(NSString *)videoPath {
    NSString *weblink = [Util trim:_edtWebLink.text];
    NSString *description = [Util trim:_edtDescription.text];
    self.mGaugeObj[PARSE_WEB_LINK] = weblink;
    self.mGaugeObj[PARSE_DESCRIPTION] = description;
    if (videoPath.length > 0) {
        self.mGaugeObj[PARSE_VIDEO] = videoPath;
        self.mGaugeObj[PARSE_VIDEO_NAME] = videoName;
    }
    int textColor = (int)[self.edtTextColor selectedRow];
    int textSize = (int)[self.edtTextSize selectedRow];
    int textFont = (int)[self.edtTextFont selectedRow];
    int backgroundColor = (int)[self.edtBackgroundColor selectedRow];
    self.mGaugeObj[PARSE_TEXT_COLOR] = [NSNumber numberWithInt:textColor];
    self.mGaugeObj[PARSE_TEXT_SIZE] = [NSNumber numberWithInt:textSize];
    self.mGaugeObj[PARSE_TEXT_FONT] = [NSNumber numberWithInt:textFont];
    self.mGaugeObj[PARSE_BG_COLOR] = [NSNumber numberWithInt:backgroundColor];
    if (hasPhoto){
        UIImage *profileImage = [Util getUploadingUserImageFromImage:_imgPhoto.image];
        NSData *imageData = UIImageJPEGRepresentation(profileImage, 0.8);
        self.mGaugeObj[PARSE_PHOTO] = [PFFileObject fileObjectWithData:imageData];
    }
    [SVProgressHUD showWithStatus:@"Please wait..." maskType:SVProgressHUDMaskTypeGradient];
    [self.mGaugeObj saveInBackgroundWithBlock:^(BOOL success, NSError* error){
        [SVProgressHUD dismiss];
        if (error) {
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        } else {
            [self onBack:nil];
        }
    }];
}
- (void)onChoosePhoto:(id)sender {
    if (![Util isPhotoAvaileble]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Photo"];
        return;
    }
    isGallery = YES;
    isCamera = NO;
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
    imagePickerController.delegate = self;
    imagePickerController.sourceType =  UIImagePickerControllerSourceTypePhotoLibrary;
    [self presentViewController:imagePickerController animated:YES completion:nil];
}

- (void)onTakePhoto:(id)sender {
    if (![Util isCameraAvailable]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Camera"];
        return;
    }
    isCamera = YES;
    isGallery = NO;
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
    imagePickerController.delegate = self;
    imagePickerController.sourceType =  UIImagePickerControllerSourceTypeCamera;
    [self presentViewController:imagePickerController animated:YES completion:nil];
}

- (void) imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<NSString *,id> *)info{
    [picker dismissViewControllerAnimated:YES completion:nil];
    if (isCamera && ![Util isCameraAvailable]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Camera"];
        return;
    }
    if (isGallery && ![Util isPhotoAvaileble]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Photo"];
        return;
    }
    NSString *type = info[UIImagePickerControllerMediaType];
    if ([type isEqualToString:(NSString *)kUTTypeMovie]){
        hasVideo = YES;
        videoUrl = info[UIImagePickerControllerMediaURL];
        _player = [AVPlayer playerWithURL:videoUrl];
        [_player addObserver:self forKeyPath:@"rate" options:0 context:nil];
        _playerLayer = [AVPlayerLayer playerLayerWithPlayer:_player];
        _playerLayer.frame = self.videoView.bounds;
        _playerLayer.needsDisplayOnBoundsChange = true;
        [self.videoView.layer addSublayer:_playerLayer];
        [_player play];
    } else {
        UIImage *image = (UIImage *)[info valueForKey:UIImagePickerControllerOriginalImage];
        hasPhoto = YES;
        [_imgPhoto setImage:[Util getUploadingUserImageFromImage:image]];
    }
}

- (void) imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissViewControllerAnimated:YES completion:nil];
    if (isGallery && ![Util isPhotoAvaileble]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Photo"];
        return;
    }
    if (isCamera && ![Util isCameraAvailable]){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Camera"];
        return;
    }
}
- (void)textField:(IQDropDownTextField *)textField didSelectItem:(NSString *)item {
    if (item.length > 0) {
        int index = (int)[textField selectedRow];
        if (textField == self.edtTextColor) {
            [self.edtDescription setTextColor:ARRAY_COLOR[index]];
        } else if (textField == self.edtTextSize) {
            int font = (int)[self.edtTextFont selectedRow];
            [self.edtDescription setFont:[UIFont fontWithName:ARRAY_FONT[font] size:index + 10]];
        } else if (textField == self.edtTextFont) {
            int size = (int)[self.edtTextSize selectedRow];
            [self.edtDescription setFont:[UIFont fontWithName:ARRAY_FONT[index] size:size + 10]];
        } else if (textField == self.edtBackgroundColor) {
            [self.edtDescription setBackgroundColor:ARRAY_COLOR[index]];
        }
    }
}
@end
