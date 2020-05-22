//
//  BasketCell.h
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface BasketCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *lblTopic;
@property (weak, nonatomic) IBOutlet UILabel *lblAmount;

@end

NS_ASSUME_NONNULL_END
