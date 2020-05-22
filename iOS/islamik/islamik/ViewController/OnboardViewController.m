//
//  OnboardViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/22/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "OnboardViewController.h"
#import "MainViewController.h"

@interface OnboardViewController ()

@end

@implementation OnboardViewController

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
- (IBAction)onNextClick:(id)sender {
    NSUserDefaults * userDefault = [NSUserDefaults standardUserDefaults];
     [userDefault setBool:YES forKey:SYSTEM_KEY_AGREE];
     [userDefault synchronize];
     MainViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"MainViewController"];
     [self.navigationController pushViewController:controller animated:YES];
}

@end
