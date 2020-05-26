//
//  SermonListCell.h
//  islamik
//
//  Created by Ales Gabrysz on 5/22/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface SermonListCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *lblMosque;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblTopic;

@end

NS_ASSUME_NONNULL_END
