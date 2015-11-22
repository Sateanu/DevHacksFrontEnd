package exception.overdose.stack.devhacksapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.database.FoodDataSource;
import exception.overdose.stack.devhacksapp.managers.RestaurantsManager;
import exception.overdose.stack.devhacksapp.models.MenuModel;
import exception.overdose.stack.devhacksapp.models.POJO.SubOrder;
import exception.overdose.stack.devhacksapp.utils.Constants;
import exception.overdose.stack.devhacksapp.views.MenuLayout;

/**
 * Created by Adriana on 22/11/2015.
 */
public class MenuActivity extends AppCompatActivity {

    private final String TAG = "MenuActivity";

    private Activity activity;

    private MenuLayout layout;
    private MenuModel model;
    private MenuLayout.ViewListener viewListener = new MenuLayout.ViewListener() {
        @Override
        public void onMinusImageViewClicked(int position) {
            HashMap newHashMap = model.getProductQuantities();
            newHashMap.put(model.getFoods().get(position).getId(), Long.valueOf(
                    (Long) newHashMap.get(model.getFoods().get(position).getId()) - 1));
            model.setProductQuantities(newHashMap, true);
        }

        @Override
        public void onPlusImageViewClicked(int position) {
            HashMap newHashMap = model.getProductQuantities();
            newHashMap.put(model.getFoods().get(position).getId(), Long.valueOf(
                    (Long) newHashMap.get(model.getFoods().get(position).getId()) + 1));
            model.setProductQuantities(newHashMap, true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = MenuActivity.this;
        long restaurantId = getExtras();
        initModel(restaurantId);
        initLayout();

        setContentView(layout);
    }

    private long getExtras() {
        long restaurantId = 0;
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            restaurantId = extras.getLong(Constants.RESTAURANT_ID, 0);
        }
        return restaurantId;
    }

    private void initModel(long restaurantId) {
        model = new MenuModel();

        model.setFoodDataSource(new FoodDataSource(MenuActivity.this));
        model.getFoodDataSource().open();

        model.setRestaurantId(restaurantId);
        model.setFoods(model.getFoodDataSource().getFoodByRestaurantId(restaurantId));
        HashMap<Long, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < model.getFoods().size(); i++) {
            hashMap.put(model.getFoods().get(i).getId(), 0);
        }

        model.setProductQuantities(hashMap);
    }


    @Override
    public void onResume() {
        super.onResume();

        model.getFoodDataSource().open();
//        model.setRestaurantId(restaurantId);
//        model.setFoods(model.getFoodDataSource().getFoodByRestaurantId(restaurantId));
    }

    @Override
    public void onPause() {
        super.onPause();
        model.getFoodDataSource().closeHelper();
    }

    private void onSaveOrderClicked() {
        Intent intent = new Intent(MenuActivity.this, NewOrderActivity.class);
        ArrayList<SubOrder> suborders = generateSuborders(model.getProductQuantities());
        if (suborders.size() == 0) {
            new AlertDialog.Builder(this)
                    .setTitle("No products ordered")
                    .setMessage("Aren't you feeling hungry?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            RestaurantsManager.getRestaurantsManager().setSubOrders(suborders);
            startActivity(intent);
        }
    }

    private ArrayList<SubOrder> generateSuborders(HashMap<Long, Integer> productQuantities) {
        ArrayList<SubOrder> suborders = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Integer quantity = entry.getValue();
            if (quantity != 0) {
                Long productId = entry.getKey();
                suborders.add(new SubOrder(2, productId, quantity));
            }
        }
        return suborders;

    }

    private void initLayout() {
        layout = (MenuLayout) View.inflate(activity, R.layout.activity_menu, null);
        layout.setModel(model);
        layout.setViewListener(viewListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (id == R.id.menu_menu_action_save) {
           onSaveOrderClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

