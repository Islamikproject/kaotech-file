//
//  OrderViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 11/16/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "OrderViewController.h"
#import "OrderCell.h"
#import <SafariServices/SafariServices.h>

@interface OrderViewController () <IQDropDownTextFieldDelegate, UITableViewDelegate, UITableViewDataSource, SFSafariViewControllerDelegate> {
    int type;
    NSMutableArray *mServerDataList;
    NSMutableArray *mDataList;
    PFUser *mUser;
}
@property (weak, nonatomic) IBOutlet IQDropDownTextField *edtLanguage;
@property (weak, nonatomic) IBOutlet UIImageView *imgMosque;
@property (weak, nonatomic) IBOutlet UIImageView *imgUsthadh;
@property (weak, nonatomic) IBOutlet UITableView *tblData;
@property (weak, nonatomic) IBOutlet UITextField *edtName;
@property (weak, nonatomic) IBOutlet UITextField *edtSubject;
@property (weak, nonatomic) IBOutlet UITextField *edtMessage;
@property (weak, nonatomic) IBOutlet UITextField *edtAmount;

@end

@implementation OrderViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
}

- (void) initialize {
    self.edtLanguage.itemList = LANGUAGE_ORDER_ARRAY;
    self.edtLanguage.isOptionalDropDown = YES;
    self.edtLanguage.selectedRow = 0;
    self.edtLanguage.delegate = self;
    mUser = nil;
    [self setType:TYPE_MOSQUE];
    [self getServerData];
}

- (void) setType:(int) index {
    type = index;
    self.imgMosque.image = [UIImage imageNamed:@"check_off"];
    self.imgUsthadh.image = [UIImage imageNamed:@"check_off"];
    if (type == TYPE_MOSQUE) {
        self.imgMosque.image = [UIImage imageNamed:@"check_on"];
    } else {
        self.imgUsthadh.image = [UIImage imageNamed:@"check_on"];
    }
    [self showData];
}

- (void) getServerData {
    if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return;
    }
    mServerDataList = [NSMutableArray new];

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
            self->mServerDataList = (NSMutableArray *) array;
        }
        [self showData];
    }];
}

- (void) showData {
    mDataList = [NSMutableArray new];
    for (int i = 0; i < mServerDataList.count; i ++) {
        int _type = [mServerDataList[i][PARSE_TYPE] intValue];
        if (type == _type) {
            [self->mDataList addObject:mServerDataList[i]];
        } else if (type == TYPE_MOSQUE && _type == TYPE_ADMIN) {
            [self->mDataList addObject:mServerDataList[i]];
        }
    }
    mUser = nil;
    [self.tblData reloadData];
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
}

-(void)textField:(nonnull IQDropDownTextField*)textField didSelectItem:(nullable NSString*)item{}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onMosqueClick:(id)sender {
    [self setType:TYPE_MOSQUE];
}
- (IBAction)onUsthadhClick:(id)sender {
    [self setType:TYPE_USTHADH];
}
- (IBAction)onSubmitClick:(id)sender {
    if ([self isValid])
        [self submit];
}
- (BOOL) isValid {
    NSString *name = [Util trim:self.edtName.text];
    NSString *subject = [Util trim:self.edtSubject.text];
    NSString *message = [Util trim:self.edtMessage.text];
    NSString *amount = [Util trim:self.edtAmount.text];
    NSString * errorMsg = @"";
    if (self.edtLanguage.selectedItem.length == 0) {
        errorMsg = @"Please select language.";
    } else if (mUser == nil) {
        errorMsg = @"Please select mosque.";
    } else if (name.length == 0) {
        errorMsg = @"Please enter name.";
    } else if (subject.length == 0) {
        errorMsg = @"Please enter subject.";
    } else if (message.length == 0) {
        errorMsg = @"Please enter message.";
    } else if (amount.length == 0) {
        errorMsg = @"Please enter amount.";
    }
    if (errorMsg.length > 0) {
        [Util showAlertTitle:self title:@"Error" message:errorMsg];
        return NO;
    }
    return YES;
}
- (void) submit {
    NSString *name = [Util trim:self.edtName.text];
    NSString *subject = [Util trim:self.edtSubject.text];
    NSString *message = [Util trim:self.edtMessage.text];
    NSString *amount = [Util trim:self.edtAmount.text];
    
    PFObject *object = [PFObject objectWithClassName:PARSE_TABLE_ORDER];
    object[PARSE_OWNER] = [PFUser currentUser];
    int index = (int)[self.edtLanguage selectedRow];
    object[PARSE_LANGUAGE] = LANGUAGE_ORDER_SYMBOL[index];
    object[PARSE_TYPE] = [NSNumber numberWithInt:type];
    object[PARSE_TO_USER] = mUser;
    object[PARSE_NAME] = name;
    object[PARSE_SUBJECT] = subject;
    object[PARSE_MESSAGE] = message;
    object[PARSE_AMOUNT] = [NSNumber numberWithDouble:[amount doubleValue]];
    
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    [object saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        [SVProgressHUD dismiss];
        if (error) {
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        }else{
            NSString * url = [NSString stringWithFormat:@"https://stripe.kaotech.org/order?order=%@&amount=%@", object.objectId, amount];
            NSURL *nsUrl = [NSURL URLWithString:url];
            SFSafariViewController *svc = [[SFSafariViewController alloc] initWithURL:nsUrl];
            svc.delegate = self;
            [self presentViewController:svc animated:YES completion:nil];
        }
    }];
}

- (void)safariViewControllerDidFinish:(SFSafariViewController *)controller {
    [self dismissViewControllerAnimated:true completion:nil];
    [self onBack:nil];
}
@end
