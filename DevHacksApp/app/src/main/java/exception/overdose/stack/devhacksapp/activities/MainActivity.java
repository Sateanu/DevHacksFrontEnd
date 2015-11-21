package exception.overdose.stack.devhacksapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.managers.RestaurantsManager;
import exception.overdose.stack.devhacksapp.models.MainModel;
import exception.overdose.stack.devhacksapp.utils.BEAPI;
import exception.overdose.stack.devhacksapp.views.MainLayout;
import exception.overdose.stack.devhacksapp.views.adapters.RestaurantsAdapter;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MvcActivity";

    private Activity activity;

    private MainLayout layout;
    private MainModel model;
    private MainLayout.ViewListener viewListener = new MainLayout.ViewListener() {
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = MainActivity.this;

        initModel();
        initLayout();

        setContentView(layout);

        Button button = (Button) findViewById(R.id.some_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivityForResult(intent, 2);
            }
        });
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

}
