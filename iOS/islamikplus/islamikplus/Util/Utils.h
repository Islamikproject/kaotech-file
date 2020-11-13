#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "AppDelegate.h"

@interface Util : NSObject
+ (void) sendPushAllNotification:(NSString *)message type:(int)type;
+ (void) sendPushNotification:(NSString *)email message:(NSString *)message type:(int)type;
+ (BOOL) isConnectableInternet;
+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message;
+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message finish:(void (^)(void))finish;
+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message info:(BOOL)info;

+ (void) setLoginUserName:(NSString*) userName password:(NSString*) password;
+ (NSString*) getLoginUserName;
+ (NSString*) getLoginUserPassword;

+ (NSString *) trim:(NSString *) string;
+ (AppDelegate*) appDelegate;

+ (NSString*) convertDateToString:(NSDate*)date;
+ (NSString*) convertNotificationDateTimeToString:(NSDate*)date;
+ (NSString*) convertDateTimeToString:(NSDate*)date;

+ (UIImage *)getUploadingImageFromImage:(UIImage *)image;
+ (UIImage *)getUploadingUserImageFromImage:(UIImage *)image;
+ (UIImage *)getUploadingInstagrameImageFromImage:(UIImage *)image;

+ (BOOL) isPhotoAvaileble;
+ (BOOL) isCameraAvailable;
+ (NSMutableArray *) getLanguageCodeList;
+ (NSMutableArray *) getLanguageNameList;
@end
