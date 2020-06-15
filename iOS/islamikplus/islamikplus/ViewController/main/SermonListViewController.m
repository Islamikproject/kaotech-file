//
//  SermonListViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "SermonListViewController.h"
#import "SermonViewController.h"
#import "SermonCell.h"
#import "VideoViewController.h"

@interface SermonListViewController () <UITableViewDelegate, UITableViewDataSource>
{
    NSMutableArray * mDataList;
}
@property (weak, nonatomic) IBOutlet UITableView *tblData;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;

@end

@implementation SermonListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    if (self.type == TYPE_JUMAH) {
        self.lblTitle.text = @"JUMAH SERMON";
    } else {
        self.lblTitle.text = @"REGULAR SERMON";
    }
}
- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self getServerData];
}
- (void) getServerData{
    if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return;
    }
    mDataList = [NSMutableArray new];

    PFQuery * query = [PFQuery queryWithClassName:PARSE_TABLE_SERMON];
    [query whereKey:PARSE_OWNER equalTo:[PFUser currentUser]];
    [query whereKey:PARSE_TYPE equalTo:[NSNumber numberWithInt:self.type]];
    [query whereKey:PARSE_IS_DELETE notEqualTo:[NSNumber numberWithBool:YES]];
    [query includeKey:PARSE_OWNER];
    [query orderByDescending:PARSE_FIELD_CREATED_AT];
    [query setLimit:1000];
    
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    [query findObjectsInBackgroundWithBlock:^(NSArray *array, NSError *error){
        [SVProgressHUD dismiss];
        self->mDataList = (NSMutableArray *) array;
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
    return 55.f;
}

- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"SermonCell";
    SermonCell *cell = (SermonCell *)[tv dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell){
        PFObject * sermonObj = [mDataList objectAtIndex:indexPath.row];
        PFUser *owner = sermonObj[PARSE_OWNER];
        NSString *mosque = sermonObj[PARSE_MOSQUE];
        NSString *raiser = sermonObj[PARSE_RAISER];
        if (self.type < TYPE_RAISE) {
            mosque = owner[PARSE_MOSQUE];
            raiser = [NSString stringWithFormat:@"%@ %@", owner[PARSE_FIRSTNAME], owner[PARSE_LASTSTNAME]];
        }
        cell.lblMosque.text = [NSString stringWithFormat:@"%@ (%@)", mosque, raiser];
        cell.lblDate.text = [Util convertDateToString:sermonObj.createdAt];
        cell.lblTitle.text = sermonObj[PARSE_TOPIC];
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
- (IBAction)onAddClick:(id)sender {
    SermonViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SermonViewController"];
    controller.type = self.type;
    [self.navigationController pushViewController:controller animated:YES];
}

@end
