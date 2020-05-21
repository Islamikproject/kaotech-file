//
//  MainViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "MainViewController.h"
#import "LoginViewController.h"
#import "SettingsViewController.h"

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
    [alert showQuestion:@"ISLAMIK+" subTitle:msg closeButtonTitle:nil duration:0.0f];
}
- (IBAction)onJumahClick:(id)sender {
    
}
- (IBAction)onRegularClick:(id)sender {
    
}
- (IBAction)onDonationClick:(id)sender {
    
}
- (IBAction)onSettingsClick:(id)sender {
    SettingsViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SettingsViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}

@end
