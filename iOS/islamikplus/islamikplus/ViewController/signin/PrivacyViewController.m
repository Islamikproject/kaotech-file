//
//  PrivacyViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/20/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "PrivacyViewController.h"
#import "TermsConditionsViewController.h"
#import <WebKit/WebKit.h>

@interface PrivacyViewController ()
@property (weak, nonatomic) IBOutlet WKWebView *webView;
@end

@implementation PrivacyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    NSString *htmlFile = [[NSBundle mainBundle] pathForResource:@"PrivacyPolicy" ofType:@"html"];
    NSURL *instructionsURL = [NSURL fileURLWithPath:htmlFile];
    [self.webView loadRequest:[NSURLRequest requestWithURL:instructionsURL]];
}

- (IBAction)onAgreeClick:(id)sender {
    TermsConditionsViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"TermsConditionsViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}

@end
