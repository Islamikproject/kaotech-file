//
//  MainViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/22/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "MainViewController.h"
#import "SermonViewController.h"
#import "DailyPrayersViewController.h"
#import "SalatViewController.h"
#import "PrayersViewController.h"
#import "NafilahViewController.h"
#import "QuranViewController.h"
#import "SettingsViewController.h"
#import "LoginViewController.h"
#import "MessagesViewController.h"

@interface MainViewController ()

@end

@implementation MainViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onSermonClick:(id)sender {
    SermonViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SermonViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onPrayersClick:(id)sender {
    DailyPrayersViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"DailyPrayersViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onSalatClick:(id)sender {
    SalatViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SalatViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onJumahClick:(id)sender {
    PrayersViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"PrayersViewController"];
    controller.type = TYPE_JUMAH_FAJR;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onNafilahClick:(id)sender {
    NafilahViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"NafilahViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onQuranClick:(id)sender {
    QuranViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"QuranViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onMessagesClick:(id)sender {
    MessagesViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"MessagesViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onSettingsClick:(id)sender {
    SettingsViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SettingsViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onLogoutClick:(id)sender {
    NSString *msg = @"Are you sure you want to logout?";
    SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
    alert.customViewColor = MAIN_COLOR;
    alert.horizontalButtons = YES;
    [alert addButton:@"Cancel" actionBlock:^(void) {
    }];
    [alert addButton:@"Okay" actionBlock:^(void) {
        [SVProgressHUD showWithStatus:@"Log out..." maskType:SVProgressHUDMaskTypeGradient];
        [PFUser logOutInBackgroundWithBlock:^(NSError *error){
            [SVProgressHUD dismiss];
            if (error){
                [Util showAlertTitle:self title:@"Log out" message:[error localizedDescription]];
            } else {
                [Util setLoginUserName:@"" password:@""];
                for(UIViewController * vc in self.navigationController.viewControllers){
                    if ([vc isKindOfClass:[LoginViewController class]]){
                        [self.navigationController popToViewController:vc animated:YES];
                        [UIApplication sharedApplication].applicationIconBadgeNumber = 0;
                        break;
                    }
                }
            }
        }];
    }];
    [alert showQuestion:@"ISLAMIK" subTitle:msg closeButtonTitle:nil duration:0.0f];
}

@end
