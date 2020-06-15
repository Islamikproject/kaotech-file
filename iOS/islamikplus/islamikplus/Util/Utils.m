#import "Utils.h"
#import <AssetsLibrary/AssetsLibrary.h>
#import "Config.h"
#import <Photos/Photos.h>
#import "SCLAlertView.h"
#import "Reachability.h"
#import "UIImageView+AFNetworking.h"

@implementation Util

+ (void) sendPushAllNotification:(NSString *)message type:(int)type{
    NSDictionary *data = [NSDictionary dictionaryWithObjectsAndKeys:
                          message, @"alert",
                          @"Increment", @"badge",
                          @"cheering.caf", @"sound",
                          @"", @"data",
                          [NSNumber numberWithInt:type], @"type",
                          nil];
    
    [PFCloud callFunctionInBackground:@"SendPushAll" withParameters:data block:^(id object, NSError *err) {
        if (err) {
            NSLog(@"Fail APNS: %@", message);
        } else {
            NSLog(@"Success APNS: %@", message);
        }
    }];
}

+ (BOOL) isConnectableInternet {
    Reachability *networkReachability = [Reachability reachabilityForInternetConnection];
    NetworkStatus networkStatus = [networkReachability currentReachabilityStatus];
    if (networkStatus == NotReachable) {
        NSLog(@"There IS NO internet connection");
        return NO;
    } else {
        NSLog(@"There IS internet connection");
        return YES;
    }
}


+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message
{
    SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
    
    alert.shouldDismissOnTapOutside = YES;
    alert.showAnimationType = SCLAlertViewShowAnimationSimplyAppear;
    [alert alertIsDismissed:^{
    }];
    alert.customViewColor = MAIN_COLOR;
    
    [alert showInfo:vc title:title subTitle:message closeButtonTitle:@"OK" duration:0.0f];
}
+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message finish:(void (^)(void))finish
{
    SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
    
    alert.shouldDismissOnTapOutside = YES;
    alert.showAnimationType = SCLAlertViewShowAnimationSimplyAppear;
    [alert alertIsDismissed:^{
        if (finish) {
            finish ();
        }
    }];
    [alert setForceHideBlock:^{
        if (finish) {
            finish ();
        }
    }];
    alert.customViewColor = MAIN_COLOR;
    [alert showInfo:vc title:title subTitle:message closeButtonTitle:@"OK" duration:0.0f];
}

+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message info:(BOOL)info
{
    SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
    
    alert.shouldDismissOnTapOutside = YES;
    alert.showAnimationType = SCLAlertViewShowAnimationSimplyAppear;
    [alert alertIsDismissed:^{
    }];
    alert.customViewColor = MAIN_COLOR;
    
    if (info)
        [alert showInfo:vc title:title subTitle:message closeButtonTitle:@"OK" duration:0.0f];
    else
        [alert showQuestion:vc title:title subTitle:message closeButtonTitle:@"OK" duration:0.0f];
}

+ (NSString*) getLoginUserName {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *userName = [defaults objectForKey:SYSTEM_KEY_USERNAME];
    return userName;
}

+ (NSString*) getLoginUserPassword {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    NSString *password = [defaults objectForKey:SYSTEM_KEY_PASSWORD];
    return password;
}
+ (void) setLoginUserName:(NSString*) userName password:(NSString*) password {
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    [defaults setObject:userName forKey:SYSTEM_KEY_USERNAME];
    [defaults setObject:password forKey:SYSTEM_KEY_PASSWORD];
    if (userName.length > 0) {
        [defaults setBool:YES forKey:SYSTEM_KEY_AUTO];
    } else {
        [defaults setBool:NO forKey:SYSTEM_KEY_AUTO];
    }
    [defaults synchronize];
    // Installation
    if (userName.length > 0 && password.length > 0) {
        PFInstallation *currentInstallation = [PFInstallation currentInstallation];
        [currentInstallation setObject:[PFUser currentUser] forKey:@"owner"];
        [currentInstallation saveInBackground];
    } else {
        PFInstallation *currentInstallation = [PFInstallation currentInstallation];
        [currentInstallation removeObjectForKey:@"owner"];
        [currentInstallation saveInBackground];
    }
}
+ (NSString *) trim:(NSString *) string {
    NSString *newString = [string stringByTrimmingCharactersInSet: [NSCharacterSet whitespaceCharacterSet]];
    return newString;
}
+ (AppDelegate*) appDelegate{
    return (AppDelegate*)[[UIApplication sharedApplication] delegate];
}

