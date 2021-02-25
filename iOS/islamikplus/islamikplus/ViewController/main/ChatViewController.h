//
//  ChatViewController.h
//  scoutmaster
//
//  Created by Ales Gabrysz on 29/09/2019.
//  Copyright © 2019 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
NS_ASSUME_NONNULL_BEGIN

@interface ChatViewController : BaseViewController
@property (strong, nonatomic) PFUser *toUser;
@property (strong, nonatomic) PFObject *bookObj;
@end

NS_ASSUME_NONNULL_END
