//
//  MainViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 5/22/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "MainViewController.h"
#import "JumahSermonViewController.h"
#import "DailyPrayersViewController.h"
#import "QuranViewController.h"
#import "SettingsViewController.h"
#import "LoginViewController.h"
#import "MessagesViewController.h"
#import "PhotoViewController.h"
#import "OrderViewController.h"
#import "BookViewController.h"
#import "GaugeListViewController.h"
#import "DonationViewController.h"
#import "NotificationViewController.h"
#import <AVKit/AVKit.h>
#import "DMActivityInstagram.h"

@interface MainViewController () {
    PFObject *mPostObj;
}
@property (weak, nonatomic) IBOutlet UILabel *lblFajrTime;
@property (weak, nonatomic) IBOutlet UILabel *lblZuhrTime;
@property (weak, nonatomic) IBOutlet UILabel *lblAsrTime;
@property (weak, nonatomic) IBOutlet UILabel *lblMaghribTime;
@property (weak, nonatomic) IBOutlet UILabel *lblIshaTime;
@property (weak, nonatomic) IBOutlet UILabel *lblLocation;
@property (weak, nonatomic) IBOutlet UIView *viewVideo;
@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;

@property (nonatomic, retain) AVPlayer *player;
@property (nonatomic, retain) AVPlayerLayer *playerLayer;

@end

@implementation MainViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
}

- (void) initialize {
    _lblLocation.text = [PFUser currentUser][PARSE_ADDRESS];
    [self getTime];
    [self getVideo];
    [self getPost];
}
- (void) getTime {
    _lblFajrTime.text = @"05:10 AM";
    _lblZuhrTime.text = @"11:41 AM";
    _lblAsrTime.text = @"02:15 PM";
    _lblMaghribTime.text = @"04:36 PM";
    _lblIshaTime.text = @"06:06 PM";
}
- (void) getVideo {
    PFQuery * query = [PFQuery queryWithClassName:PARSE_TABLE_SERMON];
    [query whereKey:PARSE_IS_DELETE notEqualTo:[NSNumber numberWithBool:YES]];
    [query includeKey:PARSE_OWNER];
    [query orderByDescending:PARSE_FIELD_CREATED_AT];
    [query findObjectsInBackgroundWithBlock:^(NSArray *array, NSError *error){
        [SVProgressHUD dismiss];
        if (!error){
            PFObject *sermonObj = [self getSermonObj:array];
            if (sermonObj != nil) {
                NSURL *videoURL = [NSURL URLWithString:sermonObj[PARSE_VIDEO]];
                self.player = [AVPlayer playerWithURL:videoURL];
                [self.player addObserver:self forKeyPath:@"rate" options:0 context:nil];
                self.playerLayer = [AVPlayerLayer playerLayerWithPlayer:self.player];
                self.playerLayer.frame = self.viewVideo.bounds;
                self.playerLayer.needsDisplayOnBoundsChange = true;
                [self.viewVideo.layer addSublayer:self.playerLayer];
                [self.player play];
            }
        }
    }];
}
-(PFObject *) getSermonObj:(NSArray*)objects {
    for (int i = 0; i < objects.count; i ++) {
        int _type = [objects[i][PARSE_OWNER][PARSE_TYPE] intValue];
        if (_type == TYPE_ADMIN) {
            return objects[i];
        }
    }
    return nil;
}
- (void) getPost {
    PFQuery * query = [PFQuery queryWithClassName:PARSE_TABLE_POST];
    [query orderByDescending:PARSE_FIELD_CREATED_AT];
    
    [query getFirstObjectInBackgroundWithBlock:^(PFObject *object, NSError *error){
        if (!error){
            self->mPostObj = object;
            PFFileObject *photoFile = object[PARSE_PHOTO];
            [self.imgPhoto sd_setImageWithURL:[NSURL URLWithString:photoFile.url] placeholderImage:[UIImage imageNamed:@"default_imag_bg"]];
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
- (IBAction)onPlayPauseClick:(id)sender {
    if ((self.player.rate != 0) && (self.player.error == nil)) {
        [self.player pause];
    } else {
        [self.player play];
    }
}
- (IBAction)onSermonClick:(id)sender {
    JumahSermonViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"JumahSermonViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onDailyClick:(id)sender {
    DailyPrayersViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"DailyPrayersViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onQuranClick:(id)sender {
    QuranViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"QuranViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onMessagesClick:(id)sender {
    MessagesViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"MessagesViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onOrderClick:(id)sender {
    OrderViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"OrderViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onBookClick:(id)sender {
    BookViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"BookViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onDonationClick:(id)sender {
    DonationViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"DonationViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onSettingsClick:(id)sender {
    SettingsViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SettingsViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onGaugeClick:(id)sender {
    GaugeListViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"GaugeListViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onFullClick:(id)sender {
    PhotoViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"PhotoViewController"];
    controller.mPhotoFile = mPostObj[PARSE_PHOTO];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onShareClick:(id)sender {
    DMActivityInstagram * instagramActivity = [[DMActivityInstagram alloc] init];
    PFFileObject *photoFile = mPostObj[PARSE_PHOTO];
    NSArray *activityItems = @[mPostObj[PARSE_TITLE], [NSURL URLWithString:photoFile.url]];
    UIActivityViewController *activityController = [[UIActivityViewController alloc] initWithActivityItems:activityItems applicationActivities:@[instagramActivity]];
    
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
        activityController.popoverPresentationController.sourceView = self.view;
        activityController.popoverPresentationController.sourceRect = CGRectMake(self.view.bounds.size.width/2, self.view.bounds.size.height/4, 0, 0);
    }
    
    [self presentViewController:activityController animated:YES completion:nil];
}
- (IBAction)onNotificationClick:(id)sender {
    NotificationViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"NotificationViewController"];
    [self.navigationController pushViewController:controller animated:YES];
}
- (IBAction)onLogoutClick:(id)sender {
    NSString *msg = @"Are you sure you want to logout?";
    SCLAlertView *alert = [[SCLAlertView alloc] initWithNewWindow];
    alert.customViewColor = MAIN_COLOR;
    alert.horizontalButtons = YES;
    [alert addButton:@"Cancel" actionBlock:^(void) {
    }];
    [alert addButton:@"Okay" actionBlock:^(void) {
        [SVProgressHUD showWithStatus:@"Log out..." maskType:SVProgressHUDMaskTypeGradient];
        [PFUser logOutInBackgroundWithBlock:^(NSError *error){
            [SVProgressHUD dismiss];
            if (error){
                [Util showAlertTitle:self title:@"Log out" message:[error localizedDescription]];
            } else {
                [Util setLoginUserName:@"" password:@""];
                for(UIViewController * vc in self.navigationController.viewControllers){
                    if ([vc isKindOfClass:[LoginViewController class]]){
                        [self.navigationController popToViewController:vc animated:YES];
                        [UIApplication sharedApplication].applicationIconBadgeNumber = 0;
                        break;
                    }
                }
            }
        }];
    }];
    [alert showQuestion:@"ISLAMIK" subTitle:msg closeButtonTitle:nil duration:0.0f];
}
- (void) viewWillLayoutSubviews
{
    [super viewWillLayoutSubviews];
    self.playerLayer.frame = self.viewVideo.bounds;
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
@end
