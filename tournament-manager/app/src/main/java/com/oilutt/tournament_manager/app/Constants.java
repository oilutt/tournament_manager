package com.oilutt.tournament_manager.app;

/**
 * Created by oilut on 21/08/2017.
 */

public class Constants {

    public static final int MAX_TIME_BTN = 3000;
    public static final int REQUEST_CAMERA = 234;
    public static final int PICK_PHOTO_CODE = 321;
    public static final int PICK_PHOTO = 236;
    public static final int PICK_PDF = 235;
    public static final int REQUEST_NETWORK_FILTER = 322;
    public static final int MAX_ITENS = 25;
    public static final int MAX_LEGHT_FILE = 10 * 1024;
    public static final int MAX_LEGHT_FILE_VIDEO = 40 * 1024;
    public static final int EDIT_CAMP = 1234;
    public static final int REQUEST_INVITE = 1235;

    public static final int TIME_HANDLER = 2000;

    public static final String PREFERENCES_KEY = "APP_PREFERENCES";

    public static final class Urls {
        public static final String BASE_URL = "http://apihmg.bancodoc.com.br/api/";
        public static final String TOKEN = "authtoken";
    }

    public static final class HEADER {
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String AUTHORIZATION = "Authorization";
        public static final String APPLICATION_JSON = "application/json";
        public static final String APPLICATION_URLENCODED = "application/x-www-form-urlencoded";
    }

    public class SharedPreferences {
        public static final String PATH_APP = "tournamentmaker_shared_preferences";
        public static final String TOKEN = "access_token";
    }
}
