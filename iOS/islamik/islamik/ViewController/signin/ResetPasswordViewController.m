//
//  ResetPasswordViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "ResetPasswordViewController.h"

@interface ResetPasswordViewController ()
@property (weak, nonatomic) IBOutlet UITextField *edtEmail;

@end

@implementation ResetPasswordViewController

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
- (IBAction)onResetPasswordClick:(id)sender {
    if ([self isValid]) {
        [self resetPassword];
    }
}

- (BOOL) isValid {
    _edtEmail.text = [Util trim:_edtEmail.text];
    NSString *email = _edtEmail.text;
    NSString * errorMsg = @"";
    if (email.length == 0){
        errorMsg = @"Please enter your email.";
    }else if(![email isEmail]){
        errorMsg = @"Email address is invalid.";
    }else if([email containsString:@".."]){
        errorMsg = @"Email address is invalid.";
    }
    if(errorMsg.length > 0){
        [Util showAlertTitle:self title:@"Reset Password" message:errorMsg];
        return NO;
    } else if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return NO;
    }
    return YES;
}

- (void) resetPassword {
    self.edtEmail.text = [Util trim:self.edtEmail.text.lowercaseString];
    NSString *email = self.edtEmail.text;
    [SVProgressHUD showWithStatus:@"please_wait" maskType:SVProgressHUDMaskTypeGradient];
    [PFUser requestPasswordResetForEmailInBackground:email block:^(BOOL succeeded,NSError *error) {
        [SVProgressHUD dismiss];
        if (!error) {
            [Util showAlertTitle:self
                           title:@"Success"
                         message: @"We've sent a password reset link to your email."
                          finish:^(void) {
                              [self onBack:nil];
                          }];
        } else {
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        }
    }];
}
@end
