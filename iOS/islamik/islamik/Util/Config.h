
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
    CONTINENT_AFRICA = 0,
    CONTINENT_AMERICA = 1,
    CONTINENT_ASIA = 2,
    CONTINENT_AUSTRALIA = 3,
    CONTINENT_EUROPA = 4
};
enum {
    TYPE_FAJR = 0,
    TYPE_ZUHR = 1,
    TYPE_ASR = 2,
    TYPE_MAGHRIB = 3,
    TYPE_ISHA = 4,
    TYPE_QURAN = 5
};
enum {
    TYPE_ONE = 1,
    TYPE_GROUP = 2
};
enum {
    TYPE_BOOK = 0,
    STATE_PENDING = 0,
    STATE_ACCEPT = 1,
    STATE_REJECT = 2
};
enum {
    TYPE_ORDER = 0,
    TYPE_DONATION = 1
};
#define STRING_AMOUNT                                           [[NSArray alloc] initWithObjects:@"", @"$1", @"$4", @"$8", @"$12", @"$20", nil]
#define ARRAY_AMOUNT                                            [[NSArray alloc] initWithObjects:[NSNumber numberWithFloat:0.0f], [NSNumber numberWithFloat:1.0f], [NSNumber numberWithFloat:4.0f],[NSNumber numberWithFloat:8.0f], [NSNumber numberWithFloat:12.0f], [NSNumber numberWithFloat:20.0f], nil]
#define SPEED_VALUE                                             [[NSArray alloc] initWithObjects:[NSNumber numberWithInt:1], [NSNumber numberWithInt:2], [NSNumber numberWithInt:3],[NSNumber numberWithInt:5], nil]

#define STRING_SESSION_PRICE                                    [[NSArray alloc] initWithObjects:@"$0.00", @"$4.88", @"$8.88", @"$10.88", @"$14.44", @"$18.88", @"$22.22", @"$33.44", nil]
#define ARRAY_SESSION_PRICE                                     [[NSArray alloc] initWithObjects:[NSNumber numberWithFloat:0.0f], [NSNumber numberWithFloat:4.88f], [NSNumber numberWithFloat:8.88f],[NSNumber numberWithFloat:10.88f], [NSNumber numberWithFloat:14.44f], [NSNumber numberWithFloat:18.88f], [NSNumber numberWithFloat:22.22f], [NSNumber numberWithFloat:33.44f], nil]
#define STRING_SESSION_GROUP                                    [[NSArray alloc] initWithObjects:@"$0.00", @"$2.44", @"$4.44", @"$5.44", @"$9.88", @"$11.11", @"$16.44", nil]
#define ARRAY_SESSION_GROUP                                     [[NSArray alloc] initWithObjects:[NSNumber numberWithFloat:0.0f], [NSNumber numberWithFloat:2.44f], [NSNumber numberWithFloat:4.44f],[NSNumber numberWithFloat:5.44f], [NSNumber numberWithFloat:9.88f], [NSNumber numberWithFloat:11.11f], [NSNumber numberWithFloat:16.44f], nil]

#define ANIMATION_TIME                                          0.1
#define TIME_SPEED                                              15
#define MAIN_COLOR                                              [UIColor colorWithRed:14/255.f green:97/255.f blue:41/255.f alpha:1.f]
#define ARRAY_RECITER                                           [[NSArray alloc] initWithObjects:@"ABDUL HADI KANAKERI", @"AL-HUSSAYNI AL-AZAZY (With Children)", nil]
#define RECITER_URL                                             @"https://www.quran411.com/quran/kanakeri/"
#define PRAYER_TIME_URL                                         @"https://www.islamicfinder.org/"
#define STRING_COLOR                                            [[NSArray alloc] initWithObjects:@"Dark Blue", @"White", @"Black", @"Green", @"Beige", nil]
#define ARRAY_COLOR                                             [[NSArray alloc] initWithObjects:[UIColor colorWithRed:0/255.f green:70/255.f blue:127/255.f alpha:1.f], [UIColor colorWithRed:255/255.f green:255/255.f blue:255/255.f alpha:1.f], [UIColor colorWithRed:0/255.f green:0/255.f blue:0/255.f alpha:1.f], [UIColor colorWithRed:14/255.f green:97/255.f blue:41/255.f alpha:1.f], [UIColor colorWithRed:225/255.f green:198/255.f blue:153/255.f alpha:1.f], nil]
#define ARRAY_STRING_SIZE                                       [[NSArray alloc] initWithObjects:@"10", @"11", @"12", @"13", @"14", @"15", @"16", @"17", @"18", @"19", @"20", @"21", @"22", @"23", @"24", @"25", @"26", @"27", @"28", @"29", @"30", nil]
#define ARRAY_FONT                                              [[NSArray alloc] initWithObjects:@"Angeline Vintage", @"Austina Alma", @"CalligraphyFLF", @"Drexs", @"Gamiela Demo", @"Vonique 92", nil]

