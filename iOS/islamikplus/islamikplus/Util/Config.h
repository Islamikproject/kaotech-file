
#define SYSTEM_KEY_AGREE                                        @"AGREE"
#define SYSTEM_KEY_AUTO                                         @"AUTO"
#define SYSTEM_KEY_USERNAME                                     @"USERNAME"
#define SYSTEM_KEY_PASSWORD                                     @"PASSWORD"

#define NOTIFICATION_ACTIVE                                     @"NOTIFICATION_ACTIVE"
#define NOTIFICATION_BACKGROUND                                 @"NOTIFICATION_BACKGROUND"
#define PUSH_NOTIFICATION_TYPE                                  @"type"
#define PUSH_NOTIFICATION_DATA                                  @"data"

#define kChatReceiveNotificationUsers                           @"ChatReceiveNotificationUsers"
#define kChatReceiveNotification                                @"ChatReceiveNotification"
enum {
    PUSH_TYPE_BOOK = 0,
    PUSH_TYPE_CHAT = 1
};
enum {
    TYPE_JUMAH = 0,
    TYPE_REGULAR = 1,
    TYPE_RAISE = 2
};

enum {
    TYPE_MOSQUE = 100,
    TYPE_USER = 200,
    TYPE_USTHADH = 300,
    TYPE_INFLUENCER_WOMEN = 400,
    TYPE_INFLUENCER_KID = 401,
    TYPE_INFLUENCER_OTHER = 402,
    TYPE_ADMIN = 500
};

enum {
    TYPE_BOOK = 0,
    STATE_PENDING = 0,
    STATE_ACCEPT = 1,
    STATE_REJECT = 2
};
#define STRING_AMOUNT                                           [[NSArray alloc] initWithObjects:@"", @"$1", @"$4", @"$8", @"$12", @"$20", nil]
#define ONE_PRICE                                               [[NSArray alloc] initWithObjects:@"$0.00", @"$4.88", @"$8.88", @"$10.88", @"$14.44", @"$18.88", @"$22.22", @"$33.44", nil]
#define GROUP_PRICE                                             [[NSArray alloc] initWithObjects:@"$0.00", @"$2.44", @"$4.44", @"$5.44", @"$9.88", @"$11.11", @"$16.44", nil]
#define ARRAY_AMOUNT                                            [[NSArray alloc] initWithObjects:[NSNumber numberWithFloat:0.0f], [NSNumber numberWithFloat:1.0f], [NSNumber numberWithFloat:4.0f],[NSNumber numberWithFloat:8.0f], [NSNumber numberWithFloat:12.0f], [NSNumber numberWithFloat:20.0f], nil]
#define MAIN_COLOR                                              [UIColor colorWithRed:14/255.f green:97/255.f blue:41/255.f alpha:1.f]

#define PARSE_FIELD_OBJECT_ID                                   @"objectId"
#define PARSE_FIELD_USER                                        @"user"
#define PARSE_FIELD_CHANNELS                                    @"channels"
#define PARSE_FIELD_CREATED_AT                                  @"createdAt"
#define PARSE_FIELD_UPDATED_AT                                  @"updatedAt"

#define PARSE_TABLE_PAYMENT                                     @"Payment"
#define PARSE_TABLE_SERMON                                      @"Sermon"
#define PARSE_TABLE_MESSAGES                                    @"Messages"
#define PARSE_TABLE_POST                                        @"Post"
#define PARSE_TABLE_BOOK                                        @"Book"
#define PARSE_TABLE_NOTIFICATION                                @"Notification"
#define PARSE_TABLE_CHAT                                        @"Chat"

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
#define PARSE_PRICE                                             @"price"
#define PARSE_GROUP_PRICE                                       @"groupPrice"
#define PARSE_AVATAR                                            @"avatar"

#define PARSE_OWNER                                             @"owner"
#define PARSE_TOPIC                                             @"topic"
#define PARSE_VIDEO                                             @"video"
#define PARSE_VIDEO_NAME                                        @"videoName"
#define PARSE_RAISER                                            @"raiser"
#define PARSE_IS_DELETE                                         @"isDelete"
#define PARSE_LANGUAGE                                          @"language"
#define PARSE_IS_AUDIO                                          @"isAudio"

#define PARSE_TO_USER                                           @"toUser"
#define PARSE_AMOUNT                                            @"amount"
#define PARSE_SERMON                                            @"sermon"
#define PARSE_NAME                                              @"name"

#define PARSE_QUESTION                                          @"question"
#define PARSE_ANSWER                                            @"answer"
#define PARSE_RATE                                              @"rate"

#define PARSE_TITLE                                             @"title"
#define PARSE_DESCRIPTION                                       @"description"
#define PARSE_PHOTO                                             @"photo"

#define PARSE_BOOK_DATE                                         @"bookDate"
#define PARSE_CHILD_NAME                                        @"childName"

#define PARSE_STATE                                             @"state"
#define PARSE_BOOK_OBJ                                          @"bookObj"
#define PARSE_MESSAGE                                           @"message"

#define PARSE_SENDER                                            @"sender"
#define PARSE_RECEIVER                                          @"receiver"
#define PARSE_VOICE_FILE                                        @"voiceFile"
