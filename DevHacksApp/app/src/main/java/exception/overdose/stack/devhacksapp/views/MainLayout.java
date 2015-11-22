package exception.overdose.stack.devhacksapp.views;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.interfaces.mvc.OnChangeListener;
import exception.overdose.stack.devhacksapp.models.MainModel;
import exception.overdose.stack.devhacksapp.views.adapters.RestaurantsAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class MainLayout extends RelativeLayout implements OnChangeListener<MainModel> {

    private final String TAG = "MainLayout";

    private MainModel model;
    private ViewListener viewListener;

    private StickyListHeadersListView restaurantsListView;
    private RestaurantsAdapter restaurantsAdapter;

    public interface ViewListener {
    }

    public MainLayout(Context context) {
        super(context);
    }

    public MainLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MainLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initLayout();
        initToolbar();
    }

    private void initLayout() {
        restaurantsListView = (StickyListHeadersListView) findViewById(R.id.activity_main_restaurants_listview);
        restaurantsListView.setFastScrollEnabled(true);
    }

    private void initToolbar() {
    }

    private void updateView() {

        if(restaurantsAdapter == null)
        {
            restaurantsAdapter = new RestaurantsAdapter(getContext(), getModel().getRestaurants());
            restaurantsListView.setAdapter(restaurantsAdapter);
        }
        else
        {
            restaurantsAdapter.setCurrentItems(getModel().getRestaurants());
            restaurantsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChange() {
        updateView();
    }

    public MainModel getModel() {
        return model;
    }

    public void setModel(MainModel model) {
        this.model = model;
        this.model.addListener(this);
        updateView();
    }

    public ViewListener getViewListener() {
        return viewListener;
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

}
