//
//  VideoViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "VideoViewController.h"
#import <AVKit/AVKit.h>

@interface VideoViewController ()
@property (weak, nonatomic) IBOutlet UIView *videoView;
@property (nonatomic, retain) AVPlayer *player;
@property (nonatomic, retain) AVPlayerLayer *playerLayer;
@end

@implementation VideoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
}

- (void) initialize {
    NSURL *videoURL = [NSURL URLWithString:self.mSermonObj[PARSE_VIDEO]];
    _player = [AVPlayer playerWithURL:videoURL];
    [_player addObserver:self forKeyPath:@"rate" options:0 context:nil];
    _playerLayer = [AVPlayerLayer playerLayerWithPlayer:_player];
    _playerLayer.frame = self.videoView.bounds;
    _playerLayer.needsDisplayOnBoundsChange = true;
    [self.videoView.layer addSublayer:_playerLayer];
    [_player play];
}
- (void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
}
- (void) viewWillLayoutSubviews
{
    [super viewWillLayoutSubviews];
    self.playerLayer.frame = self.videoView.bounds;
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context {
    if ([keyPath isEqualToString:@"rate"]) {
        if ([self.player rate]) {
            NSLog(@"[self changeToPause];  // This changes the button to Pause");
        } else {
            NSLog(@"[self changeToPlay];  // This changes the button to Play");
        }
    }
}
@end
