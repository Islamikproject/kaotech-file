//
//  SermonListViewController.h
//  islamik
//
//  Created by Ales Gabrysz on 5/22/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
NS_ASSUME_NONNULL_BEGIN

@interface SermonListViewController : BaseViewController
@property (atomic) int sermonType;
@property (nonatomic, retain) PFUser* mUserObj;
@end

NS_ASSUME_NONNULL_END
