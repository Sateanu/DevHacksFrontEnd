package exception.overdose.stack.devhacksapp.models.POJO;

/**
 * Created by alexbuicescu on 21.11.2015.
 */
public class Food {

    private long Id;
    private String Name;
    private float Price;
    private long RestaurantID;
    private String Description;
    private String Category;

    public Food(long id, String name, float price, long restaurantID, String category, String description) {
        Id = id;
        Name = name;
        Price = price;
        RestaurantID = restaurantID;
        Category = category;
        Description = description;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public long getRestaurantID() {
        return RestaurantID;
    }

    public void setRestaurantID(long restaurantID) {
        RestaurantID = restaurantID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
