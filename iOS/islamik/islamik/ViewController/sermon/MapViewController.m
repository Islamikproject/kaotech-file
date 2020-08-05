//
//  MapViewController.m
//  islamik
//
//  Created by Ales Gabrysz on 8/5/20.
//  Copyright © 2020 Ales Gabrysz. All rights reserved.
//

#import "MapViewController.h"
#import <MapKit/MapKit.h>
#import "SermonListViewController.h"

@interface MapViewController () <MKMapViewDelegate> {
    NSMutableArray *mDataList;
}
@property (weak, nonatomic) IBOutlet UIView *mapView;
@property (weak, nonatomic) IBOutlet MKMapView *m_mapView;
@end

@implementation MapViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self getServerData];
}

- (void) getServerData {
    if (![Util isConnectableInternet]){
        [Util showAlertTitle:self title:@"Network Error" message:@"Please check your network state."];
        return;
    }
    mDataList = [NSMutableArray new];

    PFQuery *query = [PFUser query];
    [query whereKey:PARSE_TYPE equalTo:[NSNumber numberWithInt:TYPE_MOSQUE]];
    [query orderByAscending:PARSE_FIRSTNAME];
    [query setLimit:1000];
    
    [SVProgressHUD showWithMaskType:SVProgressHUDMaskTypeClear];
    [query findObjectsInBackgroundWithBlock:^(NSArray *array, NSError *error){
        [SVProgressHUD dismiss];
        if (error){
            [Util showAlertTitle:self title:@"Error" message:error.localizedDescription];
        } else {
            self->mDataList = (NSMutableArray *) array;
            [self reloadMapData];
        }
    }];
}

- (void) reloadMapData {
    NSMutableArray * annotations = [NSMutableArray new];
    for(PFObject * obj in mDataList){
        PFGeoPoint * geoLocation = obj[PARSE_LON_LAT];
        MKPointAnnotation *annotation = [[MKPointAnnotation alloc] init];
        [annotation setCoordinate:CLLocationCoordinate2DMake(geoLocation.latitude, geoLocation.longitude)];
        [annotation setTitle:[NSString stringWithFormat:@"%lu", (unsigned long)[mDataList indexOfObject:obj]]];
        [annotations addObject:annotation];
    }
    self.m_mapView.delegate = self;
    self.m_mapView.pitchEnabled = YES;
    self.m_mapView.showsBuildings = YES;
    self.m_mapView.showsCompass = YES;
    self.m_mapView.showsScale = YES;
    self.m_mapView.showsTraffic = YES;
    [self.m_mapView removeAnnotations:self.m_mapView.annotations];
    [self.m_mapView addAnnotations:annotations];
}

#pragma mark - mkmapkit delegate
-(MKAnnotationView *) mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation {
    MKPinAnnotationView *MyPin=[[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:@"current"];
    MyPin.pinColor = MKPinAnnotationColorRed;
    MyPin.draggable = YES;
    MyPin.animatesDrop=TRUE;
    MyPin.canShowCallout = NO;
    MyPin.highlighted = NO;
    return MyPin;
}
- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view
{
    MKPointAnnotation *annotation = [view annotation];
    int indexOfPin = [annotation.title intValue];
    PFUser *user = [mDataList objectAtIndex:indexOfPin];
    
   SermonListViewController * controller = [[UIStoryboard storyboardWithName:@"Main" bundle:nil] instantiateViewControllerWithIdentifier:@"SermonListViewController"];
   controller.mUserObj = user;
   [self.navigationController pushViewController:controller animated:YES];
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
