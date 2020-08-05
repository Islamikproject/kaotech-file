//
//  SettingsViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "SettingsViewController.h"
#import "EditProfileViewController.h"
#import "PaymentViewController.h"
#import "AboutViewController.h"
#import "TermsViewController.h"
#import "LoginViewController.h"
#import <MessageUI/MessageUI.h>

@interface SettingsViewController ()<MFMailComposeViewControllerDelegate>

@end

@implementation SettingsViewController

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
- (IBAction)onProfileClick:(id)sender {
    EditProfileViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"EditProfileViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onPaymentClick:(id)sender {
    PaymentViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"PaymentViewController"];
    controller.IS_SIGNUP = NO;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onRateAppClick:(id)sender {
    NSString *msg = @"If you love our app, we would dig it if you take a couple of seconds to rate us in the app market.";
    SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
    alert.customViewColor = MAIN_COLOR;
    alert.horizontalButtons = YES;
    
    [alert addButton:@"RATE APP" actionBlock:^(void) {
        NSString * url = [NSString stringWithFormat:@"itms-apps://itunes.apple.com/app/id%@", @"1519147738"];
        if ([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString: url]]){
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
        }
    }];
    [alert addButton:@"CLOSE" actionBlock:^(void) {}];
    [alert showInfo:@"Rate App" subTitle:msg closeButtonTitle:nil duration:0.0f];
}
- (IBAction)onSendFeedbackClick:(id)sender {
    if([MFMailComposeViewController canSendMail]) {
        MFMailComposeViewController *mailCont = [[MFMailComposeViewController alloc] init];
        mailCont.mailComposeDelegate = self;
        [mailCont setSubject:@"Send Feedback"];
        [mailCont setToRecipients:[NSArray arrayWithObject:@"admin@osseniapp.com"]];
        [mailCont setMessageBody:@"" isHTML:NO];
        [self presentModalViewController:mailCont animated:YES];
    }else{
        [Util showAlertTitle:self title:@"Error" message:@"You can't send email with this device. Please config your mail account first."];
    }
}
- (IBAction)onAboutAppClick:(id)sender {
    AboutViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"AboutViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onPrivacyPolicyClick:(id)sender {
    TermsViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"TermsViewController"];
    controller.runMode = RUN_MODE_PRIVACY;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onTermsConditionClick:(id)sender {
    TermsViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"TermsViewController"];
    controller.runMode = RUN_MODE_TERMS;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onLogOutClick:(id)sender {
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
- (void)mailComposeController:(MFMailComposeViewController*)controller didFinishWithResult:(MFMailComposeResult)result error:(NSError*)error {
    [self dismissModalViewControllerAnimated:YES];
}
@end