#define PARSE_FIELD_OBJECT_ID                                   @"objectId"
#define PARSE_FIELD_USER                                        @"user"
#define PARSE_FIELD_CHANNELS                                    @"channels"
#define PARSE_FIELD_CREATED_AT                                  @"createdAt"
#define PARSE_FIELD_UPDATED_AT                                  @"updatedAt"

#define PARSE_TABLE_USER                                        @"User"
#define PARSE_TABLE_PAYMENT                                     @"Payment"
#define PARSE_TABLE_SERMON                                      @"Sermon"
#define PARSE_TABLE_MESSAGES                                    @"Messages"
#define PARSE_TABLE_POST                                        @"Post"
#define PARSE_TABLE_ORDER                                       @"Order"
#define PARSE_TABLE_BOOK                                        @"Book"
#define PARSE_TABLE_GAUGE                                       @"Gauge"
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
#define PARSE_CONTINENT                                         @"continent"

#define PARSE_OWNER                                             @"owner"
#define PARSE_TOPIC                                             @"topic"
#define PARSE_VIDEO                                             @"video"
#define PARSE_VIDEO_NAME                                        @"videoName"
#define PARSE_RAISER                                            @"raiser"
#define PARSE_IS_DELETE                                         @"isDelete"

#define PARSE_TO_USER                                           @"toUser"
#define PARSE_AMOUNT                                            @"amount"
#define PARSE_SERMON                                            @"sermon"
#define PARSE_NAME                                              @"name"
#define PARSE_CHARGE_ID                                         @"chargeId"
#define PARSE_LANGUAGE                                          @"language"

#define PARSE_QUESTION                                          @"question"
#define PARSE_ANSWER                                            @"answer"
#define PARSE_RATE                                              @"rate"

#define PARSE_TITLE                                             @"title"
#define PARSE_DESCRIPTION                                       @"description"
#define PARSE_PHOTO                                             @"photo"

#define PARSE_SUBJECT                                           @"subject"
#define PARSE_MESSAGE                                           @"message"

#define PARSE_BOOK_DATE                                         @"bookDate"
#define PARSE_CHILD_NAME                                        @"childName"

#define PARSE_STATE                                             @"state"
#define PARSE_BOOK_OBJ                                          @"bookObj"

#define PARSE_SENDER                                            @"sender"
#define PARSE_RECEIVER                                          @"receiver"
#define PARSE_VOICE_FILE                                        @"voiceFile"

#define PARSE_WEB_LINK                                          @"webLink"
#define PARSE_BG_COLOR                                          @"bgColor"
#define PARSE_TEXT_COLOR                                        @"textColor"
#define PARSE_TEXT_SIZE                                         @"textSize"
#define PARSE_TEXT_FONT                                         @"textFont"

#define LANGUAGE_ARRAY                                          [[NSArray alloc] initWithObjects:@"English", @"Arabic", nil]
#define LANGUAGE_ORDER_ARRAY                                    [[NSArray alloc] initWithObjects:@"English", @"Arabic", @"French", nil]
#define LANGUAGE_ORDER_SYMBOL                                   [[NSArray alloc] initWithObjects:@"en", @"ar", @"fr", nil]
#define SPEED_ARRAY                                             [[NSArray alloc] initWithObjects:@"SLOW", @"INTERMEDIATE", @"FAST", @"SUPER FAST", nil]

