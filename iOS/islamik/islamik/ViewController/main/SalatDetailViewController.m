//
//  SalatDetailViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/26/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "SalatDetailViewController.h"
#import "SurahModel.h"

@interface SalatDetailViewController (){
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

@implementation SalatDetailViewController

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
    } else if (count == 1) {
        self.viewFirst.hidden = NO;
    } else if (count == 2) {
        self.viewSecond.hidden = NO;
    } else if (count == 3) {
        self.viewThird.hidden = NO;
    } else if (count == 4) {
        self.viewFourth.hidden = NO;
    } else if (count == 5) {
        self.viewThird.hidden = NO;
    } else if (count == 6) {
        self.viewFifth.hidden = NO;
    } else if (count == 7) {
        self.viewSixth.hidden = NO;
    } else if (count == 8) {
        [self onBack:nil];
    }
    
    if (count != 0 && count < 8) {
        timer=[NSTimer scheduledTimerWithTimeInterval:TIME_SPEED target:self selector:@selector(updateUI) userInfo:nil repeats:NO];
    }
}

- (void) setContent:(BOOL) isFirst{
    SurahModel *model = self.mDataList[0];
    NSString * html = @"<table style='width:100%'>";
    html = [html stringByAppendingFormat:@"<tr><th style='font-size:30px'> Allah Akbar <br></th></tr>"];
    html = [html stringByAppendingFormat:@"<tr><th style='font-size:25px'> %@ <br></th></tr>", MAIN_CHAPTER];
    html = [html stringByAppendingFormat:@"<tr><td style='font-size:20px'> %@ <br><br><br></td></tr>", [Util getVerseString:MAIN_VERSE start:0 end:MAIN_VERSE.count-1]];
    html = [html stringByAppendingFormat:@"<tr><th style='font-size:25px'> %@ <br></th></tr>", model.chapter];
    html = [html stringByAppendingFormat:@"<tr><td style='font-size:20px'> %@ </td></tr>", model.verse];
    html = [html stringByAppendingFormat:@"<tr><th style='font-size:30px'> Allah Akbar <br></th></tr>"];
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
