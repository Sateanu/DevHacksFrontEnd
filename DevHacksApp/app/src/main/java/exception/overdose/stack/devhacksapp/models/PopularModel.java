package exception.overdose.stack.devhacksapp.models;

import java.util.ArrayList;

import exception.overdose.stack.devhacksapp.interfaces.mvc.SimpleObservable;
import exception.overdose.stack.devhacksapp.models.POJO.Restaurant;

/**
 * Created by Adriana on 21/11/2015.
 */
public class PopularModel extends SimpleObservable<PopularModel> {
    private ArrayList<Restaurant> restaurants;

    public ArrayList<Restaurant> getRestaurants() {
        if(restaurants == null)
        {
            restaurants = new ArrayList<>();
        }
        return restaurants;
    }

    public void setRestaurants(ArrayList<Restaurant> restaurants, boolean... notify) {
        this.restaurants = restaurants;
        if(notify.length > 0 && notify[0])
        {
            notifyObservers();
        }
    }
}