#define MAIN_CHAPTER                                            @"001. AL-FATIHA"
#define MAIN_VERSE                                              [[NSArray alloc] initWithObjects:@"1. Bismillaahir Rahmaanir Raheem", @"2. Alhamdu lillaahi Rabbil 'aalameen", @"3. Ar-Rahmaanir-Raheem", @"4. Maaliki Yawmid-Deen", @"5. Iyyaaka na'budu wa lyyaaka nasta'een", @"6. Ihdinas-Siraatal-Mustaqeem", @"7. Siraatal-lazeena an'amta 'alaihim ghayril-maghdoobi 'alaihim wa lad-daaalleen", nil]
#define MAIN_VERSE_ARABIC                                       [[NSArray alloc] initWithObjects:@"1. بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ", @"2. الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", @"3. الرَّحْمَنِ الرَّحِيمِ", @"4. مَالِكِ يَوْمِ الدِّينِ", @"5. إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", @"6. اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", @"7. صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", nil]
#define CHAPTER_ARRAY                                           [[NSArray alloc] initWithObjects:@"002. AL-BAQARAH", @"003. AL-IMRAN", @"004. AN-NISA", @"005. AL-MAIDAH", @"006. AL-AN'AM", @"007. AL-A'RAF", @"008. AL-ANFAL", @"009. AT-TAUBAH", @"010. YUNUS", @"011. HUD", @"012. YUSUF", @"013. AR-RA'D", @"014. IBRAHIM", @"015. AL-HIJR", @"016. AN-NAHL", @"017. AL-ISRA", @"018. AL-KAHF", @"019. MARYAM", @"020. TAHA", @"021. AL-ANBIYA", @"022. AL-HAJJ", @"023. AL-MU'MINUN", @"024. AN-NUR", @"025. AL-FURQAN", @"026. ASH-SHUARA", @"027. AN-NAML", @"028. AL-QASAS", @"029. AL-ANKABUT", @"030. AR-RUM", @"031. LUQMAN", @"032. AS-SAJDAH", @"033. AL-AHZAB", @"034. SABA", @"035. FATIR", @"036. YASEEN", @"037. AS-SAFFAT", @"038. SAD", @"039. AZ-ZUMAR", @"040. AL-MU'MIN", @"041. FUSSILAT", @"042. ASH-SHURA", @"043. AZ-ZUKHRUF", @"044. AD-DUKHAN", @"045. AL-JASIYAH", @"046. AL-AHQAF", @"047. MUHAMMAD", @"048. AL-FATH", @"049. AL-HUJURAT", @"050. QAF", @"051. AD-DHARIYAT", @"052. AT-TUR", @"053. AN-NAJM", @"054. AL-QAMAR", @"055. AR-RAHMAN", @"056. AL-WAQI'AH", @"057. AL-HADID", @"058. AL-MUJADILAH", @"059. AL-HASHR", @"060. AL-MUMTAHANAH", @"061. AS-SAFF", @"062. AL-JUMU'AH", @"063. AL-MUNAFIQUN", @"064. AT-TAGHABUN", @"065. AT-TALAQ", @"066. AT-TAHRIM", @"067. AL-MULK", @"068. AL-QALAM", @"069. AL-HAQQAH", @"070. AL-MA'ARIJ", @"071. NUH", @"072. AL-JINN", @"073. AL-MUZZAMMIL", @"074. AL-MUDDATHTHIR", @"075. AL-QIYAMAH", @"076. AL-INSAN", @"077. AL-MURSALAAT", @"078. AN-NABA", @"079. AN-NAZIAT", @"080. ABASA", @"081. AT-TAKWIR", @"082. AL-INFITAAR", @"083. AL-MUTAFFIFIN", @"084. AL-INSHIQAQ", @"085. AL-BUROOJ", @"086. AT-TARIQ", @"087. AL-A'LA", @"088. AL-GHAASHIYAH", @"089. AL-FAJR", @"090. AL-BALAD", @"091. ASH-SHAMS", @"092. AL-LAIL", @"093. AD-DUHA", @"094. AL-INSHIRAH", @"095. AT-TIN", @"096. AL-ALAQ", @"097. AL-QADR", @"098. AL-BAYYINAH", @"099. AL-ZILZAL", @"100. AL-'ADIYAT", @"101. AL-QARI'AH", @"102. AT-TAKATHUR", @"103. AL-'ASR", @"104. AL-HUMAZAH", @"105. AL-FIL", @"106. QURAISH", @"107. AL-MA'UN", @"108. AL-KAUTHAR", @"109. AL-KAFIRUN", @"110. AN-NASR", @"111. AL-LAHAB", @"112. AL-IKHLAS", @"113. AL-FALAQ", @"114. AN-NAS", nil]
