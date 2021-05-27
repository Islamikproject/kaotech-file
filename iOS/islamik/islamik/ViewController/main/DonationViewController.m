//
//  OrderViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 11/16/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "DonationViewController.h"
#import "OrderCell.h"
#import <SafariServices/SafariServices.h>

@interface DonationViewController () <UITableViewDelegate, UITableViewDataSource, SFSafariViewControllerDelegate> {
    int type;
    NSMutableArray *mServerDataList;
    NSMutableArray *mDataList;
    PFUser *mUser;
}
@property (weak, nonatomic) IBOutlet UIImageView *imgMosque;
@property (weak, nonatomic) IBOutlet UIImageView *imgScholars;
@property (weak, nonatomic) IBOutlet UIImageView *imgInfluencers;
@property (weak, nonatomic) IBOutlet UITableView *tblData;
@property (weak, nonatomic) IBOutlet UITextField *edtAmount;

@end

@implementation DonationViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
}

- (void) initialize {
    mUser = nil;
    [self setType:TYPE_MOSQUE];
    [self getServerData];
}

- (void) setType:(int) index {
    type = index;
    self.imgMosque.image = [UIImage imageNamed:@"check_off"];
    self.imgScholars.image = [UIImage imageNamed:@"check_off"];
    self.imgInfluencers.image = [UIImage imageNamed:@"check_off"];
    if (type == TYPE_MOSQUE) {
        self.imgMosque.image = [UIImage imageNamed:@"check_on"];
    } else if (type == TYPE_USTHADH) {
        self.imgScholars.image = [UIImage imageNamed:@"check_on"];
    } else {
        self.imgInfluencers.image = [UIImage imageNamed:@"check_on"];
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
        if (_type == TYPE_ADMIN || _type == type || (type == TYPE_INFLUENCER_WOMEN && _type >= TYPE_INFLUENCER_WOMEN)) {
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
- (IBAction)onScholarsClick:(id)sender {
    [self setType:TYPE_USTHADH];
}
- (IBAction)onInfluencersClick:(id)sender {
    [self setType:TYPE_INFLUENCER_WOMEN];
}
- (IBAction)onSubmitClick:(id)sender {
    if ([self isValid])
        [self submit];
}
- (BOOL) isValid {
    NSString *amount = [Util trim:self.edtAmount.text];
    if (amount.length == 0) {
        [Util showAlertTitle:self title:@"Error" message:@"Please enter amount."];
        return NO;
    }
    return YES;
}
- (void) submit {
    NSString *amount = [Util trim:self.edtAmount.text];
    
    PFObject *object = [PFObject objectWithClassName:PARSE_TABLE_ORDER];
    object[PARSE_OWNER] = [PFUser currentUser];
    object[PARSE_LANGUAGE] = @"";
    object[PARSE_TYPE] = [NSNumber numberWithInt:TYPE_DONATION];
    object[PARSE_TO_USER] = mUser;
    object[PARSE_NAME] = [NSString stringWithFormat:@"%@ %@", [PFUser currentUser][PARSE_FIRSTNAME], [PFUser currentUser][PARSE_LASTSTNAME]];
    object[PARSE_SUBJECT] = @"CHARITY & DONATION";
    object[PARSE_MESSAGE] = @"";
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
