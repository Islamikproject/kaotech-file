//
//  PhotoViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 11/16/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "PhotoViewController.h"

@interface PhotoViewController ()
@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;

@end

@implementation PhotoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self.imgPhoto sd_setImageWithURL:[NSURL URLWithString:self.mPhotoFile.url] placeholderImage:[UIImage imageNamed:@"default_imag_bg"]];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onCloseClick:(id)sender {
    [self onBack:nil];
}

@end
