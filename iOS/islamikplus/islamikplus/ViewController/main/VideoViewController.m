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

- (IBAction)onDeleteClick:(id)sender {
    NSString *msg = @"Are you sure you want to delete this sermon?";
    SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
    alert.customViewColor = MAIN_COLOR;
    alert.horizontalButtons = YES;
    [alert addButton:@"No" actionBlock:^(void) {
    }];
    [alert addButton:@"Yes" actionBlock:^(void) {
        [SVProgressHUD showWithStatus:@"Please wait..." maskType:SVProgressHUDMaskTypeGradient];
        self.mSermonObj[PARSE_IS_DELETE] = [NSNumber numberWithBool:YES];
        [self.mSermonObj saveInBackgroundWithBlock:^(BOOL success, NSError* error){
            [SVProgressHUD dismiss];
            if (error) {
                [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
            } else {
                [self onBack:nil];
            }
        }];
    }];
    [alert showQuestion:@"Delete" subTitle:msg closeButtonTitle:nil duration:0.0f];
}

@end
