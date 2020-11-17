//
//  JumahSermonViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 11/16/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "JumahSermonViewController.h"
#import "SermonViewController.h"

@interface JumahSermonViewController () {
    bool isShow;
}
@property (weak, nonatomic) IBOutlet UIButton *btnJumah;
@property (weak, nonatomic) IBOutlet UIButton *btnRegular;
@property (weak, nonatomic) IBOutlet UIView *viewJumah;
@property (weak, nonatomic) IBOutlet UIView *viewRegular;
@property (weak, nonatomic) IBOutlet UIImageView *imgInfluencers;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightInfluencers;

@end

@implementation JumahSermonViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self setType:TYPE_JUMAH];
}

- (void) setType:(int) type {
    self.btnJumah.layer.borderColor = UIColor.whiteColor.CGColor;
    self.btnJumah.layer.borderWidth = 1;
    self.btnJumah.backgroundColor = UIColor.clearColor;
    [self.btnJumah setTitleColor:[UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1.0] forState:UIControlStateNormal];
    
    self.btnRegular.layer.borderColor = UIColor.whiteColor.CGColor;
    self.btnRegular.layer.borderWidth = 1;
    self.btnRegular.backgroundColor = UIColor.clearColor;
    [self.btnRegular setTitleColor:[UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1.0] forState:UIControlStateNormal];
    
    self.viewJumah.hidden = YES;
    self.viewRegular.hidden = YES;
    
    if (type == TYPE_JUMAH) {
        self.btnJumah.backgroundColor = UIColor.whiteColor;
        [self.btnJumah setTitleColor:[UIColor colorWithRed:14/255.0 green:97/255.0 blue:41/255.0 alpha:1.0] forState:UIControlStateNormal];
        self.viewJumah.hidden = NO;
    } else if (type == TYPE_REGULAR) {
        self.btnRegular.backgroundColor = UIColor.whiteColor;
        [self.btnRegular setTitleColor:[UIColor colorWithRed:14/255.0 green:97/255.0 blue:41/255.0 alpha:1.0] forState:UIControlStateNormal];
        self.viewRegular.hidden = NO;
        [self showFluencers:NO];
    }
}

- (void) showFluencers:(BOOL) show {
    isShow = show;
    if (isShow) {
        self.imgInfluencers.image = [UIImage imageNamed:@"ic_up"];
        self.heightInfluencers.constant = 230.0f;
    } else {
        self.imgInfluencers.image = [UIImage imageNamed:@"ic_down"];
        self.heightInfluencers.constant = 0.0f;
    }
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onJumahClick:(id)sender {
    [self setType:TYPE_JUMAH];
}
- (IBAction)onRegularClick:(id)sender {
    [self setType:TYPE_REGULAR];
}
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
- (IBAction)onInfluencersClick:(id)sender {
    [self showFluencers:!isShow];
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
