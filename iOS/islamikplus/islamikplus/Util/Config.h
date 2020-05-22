
#define SYSTEM_KEY_AGREE                                        @"AGREE"
#define SYSTEM_KEY_AUTO                                         @"AUTO"
#define SYSTEM_KEY_USERNAME                                     @"USERNAME"
#define SYSTEM_KEY_PASSWORD                                     @"PASSWORD"

enum {
    TYPE_JUMAH = 0,
    TYPE_REGULAR = 1,
    TYPE_RAISE = 2
};
#define STRING_AMOUNT                                           [[NSArray alloc] initWithObjects:@"", @"$1", @"$4", @"$8", @"$12", @"$20", nil]
#define ARRAY_AMOUNT                                            [[NSArray alloc] initWithObjects:[NSNumber numberWithFloat:0.0f], [NSNumber numberWithFloat:1.0f], [NSNumber numberWithFloat:4.0f],[NSNumber numberWithFloat:8.0f], [NSNumber numberWithFloat:12.0f], [NSNumber numberWithFloat:20.0f], nil]
#define MAIN_COLOR                                              [UIColor colorWithRed:14/255.f green:97/255.f blue:41/255.f alpha:1.f]

#define PARSE_FIELD_OBJECT_ID                                   @"objectId"
#define PARSE_FIELD_USER                                        @"user"
#define PARSE_FIELD_CHANNELS                                    @"channels"
#define PARSE_FIELD_CREATED_AT                                  @"createdAt"
#define PARSE_FIELD_UPDATED_AT                                  @"updatedAt"

#define PARSE_TABLE_USER                                        @"User"
#define PARSE_TABLE_PAYMENT                                     @"Payment"
#define PARSE_TABLE_SERMON                                      @"Sermon"

#define PARSE_TYPE                                              @"type"
#define PARSE_FIRSTNAME                                         @"firstName"
#define PARSE_LASTSTNAME                                        @"lastName"
#define PARSE_USER_NAME                                         @"username"
#define PARSE_EMAIL                                             @"email"
#define PARSE_PASSWORD                                          @"password"
#define PARSE_PHONE_NUMBER                                      @"phoneNumber"
#define PARSE_EMAIL_ADDRESS                                     @"emailAddress"
#define PARSE_MOSQUE                                            @"mosque"
#define PARSE_LON_LAT                                           @"lonLat"
#define PARSE_ADDRESS                                           @"address"
#define PARSE_ACCOUNT_ID                                        @"accountId"

#define PARSE_OWNER                                             @"owner"
#define PARSE_TOPIC                                             @"topic"
#define PARSE_VIDEO                                             @"video"
#define PARSE_VIDEO_NAME                                        @"videoName"
#define PARSE_RAISER                                            @"raiser"

#define PARSE_TO_USER                                           @"toUser"
#define PARSE_AMOUNT                                            @"amount"
#define PARSE_SERMON                                            @"sermon"
#define PARSE_NAME                                              @"name"
