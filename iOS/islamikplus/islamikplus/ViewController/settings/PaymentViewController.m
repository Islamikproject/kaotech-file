//
//  PaymentViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "PaymentViewController.h"
#import "MainViewController.h"
#import <WebKit/WebKit.h>

@interface PaymentViewController ()
@property (weak, nonatomic) IBOutlet UIButton *btnBack;
@property (weak, nonatomic) IBOutlet UIButton *btnDone;
@property (weak, nonatomic) IBOutlet WKWebView *webView;

@end

@implementation PaymentViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}
- (void) viewWillAppear:(BOOL)animated
{
    [self initialize];
}
- (void) initialize {
    self.btnBack.hidden = YES;
    self.btnDone.hidden = YES;
    if (self.IS_SIGNUP) {
        self.btnDone.hidden = NO;
    } else {
        self.btnBack.hidden = NO;
    }
    NSString *phonenumber = [Util getLoginUserName];
    NSString *password = [Util getLoginUserPassword];
    NSString * url = [NSString stringWithFormat:@"https://stripe.kaotech.org?email=%@&password=%@", phonenumber, password];
    NSURL *nsUrl = [NSURL URLWithString:url];
    NSURLRequest *request = [NSURLRequest requestWithURL:nsUrl cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData timeoutInterval:30];

    [self.webView loadRequest:request];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onDoneClick:(id)sender {
    MainViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"MainViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}

@end
