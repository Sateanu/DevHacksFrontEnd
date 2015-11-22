package exception.overdose.stack.devhacksapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.managers.RestaurantsManager;
import exception.overdose.stack.devhacksapp.models.MainModel;
import exception.overdose.stack.devhacksapp.models.PopularModel;
import exception.overdose.stack.devhacksapp.utils.Constants;
import exception.overdose.stack.devhacksapp.views.MainLayout;
import exception.overdose.stack.devhacksapp.views.PopularLayout;

public class PopularActivity extends AppCompatActivity {

    private final String TAG = "MvcActivity";

    private Activity activity;

    private PopularLayout layout;
    private PopularModel model;
    private PopularLayout.ViewListener viewListener = new PopularLayout.ViewListener() {
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

        activity = PopularActivity.this;

        initModel();
        initLayout();

        setContentView(layout);
    }

    private void initModel() {
        model = new PopularModel();
    }

    private void initLayout() {
        layout = (PopularLayout) View.inflate(activity, R.layout.activity_popular, null);
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
        getMenuInflater().inflate(R.menu.menu_popular, menu);
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
