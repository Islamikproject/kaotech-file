//
//  ReadyViewController.h
//  islamik
//
//  Created by Ales Gabrysz on 5/25/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
NS_ASSUME_NONNULL_BEGIN

@interface ReadyViewController : BaseViewController
@property (atomic) int type;
@property (nonatomic, strong) NSMutableArray *mDataList;
@end

NS_ASSUME_NONNULL_END
