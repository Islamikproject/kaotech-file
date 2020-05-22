//
//  AppStateManager.m
//  Partner
//
//  Created by star on 12/8/15.
//  Copyright (c) 2015 zapporoo. All rights reserved.
//

#import "AppStateManager.h"
#import <AVFoundation/AVFoundation.h>


#define SOUND_VOLUME    1.0

@interface AppStateManager() <AVAudioPlayerDelegate>
@end

@implementation AppStateManager

- (void)resetAlertCount {
    self.alertCount = 0;
}

@end
