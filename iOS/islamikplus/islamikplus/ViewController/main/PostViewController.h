//
//  PostViewController.h
//  islamikplus
//
//  Created by Ales Gabrysz on 11/12/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
NS_ASSUME_NONNULL_BEGIN

@interface PostViewController : BaseViewController
@property (nonatomic, retain) PFObject* mPostObj;
@end

NS_ASSUME_NONNULL_END
