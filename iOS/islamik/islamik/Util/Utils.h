#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "AppDelegate.h"

@interface Util : NSObject
+ (BOOL) isConnectableInternet;
+ (void) sendPushNotification:(NSString *)email message:(NSString *)message type:(int)type;
+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message;
+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message finish:(void (^)(void))finish;
+ (void)showAlertTitle:(UIViewController *)vc title:(NSString *)title message:(NSString *)message info:(BOOL)info;

+ (void) setLoginUserName:(NSString*) userName password:(NSString*) password;
+ (NSString*) getLoginUserName;
+ (NSString*) getLoginUserPassword;

+ (NSString *) trim:(NSString *) string;
+ (AppDelegate*) appDelegate;

+ (NSString*) convertDateToString:(NSDate*)date;
+ (NSString*) convertDateTimeToString:(NSDate*)date;
+ (NSDate*) convertStringToDateTime:(NSString*)date time:(NSString *)time;

+ (UIImage *)getUploadingImageFromImage:(UIImage *)image;
+ (UIImage *)getUploadingUserImageFromImage:(UIImage *)image;
+ (UIImage *)getUploadingInstagrameImageFromImage:(UIImage *)image;

+ (BOOL) isPhotoAvaileble;
+ (BOOL) isCameraAvailable;
+ (NSMutableArray *) getLanguageCodeList;
+ (NSMutableArray *) getLanguageNameList;

+ (NSArray *) getEnglishVerseArray:(NSString *)key;
+ (NSArray *) getArabicVerseArray:(NSString *)key;
+ (NSString *) getVerseString:(NSArray *)data start:(NSInteger)start end:(NSInteger)end;
+ (NSString *) getReciterPath:(NSInteger)chapter;

@end
