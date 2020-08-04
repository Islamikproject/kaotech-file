//
//  MessageCell.h
//  islamikplus
//
//  Created by Ales Gabrysz on 8/4/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface MessageCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *lblName;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UILabel *lblQuestion;
@property (weak, nonatomic) IBOutlet UILabel *lblAnswer;

@end

NS_ASSUME_NONNULL_END
