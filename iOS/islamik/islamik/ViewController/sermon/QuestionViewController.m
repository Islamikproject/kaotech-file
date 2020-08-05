//
//  QuestionViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 8/5/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "QuestionViewController.h"
#import <HCSStarRatingView/HCSStarRatingView.h>

@interface QuestionViewController ()
@property (weak, nonatomic) IBOutlet HCSStarRatingView *viewRating;
@property (weak, nonatomic) IBOutlet UILabel *lblTopic;
@property (weak, nonatomic) IBOutlet UITextView *edtQuestion;

@end

@implementation QuestionViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.edtQuestion.layer.borderColor = [UIColor lightGrayColor].CGColor;
    self.edtQuestion.layer.borderWidth = 1.f;
    self.edtQuestion.layer.cornerRadius = 4.f;
    [self initialize];
}

- (void) initialize {
    self.viewRating.value = 0;
    self.lblTopic.text = self.mSermonObj[PARSE_TOPIC];
    self.edtQuestion.text = @"";
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onSubmitClick:(id)sender {
    NSString *question = [Util trim:self.edtQuestion.text];
    if (question.length == 0) {
        [Util showAlertTitle:self title:@"Error" message:@"Please enter question."];
    } else {
        [self submit];
    }
}

- (void) submit {
    NSString *question = [Util trim:self.edtQuestion.text];
    PFObject *object = [PFObject objectWithClassName:PARSE_TABLE_MESSAGES];
    object[PARSE_OWNER] = [PFUser currentUser];
    object[PARSE_SERMON] = self.mSermonObj;
    object[PARSE_MOSQUE] = self.mSermonObj[PARSE_OWNER];
    object[PARSE_QUESTION] = question;
    object[PARSE_ANSWER] = @"";
    int rate = self.viewRating.value;
    object[PARSE_RATE] = [NSNumber numberWithInt:rate];
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    [object saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        [SVProgressHUD dismiss];
        if (error) {
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        }else{
            [self.navigationController popViewControllerAnimated:YES];
        }
    }];
}
@end
