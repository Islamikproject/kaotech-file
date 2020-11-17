//
//  BookViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 11/16/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "BookViewController.h"
#import "OrderCell.h"

@interface BookViewController ()<UITableViewDelegate, UITableViewDataSource>{
    NSDate *date;
    NSDate *time;
    int type;
    NSMutableArray *mDataList;
    PFUser *mUser;
    int price;
    int groupPrice;
}
@property (weak, nonatomic) IBOutlet UITableView *tblData;
@property (weak, nonatomic) IBOutlet UIImageView *checkOne;
@property (weak, nonatomic) IBOutlet UIImageView *checkGroup;
@property (weak, nonatomic) IBOutlet UITextField *edtChildren;
@property (weak, nonatomic) IBOutlet UITextField *edtChildrenA;
@property (weak, nonatomic) IBOutlet UITextField *edtChildrenB;
@property (weak, nonatomic) IBOutlet UITextField *edtChildrenC;
@property (weak, nonatomic) IBOutlet UITextField *edtChildrenD;
@property (weak, nonatomic) IBOutlet UITextField *edtAmount;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightChildren;
@property (weak, nonatomic) IBOutlet UITextField *edtDate;
@property (weak, nonatomic) IBOutlet UITextField *edtTime;

@end

@implementation BookViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
}

- (void) initialize {
    UIDatePicker *picker1   = [[UIDatePicker alloc] initWithFrame:CGRectMake(0, 14, 97, 41)];
    [picker1 setDatePickerMode:UIDatePickerModeDate];
    picker1.backgroundColor = [UIColor whiteColor];
    picker1.tag = 0;
    [picker1 addTarget:self action:@selector(dateSelected:) forControlEvents:UIControlEventValueChanged];
    self.edtDate.inputView  = picker1;
    
    UIDatePicker *picker2   = [[UIDatePicker alloc] initWithFrame:CGRectMake(0, 14, 97, 41)];
    [picker2 setDatePickerMode:UIDatePickerModeTime];
    picker2.backgroundColor = [UIColor whiteColor];
    picker2.tag = 1;
    [picker2 addTarget:self action:@selector(dateSelected:) forControlEvents:UIControlEventValueChanged];
    self.edtTime.inputView  = picker2;
    
    mUser = nil;
    price = 0;
    groupPrice = 0;
    [self setType:TYPE_ONE];
    [self getServerData];
}

- (void) setType:(int) index {
    type = index;
    self.checkOne.image = [UIImage imageNamed:@"check_off"];
    self.checkGroup.image = [UIImage imageNamed:@"check_off"];
    self.heightChildren.constant = 0.f;
    if (type == TYPE_ONE) {
        self.checkOne.image = [UIImage imageNamed:@"check_on"];
    } else {
        self.checkGroup.image = [UIImage imageNamed:@"check_on"];
        self.heightChildren.constant = 200.f;
    }
    [self setPrice];
}

- (void) setPrice {
    if (mUser != nil) {
        price = [mUser[PARSE_PRICE] intValue];
        groupPrice = [mUser[PARSE_GROUP_PRICE] intValue];
        if (type == TYPE_ONE) {
            self.edtAmount.text = STRING_SESSION_PRICE[price];
        } else {
            self.edtAmount.text = STRING_SESSION_GROUP[groupPrice];
        }
    }
}

