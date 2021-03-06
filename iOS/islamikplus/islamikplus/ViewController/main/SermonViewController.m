//
//  SermonViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "SermonViewController.h"
#import <MobileCoreServices/MobileCoreServices.h>
#import <IQMediaPickerController/IQMediaPickerController.h>

static SermonViewController *_sharedViewController = nil;

@interface SermonViewController () <IQDropDownTextFieldDelegate, UIImagePickerControllerDelegate, UINavigationControllerDelegate, IQMediaPickerControllerDelegate>{
    BOOL isCameraOpen;
    NSMutableArray * languageCode;
    NSMutableArray * languageName;
}

@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UITextView *edtTopic;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *edtAmount;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *edtLanguage;
@property (weak, nonatomic) IBOutlet UIButton *btnNext;
@end

@implementation SermonViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    _sharedViewController = self;
    
    if (self.type == TYPE_JUMAH) {
        self.lblTitle.text = @"JUMAH SERMON";
    } else {
        self.lblTitle.text = @"REGULAR SERMON";
    }

    self.edtTopic.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.edtTopic.layer.borderWidth = 1.f;
    self.edtTopic.layer.cornerRadius = 4.f;
    [self initialize];
}

- (void) initialize {
    self.edtAmount.itemList = STRING_AMOUNT;
    self.edtAmount.isOptionalDropDown = YES;
    self.edtTopic.text = @"";
    _edtAmount.selectedItem = @"";
    languageCode = [Util getLanguageCodeList];
    languageName = [Util getLanguageNameList];
    self.edtLanguage.itemList = languageName;
    self.edtLanguage.isOptionalDropDown = YES;
    self.edtLanguage.delegate = self;
}

+ (SermonViewController *) getInstance {
    return _sharedViewController;
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onNextClick:(id)sender {
    NSString *topic = [Util trim:_edtTopic.text];
    if (topic.length == 0) {
        [Util showAlertTitle:self title:@"Error" message:@"Please enter topic."];
    } else if (self.edtLanguage.selectedItem.length == 0) {
        [Util showAlertTitle:self title:@"Error" message:@"Please enter language."];
    } else {
        UIAlertController *actionsheet = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
        [actionsheet addAction:[UIAlertAction actionWithTitle:@"Take a new video" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
            if (![Util isCameraAvailable]){
                [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Camera"];
                return;
            }
            UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
            imagePickerController.delegate = self;
            imagePickerController.sourceType =  UIImagePickerControllerSourceTypeCamera;
            imagePickerController.mediaTypes = [NSArray arrayWithObjects:(NSString *)kUTTypeMovie, nil];
            [self presentViewController:imagePickerController animated:YES completion:nil];
        }]];
        [actionsheet addAction:[UIAlertAction actionWithTitle:@"Select video from gallery" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
            UIImagePickerController *imagePickerController = [[UIImagePickerController alloc]init];
            imagePickerController.delegate = self;
            imagePickerController.sourceType =  UIImagePickerControllerSourceTypePhotoLibrary;
            imagePickerController.mediaTypes = [NSArray arrayWithObjects:(NSString *)kUTTypeMovie, nil];
            [self presentViewController:imagePickerController animated:YES completion:nil];
        }]];
        [actionsheet addAction:[UIAlertAction actionWithTitle:@"Select audio from file" style:UIAlertActionStyleDefault handler:^(UIAlertAction *action){
            IQMediaPickerController *controller = [[IQMediaPickerController alloc] init];
            [controller setSourceType:IQMediaPickerControllerSourceTypeLibrary];//or IQMediaPickerControllerSourceTypeLibrary
            [controller setMediaTypes:@[@(PHAssetMediaTypeAudio)]];
            controller.allowsPickingMultipleItems = NO;//or NO
            controller.delegate = self;
            [self presentViewController:controller animated:YES completion:nil];
            
        }]];
        [actionsheet addAction:[UIAlertAction actionWithTitle:@"Cancel" style:UIAlertActionStyleCancel handler:^(UIAlertAction *action){
            [self dismissViewControllerAnimated:YES completion:nil];
        }]];
        if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone) {
            [self presentViewController:actionsheet animated:YES completion:nil];
        }
        //if iPad
        else {
            // Change Rect to position Popover
            UIPopoverController *popup = [[UIPopoverController alloc] initWithContentViewController:actionsheet];
            [popup presentPopoverFromRect:CGRectMake(self.view.frame.size.width/2, self.view.frame.size.height/4, 0, 0)inView:self.view permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
        }
    }
}

