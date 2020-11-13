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
#import "SermonListViewController.h"
#import "DonationViewController.h"
#import "MessagesViewController.h"
#import "DailyViewController.h"
#import "NotificationViewController.h"
#import "BookViewController.h"

@interface MainViewController ()
@property (weak, nonatomic) IBOutlet UIView *viewJumah;
@property (weak, nonatomic) IBOutlet UIView *viewBook;
@property (weak, nonatomic) IBOutlet UIView *viewDaily;

@end

@implementation MainViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
}

- (void) initialize {
    int type = [[PFUser currentUser][PARSE_TYPE] intValue];
    self.viewJumah.hidden = YES;
    self.viewBook.hidden = YES;
    self.viewDaily.hidden = YES;
    if (type == TYPE_ADMIN) {
        self.viewJumah.hidden = NO;
        self.viewDaily.hidden = NO;
    } else if (type == TYPE_MOSQUE) {
        self.viewJumah.hidden = NO;
    } else if (type == TYPE_INFLUENCER_WOMEN || type == TYPE_INFLUENCER_KID || type == TYPE_INFLUENCER_OTHER) {
        self.viewBook.hidden = NO;
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
- (IBAction)onNotificationClick:(id)sender {
    NotificationViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"NotificationViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onJumahClick:(id)sender {
    SermonListViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SermonListViewController"];
    controller.type = TYPE_JUMAH;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onRegularClick:(id)sender {
    SermonListViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SermonListViewController"];
    controller.type = TYPE_REGULAR;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onDonationClick:(id)sender {
    DonationViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"DonationViewController"];
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
- (IBAction)onBookClick:(id)sender {
    BookViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"BookViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onDailyClick:(id)sender {
    DailyViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"DailyViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
@end
