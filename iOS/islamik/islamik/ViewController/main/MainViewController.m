//
//  MainViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/22/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "MainViewController.h"
#import "SermonViewController.h"
#import "DailyPrayersViewController.h"

@interface MainViewController ()

@end

@implementation MainViewController

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
- (IBAction)onSermonClick:(id)sender {
    SermonViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SermonViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onPrayersClick:(id)sender {
    DailyPrayersViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"DailyPrayersViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onSalatClick:(id)sender {
    
}
- (IBAction)onJumahClick:(id)sender {
    
}
- (IBAction)onNafilahClick:(id)sender {
    
}
- (IBAction)onQuranClick:(id)sender {
    
}

@end
