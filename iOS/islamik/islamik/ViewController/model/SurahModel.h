//
//  SurahModel.h
//  islamik
//
//  Created by Ales Gabrysz on 5/25/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface SurahModel : NSObject
@property (atomic) NSInteger language;
@property (nonatomic, strong) NSString *chapter;
@property (nonatomic, strong) NSString *verse;
@property (atomic) NSInteger speed;
@end

NS_ASSUME_NONNULL_END
