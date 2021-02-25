//
//  ChatDetailViewController.h
//  scoutmaster
//
//  Created by Ales Gabrysz on 29/09/2019.
//  Copyright © 2019 Ales Gabrysz. All rights reserved.
//

#import "BaseViewController.h"
#import <UIKit/UIKit.h>
#import "JSQMessages.h"
#import "DemoModelData.h"
#import "AppStateManager.h"

@class ChatViewController;
@protocol ChatViewControllerDelegate <NSObject>

- (void)didDismissJSQDemoViewController:(ChatViewController *)vc;

@end

@interface ChatDetailViewController : JSQMessagesViewController <UIActionSheetDelegate, JSQMessagesComposerTextViewPasteDelegate>
@property (strong, nonatomic) PFUser *toUser;
@property (strong, nonatomic) PFObject *bookObj;
@property (strong, nonatomic) id<ChatViewControllerDelegate> delegateModal;

+ (ChatDetailViewController *)getInstance;
- (void)refreshUI;
- (void) setRoom:(PFObject *) bookObj User:(PFUser *) user ;
@end