+ (NSString*) convertDateToString:(NSDate*)date
{
    NSDateFormatter * formatter = [NSDateFormatter new];
    [formatter setDateFormat:@"MMM dd, yyyy"];
    return [formatter stringFromDate:date];
}
+ (NSString*) convertDateTimeToString:(NSDate*)date
{
    NSDateFormatter * formatter = [NSDateFormatter new];
    [formatter setDateFormat:@"yyyy-MM-dd_hhmmss"];
    return [formatter stringFromDate:date];
}
+ (UIImage *)getUploadingImageFromImage:(UIImage *)image {
    CGFloat maxResolution = 640.f;
    if (image.size.width < maxResolution) {
        CGSize newSize = CGSizeMake(image.size.width, image.size.height);
        UIGraphicsBeginImageContext(newSize);
        // CGContextSetFillColorWithColor(UIGraphicsGetCurrentContext(), [UIColor blackColor].CGColor);
        // CGContextFillRect(UIGraphicsGetCurrentContext(), CGRectMake(0, 0, newSize.width, newSize.height));
        [image drawInRect:CGRectMake(0,
                                     0,
                                     image.size.width,
                                     image.size.height)];
        UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        return newImage;
    } else {
        CGFloat rate = image.size.width / maxResolution;
        CGSize newSize = CGSizeMake(maxResolution, image.size.height / rate);
        UIGraphicsBeginImageContext(newSize);
        [image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
        UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        return newImage;
    }
}
+ (UIImage *)getUploadingUserImageFromImage:(UIImage *)image
{
    CGSize imageSize = image.size;
    if(imageSize.width > imageSize.height){
        imageSize.width = imageSize.height;
    }else if(imageSize.height > imageSize.width){
        imageSize.height = imageSize.width;
    }
    UIGraphicsBeginImageContext(imageSize);
    CGRect drawRect = CGRectMake( - ((image.size.width - imageSize.width)/2.f),
                                 - ((image.size.height - imageSize.height)/2.f),
                                 image.size.width,
                                 image.size.height);
    [image drawInRect:drawRect];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}
+ (UIImage *)getUploadingInstagrameImageFromImage:(UIImage *)image
{
    CGSize  imageSize = image.size;
    if(((imageSize.height * image.scale) >= 612 && (imageSize.width * image.scale) >= 612)){
        return image;
    }
    CGFloat minResolution = 613.f;
    float scale = minResolution / imageSize.width;
    if (minResolution / imageSize.height > scale) {
        scale = minResolution / imageSize.height;
    }
    CGSize newSize = CGSizeMake(imageSize.width * scale , imageSize.height * scale);
    UIGraphicsBeginImageContext(newSize);
    [image drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}
+ (BOOL) isPhotoAvaileble {
    if ([PHPhotoLibrary authorizationStatus] == PHAuthorizationStatusDenied || [PHPhotoLibrary authorizationStatus] == PHAuthorizationStatusRestricted){
        return NO;
    } else if ([PHPhotoLibrary authorizationStatus] == PHAuthorizationStatusNotDetermined){
        [PHPhotoLibrary requestAuthorization:^(PHAuthorizationStatus status){
        }];
        return YES;
    } else {
        return YES;
    }
}

+ (BOOL) isCameraAvailable {
    if (NSFoundationVersionNumber >= NSFoundationVersionNumber_iOS_7_0) {
        AVAuthorizationStatus status = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
        if (status == AVAuthorizationStatusDenied || status == AVAuthorizationStatusRestricted) {
            return NO;
        }
        else if (status == AVAuthorizationStatusNotDetermined) {
            [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo completionHandler:nil];
            return YES;
        }
        return YES;
    }
    else
        return YES;
}
@end
