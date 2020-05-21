//
//  TermsViewController.h
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
NS_ASSUME_NONNULL_BEGIN

#define RUN_MODE_TERMS 0
#define RUN_MODE_PRIVACY 1
@interface TermsViewController : BaseViewController
@property (atomic) int runMode;
@end

NS_ASSUME_NONNULL_END
