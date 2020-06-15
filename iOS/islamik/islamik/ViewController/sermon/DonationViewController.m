//
//  DonationViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/23/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "DonationViewController.h"
#import "Stripe/Stripe.h"
#import "StripeRest.h"

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
    NSNumber *amount = self.mSermonObj[PARSE_AMOUNT];
    self.edtAmount.text = [NSString stringWithFormat:@"%.2f", amount.floatValue];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
//[0]    (null)    @"message" : @"You must verify a phone number on your Stripe account before you can send raw credit card numbers to the Stripe API. You can avoid this requirement by using Stripe.js, the Stripe mobile bindings, or Stripe Checkout. For more information, see https://dashboard.stripe.com/phone-verification."    
- (IBAction)onPayClick:(id)sender {
    if ([self isValid]) {
        [self chargeCustomer];
    }
}

- (BOOL) isValid {
    NSString * errorMsg = @"";
    if (self.edtName.text == 0) {
        errorMsg = @"Please enter your name.";
    } else if (self.edtAmount.text == 0) {
        errorMsg = @"Please enter amount.";
    }
    if (errorMsg.length > 0) {
        [Util showAlertTitle:self title:@"Error" message:errorMsg];
        return NO;
    }
    return YES;
}

- (void) chargeCustomer {
    NSString * cardNum = self.edtCard.cardNumber;
    NSString * expire = [NSString stringWithFormat:@"%lu/%lu", (unsigned long)self.edtCard.expirationMonth, (unsigned long)self.edtCard.expirationYear];
    NSString * cvc = self.edtCard.cvc;
    NSArray *paths = [expire pathComponents];
    
    PFUser *userObj = self.mSermonObj[PARSE_OWNER];
    
    NSString * senderName = self.edtName.text;
    NSString * receiverName = userObj[PARSE_MOSQUE];
    
    NSString *description = [NSString stringWithFormat:@"%@ paid to %@",senderName, receiverName];
    NSString *amount = [NSString stringWithFormat:@"%d", (int)([self.edtAmount.text floatValue] * 100)];
    NSString *application_fee = [NSString stringWithFormat:@"%d", (int)([self.edtAmount.text floatValue] * 0.1 * 100)];
    NSString *accountId = self.mSermonObj[PARSE_OWNER][PARSE_ACCOUNT_ID];
    NSMutableDictionary *metadata = [[NSMutableDictionary alloc] initWithObjectsAndKeys:
                                     @"iOS", @"DeviceType",
                                     nil];
    NSMutableDictionary *chargeDict = [[NSMutableDictionary alloc] initWithObjectsAndKeys:
                                       amount, @"amount",
                                       @"usd", @"currency",
                                       @"true", @"capture",
                                       accountId, @"destination",
                                       application_fee, @"application_fee",
                                       description, @"description",
                                       metadata, @"metadata",
                                       nil];
    NSMutableDictionary *tokenDict = [[NSMutableDictionary alloc] initWithObjectsAndKeys:
                                      [[NSMutableDictionary alloc] initWithObjectsAndKeys:
                                       cardNum, @"number",
                                       paths[1], @"exp_year",
                                       paths[0], @"exp_month",
                                       cvc, @"cvc",
                                       @"usd", @"currency",
                                       nil],
                                      @"card",
                                      nil];
    [StripeRest setCharges:chargeDict tokenDict:tokenDict completionBlock:^(id response, NSError *err) {
        if (err) {
            [SVProgressHUD dismiss];
            [Util showAlertTitle:self title:@"" message:@"Unable to process payment. Please check your details and try again."];
        } else {
            NSDictionary *dict = response;
            NSString *  mainId = dict[@"id"];
            [self saveDonation:mainId];
        }
    }];
}

- (void) saveDonation:(NSString* )chargeId{
    PFObject *object = [PFObject objectWithClassName:PARSE_TABLE_PAYMENT];
    object[PARSE_TO_USER] = self.mSermonObj[PARSE_OWNER];
    object[PARSE_SERMON] = self.mSermonObj;
    object[PARSE_NAME] = self.edtName.text;
    object[PARSE_AMOUNT] = [NSNumber numberWithFloat: [self.edtAmount.text floatValue]];
    object[PARSE_CHARGE_ID] = chargeId;
    
    [object saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        if (succeeded) {
            NSString *message = @"Thanks for your donation, it has been sent to the mosque account.";
            SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
            alert.customViewColor = MAIN_COLOR;
            alert.horizontalButtons = YES;
            [alert addButton:@"YES" actionBlock:^(void) {
                [self onBack:nil];
            }];
            [alert showSuccess:@"Success" subTitle:message closeButtonTitle:nil duration:0.0f];
        }else{
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        }
    }];
}
@end
