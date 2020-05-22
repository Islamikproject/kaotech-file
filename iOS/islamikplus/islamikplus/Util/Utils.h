#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "AppDelegate.h"

@interface Util : NSObject
+ (void) sendPushAllNotification:(NSString *)message type:(int)type;
+ (BOOL) isConnectableInternet;
+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message;
+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message finish:(void (^)(void))finish;
+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message info:(BOOL)info;

+ (void) setLoginUserName:(NSString*) userName password:(NSString*) password;
+ (NSString*) getLoginUserName;
+ (NSString*) getLoginUserPassword;

+ (NSString *) trim:(NSString *) string;
+ (NSString *) checkSpace:(NSString *) string;
+ (AppDelegate*) appDelegate;

+ (NSString*) convertDateToString:(NSDate*)date;
+ (NSString*) convertDateTimeToString:(NSDate*)date;

+ (BOOL) stringContainsInArray:(NSString*)string :(NSArray*)stringArray;
+ (BOOL) stringContainNumber:(NSString *) string;
+ (BOOL) stringContainLetter:(NSString *) string;
+ (BOOL) isContainsUpperCase:(NSString *) password;
+ (BOOL) isContainsLowerCase:(NSString *) password;
+ (BOOL) isContainsNumber:(NSString *) password;
+ (BOOL) isContainsSpecial:(NSString *) password;
+ (BOOL) stringIsNumber:(NSString*) str;

+ (UIImage *)getUploadingImageFromImage:(UIImage *)image;
+ (UIImage *)getUploadingUserImageFromImage:(UIImage *)image;
+ (UIImage *)getUploadingInstagrameImageFromImage:(UIImage *)image;

+ (BOOL) isPhotoAvaileble;
+ (BOOL) isCameraAvailable;
@end
