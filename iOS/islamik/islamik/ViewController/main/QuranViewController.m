//
//  QuranViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/26/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "QuranViewController.h"
#import "ReadyViewController.h"
#import "SurahModel.h"

@interface QuranViewController () <IQDropDownTextFieldDelegate> {
    NSMutableArray *arrayChapter;
    NSMutableArray *arrayVerse;
}
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spLanguage;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spChapter;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spVerseStart;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spVerseEnd;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spReciter;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spSpeed;
@end

@implementation QuranViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
}
- (void)initialize {
    self.spLanguage.itemList = LANGUAGE_ARRAY;
    self.spLanguage.isOptionalDropDown = YES;
    self.spLanguage.selectedRow = 0;
    
    self.spSpeed.itemList = SPEED_ARRAY;
    self.spSpeed.isOptionalDropDown = YES;
    self.spSpeed.selectedRow = 0;
    
    self.spReciter.itemList = ARRAY_RECITER;
    self.spReciter.isOptionalDropDown = YES;
    self.spReciter.selectedRow = 0;
    
    arrayChapter = [NSMutableArray new];
    [arrayChapter addObject:MAIN_CHAPTER];
    for (int i = 0; i < CHAPTER_ARRAY.count; i ++) {
        [arrayChapter addObject:CHAPTER_ARRAY[i]];
    }
    self.spChapter.itemList = arrayChapter;
    self.spChapter.isOptionalDropDown = YES;
    self.spChapter.selectedRow = 0;
    self.spChapter.delegate = self;
    self.spVerseStart.isOptionalDropDown = YES;
    self.spVerseEnd.isOptionalDropDown = YES;
    [self setVerses];
}
-(void) setVerses {
    
    NSArray *verses = MAIN_VERSE;
    if (self.spChapter.selectedRow > 0) {
        verses = [Util getEnglishVerseArray:self.spChapter.selectedItem];
    }
    NSMutableArray * verseNumber = [NSMutableArray new];
    for (int i = 0; i < verses.count; i ++) {
        [verseNumber addObject:[NSString stringWithFormat:@"%d", (i + 1)]];
    }
    self.spVerseStart.selectedItem = @"";
    self.spVerseEnd.selectedItem = @"";
    self.spVerseStart.itemList = verseNumber;
    self.spVerseStart.selectedRow = 0;
    self.spVerseEnd.itemList = verseNumber;
    self.spVerseEnd.selectedRow = verseNumber.count-1;
}
-(void)textField:(nonnull IQDropDownTextField*)textField didSelectItem:(nullable NSString*)item
{
    if (item.length == 0) {
        
    } else {
        [self setVerses];
    }
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onDoneClick:(id)sender {
    if ([self isValid]) {
        NSMutableArray *dataList = [NSMutableArray new];
        [dataList addObject:[self getSurahModel]];
        ReadyViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"ReadyViewController"];
        controller.type = TYPE_QURAN;
        controller.mDataList = dataList;
        [self.navigationController pushViewController:controller animated:YES];
    }
}
- (BOOL) isValid {
    NSString * errorMsg = @"";
    if (self.spLanguage.selectedItem.length == 0) {
        errorMsg = @"Please choose Language.";
    } else if (self.spChapter.selectedItem.length == 0) {
        errorMsg = @"Please choose Surah(Chapter).";
    } else if (self.spVerseStart.selectedItem.length == 0 || self.spVerseEnd.selectedItem.length == 0) {
        errorMsg = @"Please choose Āyāt (Verses).";
    } else if (self.spReciter.selectedItem.length == 0) {
        errorMsg = @"Please choose Reciter.";
    } else if (self.spSpeed.selectedItem.length == 0) {
        errorMsg = @"Please choose Speed.";
    } else if (self.spVerseStart.selectedRow > self.spVerseEnd.selectedRow) {
        errorMsg = @"Start Āyāt (Verses) must be <= End Āyāt (Verses).";
    }
    if (errorMsg.length > 0) {
        [Util showAlertTitle:self title:@"Error" message:errorMsg];
        return NO;
    }
    return YES;
}
- (SurahModel *) getSurahModel {
    SurahModel *model = [SurahModel new];
    model.language = self.spLanguage.selectedRow;
    model.speed = self.spSpeed.selectedRow;
    model.surahId = self.spChapter.selectedRow;
    model.chapter = MAIN_CHAPTER;
    NSArray *verses = MAIN_VERSE;
    if (model.language == 1) {
        verses = MAIN_VERSE_ARABIC;
    }
    if (self.spChapter.selectedRow > 0) {
        model.chapter = CHAPTER_ARRAY[self.spChapter.selectedRow - 1];
        verses = [Util getEnglishVerseArray:self.spChapter.selectedItem];
        if (model.language == 1) {
            verses = [Util getArabicVerseArray:self.spChapter.selectedItem];
        }
    }
    model.verse = [Util getVerseString:verses start:self.spVerseStart.selectedRow end:self.spVerseEnd.selectedRow];
    return model;
}
@end
