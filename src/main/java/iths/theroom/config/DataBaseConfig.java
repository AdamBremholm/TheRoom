package iths.theroom.config;

public final class DataBaseConfig {

    private DataBaseConfig() {
        throw new UnsupportedOperationException();
    }

    public static final String TABLE_MESSAGE = "message";
    public static final String TABLE_USER = "user";
    public static final String TABLE_ROOM = "room";
    public static final String TABLE_ROLE = "role";
    public static final String TABLE_AVATAR = "avatar";
    public static final String TABLE_MESSAGE_RATING = "message_rating";
    public static final String COLUMN_ROOM_NAME = "room_name";
    public static final String COLUMN_ROOM_MESSAGES = "room_messages";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_ROOM_ID = "room_id";
    public static final String COLUMN_ROLE_ID = "role_id";

    public static final String COLUMN_MESSAGES_ID = "messages_id";
    public static final String GLOBAL_ADMIN = "global_admin";
    public static final String MODERATOR = "moderator";
    public static final String USER = "user";
    public static final String GUEST = "guest";

    //Join tables
    public static final String JOIN_TABLE_ROOM_MESSAGE = "room_message";
    public static final String JOIN_TABLE_USER_ROLE = "user_role";

}
