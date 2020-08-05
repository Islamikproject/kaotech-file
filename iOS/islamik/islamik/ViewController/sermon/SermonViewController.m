//
//  SermonViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/22/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "SermonViewController.h"
#import "SermonListViewController.h"
#import "SermonCell.h"
#import "MapViewController.h"

@interface SermonViewController () <UITableViewDelegate, UITableViewDataSource>
{
    NSMutableArray * mDataList;
}
@property (weak, nonatomic) IBOutlet UITableView *tblData;

@end

@implementation SermonViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self getServerData];
}
- (void) getServerData{
    if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return;
    }
    mDataList = [NSMutableArray new];

    PFQuery *query = [PFUser query];
    [query whereKey:PARSE_TYPE equalTo:[NSNumber numberWithInt:TYPE_MOSQUE]];
    [query orderByAscending:PARSE_FIRSTNAME];
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
    static NSString *cellIdentifier = @"SermonCell";
    SermonCell *cell = (SermonCell *)[tv dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell){
        PFUser * userObj = [mDataList objectAtIndex:indexPath.row];
        cell.lblName.text = userObj[PARSE_MOSQUE];
        cell.lblAddress.text = userObj[PARSE_ADDRESS];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    PFUser * userObj = [mDataList objectAtIndex:indexPath.row];
    SermonListViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SermonListViewController"];
    controller.mUserObj = userObj;
    [self.navigationController pushViewController:controller animated:YES];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onNearClick:(id)sender {
    MapViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"MapViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
@end
