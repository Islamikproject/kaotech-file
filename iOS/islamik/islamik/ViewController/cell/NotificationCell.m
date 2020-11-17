//
//  NotificationCell.m
//  islamik
//
//  Created by Ales Gabrysz on 11/16/20.
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
- (IBAction)onVideoClick:(id)sender {
    [_delegate didTapVideo:self.mNotificationObj];
}

@end
