//
//  AnswerViewController.h
//  islamikplus
//
//  Created by Ales Gabrysz on 8/4/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
NS_ASSUME_NONNULL_BEGIN

@interface AnswerViewController : BaseViewController
@property (nonatomic, retain) PFObject* mMessageObj;
@end

NS_ASSUME_NONNULL_END
