package exception.overdose.stack.devhacksapp.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.models.MainModel;
import exception.overdose.stack.devhacksapp.views.MainLayout;

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
    }

    private void initModel() {
        model = new MainModel();
    }

    private void initLayout() {
        layout = (MainLayout) View.inflate(activity, R.layout.activity_main, null);
        layout.setModel(model);
        layout.setViewListener(viewListener);
    }
}
