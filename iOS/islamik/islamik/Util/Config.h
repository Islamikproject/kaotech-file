
#define SYSTEM_KEY_AGREE                                        @"AGREE"

enum {
    TYPE_JUMAH = 0,
    TYPE_REGULAR = 1,
    TYPE_RAISE = 2
};
enum {
    TYPE_FAJR = 0,
    TYPE_ZUHR = 1,
    TYPE_ASR = 2,
    TYPE_MAGHRIB = 3,
    TYPE_ISHA = 4
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
#define PARSE_CHARGE_ID                                         @"chargeId"

#define LANGUAGE_ARRAY                                          [[NSArray alloc] initWithObjects:@"English", @"Arabic", nil]
#define SPEED_ARRAY                                             [[NSArray alloc] initWithObjects:@"SLOW", @"INTERMEDIATE", @"FAST", @"SUPER FAST", nil]

#define MAIN_CHAPTER                                            @"001. AL-FATIHA"
#define MAIN_VERSE                                              [[NSArray alloc] initWithObjects:@"1. Bismillaahir Rahmaanir Raheem", @"2. Alhamdu lillaahi Rabbil 'aalameen", @"3. Ar-Rahmaanir-Raheem", @"4. Maaliki Yawmid-Deen", @"5. Iyyaaka na'budu wa lyyaaka nasta'een", @"6. Ihdinas-Siraatal-Mustaqeem", @"7. Siraatal-lazeena an'amta 'alaihim ghayril-maghdoobi 'alaihim wa lad-daaalleen", nil]
#define CHAPTER_ARRAY                                           [[NSArray alloc] initWithObjects:@"002. AL-BAQARAH", @"003. AL-IMRAN", @"004. AN-NISA", @"005. AL-MAIDAH", @"006. AL-AN'AM", @"007. AL-A'RAF", @"008. AL-ANFAL", @"009. AT-TAUBAH", @"010. YUNUS", @"011. HUD", @"012. YUSUF", @"013. AR-RA'D", @"014. IBRAHIM", @"015. AL-HIJR", @"016. AN-NAHL", @"017. AL-ISRA", @"018. AL-KAHF", @"019. MARYAM", @"020. TAHA", @"021. AL-ANBIYA", @"022. AL-HAJJ", @"023. AL-MU'MINUN", @"024. AN-NUR", @"025. AL-FURQAN", @"026. ASH-SHUARA", @"027. AN-NAML", @"028. AL-QASAS", @"029. AL-ANKABUT", @"030. AR-RUM", @"031. LUQMAN", @"032. AS-SAJDAH", @"033. AL-AHZAB", @"034. SABA", @"035. FATIR", @"036. YASEEN", @"037. AS-SAFFAT", @"038. SAD", @"039. AZ-ZUMAR", @"040. AL-MU'MIN", @"041. FUSSILAT", @"042. ASH-SHURA", @"043. AZ-ZUKHRUF", @"044. AD-DUKHAN", @"045. AL-JASIYAH", @"046. AL-AHQAF", @"047. MUHAMMAD", @"048. AL-FATH", @"049. AL-HUJURAT", @"050. QAF", @"051. AD-DHARIYAT", @"052. AT-TUR", @"053. AN-NAJM", @"054. AL-QAMAR", @"055. AR-RAHMAN", @"056. AL-WAQI'AH", @"057. AL-HADID", @"058. AL-MUJADILAH", @"059. AL-HASHR", @"060. AL-MUMTAHANAH", @"061. AS-SAFF", @"062. AL-JUMU'AH", @"063. AL-MUNAFIQUN", @"064. AT-TAGHABUN", @"065. AT-TALAQ", @"066. AT-TAHRIM", @"067. AL-MULK", @"068. AL-QALAM", @"069. AL-HAQQAH", @"070. AL-MA'ARIJ", @"071. NUH", @"072. AL-JINN", @"073. AL-MUZZAMMIL", @"074. AL-MUDDATHTHIR", @"075. AL-QIYAMAH", @"076. AL-INSAN", @"077. AL-MURSALAAT", @"078. AN-NABA", @"079. AN-NAZIAT", @"080. ABASA", @"081. AT-TAKWIR", @"082. AL-INFITAAR", @"083. AL-MUTAFFIFIN", @"084. AL-INSHIQAQ", @"085. AL-BUROOJ", @"086. AT-TARIQ", @"087. AL-A'LA", @"088. AL-GHAASHIYAH", @"089. AL-FAJR", @"090. AL-BALAD", @"091. ASH-SHAMS", @"092. AL-LAIL", @"093. AD-DUHA", @"094. AL-INSHIRAH", @"095. AT-TIN", @"096. AL-ALAQ", @"097. AL-QADR", @"098. AL-BAYYINAH", @"099. AL-ZILZAL", @"100. AL-'ADIYAT", @"101. AL-QARI'AH", @"102. AT-TAKATHUR", @"103. AL-'ASR", @"104. AL-HUMAZAH", @"105. AL-FIL", @"106. QURAISH", @"107. AL-MA'UN", @"108. AL-KAUTHAR", @"109. AL-KAFIRUN", @"110. AN-NASR", @"111. AL-LAHAB", @"112. AL-IKHLAS", @"113. AL-FALAQ", @"114. AN-NAS", nil]
