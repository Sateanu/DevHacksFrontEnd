package exception.overdose.stack.devhacksapp.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.text.SimpleDateFormat;

/**
 * Created by alexbuicescu on 03.10.2015.
 */
public class Constants {
    public static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat simpleDateTimeFormat=new SimpleDateFormat("HH:mm, dd-MM-yyyy");
    public static int EVENT_IMAGE=0;
    public static int PROFILE_IMAGE=1;
    public static String COORDINATES="COORDINATES";
    public static String LOCATION="LOCATION";
        public static final String RESTAURANT_ID = "RESTAURANT_ID";
        public static final String PREF_USER_ID = "PREF_USER_ID";
    public static final String PREF_USER_LOCATION_LATITUDE ="PREF_USER_LOCATION_LATITUDE";
    public static final String PREF_USER_LOCATION_LONGITUDE ="PREF_USER_LOCATION_LONGITUDE";

    public static final String PREF_MY_ID ="PREF_MY_ID";
    public static final String PICK_A_LOCATION="Pick a location";

    public static final String SUBORDERS ="SUBORDERS";
    public static final DisplayImageOptions DISPLAY_IMAGE_OPTIONS = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .build();
}
