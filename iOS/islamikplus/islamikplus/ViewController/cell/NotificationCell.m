//
//  NotificationCell.m
//  islamikplus
//
//  Created by Ales Gabrysz on 11/12/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "NotificationCell.h"

@implementation NotificationCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)onAcceptClick:(id)sender {
    [_delegate didTapAccept:self.mNotificationObj];
}

- (IBAction)onRejectClick:(id)sender {
    [_delegate didTapReject:self.mNotificationObj];
}
@end
