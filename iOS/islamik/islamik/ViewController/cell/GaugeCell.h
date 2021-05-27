//
//  GaugeCell.h
//  islamikplus
//
//  Created by Ales Gabrysz on 5/26/21.
//  Copyright © 2021 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface GaugeCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;
@property (weak, nonatomic) IBOutlet UILabel *lblDescription;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblWebLink;

@end

NS_ASSUME_NONNULL_END
