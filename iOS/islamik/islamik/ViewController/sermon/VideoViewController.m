//
//  VideoViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "VideoViewController.h"
#import <AVKit/AVKit.h>
#import <SafariServices/SafariServices.h>
#import "QuestionViewController.h"
#import "DMActivityInstagram.h"

@interface VideoViewController () <SFSafariViewControllerDelegate>
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
            [self showConfirmDialog];
        }
    }
}
- (void) showConfirmDialog {
    NSNumber *amount = self.mSermonObj[PARSE_AMOUNT];
    NSString *message = [NSString stringWithFormat:@"The mosque has a virtual basket of « %@ » would you like to make a donation", [NSString stringWithFormat:@"$%.2f", amount.floatValue]];
    SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
    alert.customViewColor = MAIN_COLOR;
    alert.horizontalButtons = YES;
    [alert addButton:@"NO" actionBlock:^(void) {}];
    [alert addButton:@"YES" actionBlock:^(void) {
        NSString * url = [NSString stringWithFormat:@"https://stripe.kaotech.org/donation?sermon=%@", self.mSermonObj.objectId];
        NSURL *nsUrl = [NSURL URLWithString:url];
        SFSafariViewController *svc = [[SFSafariViewController alloc] initWithURL:nsUrl];
        svc.delegate = self;
        [self presentViewController:svc animated:YES completion:nil];
    }];
    [alert showQuestion:@"ISLAMIK" subTitle:message closeButtonTitle:nil duration:0.0f];
}

- (void)safariViewControllerDidFinish:(SFSafariViewController *)controller {
    [self dismissViewControllerAnimated:true completion:nil];
}
- (IBAction)onShareClick:(id)sender {
    [self shareVideo:self.mSermonObj[PARSE_VIDEO] title:self.mSermonObj[PARSE_TOPIC]];
}
- (IBAction)onRateClick:(id)sender {
    QuestionViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"QuestionViewController"];
    controller.mSermonObj = self.mSermonObj;
    [self.navigationController pushViewController:controller animated:YES];
}

- (void) shareVideo:(NSString*)url title:(NSString*)title
{
    DMActivityInstagram * instagramActivity = [[DMActivityInstagram alloc] init];
    NSArray *activityItems = @[title, [NSURL URLWithString:url]];
    UIActivityViewController *activityController = [[UIActivityViewController alloc] initWithActivityItems:activityItems applicationActivities:@[instagramActivity]];
    
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
        activityController.popoverPresentationController.sourceView = self.view;
        activityController.popoverPresentationController.sourceRect = CGRectMake(self.view.bounds.size.width/2, self.view.bounds.size.height/4, 0, 0);
    }
    
    [self presentViewController:activityController animated:YES completion:nil];
}
@end
