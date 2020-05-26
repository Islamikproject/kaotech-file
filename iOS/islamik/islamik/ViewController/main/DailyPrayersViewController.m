//
//  DailyPrayersViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/25/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "DailyPrayersViewController.h"
#import "PrayersViewController.h"

@interface DailyPrayersViewController ()

@end

@implementation DailyPrayersViewController

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
- (IBAction)onFajrClick:(id)sender {
    PrayersViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"PrayersViewController"];
    controller.type = TYPE_FAJR;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onZuhrClick:(id)sender {
    PrayersViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"PrayersViewController"];
    controller.type = TYPE_ZUHR;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onAsrClick:(id)sender {
    PrayersViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"PrayersViewController"];
    controller.type = TYPE_ASR;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onMaghribClick:(id)sender {
    PrayersViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"PrayersViewController"];
    controller.type = TYPE_MAGHRIB;
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onIshaClick:(id)sender {
    PrayersViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"PrayersViewController"];
    controller.type = TYPE_ISHA;
    [self.navigationController pushViewController:controller animated:YES];
}

@end
