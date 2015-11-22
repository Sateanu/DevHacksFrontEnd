package exception.overdose.stack.devhacksapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.managers.RestaurantsManager;
import exception.overdose.stack.devhacksapp.models.MainModel;
import exception.overdose.stack.devhacksapp.utils.Constants;
import exception.overdose.stack.devhacksapp.views.MainLayout;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MvcActivity";

    private Activity activity;

    private MainLayout layout;
    private MainModel model;
    private MainLayout.ViewListener viewListener = new MainLayout.ViewListener() {
        @Override
        public void onItemClicked(int position) {
            Intent intent=new Intent(activity,MenuActivity.class);
            intent.putExtra(Constants.RESTAURANT_ID,model.getRestaurants().get(position).getId());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = MainActivity.this;

        initModel();
        initLayout();

        setContentView(layout);
    }

    private void initModel() {
        model = new MainModel();
    }

    private void initLayout() {
        layout = (MainLayout) View.inflate(activity, R.layout.activity_main, null);
        layout.setModel(model);
        layout.setViewListener(viewListener);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        model.setRestaurants(RestaurantsManager.getRestaurantsManager().getRestaurants(), true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.menu_main_action_history) {
//            viewListener.onSendOrderClicked();
            return true;
        }
        if (id == R.id.menu_main_action_popular) {
//            viewListener.onSendOrderClicked();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
