//
//  AppStateManager.h
//  Partner
//
//  Created by star on 12/8/15.
//  Copyright (c) 2015 zapporoo. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AppStateManager : NSObject

@property (assign, nonatomic) int alertCount;

- (void)resetAlertCount;

@end
