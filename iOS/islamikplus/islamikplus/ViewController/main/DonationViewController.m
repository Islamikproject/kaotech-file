//
//  DonationViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "DonationViewController.h"
#import "AmountCell.h"
#import "BasketCell.h"

@interface DonationViewController () <UITableViewDelegate, UITableViewDataSource>
{
    int type;
    NSMutableArray * mDataList;
}
@property (weak, nonatomic) IBOutlet UIButton *btnAmount;
@property (weak, nonatomic) IBOutlet UIButton *btnRaise;
@property (weak, nonatomic) IBOutlet UIButton *btnBaskets;
@property (weak, nonatomic) IBOutlet UIView *viewTable;
@property (weak, nonatomic) IBOutlet UITextField *edtTotal;
@property (weak, nonatomic) IBOutlet UITableView *tblData;
@property (weak, nonatomic) IBOutlet UIView *viewAmount;
@property (weak, nonatomic) IBOutlet UIView *viewBaskets;

@property (weak, nonatomic) IBOutlet UIView *viewRaise;
@property (weak, nonatomic) IBOutlet UITextField *edtRaiser;
@property (weak, nonatomic) IBOutlet UITextField *edtMosque;
@property (weak, nonatomic) IBOutlet UITextField *edtReason;
@property (weak, nonatomic) IBOutlet UITextField *edtAmount;

@end

@implementation DonationViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.edtTotal.layer.borderColor = [UIColor blackColor].CGColor;
    self.edtTotal.layer.borderWidth = 1.f;
    self.edtTotal.layer.cornerRadius = 20.f;
    self.edtTotal.text = @"";
    [self initialize:0];
    [self getServerData];
}
- (void) initialize:(int)index {
    type = index;
    _viewTable.hidden = YES;
    _viewAmount.hidden = YES;
    _viewBaskets.hidden = YES;
    _viewRaise.hidden = YES;
    [_btnAmount setTitleColor:UIColor.whiteColor forState:UIControlStateNormal];
    [_btnBaskets setTitleColor:UIColor.whiteColor forState:UIControlStateNormal];
    [_btnRaise setTitleColor:UIColor.whiteColor forState:UIControlStateNormal];
    if (type == 0) {
        _viewTable.hidden = NO;
        _viewAmount.hidden = NO;
        [_btnAmount setTitleColor:UIColor.blackColor forState:UIControlStateNormal];
        [self.tblData reloadData];
    } else if (type == 1) {
        _viewRaise.hidden = NO;
        _edtRaiser.text = @"";
        _edtMosque.text = @"";
        _edtReason.text = @"";
        _edtAmount.text = @"";
        [_btnRaise setTitleColor:UIColor.blackColor forState:UIControlStateNormal];
    } else if (type == 2) {
        _viewTable.hidden = NO;
        _viewBaskets.hidden = NO;
        [_btnBaskets setTitleColor:UIColor.blackColor forState:UIControlStateNormal];
        [self.tblData reloadData];
    }
}
- (void) getServerData{
    if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return;
    }
    mDataList = [NSMutableArray new];

    PFQuery * query = [PFQuery queryWithClassName:PARSE_TABLE_PAYMENT];
    [query whereKey:PARSE_TO_USER equalTo:[PFUser currentUser]];
    [query includeKey:PARSE_TO_USER];
    [query includeKey:PARSE_SERMON];
    [query orderByDescending:PARSE_FIELD_CREATED_AT];
    [query setLimit:1000];
    
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    [query findObjectsInBackgroundWithBlock:^(NSArray *array, NSError *error){
        [SVProgressHUD dismiss];
        if (error){
            [Util showAlertTitle:self title:@"Error" message:error.localizedDescription];
        } else {
            self->mDataList = (NSMutableArray *) array;
            [self.tblData reloadData];
            float total = 0.0f;
            for(PFObject *object in self->mDataList){
                total += [object[PARSE_AMOUNT] floatValue];
            }
            self.edtTotal.text = [NSString stringWithFormat:@"$%.2f", total];
        }
    }];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return mDataList.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 35.f;
}

- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    PFObject * paymentObj = [mDataList objectAtIndex:indexPath.row];
    if (type == 0) {
        AmountCell *cell = (AmountCell *)[tv dequeueReusableCellWithIdentifier:@"AmountCell"];
        if(cell){
            cell.lblName.text = paymentObj[PARSE_NAME];
            NSNumber *amount = paymentObj[PARSE_AMOUNT];
            cell.lblAmount.text = [NSString stringWithFormat:@"$%.2f", amount.floatValue];
            cell.lblDate.text = [Util convertDateToString:paymentObj.createdAt];
        }
        return cell;
    } else {
        BasketCell *cell = (BasketCell *)[tv dequeueReusableCellWithIdentifier:@"BasketCell"];
        if(cell){
            PFObject *sermonObj = paymentObj[PARSE_SERMON];
            cell.lblTopic.text = sermonObj[PARSE_TOPIC];
            NSNumber *amount = paymentObj[PARSE_AMOUNT];
            cell.lblAmount.text = [NSString stringWithFormat:@"$%.2f", amount.floatValue];
        }
        return cell;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onAmountClick:(id)sender {
    [self initialize:0];
}
- (IBAction)onRaiseClick:(id)sender {
    [self initialize:1];
}
- (IBAction)onBasketsClick:(id)sender {
    [self initialize:2];
}
- (IBAction)onNextClick:(id)sender {
    if ([self isValid]) {
        NSString *mosque = [Util trim:_edtMosque.text];
        NSString *reason = [Util trim:_edtReason.text];
        NSString *amount = [Util trim:_edtAmount.text];
        NSString *message = [NSString stringWithFormat:@"Dear ISLAMIK users, « %@ » just launched a fundraising campaign to « %@ » and is seeking a total amount of « %@ »", mosque, reason, amount];
        
        SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
        alert.customViewColor = MAIN_COLOR;
        alert.horizontalButtons = YES;
        [alert addButton:@"CANCEL" actionBlock:^(void) {}];
        [alert addButton:@"SEND >>" actionBlock:^(void) {
            [self send];
        }];
        [alert showError:@"ISLAMIK+" subTitle:message closeButtonTitle:nil duration:0.0f];
    }
}
- (BOOL) isValid {
    NSString *raiser = [Util trim:_edtRaiser.text];
    NSString *mosque = [Util trim:_edtMosque.text];
    NSString *reason = [Util trim:_edtReason.text];
    NSString *amount = [Util trim:_edtAmount.text];
    NSString * errorMsg = @"";
    if (raiser.length == 0) {
        errorMsg = @"Please enter name of the fundraiser.";
    } else if (mosque.length == 0) {
        errorMsg = @"Please enter name of the mosque.";
    } else if (reason.length == 0) {
        errorMsg = @"Please enter reasons for fundraising.";
    } else if (amount.length == 0) {
        errorMsg = @"Please enter amount.";
    }
    if (errorMsg.length > 0) {
        [Util showAlertTitle:self title:@"Error" message:errorMsg];
        return NO;
    }
    return YES;
}
- (void) send {
    NSString *raiser = [Util trim:_edtRaiser.text];
    NSString *mosque = [Util trim:_edtMosque.text];
    NSString *reason = [Util trim:_edtReason.text];
    NSNumber *amount = [NSNumber numberWithInt: [self.edtAmount.text intValue]];
    
    PFObject *object = [PFObject objectWithClassName:PARSE_TABLE_SERMON];
    object[PARSE_OWNER] = [PFUser currentUser];
    object[PARSE_TYPE] = [NSNumber numberWithInt: TYPE_RAISE];
    object[PARSE_RAISER] = raiser;
    object[PARSE_MOSQUE] = mosque;
    object[PARSE_TOPIC] = reason;
    object[PARSE_AMOUNT] = amount;
    object[PARSE_VIDEO] = @"";
    object[PARSE_VIDEO_NAME] = @"";
    
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    [object saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        [SVProgressHUD dismiss];
        if (succeeded) {
            NSString *message = [NSString stringWithFormat:@"Dear ISLAMIK users, « %@ » just launched a fundraising campaign to « %@ » and is seeking a total amount of « %@ »", mosque, reason, amount];
            [Util sendPushAllNotification:message type:TYPE_RAISE];
            [self initialize:1];
        }else{
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        }
    }];
}
@end
