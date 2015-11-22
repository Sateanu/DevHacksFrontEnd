package exception.overdose.stack.devhacksapp.activities;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.database.FoodDataSource;
import exception.overdose.stack.devhacksapp.database.RestaurantDataSource;
import exception.overdose.stack.devhacksapp.managers.OrdersManager;
import exception.overdose.stack.devhacksapp.managers.RestaurantsManager;
import exception.overdose.stack.devhacksapp.models.MainModel;
import exception.overdose.stack.devhacksapp.models.NewOrderModel;
import exception.overdose.stack.devhacksapp.models.POJO.Orders;
import exception.overdose.stack.devhacksapp.models.POJO.SubOrder;
import exception.overdose.stack.devhacksapp.utils.BEAPI;
import exception.overdose.stack.devhacksapp.utils.Constants;
import exception.overdose.stack.devhacksapp.views.MainLayout;
import exception.overdose.stack.devhacksapp.views.NewOrderLayout;

/**
 * Created by Adriana on 22/11/2015.
 */
public class NewOrderActivity  extends AppCompatActivity {

    private final String TAG = "NewOrderActivity";
    private final int REQ_CODE_MAPS=1;

    private Activity activity;

    private NewOrderLayout layout;
    private NewOrderModel model;
    private NewOrderLayout.ViewListener viewListener = new NewOrderLayout.ViewListener() {
        @Override
        public void onMapsClicked() {
            model.getNewOrder().setLocation(layout.getLocation());
            Intent intent=new Intent(activity,MapsActivity.class);
            startActivityForResult(intent,REQ_CODE_MAPS);
        }

        @Override
        public void onSendOrderClicked() {
            new BEAPI.SendOrderAsync(model.getSubOrders()).execute(model.getNewOrder());

            Intent intent = new Intent(activity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            activity.finish();
        }

        @Override
        public void onTimeClicked() {
            model.getNewOrder().setLocation(layout.getLocation());

            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                    calendar.set(Calendar.MINUTE, selectedMinute);
                    calendar.clear(Calendar.SECOND); //reset seconds to zero

                    model.getNewOrder().setTime(calendar.getTimeInMillis());
                    model.update(true);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = NewOrderActivity.this;

        initModel();
        initExtra();
        initLayout();

        setContentView(layout);
    }

    private void initModel() {
        model = new NewOrderModel();
        model.setSubOrders(RestaurantsManager.getRestaurantsManager().getSubOrders());
    }

    private void initExtra()
    {
        if(getIntent() != null)
        {
            if(getIntent().getExtras() != null)
            {
                long restaurantId =getIntent().getExtras().getLong(Constants.RESTAURANT_ID);
                model.setNewOrder(new Orders(-1, restaurantId, 0, 0, RestaurantsManager.getRestaurantsManager().getMyId()));
                RestaurantDataSource restaurantDataSource = new RestaurantDataSource(activity);
                restaurantDataSource.open();
                model.setRestaurantName(restaurantDataSource.getRestaurant(restaurantId).getName());
                restaurantDataSource.closeHelper();

                FoodDataSource foodDataSource = new FoodDataSource(activity);
                foodDataSource.open();

                float price = 0;

                for(SubOrder subOrder : RestaurantsManager.getRestaurantsManager().getSubOrders())
                {
                    price += foodDataSource.getFood(subOrder.getFoodID()).getPrice() * subOrder.getQuantity();
                }

                model.getNewOrder().setPrice(price);

                foodDataSource.closeHelper();

            }
        }
    }

    private void initLayout() {
        layout = (NewOrderLayout) View.inflate(activity, R.layout.activity_neworder, null);
        layout.setModel(model);
        layout.setViewListener(viewListener);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE_MAPS)
        {
            if(resultCode == RESULT_OK)
            {
                String location = data.getStringExtra("location");
                String longitude = data.getStringExtra("longitude");
                String latitude = data.getStringExtra("latitude");
                model.getNewOrder().setLocation(location);
                model.getNewOrder().setLongitude(Float.parseFloat(longitude));
                model.getNewOrder().setLatitude(Float.parseFloat(latitude));
                model.update(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_order, menu);
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
        if (id == R.id.menu_new_order_action_save) {
            viewListener.onSendOrderClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
