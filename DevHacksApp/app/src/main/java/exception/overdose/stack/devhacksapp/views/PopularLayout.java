package exception.overdose.stack.devhacksapp.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.interfaces.mvc.OnChangeListener;
import exception.overdose.stack.devhacksapp.models.MainModel;
import exception.overdose.stack.devhacksapp.models.PopularModel;
import exception.overdose.stack.devhacksapp.utils.ViewUtils;
import exception.overdose.stack.devhacksapp.views.adapters.RestaurantsAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class PopularLayout extends RelativeLayout implements OnChangeListener<PopularModel> {

    private final String TAG = "MainLayout";

    private PopularModel model;
    private ViewListener viewListener;

    private StickyListHeadersListView restaurantsListView;
    private RestaurantsAdapter restaurantsAdapter;
    private Toolbar toolbar;

    public interface ViewListener {
        void onItemClicked(int position);
    }

    public PopularLayout(Context context) {
        super(context);
    }

    public PopularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopularLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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
        restaurantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getViewListener().onItemClicked(position);
            }
        });
    }

    private void initToolbar() {

        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        ViewUtils.setActionBarTitle(getContext(), "Most Popular", true);
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

    public PopularModel getModel() {
        return model;
    }

    public void setModel(PopularModel model) {
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
