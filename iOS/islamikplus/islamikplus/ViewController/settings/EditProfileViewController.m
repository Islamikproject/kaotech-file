//
//  EditProfileViewController.m
//  islamikplus
//
//  Created by Ales Gabrysz on 5/21/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "EditProfileViewController.h"
@import GooglePlaces;

@interface EditProfileViewController()<GMSAutocompleteViewControllerDelegate>{
    PFUser *me;
    CLLocationCoordinate2D mLatLng;
}
@property (weak, nonatomic) IBOutlet UITextField *edtFirstname;
@property (weak, nonatomic) IBOutlet UITextField *edtSurname;
@property (weak, nonatomic) IBOutlet UITextField *edtMosque;
@property (weak, nonatomic) IBOutlet UITextField *edtAddress;
@property (weak, nonatomic) IBOutlet UITextField *edtPhonenumber;
@property (weak, nonatomic) IBOutlet UITextField *edtEmail;
@property (weak, nonatomic) IBOutlet UITextField *edtPassword;
@property (weak, nonatomic) IBOutlet UITextField *edtConfirmPassword;
@end

@implementation EditProfileViewController{
    GMSAutocompleteFilter *_filter;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self initialize];

}
- (void) initialize {
    me = [PFUser currentUser];
    PFGeoPoint *point = [me objectForKey:PARSE_LON_LAT];
    mLatLng = CLLocationCoordinate2DMake(point.latitude, point.longitude);
    _edtFirstname.text = me[PARSE_FIRSTNAME];
    _edtSurname.text = me[PARSE_LASTSTNAME];
    _edtMosque.text = me[PARSE_MOSQUE];
    _edtAddress.text = me[PARSE_ADDRESS];
    _edtPhonenumber.text = me[PARSE_PHONE_NUMBER];
    _edtEmail.text = me[PARSE_EMAIL_ADDRESS];
    _edtPassword.text = [Util getLoginUserPassword];
    _edtConfirmPassword.text = [Util getLoginUserPassword];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/
- (IBAction)onLocationClick:(id)sender {
    GMSAutocompleteViewController *acController = [[GMSAutocompleteViewController alloc] init];
    acController.delegate = self;

    // Specify the place data types to return.
    GMSPlaceField fields = (GMSPlaceFieldFormattedAddress | GMSPlaceFieldCoordinate);
    acController.placeFields = fields;

    // Specify a filter.
    _filter = [[GMSAutocompleteFilter alloc] init];
    _filter.type = kGMSPlacesAutocompleteTypeFilterAddress;
    acController.autocompleteFilter = _filter;

    // Display the autocomplete view controller.
    [self presentViewController:acController animated:YES completion:nil];
}
- (IBAction)onSaveChangeClick:(id)sender {
    if ([self isValid]) {
        [self save];
    }
}

- (BOOL) isValid {
    NSString *firstName = [Util trim:_edtFirstname.text];
    NSString *surname = [Util trim:_edtSurname.text];
    NSString *mosque = [Util trim:_edtMosque.text];
    NSString *address = [Util trim:_edtAddress.text];
    NSString *phoneNumber = [Util trim:_edtPhonenumber.text];
    NSString *email = [Util trim:_edtEmail.text];
    NSString *password = _edtPassword.text;
    NSString *confirmPassword = _edtConfirmPassword.text;
    NSString * errorMsg = @"";
    if (firstName.length == 0) {
        errorMsg = @"Please enter first name of the main imam.";
    } else if (surname.length == 0) {
        errorMsg = @"Please enter surname of the main imam.";
    } else if (mosque.length == 0) {
        errorMsg = @"Please enter name of the mosque.";
    } else if (address.length == 0) {
        errorMsg = @"Please enter address of the mosque.";
    } else if (phoneNumber.length == 0) {
        errorMsg = @"Please enter phone number.";
    } else if (email.length == 0) {
        errorMsg = @"Please enter email address.";
    } else if (![email isEmail]) {
        errorMsg = @"Email address is invalid.";
    } else if (password.length == 0) {
        errorMsg = @"Please enter password.";
    } else if (confirmPassword.length == 0) {
        errorMsg = @"Please enter confirm password.";
    } else if (![password isEqualToString:confirmPassword]) {
        errorMsg = @"Password doesn't match.";
    }
    if (errorMsg.length > 0) {
        [Util showAlertTitle:self title:@"Edit Profile" message:errorMsg];
        return NO;
    } else if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return NO;
    }
    return YES;
}

-(void) save {
    NSString *phoneNumber = [Util trim:_edtPhonenumber.text];
    me[PARSE_EMAIL] = [Util trim:_edtEmail.text];
    me[PARSE_USER_NAME] = [Util trim:_edtPhonenumber.text];
    me[PARSE_PASSWORD] = _edtPassword.text;
    me[PARSE_EMAIL_ADDRESS] = [Util trim:_edtEmail.text];
    me[PARSE_PHONE_NUMBER] = [Util trim:_edtPhonenumber.text];
    me[PARSE_FIRSTNAME] = [Util trim:_edtFirstname.text];
    me[PARSE_LASTSTNAME] = [Util trim:_edtSurname.text];
    me[PARSE_MOSQUE] = [Util trim:_edtMosque.text];
    me[PARSE_ADDRESS] = [Util trim:_edtAddress.text];
    [me setObject:[PFGeoPoint geoPointWithLocation:[[CLLocation alloc] initWithLatitude:mLatLng.latitude longitude:mLatLng.longitude]] forKey:PARSE_LON_LAT];
    
    [SVProgressHUD showWithStatus:@"Please wait..." maskType:SVProgressHUDMaskTypeGradient];
    [me saveInBackgroundWithBlock:^(BOOL success, NSError* error){
        if (error) {
            [SVProgressHUD dismiss];
            [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
        } else {
            NSString *password = [Util getLoginUserPassword];
            [PFUser logInWithUsernameInBackground:phoneNumber password:password block:^(PFObject *object, NSError *error){
                [SVProgressHUD dismiss];
                if(!error){
                    [Util setLoginUserName:phoneNumber password:password];
                    [Util showAlertTitle:self title:@"Success" message:@"Your profile successfully changed." finish:^{
                        [self.navigationController popViewControllerAnimated:YES];
                    }];
                }else{
                    [Util showAlertTitle:self title:@"Error" message:[error localizedDescription]];
                }
            }];
        }
    }];
}


// Handle the user's selection.
- (void)viewController:(GMSAutocompleteViewController *)viewController didAutocompleteWithPlace:(GMSPlace *)place {
    [self dismissViewControllerAnimated:YES completion:nil];
    NSLog(@"Place name %@", place.name);
    NSLog(@"Place address %@", place.formattedAddress);
    NSLog(@"Place attributions %@", place.attributions.string);

    _edtAddress.text = place.formattedAddress;
    mLatLng = place.coordinate;
}

- (void)viewController:(GMSAutocompleteViewController *)viewController didFailAutocompleteWithError:(NSError *)error {
  [self dismissViewControllerAnimated:YES completion:nil];
  // TODO: handle the error.
  NSLog(@"Error: %@", [error description]);
}

  // User canceled the operation.
- (void)wasCancelled:(GMSAutocompleteViewController *)viewController {
  [self dismissViewControllerAnimated:YES completion:nil];
}

  // Turn the network activity indicator on and off again.
- (void)didRequestAutocompletePredictions:(GMSAutocompleteViewController *)viewController {
  [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
}

- (void)didUpdateAutocompletePredictions:(GMSAutocompleteViewController *)viewController {
  [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
}
@end
