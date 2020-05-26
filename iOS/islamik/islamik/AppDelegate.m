//
//  AppDelegate.m
//  ScreenBreak
//
//  Created by Ales Gabrysz on 19/09/2019.
//  Copyright © 2019 Ales Gabrysz. All rights reserved.
//

#import "AppDelegate.h"
#import "OnboardViewController.h"
#import "MainViewController.h"
#import "Config.h"

@interface AppDelegate ()
@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    [GMSPlacesClient provideAPIKey:@"AIzaSyDxZ0qrWBRaC5Lb4IieW-pN0f0PpEGqlj4"];
    [PFUser enableAutomaticUser];
    [Parse initializeWithConfiguration:[ParseClientConfiguration configurationWithBlock:^(id<ParseMutableClientConfiguration> configuration) {
        configuration.applicationId = @"b0484b2d-2135-4a2d-a924-b916750cf001";
        configuration.clientKey = @"1761390b-b5d1-4a1a-a7f4-4f4428dc9001";
        configuration.server = @"https://parse.kaotech.org:20001/parse";
    }]];
    [PFUser enableRevocableSessionInBackground];
    [Stripe setDefaultPublishableKey:@"pk_test_TYooMQauvdEDq54NiTphI7jx"];
    
    // Push Notification
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0) {
        [[UIApplication sharedApplication] registerUserNotificationSettings:[UIUserNotificationSettings settingsForTypes:(UIUserNotificationTypeSound | UIUserNotificationTypeAlert | UIUserNotificationTypeBadge) categories:nil]];
        [[UIApplication sharedApplication] registerForRemoteNotifications];
    } else {
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:UIRemoteNotificationTypeSound | UIRemoteNotificationTypeAlert | UIRemoteNotificationTypeBadge];
    }
    
    self.window = [[UIWindow alloc] initWithFrame:UIScreen.mainScreen.bounds];
    BOOL agree = [[NSUserDefaults standardUserDefaults] boolForKey:SYSTEM_KEY_AGREE];
    UINavigationController * mainNav = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"AppMainNav"];
    if(!agree){
        OnboardViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"OnboardViewController"];
        [mainNav setViewControllers:@[controller] animated:NO];
    } else{
        MainViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"MainViewController"];
        [mainNav setViewControllers:@[controller] animated:NO];
    }
    self.window.rootViewController = mainNav;
    [self.window makeKeyAndVisible];
    return YES;
}
- (void) application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken{
    PFInstallation *currentInstallation = [PFInstallation currentInstallation];
    [currentInstallation setDeviceTokenFromData:deviceToken];
    [currentInstallation saveInBackground];
}


- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    application.applicationIconBadgeNumber = 0;
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    [NSNotificationCenter.defaultCenter postNotificationName:NOTIFICATION_BACKGROUND object:nil];
}


- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    
    NSInteger pushType = [[userInfo objectForKey:PUSH_NOTIFICATION_TYPE] integerValue];
    
    application.applicationIconBadgeNumber = 0;
    if (application.applicationState == UIApplicationStateInactive || application.applicationState == UIApplicationStateBackground) {
        [PFAnalytics trackAppOpenedWithRemoteNotificationPayload:userInfo];
    } else { // active status
        PFInstallation *currentInstallation = [PFInstallation currentInstallation];
        currentInstallation.badge = 0;
        [currentInstallation saveInBackground];
    }
}
@end
