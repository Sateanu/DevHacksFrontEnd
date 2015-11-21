package exception.overdose.stack.devhacksapp.managers;

import android.content.Context;

import java.util.ArrayList;

import exception.overdose.stack.devhacksapp.database.RestaurantDataSource;
import exception.overdose.stack.devhacksapp.models.POJO.Restaurant;
import exception.overdose.stack.devhacksapp.utils.Constants;
import exception.overdose.stack.devhacksapp.utils.PrefUtils;

/**
 * Created by alexbuicescu on 21.11.2015.
 */
public class RestaurantsManager {

    private static RestaurantsManager instance;
    private Context context;

    private ArrayList<Restaurant> restaurants;

    private RestaurantsManager(Context context)
    {
//        restaurants = new ArrayList<>();

        RestaurantDataSource restaurantDataSource = new RestaurantDataSource(context);
        restaurantDataSource.open();

        restaurants = restaurantDataSource.getAllRestaurants();

        restaurantDataSource.closeHelper();
    }

    public static RestaurantsManager getRestaurantsManager(Context context)
    {
        if(instance == null)
        {
            instance = new RestaurantsManager(context);
        }
        instance.context = context;
        return instance;
    }

    public static RestaurantsManager getRestaurantsManager()
    {
        return instance;
    }

    public ArrayList<Restaurant> getRestaurants()
    {
        return restaurants;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants)
    {
        this.restaurants = restaurants;
    }

    public void setMyId(long id)
    {
        PrefUtils.setLongToPrefs(context, Constants.PREF_MY_ID, id);
    }

    public long getMyId()
    {
        return PrefUtils.getLongFromPrefs(context, Constants.PREF_MY_ID, 0);
    }
}
