package iths.theroom.config;

public final class DataBaseConfig {

    private DataBaseConfig() {
        throw new UnsupportedOperationException();
    }

    //Tables
    public static final String TABLE_MESSAGE = "message";
    public static final String TABLE_USER = "user";
    public static final String TABLE_ROOM = "room";
    public static final String TABLE_AVATAR = "avatar";
    public static final String TABLE_MESSAGE_RATING = "message_rating";
    public static final String TABLE_USER_INCREASE = "user_increase";
    public static final String TABLE_USER_DECREASE = "user_decrease";

    //Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RATING_ID = "rating_id";
    public static final String COLUMN_ROOM_NAME = "room_name";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_ROOM_ID = "room_id";
    public static final String COLUMN_BANNED_ID = "banned_id";
    public static final String COLUMN_PROFILE_ID = "profile_id";
}
