package exception.overdose.stack.devhacksapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.database.OrderDataSource;
import exception.overdose.stack.devhacksapp.models.OrdersModel;
import exception.overdose.stack.devhacksapp.views.OrdersLayout;

/**
 * Created by Adriana on 21/11/2015.
 */
public class                        OrdersActivity extends AppCompatActivity {

    private final String TAG = "OrdersActivity";

    private Activity activity;

    private OrdersLayout layout;
    private OrdersModel model;
    private OrdersLayout.ViewListener viewListener = new OrdersLayout.ViewListener() {
        @Override
        public void onOrderClicked(int position) {
            layout.showOrderDetails(position);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = OrdersActivity.this;

        initModel();
        initLayout();

        setContentView(layout);
    }

    private void initModel() {
        model = new OrdersModel();

        model.setOrderDataSource(new OrderDataSource(OrdersActivity.this));
        model.getOrderDataSource().open();


        model.setOrders(model.getOrderDataSource().getAllOrders());
    }


    @Override
    public void onResume() {
        super.onResume();

        model.getOrderDataSource().open();
        model.setOrders(model.getOrderDataSource().getAllOrders(), true);
    }

    @Override
    public void onPause() {
        super.onPause();
        model.getOrderDataSource().closeHelper();
    }

    private void initLayout() {
        layout = (OrdersLayout) View.inflate(activity, R.layout.activity_orders, null);
        layout.setModel(model);
        layout.setViewListener(viewListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orders, menu);
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

        return super.onOptionsItemSelected(item);
    }

}
