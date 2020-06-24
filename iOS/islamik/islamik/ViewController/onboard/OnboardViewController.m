//
//  OnboardViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/22/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "OnboardViewController.h"
#import "PrivacyViewController.h"

@interface OnboardViewController ()

@end

@implementation OnboardViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (IBAction)onNextClick:(id)sender {
    PrivacyViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"PrivacyViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}

@end
