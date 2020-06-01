//
//  FajrDetailViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/25/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "FajrDetailViewController.h"
#import "SurahModel.h"

@interface FajrDetailViewController (){
    NSTimer *timer;
    int count;
    float speed;
    CGPoint current_position;
}
@property (weak, nonatomic) IBOutlet UIView *viewFirst;
@property (weak, nonatomic) IBOutlet UIView *viewSecond;
@property (weak, nonatomic) IBOutlet UIView *viewThird;
@property (weak, nonatomic) IBOutlet UIView *viewFourth;
@property (weak, nonatomic) IBOutlet UIView *viewFajr;
@property (weak, nonatomic) IBOutlet UIView *viewFifth;
@property (weak, nonatomic) IBOutlet UIView *viewSixth;
@property (weak, nonatomic) IBOutlet UITextView *txtContent;

@end

@implementation FajrDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    count = -1;
    SurahModel *model = self.mDataList[0];
    speed = [SPEED_VALUE[model.speed] intValue];
    [self updateUI];
}

- (void) updateUI {
    count ++;
    self.viewFirst.hidden = YES;
    self.viewSecond.hidden = YES;
    self.viewThird.hidden = YES;
    self.viewFourth.hidden = YES;
    self.viewFajr.hidden = YES;
    self.viewFifth.hidden = YES;
    self.viewSixth.hidden = YES;
    if (count == 0) {
        self.viewFajr.hidden = NO;
        [self setContent:YES];
    } else if (count == 6) {
        self.viewFajr.hidden = NO;
        [self setContent:NO];
    } else if (count == 1 || count == 7) {
        self.viewFirst.hidden = NO;
    } else if (count == 2 || count == 8) {
        self.viewSecond.hidden = NO;
    } else if (count == 3 || count == 9) {
        self.viewThird.hidden = NO;
    } else if (count == 4 || count == 10) {
        self.viewFourth.hidden = NO;
    } else if (count == 5 || count == 11) {
        self.viewThird.hidden = NO;
    } else if (count == 12) {
        self.viewFifth.hidden = NO;
    } else if (count == 13) {
        self.viewSixth.hidden = NO;
    } else if (count == 14) {
        [self onBack:nil];
    }
    
    if (count != 0 && count != 6 && count < 14) {
        timer=[NSTimer scheduledTimerWithTimeInterval:TIME_SPEED target:self selector:@selector(updateUI) userInfo:nil repeats:NO];
    }
}

- (void) setContent:(BOOL) isFirst{
    SurahModel *model = self.mDataList[0];
    if (!isFirst) {
        model = self.mDataList[1];
    }
    NSString *main_verse = [Util getVerseString:MAIN_VERSE start:0 end:MAIN_VERSE.count-1];
    if (model.language == 1) {
        main_verse = [Util getVerseString:MAIN_VERSE_ARABIC start:0 end:MAIN_VERSE.count-1];
    }
    NSString * html = @"<table style='width:100%'>";
    html = [html stringByAppendingFormat:@"<tr><th style='font-size:30px'> Allah Akbar <br><br></th></tr>"];
    html = [html stringByAppendingFormat:@"<tr><th style='font-size:25px'> %@ <br><br></th></tr>", MAIN_CHAPTER];
    html = [html stringByAppendingFormat:@"<tr><td style='font-size:20px'> %@ <br><br><br></td></tr>", main_verse];
    html = [html stringByAppendingFormat:@"<tr><th style='font-size:25px'> %@ <br><br></th></tr>", model.chapter];
    html = [html stringByAppendingFormat:@"<tr><td style='font-size:20px'> %@ <br><br><br></td></tr>", model.verse];
    html = [html stringByAppendingFormat:@"<tr><th style='font-size:30px'> Allah Akbar </th></tr>"];
    html = [html stringByAppendingFormat:@"</table>"];
    
    NSAttributedString *attributedString = [[NSAttributedString alloc]
              initWithData: [html dataUsingEncoding:NSUnicodeStringEncoding]
                   options: @{ NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType }
        documentAttributes: nil
                     error: nil
    ];
    self.txtContent.attributedText = attributedString;
    current_position = CGPointZero;
    [self performSelector:@selector(moveTextView) withObject:nil afterDelay:ANIMATION_TIME];
}

- (void) moveTextView {
    current_position.y = current_position.y + speed;
    if(current_position.y >= self.txtContent.contentSize.height){
        [self updateUI];
        return;
    }
    [UIView animateWithDuration:ANIMATION_TIME animations:^{
        [self.txtContent setContentOffset:self->current_position animated:YES];
    } completion:^(BOOL finished) {
        if(finished){
            [self performSelector:@selector(moveTextView) withObject:nil afterDelay:ANIMATION_TIME];
        }
    }];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