-(void)mediaPickerController:(IQMediaPickerController *)controller didFinishMedias:(IQMediaPickerSelection *)selection
{
    NSLog(@"Info: %@",selection);   //Here you'll get the information about captured or picked assets

//    selectedMedias = selection;

}

- (void)mediaPickerControllerDidCancel:(IQMediaPickerController *)controller;
{
}


#pragma mark - image pickerview
- (void) imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<NSString *,id> *)info{
    [picker dismissViewControllerAnimated:YES completion:nil];
    if (![Util isCameraAvailable] && isCameraOpen){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Camera"];
        return;
    }
    
    NSString *type = info[UIImagePickerControllerMediaType];
    if ([type isEqualToString:(NSString *)kUTTypeMovie]){
        NSURL *url = info[UIImagePickerControllerMediaURL];
        [self uploadVideo:url];
    }
}

- (void) imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissViewControllerAnimated:YES completion:nil];
    if (![Util isCameraAvailable] && isCameraOpen){
        [Util showAlertTitle:self title:@"Error" message:@"Check your permissions in Settings > Privacy > Camera"];
        return;
    }
}

- (void) uploadVideo:(NSURL*) videoUrl {
    NSData *videoData = [NSData dataWithContentsOfURL:videoUrl];
    FIRStorage *storage = [FIRStorage storage];
    FIRStorageReference *storageRef = [storage reference];
    NSString *extension = [[videoUrl lastPathComponent] pathExtension];
    NSString *videoName = [NSString stringWithFormat:@"%@.%@", [Util convertDateTimeToString:[NSDate date]], extension];
    NSString *storagePath = [NSString stringWithFormat:@"file/%@", videoName];
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
                  [self registerSermon:videoName videoPath:URL.absoluteString isAudio:NO];
              }
        }];
      }
    }];
}
- (void) uploadAudio:(NSURL*) audioUrl {
    NSData *audioData = [NSData dataWithContentsOfURL:audioUrl];
    FIRStorage *storage = [FIRStorage storage];
    FIRStorageReference *storageRef = [storage reference];
    NSString *extension = [[audioUrl lastPathComponent] pathExtension];
    NSString *audioName = [NSString stringWithFormat:@"%@.%@", [Util convertDateTimeToString:[NSDate date]], extension];
    NSString *storagePath = [NSString stringWithFormat:@"file/%@", audioName];
    FIRStorageReference *fileRef = [storageRef child:storagePath];
    
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    FIRStorageUploadTask *uploadTask = [fileRef putData:audioData
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
                  [self registerSermon:audioName videoPath:URL.absoluteString isAudio:YES];
              }
        }];
      }
    }];
}
- (void) registerSermon:(NSString *)videoName videoPath:(NSString *)videoPath isAudio:(BOOL) isAudio{
    NSString *topic = [Util trim:_edtTopic.text];;
    NSString *language = languageCode[(int)[self.edtLanguage selectedRow]];
    
    PFObject *object = [PFObject objectWithClassName:PARSE_TABLE_SERMON];
    object[PARSE_OWNER] = [PFUser currentUser];
    object[PARSE_TYPE] = [NSNumber numberWithInt:self.type];
    object[PARSE_TOPIC] = topic;
    object[PARSE_RAISER] = @"";
    object[PARSE_MOSQUE] = @"";
    object[PARSE_VIDEO] = videoPath;
    object[PARSE_VIDEO_NAME] = videoName;
    object[PARSE_IS_DELETE] = [NSNumber numberWithBool:NO];
    object[PARSE_IS_AUDIO] = [NSNumber numberWithBool:isAudio];
    object[PARSE_LANGUAGE] = language;
    int index = (int)[self.edtAmount selectedRow];
    if (index == -1) {
        index = 0;
    }
    object[PARSE_AMOUNT] = ARRAY_AMOUNT[index];
    
    [object saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        [SVProgressHUD dismiss];
        if (error) {
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        }else{
            NSString *message = [NSString stringWithFormat:@"Imam %@ is going live for JUMAH SERMON and will talk about \n %@", [PFUser currentUser][PARSE_MOSQUE], topic];
            if (self.type == TYPE_REGULAR) {
                message = [NSString stringWithFormat:@"Imam %@ is going live for REGULAR SERMON and will talk about \n %@", [PFUser currentUser][PARSE_MOSQUE], topic];
            }
            [Util sendPushAllNotification:message type:self.type];
            [self.navigationController popViewControllerAnimated:YES];
        }
    }];
}
@end
