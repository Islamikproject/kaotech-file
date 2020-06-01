//
//  DonationViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/23/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "DonationViewController.h"
#import "Stripe/Stripe.h"

@interface DonationViewController ()
@property (weak, nonatomic) IBOutlet UITextField *edtName;
@property (weak, nonatomic) IBOutlet STPPaymentCardTextField *edtCard;
@property (weak, nonatomic) IBOutlet UITextField *edtAmount;
@property (weak, nonatomic) IBOutlet UIButton *btnPay;

@end

@implementation DonationViewController

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
- (IBAction)onPayClick:(id)sender {
    
}
@end
