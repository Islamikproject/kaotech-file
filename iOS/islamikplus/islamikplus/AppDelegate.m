//
//  AppDelegate.m
//  ScreenBreak
//
//  Created by Ales Gabrysz on 19/09/2019.
//  Copyright © 2019 Ales Gabrysz. All rights reserved.
//

#import "AppDelegate.h"
#import "PrivacyViewController.h"
#import "LoginViewController.h"
@import GooglePlaces;

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
    
    self.window = [[UIWindow alloc] initWithFrame:UIScreen.mainScreen.bounds];
    BOOL agree = [[NSUserDefaults standardUserDefaults] boolForKey:SYSTEM_KEY_AGREE];
    UINavigationController * mainNav = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"AppMainNav"];
    if(!agree){
        PrivacyViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"PrivacyViewController"];
        [mainNav setViewControllers:@[controller] animated:NO];
    } else{
        LoginViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"LoginViewController"];
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

@end