- (void) getServerData {
    if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return;
    }
    mDataList = [NSMutableArray new];

    PFQuery *query = [PFUser query];
    [query whereKey:PARSE_TYPE notEqualTo:[NSNumber numberWithInt:TYPE_USER]];
    
    [query orderByAscending:PARSE_FIRSTNAME];
    [query setLimit:1000];
    
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    [query findObjectsInBackgroundWithBlock:^(NSArray *array, NSError *error){
        [SVProgressHUD dismiss];
        if (error){
            [Util showAlertTitle:self title:@"Error" message:error.localizedDescription];
        } else {
            self->mDataList = (NSMutableArray *) array;
        }
        [self.tblData reloadData];
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
    return 60.f;
}

- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    OrderCell *cell = (OrderCell *)[tv dequeueReusableCellWithIdentifier:@"OrderCell"];
    if(cell){
        PFUser * userObj = [mDataList objectAtIndex:indexPath.row];
        cell.lblName.text = userObj[PARSE_MOSQUE];
        cell.lblAddress.text = userObj[PARSE_ADDRESS];
        PFFileObject *photoFile = userObj[PARSE_AVATAR];
        [cell.imgAvatar sd_setImageWithURL:[NSURL URLWithString:photoFile.url] placeholderImage:[UIImage imageNamed:@"default_profile"]];
        cell.imgCheck.image = [UIImage imageNamed:@"check_off"];
        if (mUser != nil && [userObj.objectId isEqualToString:mUser.objectId]) {
            cell.imgCheck.image = [UIImage imageNamed:@"check_on"];
        }
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    mUser = [mDataList objectAtIndex:indexPath.row];
    [self.tblData reloadData];
    [self setPrice];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
-(void)dateSelected:(UIDatePicker*)datePicker{
    if (datePicker.tag == 0){
        date = datePicker.date;
        NSDateFormatter *dformat = [[NSDateFormatter alloc] init];
        [dformat setDateFormat:@"MM/dd/yyyy"];
        self.edtDate.text = [dformat stringFromDate:date];
    }else{
        time = datePicker.date;
        NSDateFormatter *dformat = [[NSDateFormatter alloc] init];
        [dformat setDateFormat:@"hh:mm"];
        self.edtTime.text = [dformat stringFromDate:time];
    }
}

- (IBAction)onOneClick:(id)sender {
    [self setType:TYPE_ONE];
}
- (IBAction)onGroupClick:(id)sender {
    [self setType:TYPE_GROUP];
}
- (IBAction)onSubmitClick:(id)sender {
    if ([self isValid])
        [self submit];
}
- (BOOL) isValid {
    NSString *strDate = [Util trim:self.edtDate.text];
    NSString *strTime = [Util trim:self.edtTime.text];
    NSString *childName = [Util trim:self.edtChildren.text];
    NSString * errorMsg = @"";
    if (mUser == nil) {
        errorMsg = @"Please select mosque.";
    } else if (strDate.length == 0) {
        errorMsg = @"Please choose date.";
    } else if (strTime.length == 0) {
        errorMsg = @"Please choose time.";
    } else if (childName.length == 0) {
        errorMsg = @"Please enter children.";
    }
    if (errorMsg.length > 0) {
        [Util showAlertTitle:self title:@"Error" message:errorMsg];
        return NO;
    }
    return YES;
}
- (void) submit {
    NSString *strDate = [Util trim:self.edtDate.text];
    NSString *strTime = [Util trim:self.edtTime.text];
    NSString *childName = [Util trim:self.edtChildren.text];
    NSString *childNameA = [Util trim:self.edtChildren.text];
    NSString *childNameB = [Util trim:self.edtChildren.text];
    NSString *childNameC = [Util trim:self.edtChildren.text];
    NSString *childNameD = [Util trim:self.edtChildren.text];
    NSDate *bookDate = [Util convertStringToDateTime:strDate time:strTime];
    
    PFObject *object = [PFObject objectWithClassName:PARSE_TABLE_BOOK];
    object[PARSE_OWNER] = [PFUser currentUser];
    object[PARSE_TYPE] = [NSNumber numberWithInt:type];
    object[PARSE_TO_USER] = mUser;
    object[PARSE_BOOK_DATE] = bookDate;
    
    NSMutableArray *childList = [NSMutableArray new];
    [childList addObject:childName];
    object[PARSE_PRICE] = [NSNumber numberWithInt:price];
    if (type == TYPE_GROUP) {
        object[PARSE_PRICE] = [NSNumber numberWithInt:groupPrice];
        if (childNameA.length > 0)
            [childList addObject:childNameA];
        if (childNameB.length > 0)
            [childList addObject:childNameB];
        if (childNameC.length > 0)
            [childList addObject:childNameC];
        if (childNameD.length > 0)
            [childList addObject:childNameD];
    }
    object[PARSE_CHILD_NAME] = childList;
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    [object saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        if (error) {
            [SVProgressHUD dismiss];
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        }else{
            PFUser *me = [PFUser currentUser];
            PFObject *notificationObj = [PFObject objectWithClassName:PARSE_TABLE_NOTIFICATION];
            notificationObj[PARSE_OWNER] = me;
            notificationObj[PARSE_TYPE] = [NSNumber numberWithInt:TYPE_BOOK];
            notificationObj[PARSE_TO_USER] = self->mUser;
            notificationObj[PARSE_STATE] = [NSNumber numberWithInt:STATE_PENDING];
            notificationObj[PARSE_BOOK_OBJ] = object;
            NSString *message = [NSString stringWithFormat:@"%@ %@ just book a session for (%@ and $%@).", me[PARSE_FIRSTNAME], me[PARSE_LASTSTNAME], [Util convertDateToString:bookDate], self.edtAmount.text];
            notificationObj[PARSE_MESSAGE] = message;
            [notificationObj saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
                [SVProgressHUD dismiss];
                [Util sendPushNotification:self->mUser[PARSE_EMAIL_ADDRESS] message:message type:TYPE_BOOK];
                [self onBack:nil];
            }];
        }
    }];
}
@end
