//
//  BookViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 11/13/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "BookViewController.h"

@interface BookViewController () <IQDropDownTextFieldDelegate>
@property (weak, nonatomic) IBOutlet IQDropDownTextField *edtPrice;
@property (weak, nonatomic) IBOutlet IQDropDownTextField *edtGroup;

@end

@implementation BookViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];
}

- (void) initialize {
    int price = [[PFUser currentUser][PARSE_PRICE] intValue];
    int group = [[PFUser currentUser][PARSE_GROUP_PRICE] intValue];
    self.edtPrice.itemList = ONE_PRICE;
    self.edtPrice.isOptionalDropDown = YES;
    self.edtPrice.delegate = self;
    self.edtGroup.itemList = GROUP_PRICE;
    self.edtGroup.isOptionalDropDown = YES;
    self.edtGroup.delegate = self;
    self.edtPrice.selectedRow = price;
    self.edtGroup.selectedRow = group;
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onSaveClick:(id)sender {
    if ([self isValid]) {
        [self save];
    }
}

- (BOOL) isValid {
    if (self.edtPrice.selectedItem.length == 0) {
        [Util showAlertTitle:self title:@"Error" message:@"Please select price of 1/1 session."];
        return NO;
    }
    if (self.edtGroup.selectedItem.length == 0) {
        [Util showAlertTitle:self title:@"Error" message:@"Please select price of group session."];
        return NO;
    }
    return YES;
}

- (void) save {
    int price = (int)[self.edtPrice selectedRow];
    int group = (int)[self.edtGroup selectedRow];
    PFUser *me = [PFUser currentUser];
    me[PARSE_PRICE] = [NSNumber numberWithInt:price];
    me[PARSE_GROUP_PRICE] = [NSNumber numberWithInt:group];
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    [me saveInBackgroundWithBlock:^(BOOL succeeded, NSError * _Nullable error) {
        [SVProgressHUD dismiss];
        if (error) {
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        }else{
            [Util showAlertTitle:self title:@"Success" message:@"Successfully changed." finish:^{
                [self.navigationController popViewControllerAnimated:YES];
            }];
        }
    }];
}

#pragma mark    IQDropDownTextField
- (void)textField:(IQDropDownTextField *)textField didSelectItem:(NSString *)item {}
@end
