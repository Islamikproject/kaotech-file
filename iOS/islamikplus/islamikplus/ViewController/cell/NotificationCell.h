//
//  NotificationCell.h
//  islamikplus
//
//  Created by Ales Gabrysz on 11/12/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <Parse/Parse.h>

NS_ASSUME_NONNULL_BEGIN
@protocol NotificationCellDelegate <NSObject>
- (void)didTapAccept:(PFObject *)notificationObj;
- (void)didTapReject:(PFObject *)notificationObj;
- (void)didTapVideo:(PFObject *)notificationObj;
- (void)didTapCall:(PFObject *)notificationObj;
@end

@interface NotificationCell : UITableViewCell
@property (nonatomic, retain) id<NotificationCellDelegate> delegate;
@property (nonatomic, retain) PFObject *mNotificationObj;
@property (weak, nonatomic) IBOutlet UIImageView *imgAvatar;
@property (weak, nonatomic) IBOutlet UILabel *lblMessage;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightAccept;
@property (weak, nonatomic) IBOutlet UIButton *btnVideo;
@property (weak, nonatomic) IBOutlet UIButton *btnCall;
@end

NS_ASSUME_NONNULL_END
