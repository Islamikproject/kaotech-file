//
//  PostViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 11/12/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "PostViewController.h"

@interface PostViewController ()<UINavigationControllerDelegate, UIImagePickerControllerDelegate>{
    BOOL isCamera;
    BOOL isGallery;
    BOOL hasPhoto;
}
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;
@property (weak, nonatomic) IBOutlet UITextField *edtTitle;
@property (weak, nonatomic) IBOutlet UITextView *edtDescription;

@end

@implementation PostViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    if (self.mPostObj != nil) {
        self.lblTitle.text = @"EDIT POST";
    } else {
        self.lblTitle.text = @"CREAT POST";
    }
    self.edtDescription.layer.borderColor = [UIColor blackColor].CGColor;
    self.edtDescription.layer.borderWidth = 1.f;
    [self initialize];
}

- (void) initialize {
    self.edtTitle.text = @"";
    self.edtDescription.text = @"";
    if (self.mPostObj != nil) {
        self.edtTitle.text = self.mPostObj[PARSE_TITLE];
        self.edtDescription.text = self.mPostObj[PARSE_DESCRIPTION];
        PFFileObject *photoFile = self.mPostObj[PARSE_PHOTO];
        [self.imgPhoto sd_setImageWithURL:[NSURL URLWithString:photoFile.url] placeholderImage:[UIImage imageNamed:@"upload_photo"]];
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
- (IBAction)onUploadClick:(id)sender {
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
    [self presentViewController:actionsheet animated:YES completion:nil];
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
    UIImage *image = (UIImage *)[info valueForKey:UIImagePickerControllerOriginalImage];
    hasPhoto = YES;
    [_imgPhoto setImage:[Util getUploadingUserImageFromImage:image]];
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
- (IBAction)onSaveClick:(id)sender {
    if ([self isValid]) {
        if (self.mPostObj != nil) {
            [self save];
        } else {
            [self create];
        }
    }
}

- (BOOL) isValid {
    NSString *title = [Util trim:_edtTitle.text];
    NSString *description = [Util trim:_edtDescription.text];
    NSString * errorMsg = @"";
    if (title.length == 0) {
        errorMsg = @"Please enter title.";
    } else if (description.length == 0) {
        errorMsg = @"Please enter description.";
    } else if (!self.mPostObj && !hasPhoto) {
        errorMsg = @"Please upload photo.";
    }
    if (errorMsg.length > 0) {
        [Util showAlertTitle:self title:@"Error" message:errorMsg];
        return NO;
    } else if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return NO;
    }
    return YES;
}

- (void) create {
    NSString *title = [Util trim:_edtTitle.text];
    NSString *description = [Util trim:_edtDescription.text];
    PFObject *object = [PFObject objectWithClassName:PARSE_TABLE_POST];
    object[PARSE_OWNER] = [PFUser currentUser];
    object[PARSE_TITLE] = title;
    object[PARSE_DESCRIPTION] = description;
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

- (void) save {
    NSString *title = [Util trim:_edtTitle.text];
    NSString *description = [Util trim:_edtDescription.text];
    self.mPostObj[PARSE_OWNER] = [PFUser currentUser];
    self.mPostObj[PARSE_TITLE] = title;
    self.mPostObj[PARSE_DESCRIPTION] = description;
    if (hasPhoto){
        UIImage *profileImage = [Util getUploadingUserImageFromImage:_imgPhoto.image];
        NSData *imageData = UIImageJPEGRepresentation(profileImage, 0.8);
        self.mPostObj[PARSE_PHOTO] = [PFFileObject fileObjectWithData:imageData];
    }
    [SVProgressHUD showWithStatus:@"Please wait..." maskType:SVProgressHUDMaskTypeGradient];
    [self.mPostObj saveInBackgroundWithBlock:^(BOOL success, NSError* error){
        [SVProgressHUD dismiss];
        if (error) {
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        } else {
            [self onBack:nil];
        }
    }];
}
@end
