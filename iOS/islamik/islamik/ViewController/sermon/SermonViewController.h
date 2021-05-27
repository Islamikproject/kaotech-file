//
//  SermonViewController.h
//  islamik
//
//  Created by Ales Gabrysz on 5/22/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
NS_ASSUME_NONNULL_BEGIN

@interface SermonViewController : BaseViewController
@property (atomic) int sermonType;
@property (atomic) int userType;
@property (atomic) int continentType;
@end

NS_ASSUME_NONNULL_END
