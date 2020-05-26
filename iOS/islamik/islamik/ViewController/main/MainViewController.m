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
#import "SalatViewController.h"
#import "PrayersViewController.h"
#import "NafilahViewController.h"
#import "QuranViewController.h"

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
    SalatViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SalatViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onJumahClick:(id)sender {
    PrayersViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"PrayersViewController"];
    controller.type = TYPE_JUMAH_FAJR;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onNafilahClick:(id)sender {
    NafilahViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"NafilahViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onQuranClick:(id)sender {
    QuranViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"QuranViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}

@end
