//
//  LoginViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/20/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "LoginViewController.h"
#import "ResetPasswordViewController.h"
#import "MainViewController.h"
#import "TermsViewController.h"

@interface LoginViewController ()
@property (weak, nonatomic) IBOutlet UITextField *edtPhoneNumber;
@property (weak, nonatomic) IBOutlet UITextField *edtPassword;

@end

@implementation LoginViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
}
- (void) initialize {
    BOOL isAuto = [[NSUserDefaults standardUserDefaults] boolForKey:SYSTEM_KEY_AUTO];
    NSString *phonenumber = [Util getLoginUserName];
    NSString *password = [Util getLoginUserPassword];
    if (phonenumber.length > 0 && password.length > 0){
        _edtPhoneNumber.text = phonenumber;
        _edtPassword.text = password;
        if (isAuto) {
            [self login:phonenumber password:password];
        }
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
- (IBAction)onLoginClick:(id)sender {
    NSString *phonenumber = _edtPhoneNumber.text;
    NSString *password = _edtPassword.text;
    if ([self isValid]) {
        [self login:phonenumber password:password];
    }
}
- (IBAction)onForgotPasswordClick:(id)sender {
    ResetPasswordViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"ResetPasswordViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onTermsClick:(id)sender {
    TermsViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"TermsViewController"];
    controller.runMode = RUN_MODE_TERMS;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onPrivacyClick:(id)sender {
    TermsViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"TermsViewController"];
    controller.runMode = RUN_MODE_PRIVACY;
    [self.navigationController pushViewController:controller animated:YES];
}

- (BOOL) isValid {
    NSString *phonenumber = _edtPhoneNumber.text;
    NSString *password = _edtPassword.text;
    NSString * errorMsg = @"";
    if (phonenumber.length == 0) {
        errorMsg = @"Please enter your phone number.";
    } else if (password.length == 0) {
        errorMsg = @"Please enter your password.";
    }
    if (errorMsg.length > 0) {
        [Util showAlertTitle:self title:@"Login" message:errorMsg];
        return NO;
    }
    return YES;
}
- (void) login: (NSString *) phonenumber password: (NSString *) password {
    if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return;
    }
    [SVProgressHUD setForegroundColor:MAIN_COLOR];
    [SVProgressHUD showWithStatus:@"Please wait..." maskType:SVProgressHUDMaskTypeGradient];
    [PFUser logInWithUsernameInBackground:phonenumber password:password block:^(PFUser *user, NSError *error) {
        [SVProgressHUD dismiss];
        if (user) {
            int userType = [user[PARSE_TYPE] intValue];
            if (userType == TYPE_USER) {
                [Util showAlertTitle:self title:@"Login Failed" message:@"This user is customer."];
            } else {
                [Util setLoginUserName:phonenumber password:password];
                [self gotoNextScreen];
            }
        } else {
            NSString *errorString = @"Password entered is incorrect.";
            [Util showAlertTitle:self title:@"Login Failed" message:errorString finish:^{
                [self->_edtPassword becomeFirstResponder];
            }];
        }
    }];
}
- (void) gotoNextScreen {
    MainViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"MainViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
@end
