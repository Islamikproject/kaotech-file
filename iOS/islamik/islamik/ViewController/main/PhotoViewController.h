//
//  PhotoViewController.h
//  islamik
//
//  Created by Ales Gabrysz on 11/16/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
NS_ASSUME_NONNULL_BEGIN

@interface PhotoViewController : BaseViewController
@property (nonatomic, retain) PFFileObject* mPhotoFile;
@end

NS_ASSUME_NONNULL_END
