//
//  ReadyViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/25/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "ReadyViewController.h"
#import "FajrDetailViewController.h"
#import "ZuhrDetailViewController.h"
#import "MaghribDetailViewController.h"
#import "SalatDetailViewController.h"
#import "QuranDetailViewController.h"

@interface ReadyViewController () {
    NSTimer *timer;
    int count;
}
@property (weak, nonatomic) IBOutlet UILabel *lblCount;

@end

@implementation ReadyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    count = 8;
    timer=[NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(updateUI) userInfo:nil repeats:YES];
}

- (void)updateUI {
    if (count > 0) {
        count --;
        self.lblCount.text = [NSString stringWithFormat:@"%d", count];
    } else {
        [timer invalidate];
        [self gotoNextScreen];
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
- (void)gotoNextScreen {
    [self.navigationController popViewControllerAnimated:YES];
    if (self.type == TYPE_FAJR) {
        FajrDetailViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"FajrDetailViewController"];
        controller.mDataList = self.mDataList;
        [self.navigationController pushViewController:controller animated:YES];
    } else if (self.type == TYPE_ZUHR || self.type == TYPE_ASR || self.type == TYPE_ISHA) {
        ZuhrDetailViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"ZuhrDetailViewController"];
        controller.mDataList = self.mDataList;
        [self.navigationController pushViewController:controller animated:YES];
    } else if (self.type == TYPE_MAGHRIB) {
        MaghribDetailViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"MaghribDetailViewController"];
        controller.mDataList = self.mDataList;
        [self.navigationController pushViewController:controller animated:YES];
    } else if (self.type == TYPE_SALAT) {
        SalatDetailViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SalatDetailViewController"];
        controller.mDataList = self.mDataList;
        [self.navigationController pushViewController:controller animated:YES];
    } else if (self.type == TYPE_QURAN) {
        QuranDetailViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"QuranDetailViewController"];
        controller.mDataList = self.mDataList;
        [self.navigationController pushViewController:controller animated:YES];
    }
}
@end
