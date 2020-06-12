//
//  PrivacyViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/20/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "PrivacyViewController.h"
#import "LoginViewController.h"
#import <WebKit/WebKit.h>

@interface PrivacyViewController ()
@property (weak, nonatomic) IBOutlet WKWebView *webView;
@end

@implementation PrivacyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    NSString *htmlFile = [[NSBundle mainBundle] pathForResource:@"TermsConditions" ofType:@"html"];
    NSURL *instructionsURL = [NSURL fileURLWithPath:htmlFile];
    [self.webView loadRequest:[NSURLRequest requestWithURL:instructionsURL]];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onAgreeClick:(id)sender {
    NSUserDefaults * userDefault = [NSUserDefaults standardUserDefaults];
    [userDefault setBool:YES forKey:SYSTEM_KEY_AGREE];
    [userDefault synchronize];
    LoginViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"LoginViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}

@end
