//
//  JumahSermonViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 11/16/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "JumahSermonViewController.h"
#import "SermonViewController.h"

@interface JumahSermonViewController ()

@end

@implementation JumahSermonViewController

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
- (IBAction)onAfricaClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_MOSQUE continent:CONTINENT_AFRICA];
}
- (IBAction)onAmericaClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_MOSQUE continent:CONTINENT_AMERICA];
}
- (IBAction)onAsiaClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_MOSQUE continent:CONTINENT_ASIA];
}
- (IBAction)onAustraliaClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_MOSQUE continent:CONTINENT_AUSTRALIA];
}
- (IBAction)onEuropaClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_MOSQUE continent:CONTINENT_EUROPA];
}
- (IBAction)onScholarsClick:(id)sender {
    [self gotoNextView:TYPE_REGULAR user:TYPE_USTHADH continent:-1];
}
- (IBAction)onWomenClick:(id)sender {
    [self gotoNextView:TYPE_REGULAR user:TYPE_INFLUENCER_WOMEN continent:-1];
}
- (IBAction)onKidsClick:(id)sender {
    [self gotoNextView:TYPE_REGULAR user:TYPE_INFLUENCER_KID continent:-1];
}
- (IBAction)onOthersClick:(id)sender {
    [self gotoNextView:TYPE_REGULAR user:TYPE_INFLUENCER_OTHER continent:-1];
}

- (void) gotoNextView:(int) type user:(int) user continent:(int) continent {
    SermonViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SermonViewController"];
    controller.sermonType = type;
    controller.userType = user;
    controller.continentType = continent;
    [self.navigationController pushViewController:controller animated:YES];
}
@end
