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
@property (atomic) int language;
@property (atomic) int surahId;
@property (atomic) int verseStart;
@property (atomic) int verseEnd;
@property (nonatomic, strong) NSString *surah;
@property (nonatomic, strong) NSString *verse;
@property (atomic) int speed;
@end

NS_ASSUME_NONNULL_END
