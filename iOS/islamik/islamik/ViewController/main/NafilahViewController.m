//
//  NafilahViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/26/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "NafilahViewController.h"
#import "SurahModel.h"
#import "ReadyViewController.h"

@interface NafilahViewController () <IQDropDownTextFieldDelegate>{
    int type;
}
@property (weak, nonatomic) IBOutlet UIButton *btnTwo;
@property (weak, nonatomic) IBOutlet UIButton *btnFour;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spLanguage;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spFirstChapter;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spFirstVerseStart;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spFirstVerseEnd;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spSecondChapter;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spSecondVerseStart;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spSecondVerseEnd;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *spSpeed;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *viewSecondHight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *viewThirdHight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *viewFourthHight;
@end

@implementation NafilahViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
    [self setType:TYPE_FAJR];
}
- (void)initialize {
    self.spLanguage.itemList = LANGUAGE_ARRAY;
    self.spLanguage.isOptionalDropDown = YES;
    self.spLanguage.selectedRow = 0;
    
    self.spSpeed.itemList = SPEED_ARRAY;
    self.spSpeed.isOptionalDropDown = YES;
    self.spSpeed.selectedRow = 0;
    
    self.spFirstChapter.itemList = CHAPTER_ARRAY;
    self.spFirstChapter.isOptionalDropDown = YES;
    self.spFirstChapter.selectedRow = 0;
    self.spFirstChapter.delegate = self;
    self.spFirstVerseStart.isOptionalDropDown = YES;
    self.spFirstVerseEnd.isOptionalDropDown = YES;
    [self setVerses:NO];
    
    self.spSecondChapter.itemList = CHAPTER_ARRAY;
    self.spSecondChapter.isOptionalDropDown = YES;
    self.spSecondChapter.selectedRow = 0;
    self.spSecondChapter.delegate = self;
    self.spSecondVerseStart.isOptionalDropDown = YES;
    self.spSecondVerseEnd.isOptionalDropDown = YES;
    [self setVerses:YES];
}
- (void) setType:(int) index {
    type = index;
    
    self.btnTwo.layer.borderColor = UIColor.whiteColor.CGColor;
    self.btnTwo.layer.borderWidth = 1;
    self.btnTwo.backgroundColor = UIColor.clearColor;
    [self.btnTwo setTitleColor:[UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1.0] forState:UIControlStateNormal];
    
    self.btnFour.layer.borderColor = UIColor.whiteColor.CGColor;
    self.btnFour.layer.borderWidth = 1;
    self.btnFour.backgroundColor = UIColor.clearColor;
    [self.btnFour setTitleColor:[UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1.0] forState:UIControlStateNormal];
    
    self.viewThirdHight.constant = 0;
    self.viewFourthHight.constant = 0;
    if (type == TYPE_FAJR) {
        self.btnTwo.backgroundColor = UIColor.whiteColor;
        [self.btnTwo setTitleColor:[UIColor colorWithRed:14/255.0 green:97/255.0 blue:41/255.0 alpha:1.0] forState:UIControlStateNormal];
    } else if (type == TYPE_ZUHR) {
        self.btnFour.backgroundColor = UIColor.whiteColor;
        [self.btnFour setTitleColor:[UIColor colorWithRed:14/255.0 green:97/255.0 blue:41/255.0 alpha:1.0] forState:UIControlStateNormal];
        self.viewThirdHight.constant = 70;
        self.viewFourthHight.constant = 70;
    }
}
-(void) setVerses:(BOOL) isSecond {
    NSArray *verses = [NSArray new];
    if (!isSecond) {
        verses = [Util getEnglishVerseArray:self.spFirstChapter.selectedItem];
    } else {
        verses = [Util getEnglishVerseArray:self.spSecondChapter.selectedItem];
    }
    NSMutableArray * verseNumber = [NSMutableArray new];
    for (int i = 0; i < verses.count; i ++) {
        [verseNumber addObject:[NSString stringWithFormat:@"%d", (i + 1)]];
    }
    if (!isSecond) {
        self.spFirstVerseStart.selectedItem = @"";
        self.spFirstVerseEnd.selectedItem = @"";
        self.spFirstVerseStart.itemList = verseNumber;
        self.spFirstVerseStart.selectedRow = 0;
        self.spFirstVerseEnd.itemList = verseNumber;
        self.spFirstVerseEnd.selectedRow = verseNumber.count-1;
    } else {
        self.spSecondVerseStart.selectedItem = @"";
        self.spSecondVerseEnd.selectedItem = @"";
        self.spSecondVerseStart.itemList = verseNumber;
        self.spSecondVerseStart.selectedRow = 0;
        self.spSecondVerseEnd.itemList = verseNumber;
        self.spSecondVerseEnd.selectedRow = verseNumber.count-1;
    }
}
-(void)textField:(nonnull IQDropDownTextField*)textField didSelectItem:(nullable NSString*)item
{
    if (item.length == 0) {
        
    } else if (textField == self.spFirstChapter) {
        [self setVerses:NO];
    } else if (textField == self.spSecondChapter) {
        [self setVerses:YES];
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
- (IBAction)onTwoClick:(id)sender {
    [self setType:TYPE_FAJR];
}
- (IBAction)onFourClick:(id)sender {
    [self setType:TYPE_ZUHR];
}
- (IBAction)onDoneClick:(id)sender {
    if ([self isValid]) {
        NSMutableArray *dataList = [NSMutableArray new];
        [dataList addObject:[self getSurahModel:YES]];
        [dataList addObject:[self getSurahModel:NO]];
        ReadyViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"ReadyViewController"];
        controller.type = type;
        controller.mDataList = dataList;
        [self.navigationController pushViewController:controller animated:YES];
    }
}
- (BOOL) isValid {
    NSString * errorMsg = @"";
    if (self.spLanguage.selectedItem.length == 0) {
        errorMsg = @"Please choose Language.";
    } else if (self.spFirstChapter.selectedItem.length == 0) {
        errorMsg = @"Please choose Surah(Chapter) for the first rakah.";
    } else if (self.spFirstVerseStart.selectedItem.length == 0 || self.spFirstVerseEnd.selectedItem.length == 0) {
        errorMsg = @"Please choose Āyāt (Verses) for the first rakah.";
    } else if (self.spSecondChapter.selectedItem.length == 0) {
        errorMsg = @"Please choose Surah(Chapter) for the second rakah.";
    } else if (self.spSecondVerseStart.selectedItem.length == 0 || self.spSecondVerseEnd.selectedItem.length == 0) {
        errorMsg = @"Please choose Āyāt (Verses) for the second rakah.";
    } else if (self.spSpeed.selectedItem.length == 0) {
        errorMsg = @"Please choose Speed.";
    } else if (self.spFirstVerseStart.selectedRow > self.spFirstVerseEnd.selectedRow) {
        errorMsg = @"Start Āyāt (Verses) must be <= End Āyāt (Verses).";
    } else if (self.spSecondVerseStart.selectedRow > self.spSecondVerseEnd.selectedRow) {
        errorMsg = @"Start Āyāt (Verses) must be <= End Āyāt (Verses).";
    }
    if (errorMsg.length > 0) {
        [Util showAlertTitle:self title:@"Error" message:errorMsg];
        return NO;
    }
    return YES;
}
- (SurahModel *) getSurahModel:(BOOL)isFirst {
    SurahModel *model = [SurahModel new];
    model.language = self.spLanguage.selectedRow;
    model.speed = self.spSpeed.selectedRow;
    if (isFirst) {
        model.chapter = CHAPTER_ARRAY[self.spFirstChapter.selectedRow];
        NSArray *verses = [Util getEnglishVerseArray:self.spFirstChapter.selectedItem];
        model.verse = [Util getVerseString:verses start:self.spFirstVerseStart.selectedRow end:self.spFirstVerseEnd.selectedRow];
    } else {
        model.chapter = CHAPTER_ARRAY[self.spSecondChapter.selectedRow];
        NSArray *verses = [Util getEnglishVerseArray:self.spSecondChapter.selectedItem];
        model.verse = [Util getVerseString:verses start:self.spSecondVerseStart.selectedRow end:self.spSecondVerseEnd.selectedRow];
    }
    return model;
}
@end
