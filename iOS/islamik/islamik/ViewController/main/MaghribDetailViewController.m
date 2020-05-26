//
//  MaghribDetailViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/26/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "MaghribDetailViewController.h"
#import "SurahModel.h"

@interface MaghribDetailViewController (){
    NSTimer *timer;
    int count;
}
@property (weak, nonatomic) IBOutlet UIView *viewFirst;
@property (weak, nonatomic) IBOutlet UIView *viewSecond;
@property (weak, nonatomic) IBOutlet UIView *viewThird;
@property (weak, nonatomic) IBOutlet UIView *viewFourth;
@property (weak, nonatomic) IBOutlet UIView *viewMaghrib;
@property (weak, nonatomic) IBOutlet UIView *viewFifth;
@property (weak, nonatomic) IBOutlet UIView *viewSixth;
@property (weak, nonatomic) IBOutlet UIView *viewSeventh;
@end

@implementation MaghribDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
}

- (void) initialize {
    count = -1;
    [self updateUI];
}

- (void) updateUI {
    count ++;
    self.viewFirst.hidden = YES;
    self.viewSecond.hidden = YES;
    self.viewThird.hidden = YES;
    self.viewFourth.hidden = YES;
    self.viewMaghrib.hidden = YES;
    self.viewFifth.hidden = YES;
    self.viewSixth.hidden = YES;
    self.viewSeventh.hidden = YES;
    if (count == 0) {
        self.viewMaghrib.hidden = NO;
        [self moveTextView];
    } else if (count == 6) {
        self.viewMaghrib.hidden = NO;
        [self moveTextView];
    } else if (count == 13) {
        self.viewMaghrib.hidden = NO;
        [self moveTextView];
    } else if (count == 1 || count == 7 || count == 14) {
        self.viewFirst.hidden = NO;
    } else if (count == 2 || count == 8 || count == 15) {
        self.viewSecond.hidden = NO;
    } else if (count == 3 || count == 9 || count == 16) {
        self.viewThird.hidden = NO;
    } else if (count == 4 || count == 10 || count == 17) {
        self.viewFourth.hidden = NO;
    } else if (count == 5 || count == 11 || count == 18) {
        self.viewThird.hidden = NO;
    } else if (count == 12) {
        self.viewFifth.hidden = NO;
    } else if (count == 19) {
        self.viewSixth.hidden = NO;
    } else if (count == 20) {
        self.viewSeventh.hidden = NO;
    } else if (count == 21) {
        [self onBack:nil];
    }
    if (count != 0 && count != 6 && count != 13 && count < 21) {
        timer=[NSTimer scheduledTimerWithTimeInterval:15 target:self selector:@selector(updateUI) userInfo:nil repeats:NO];
    }
}

- (void) moveTextView {
    SurahModel *model = self.mDataList[0];
    double speed = [SPEED_ARRAY[model.speed] intValue] / 1000;
    timer=[NSTimer scheduledTimerWithTimeInterval:speed target:self selector:@selector(updateUI) userInfo:nil repeats:NO];
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
