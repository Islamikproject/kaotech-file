//
//  GaugeViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/27/21.
//  Copyright © 2021 Ales Gabrysz. All rights reserved.
//

#import "GaugeViewController.h"
#import <AVKit/AVKit.h>

@interface GaugeViewController ()
@property (weak, nonatomic) IBOutlet UITextView *txtDescription;
@property (weak, nonatomic) IBOutlet UILabel *lblWebLink;
@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;
@property (weak, nonatomic) IBOutlet UIView *videoView;

@property (nonatomic, retain) AVPlayer *player;
@property (nonatomic, retain) AVPlayerLayer *playerLayer;
@end

@implementation GaugeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.txtDescription.editable = NO;
    [self initialize];
}

- (void) initialize {
    if (self.mGaugeObj != nil) {
        self.lblWebLink.text = self.mGaugeObj[PARSE_WEB_LINK];
        NSString *description = self.mGaugeObj[PARSE_DESCRIPTION];
        self.txtDescription.text = description;
        if (description.length > 0) {
            int bgColor = [self.mGaugeObj[PARSE_BG_COLOR] intValue];
            int textColor = [self.mGaugeObj[PARSE_TEXT_COLOR] intValue];
            int textFont = [self.mGaugeObj[PARSE_TEXT_FONT] intValue];
            int textSize = [self.mGaugeObj[PARSE_TEXT_SIZE] intValue];
            [self.txtDescription setFont:[UIFont fontWithName:ARRAY_FONT[textFont] size:textSize + 10]];
            [self.txtDescription setTextColor:ARRAY_COLOR[textColor]];
            [self.txtDescription setBackgroundColor:ARRAY_COLOR[bgColor]];
        }
        
        PFFileObject *photoFile = self.mGaugeObj[PARSE_PHOTO];
        [self.imgPhoto sd_setImageWithURL:[NSURL URLWithString:photoFile.url] placeholderImage:[UIImage imageNamed:@"default_image_bg"]];
        NSString *video = self.mGaugeObj[PARSE_VIDEO];
        if (video.length > 0) {
            NSURL *videoURL = [NSURL URLWithString:video];
            self.player = [AVPlayer playerWithURL:videoURL];
            [self.player addObserver:self forKeyPath:@"rate" options:0 context:nil];
            self.playerLayer = [AVPlayerLayer playerLayerWithPlayer:self.player];
            self.playerLayer.frame = self.videoView.bounds;
            self.playerLayer.needsDisplayOnBoundsChange = true;
            [self.videoView.layer addSublayer:self.playerLayer];
            [self.player play];
        }
    }
}
- (IBAction)onWebLinkClick:(id)sender {
    NSString *webLink = self.mGaugeObj[PARSE_WEB_LINK];
    if (webLink.length > 0) {
        if ([[UIApplication sharedApplication] canOpenURL:[NSURL URLWithString: webLink]]){
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:webLink]];
        }
    }
}

- (void) viewWillLayoutSubviews
{
    [super viewWillLayoutSubviews];
    self.playerLayer.frame = self.videoView.bounds;
}
- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context {
    if ([keyPath isEqualToString:@"rate"]) {
        if ([self.player rate]) {
            NSLog(@"[self changeToPause];  // This changes the button to Pause");
        } else {
            NSLog(@"[self changeToPlay];  // This changes the button to Play");
        }
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

@end
