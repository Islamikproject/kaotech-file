//
//  SermonListViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/22/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "SermonListViewController.h"
#import "VideoViewController.h"
#import "SermonListCell.h"
#import <SafariServices/SafariServices.h>

@interface SermonListViewController () <UITableViewDelegate, UITableViewDataSource, SFSafariViewControllerDelegate, IQDropDownTextFieldDelegate>
{
    NSMutableArray * mDataList;
    NSMutableArray * languageCode;
    NSMutableArray * languageName;
}
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UITableView *tblData;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *edtLanguage;

@end

@implementation SermonListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.lblTitle.text = self.mUserObj[PARSE_MOSQUE];
    [self initialize];
}
- (void) initialize {
    languageCode = [Util getLanguageCodeList];
    languageName = [Util getLanguageNameList];
    self.edtLanguage.itemList = languageName;
    self.edtLanguage.isOptionalDropDown = YES;
    self.edtLanguage.delegate = self;
    [self getServerData];
}

- (void) getServerData{
    if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return;
    }
    mDataList = [NSMutableArray new];
    NSString *language = @"";
    if (self.edtLanguage.selectedItem.length > 0) {
        int index = (int)[self.edtLanguage selectedRow];
        language = languageCode[index];
    }
    PFQuery * query = [PFQuery queryWithClassName:PARSE_TABLE_SERMON];
    [query whereKey:PARSE_OWNER equalTo:self.mUserObj];
    [query whereKey:PARSE_TYPE equalTo:[NSNumber numberWithInt:self.sermonType]];
    [query whereKey:PARSE_IS_DELETE notEqualTo:[NSNumber numberWithBool:YES]];
    if (language.length > 0) {
        [query whereKey:PARSE_LANGUAGE equalTo:language];
    }
    [query includeKey:PARSE_OWNER];
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
    return 55.f;
}

- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"SermonListCell";
    SermonListCell *cell = (SermonListCell *)[tv dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell){
        PFObject * sermonObj = [mDataList objectAtIndex:indexPath.row];
        NSString *mosque = sermonObj[PARSE_MOSQUE];
        NSString *raiser = sermonObj[PARSE_RAISER];
        int type = [sermonObj[PARSE_TYPE] intValue];
        if (type < TYPE_RAISE) {
            mosque = self.mUserObj[PARSE_MOSQUE];
            raiser = [NSString stringWithFormat:@"%@ %@", self.mUserObj[PARSE_FIRSTNAME], self.mUserObj[PARSE_LASTSTNAME]];
        }
        
        cell.lblMosque.text = [NSString stringWithFormat:@"%@ (%@)", mosque, raiser];
        cell.lblDate.text = [Util convertDateToString:sermonObj.createdAt];
        cell.lblTopic.text = sermonObj[PARSE_TOPIC];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    PFObject * sermonObj = [mDataList objectAtIndex:indexPath.row];
    NSString *url = sermonObj[PARSE_VIDEO];
    if (url != nil && url.length > 0) {
        VideoViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"VideoViewController"];
        controller.mSermonObj = sermonObj;
        [self.navigationController pushViewController:controller animated:YES];
    } else {
        NSString * url = [NSString stringWithFormat:@"https://stripe.kaotech.org/donation?sermon=%@", sermonObj.objectId];
        NSURL *nsUrl = [NSURL URLWithString:url];
        SFSafariViewController *svc = [[SFSafariViewController alloc] initWithURL:nsUrl];
        svc.delegate = self;
        [self presentViewController:svc animated:YES completion:nil];
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
- (void)safariViewControllerDidFinish:(SFSafariViewController *)controller {
    [self dismissViewControllerAnimated:true completion:nil];
}

#pragma mark    IQDropDownTextField
- (void)textField:(IQDropDownTextField *)textField didSelectItem:(NSString *)item {
    [self getServerData];
}
@end
