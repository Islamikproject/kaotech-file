//
//  ZuhrDetailViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/26/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "ZuhrDetailViewController.h"
#import "SurahModel.h"

@interface ZuhrDetailViewController (){
    NSTimer *timer;
    int count;
}
@property (weak, nonatomic) IBOutlet UIView *viewFirst;
@property (weak, nonatomic) IBOutlet UIView *viewSecond;
@property (weak, nonatomic) IBOutlet UIView *viewThird;
@property (weak, nonatomic) IBOutlet UIView *viewFourth;
@property (weak, nonatomic) IBOutlet UIView *viewZuhr;
@property (weak, nonatomic) IBOutlet UIView *viewFifth;
@property (weak, nonatomic) IBOutlet UIView *viewSixth;
@property (weak, nonatomic) IBOutlet UIView *viewSeventh;

@end

@implementation ZuhrDetailViewController

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
    self.viewZuhr.hidden = YES;
    self.viewFifth.hidden = YES;
    self.viewSixth.hidden = YES;
    self.viewSeventh.hidden = YES;
    if (count == 0) {
        self.viewZuhr.hidden = NO;
        [self moveTextView];
    } else if (count == 6) {
        self.viewZuhr.hidden = NO;
        [self moveTextView];
    } else if (count == 13) {
        self.viewZuhr.hidden = NO;
        [self moveTextView];
    } else if (count == 19) {
        self.viewZuhr.hidden = NO;
        [self moveTextView];
    } else if (count == 1 || count == 7 || count == 14 || count == 20) {
        self.viewFirst.hidden = NO;
    } else if (count == 2 || count == 8 || count == 15 || count == 21) {
        self.viewSecond.hidden = NO;
    } else if (count == 3 || count == 9 || count == 16 || count == 22) {
        self.viewThird.hidden = NO;
    } else if (count == 4 || count == 10 || count == 17 || count == 23) {
        self.viewFourth.hidden = NO;
    } else if (count == 5 || count == 11 || count == 18 || count == 24) {
        self.viewThird.hidden = NO;
    } else if (count == 12) {
        self.viewFifth.hidden = NO;
    } else if (count == 25) {
        self.viewSixth.hidden = NO;
    } else if (count == 26) {
        self.viewSeventh.hidden = NO;
    } else if (count == 27) {
        [self onBack:nil];
    }
    if (count != 0 && count != 6 && count != 13 && count != 19 && count < 27) {
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
