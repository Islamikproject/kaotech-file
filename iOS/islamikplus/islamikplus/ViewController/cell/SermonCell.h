//
//  SermonCell.h
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface SermonCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *lblMosque;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;

@end

NS_ASSUME_NONNULL_END
