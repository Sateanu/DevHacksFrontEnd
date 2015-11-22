package exception.overdose.stack.devhacksapp.models;

import java.util.ArrayList;
import java.util.HashMap;

import exception.overdose.stack.devhacksapp.database.FoodDataSource;
import exception.overdose.stack.devhacksapp.database.OrderDataSource;
import exception.overdose.stack.devhacksapp.interfaces.mvc.SimpleObservable;
import exception.overdose.stack.devhacksapp.models.POJO.Food;
import exception.overdose.stack.devhacksapp.models.POJO.Orders;

/**
 * Created by Adriana on 22/11/2015.
 */
public class MenuModel extends SimpleObservable<MenuModel> {
    private ArrayList<Food> foods;
    private long restaurantId;
    private HashMap<Long,Integer> productQuantities;
    private FoodDataSource foodDataSource;

    public ArrayList<Food> getFoods() {
        if(foods==null)
            foods=new ArrayList<>();
        return foods;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setFoods(ArrayList<Food> foods, boolean... notifyObservers) {
        this.foods = foods;
        update(notifyObservers);
    }
    public void setProductQuantities(HashMap<Long,Integer> quantities, boolean... notifyObservers) {
        this.productQuantities = quantities;
        update(notifyObservers);
    }
    public HashMap<Long,Integer> getProductQuantities(){
        return productQuantities;
    }

    public void setRestaurantId(long restaurantId, boolean... notifyObservers) {
        this.restaurantId = restaurantId;
        update(notifyObservers);
    }

    public void update(boolean... notifyObservers) {
        if (notifyObservers.length > 0 && notifyObservers[0]) {
            notifyObservers();
        }
    }

    public FoodDataSource getFoodDataSource() {
        return foodDataSource;
    }

    public void setFoodDataSource(FoodDataSource eventDataSource) {
        this.foodDataSource = eventDataSource;
    }
}