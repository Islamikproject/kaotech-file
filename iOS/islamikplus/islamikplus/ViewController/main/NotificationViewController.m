//
//  NotificationViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 11/9/20.
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
    PFObject *notificationObj = [mDataList objectAtIndex:indexPath.row];
    PFObject *bookObj = notificationObj[PARSE_BOOK_OBJ];
    int state = [notificationObj[PARSE_STATE] intValue];
    if (bookObj != nil && state == STATE_PENDING) {
        return 130.f;
    } else {
        return 90.f;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tv cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *cellIdentifier = @"NotificationCell";
    NotificationCell *cell = (NotificationCell *)[tv dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell){
        PFObject *notificationObj = [mDataList objectAtIndex:indexPath.row];
        PFObject *bookObj = notificationObj[PARSE_BOOK_OBJ];
        PFUser *owner = notificationObj[PARSE_OWNER];
        int state = [notificationObj[PARSE_STATE] intValue];
        if (bookObj != nil && state == STATE_PENDING) {
            cell.heightAccept.constant = 35.f;
        } else {
            cell.heightAccept.constant = 0.f;
        }
        cell.lblMessage.text = notificationObj[PARSE_MESSAGE];
        cell.lblDate.text = [Util convertNotificationDateTimeToString:notificationObj.createdAt];
        PFFileObject *avatarFile = owner[PARSE_AVATAR];
        [cell.imgAvatar sd_setImageWithURL:[NSURL URLWithString:avatarFile.url] placeholderImage:[UIImage imageNamed:@"default_profile"]];
        cell.btnVideo.hidden =YES;
        if (state == STATE_ACCEPT) {
            cell.btnVideo.hidden =NO;
        }
        cell.mNotificationObj = notificationObj;
        cell.delegate = self;
    }
    
    return cell;
}

- (void)didTapAccept:(PFObject *)notificationObj {
    [self updateState:notificationObj state:STATE_ACCEPT];
}

- (void)didTapReject:(PFObject *)notificationObj {
    [self updateState:notificationObj state:STATE_REJECT];
}

- (void)didTapVideo:(PFObject *)notificationObj {
    ChatViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"ChatViewController"];
    controller.bookObj = notificationObj[PARSE_BOOK_OBJ];
    controller.toUser = notificationObj[PARSE_OWNER];
    [self.navigationController pushViewController:controller animated:YES];
}

- (void)didTapCall:(PFObject *)notificationObj {
    NSString *phoneNumber = notificationObj[PARSE_OWNER][PARSE_PHONE_NUMBER];
    if (phoneNumber != nil && phoneNumber.length > 0) {
        NSString *url = [NSString stringWithFormat:@"tel://%@", phoneNumber];
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
    }
}
- (void) updateState: (PFObject *) notificationObj state:(int)state {
    notificationObj[PARSE_STATE] = [NSNumber numberWithInt:state];
    NSString *msg = [NSString stringWithFormat:@"%@\n%@", notificationObj[PARSE_MESSAGE], @"You have accepted the request."];
    if (state == STATE_REJECT) {
        msg = [NSString stringWithFormat:@"%@\n%@", notificationObj[PARSE_MESSAGE], @"You have rejected the request."];
    }
    notificationObj[PARSE_MESSAGE] = msg;
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    [notificationObj saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        if (error) {
            [SVProgressHUD dismiss];
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        }else{
            PFUser *currentUser = [PFUser currentUser];
            PFUser *owner = notificationObj[PARSE_OWNER];
            PFObject *bookObj = notificationObj[PARSE_BOOK_OBJ];
            NSString *message = [NSString stringWithFormat:@"%@ %@ session request has been accepted.", currentUser[PARSE_FIRSTNAME], currentUser[PARSE_LASTSTNAME]];
            if (state == STATE_REJECT) {
                message = [NSString stringWithFormat:@"%@ %@ session request has been rejected, please select another date or message the %@ %@ directly.", currentUser[PARSE_FIRSTNAME], currentUser[PARSE_LASTSTNAME], owner[PARSE_FIRSTNAME], owner[PARSE_LASTSTNAME]];
            }
            [Util sendPushNotification:owner[PARSE_EMAIL_ADDRESS] message:message type:TYPE_BOOK];
            
            PFObject *object = [PFObject objectWithClassName:PARSE_TABLE_NOTIFICATION];
            object[PARSE_OWNER] = currentUser;
            object[PARSE_TO_USER] = owner;
            object[PARSE_TYPE] = [NSNumber numberWithInt:TYPE_BOOK];
            object[PARSE_MESSAGE] = message;
            object[PARSE_STATE] = [NSNumber numberWithInt:state];
            if (bookObj != nil) {
                object[PARSE_BOOK_OBJ] = bookObj;
            }
            [object saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
                [SVProgressHUD dismiss];
                [self getServerData];
            }];
        }
    }];
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {}
@end
