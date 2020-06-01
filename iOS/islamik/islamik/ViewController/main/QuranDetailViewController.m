//
//  QuranDetailViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/26/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "QuranDetailViewController.h"
#import "SurahModel.h"
#import <AudioToolbox/AudioToolbox.h>
#import <AVFoundation/AVFoundation.h>

@interface QuranDetailViewController (){
    float speed;
    CGPoint current_position;
    
}
@property (weak, nonatomic) IBOutlet UITextView *textContent;
@property (nonatomic, retain) AVAudioPlayer * audioPlayer;
@end

@implementation QuranDetailViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    SurahModel *model = self.mDataList[0];
    speed = [SPEED_VALUE[model.speed] intValue];
    
    NSString *audioPath = [Util getReciterPath:model.surahId];
    NSURL *audioURL = [NSURL URLWithString:audioPath];

    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSURL *mp3URL = audioURL;
        NSData *data = [NSData dataWithContentsOfURL:mp3URL];
        self.audioPlayer = [[AVAudioPlayer alloc] initWithData:data error:NULL];
        [self.audioPlayer play];
    });
    [self setContent];
}

- (void) setContent{
    SurahModel *model = self.mDataList[0];
    
    NSString * html = @"<table style='width:100%'>";
    html = [html stringByAppendingFormat:@"<tr><th style='font-size:30px'> Allah Akbar <br><br></th></tr>"];
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
    self.textContent.attributedText = attributedString;
    current_position = CGPointZero;
    [self performSelector:@selector(moveTextView) withObject:nil afterDelay:ANIMATION_TIME];
}

- (void) moveTextView {
    current_position.y = current_position.y + speed;
    if(current_position.y >= self.textContent.contentSize.height){
        [self onBack:nil];
        return;
    }
    [UIView animateWithDuration:ANIMATION_TIME animations:^{
        [self.textContent setContentOffset:self->current_position animated:YES];
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
