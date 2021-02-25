//
//  NotificationViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 11/16/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "NotificationViewController.h"
#import "NotificationCell.h"
#import "ChatViewController.h"

@interface NotificationViewController ()<UITableViewDelegate, UITableViewDataSource, NotificationCellDelegate>
{
    NSMutableArray * mDataList;
}
@property (weak, nonatomic) IBOutlet UITableView *tblData;

@end

@implementation NotificationViewController

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
    PFQuery * query = [PFQuery queryWithClassName:PARSE_TABLE_NOTIFICATION];
    [query whereKey:PARSE_TO_USER equalTo:[PFUser currentUser]];
    [query includeKey:PARSE_OWNER];
    [query includeKey:PARSE_TO_USER];
    [query includeKey:PARSE_BOOK_OBJ];
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
    return 80.f;
}

- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"NotificationCell";
    NotificationCell *cell = (NotificationCell *)[tv dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell){
        PFObject * notificationObj = [mDataList objectAtIndex:indexPath.row];
        PFUser *owner = notificationObj[PARSE_OWNER];
        int state = [notificationObj[PARSE_STATE] intValue];
        cell.lblMessage.text = notificationObj[PARSE_MESSAGE];
        cell.lblDate.text = [Util convertDateToString:notificationObj.createdAt];
        PFFileObject *photoFile = owner[PARSE_AVATAR];
        [cell.imgAvatar sd_setImageWithURL:[NSURL URLWithString:photoFile.url] placeholderImage:[UIImage imageNamed:@"default_profile"]];
        cell.btnVideo.hidden =YES;
        if (state == STATE_ACCEPT) {
            cell.btnVideo.hidden =NO;
        }
        cell.mNotificationObj = notificationObj;
        cell.delegate = self;
    }
    
    return cell;
}

- (void)didTapVideo:(PFObject *)notificationObj {
    ChatViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"ChatViewController"];
    controller.bookObj = notificationObj[PARSE_BOOK_OBJ];
    controller.toUser = notificationObj[PARSE_OWNER];
    [self.navigationController pushViewController:controller animated:YES];
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    PFObject * notificationObj = [mDataList objectAtIndex:indexPath.row];
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
