//
//  DailyViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 11/11/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "GaugeListViewController.h"
#import "GaugeViewController.h"
#import "GaugeCell.h"

@interface GaugeListViewController () <UITableViewDelegate, UITableViewDataSource>
{
    NSMutableArray * mDataList;
}
@property (weak, nonatomic) IBOutlet UITableView *tblData;

@end

@implementation GaugeListViewController

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

    PFQuery * query = [PFQuery queryWithClassName:PARSE_TABLE_GAUGE];
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
    return 100.f;
}

- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"GaugeCell";
    GaugeCell *cell = (GaugeCell *)[tv dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell){
        PFObject * gaugeObj = [mDataList objectAtIndex:indexPath.row];
        cell.lblWebLink.text = gaugeObj[PARSE_WEB_LINK];
        cell.lblDate.text = [Util convertDateToString:gaugeObj.createdAt];
        NSString *description = gaugeObj[PARSE_DESCRIPTION];
        cell.lblDescription.text = description;
        if (description.length > 0) {
            int bgColor = [gaugeObj[PARSE_BG_COLOR] intValue];
            int textColor = [gaugeObj[PARSE_TEXT_COLOR] intValue];
            int textFont = [gaugeObj[PARSE_TEXT_FONT] intValue];
            int textSize = [gaugeObj[PARSE_TEXT_SIZE] intValue] + 10;
            [cell.lblDescription setFont:[UIFont fontWithName:ARRAY_FONT[textFont] size:15]];
            [cell.lblDescription setTextColor:ARRAY_COLOR[textColor]];
            [cell.lblDescription setBackgroundColor:ARRAY_COLOR[bgColor]];
        }
        PFFileObject *photoFile = gaugeObj[PARSE_PHOTO];
        [cell.imgPhoto sd_setImageWithURL:[NSURL URLWithString:photoFile.url] placeholderImage:[UIImage imageNamed:@"default_image_bg"]];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    PFObject * gaugeObj = [mDataList objectAtIndex:indexPath.row];
    GaugeViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"GaugeViewController"];
    controller.mGaugeObj = gaugeObj;
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

@end
