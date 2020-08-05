//
//  QuestionViewController.h
//  islamik
//
//  Created by Ales Gabrysz on 8/5/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
NS_ASSUME_NONNULL_BEGIN

@interface QuestionViewController : BaseViewController
@property (nonatomic, retain) PFObject* mSermonObj;
@end

NS_ASSUME_NONNULL_END
