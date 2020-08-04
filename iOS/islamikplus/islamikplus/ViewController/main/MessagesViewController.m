//
//  MessagesViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 8/4/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "MessagesViewController.h"
#import "MessageCell.h"
#import "AnswerViewController.h"

@interface MessagesViewController () <UITableViewDelegate, UITableViewDataSource>
{
    NSMutableArray * mDataList;
}
@property (weak, nonatomic) IBOutlet UITableView *tblData;
@end

@implementation MessagesViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
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

    PFQuery * query = [PFQuery queryWithClassName:PARSE_TABLE_MESSAGES];
    [query whereKey:PARSE_MOSQUE equalTo:[PFUser currentUser]];
    [query includeKey:PARSE_OWNER];
    [query includeKey:PARSE_MOSQUE];
    [query includeKey:PARSE_SERMON];
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
    static NSString *cellIdentifier = @"MessageCell";
    MessageCell *cell = (MessageCell *)[tv dequeueReusableCellWithIdentifier:cellIdentifier];
    if(cell){
        PFObject *messageObj = [mDataList objectAtIndex:indexPath.row];
        PFUser *owner = messageObj[PARSE_OWNER];
        PFObject *sermonObj = messageObj[PARSE_SERMON];
        cell.lblName.text = [NSString stringWithFormat:@"%@ %@", owner[PARSE_FIRSTNAME], owner[PARSE_LASTSTNAME]];
        cell.lblDate.text = [Util convertDateToString:messageObj.createdAt];
        cell.lblTitle.text = sermonObj[PARSE_TOPIC];
        cell.lblQuestion.text = [NSString stringWithFormat:@"Question: %@", messageObj[PARSE_QUESTION]];
        cell.lblAnswer.text = [NSString stringWithFormat:@"Answer: %@", messageObj[PARSE_ANSWER]];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    PFObject * messageObj = [mDataList objectAtIndex:indexPath.row];
    AnswerViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"AnswerViewController"];
    controller.mMessageObj = messageObj;
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
