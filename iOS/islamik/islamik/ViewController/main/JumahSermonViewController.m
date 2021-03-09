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
    [self gotoNextView:TYPE_JUMAH user:TYPE_MOSQUE];
}
- (IBAction)onAmericaClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_MOSQUE];
}
- (IBAction)onAsiaClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_MOSQUE];
}
- (IBAction)onAustraliaClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_MOSQUE];
}
- (IBAction)onEuropaClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_MOSQUE];
}
- (IBAction)onScholarsClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_USTHADH];
}
- (IBAction)onWomenClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_INFLUENCER_WOMEN];
}
- (IBAction)onKidsClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_INFLUENCER_KID];
}
- (IBAction)onOthersClick:(id)sender {
    [self gotoNextView:TYPE_JUMAH user:TYPE_INFLUENCER_OTHER];
}

- (void) gotoNextView:(int) type user:(int) user {
    SermonViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SermonViewController"];
    controller.sermonType = type;
    controller.userType = user;
    [self.navigationController pushViewController:controller animated:YES];
}
@end
