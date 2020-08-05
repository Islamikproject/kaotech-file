//
//  AnswerViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 8/4/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "AnswerViewController.h"
#import <HCSStarRatingView/HCSStarRatingView.h>

@interface AnswerViewController ()
@property (weak, nonatomic) IBOutlet HCSStarRatingView *viewRating;
@property (weak, nonatomic) IBOutlet UILabel *lblTopic;
@property (weak, nonatomic) IBOutlet UILabel *lblQuestion;
@property (weak, nonatomic) IBOutlet UILabel *lblAnswer;

@end

@implementation AnswerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
}

- (void) initialize {
    PFObject *sermonObj = self.mMessageObj[PARSE_SERMON];
    int rating = [self.mMessageObj[PARSE_RATE] intValue];
    self.viewRating.value = rating;
    self.lblTopic.text = sermonObj[PARSE_TOPIC];
    self.lblQuestion.text = self.mMessageObj[PARSE_QUESTION];
    self.lblAnswer.text = self.mMessageObj[PARSE_ANSWER];
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
