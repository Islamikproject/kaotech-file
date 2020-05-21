//
//  TermsViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "TermsViewController.h"

@interface TermsViewController ()
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UIWebView *webView;
@end

@implementation TermsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    NSString *htmlFile = [[NSBundle mainBundle] pathForResource:@"TermsConditions" ofType:@"html"];
    if(self.runMode == RUN_MODE_TERMS){
        self.lblTitle.text = @"TERMS AND CONDITIONS";
    }else{
        self.lblTitle.text = @"PRIVACY POLICY";
        htmlFile = [[NSBundle mainBundle] pathForResource:@"PrivacyPolicy" ofType:@"html"];
    }
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

@end
