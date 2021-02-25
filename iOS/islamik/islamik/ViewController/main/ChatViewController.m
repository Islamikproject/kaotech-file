//
//  ChatViewController.m
//  scoutmaster
//
//  Created by Ales Gabrysz on 29/09/2019.
//  Copyright © 2019 Ales Gabrysz. All rights reserved.
//

#import "ChatViewController.h"
#import "ChatDetailViewController.h"

@interface ChatViewController () 
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (nonatomic, retain) ChatDetailViewController * chatDetailCtr;
@end

@implementation ChatViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    _lblTitle.text = [NSString stringWithFormat:@"%@ %@", _toUser[PARSE_FIRSTNAME], _toUser[PARSE_LASTSTNAME]];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([segue.identifier isEqualToString:@"showChat"]) {
        self.chatDetailCtr = (ChatDetailViewController *) segue.destinationViewController;
        self.chatDetailCtr.toUser = self.toUser;
        self.chatDetailCtr.bookObj = self.bookObj;
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

@end
