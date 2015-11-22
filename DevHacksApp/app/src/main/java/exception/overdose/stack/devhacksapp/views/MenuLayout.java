package exception.overdose.stack.devhacksapp.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.database.RestaurantDataSource;
import exception.overdose.stack.devhacksapp.interfaces.mvc.OnChangeListener;
import exception.overdose.stack.devhacksapp.models.MenuModel;
import exception.overdose.stack.devhacksapp.utils.ViewUtils;
import exception.overdose.stack.devhacksapp.views.adapters.FoodAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by Adriana on 22/11/2015.
 */
public class MenuLayout extends RelativeLayout implements OnChangeListener<MenuModel> {

    private final String TAG = "MenuLayout";

    private MenuModel model;
    private ViewListener viewListener;

    private StickyListHeadersListView ordersListView;
    private FoodAdapter foodAdapter;
    private Toolbar toolbar;


    public interface ViewListener {
        void onMinusImageViewClicked(int position);
        void onPlusImageViewClicked(int position);
    }

    public MenuLayout(Context context) {
        super(context);
    }

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initLayout();
        initToolbar();
    }

    private void initLayout() {
        ordersListView = (StickyListHeadersListView) findViewById(R.id.activity_menu_food_listview);
        ordersListView.setFastScrollEnabled(true);
//        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                getViewListener().onOrderClicked(position);
//            }
//        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_menu_toolbar);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
    }

    private void updateView() {
        RestaurantDataSource restaurantDataSource = new RestaurantDataSource(getContext());
        restaurantDataSource.open();

        ViewUtils.setActionBarTitle(getContext(), restaurantDataSource.getRestaurant(model.getRestaurantId()).getName(), true);
        restaurantDataSource.closeHelper();
        if(foodAdapter == null)
        {
            foodAdapter = new FoodAdapter(getContext(), getModel());
            ordersListView.setAdapter(foodAdapter);
        }
        else
        {
            foodAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChange() {
        updateView();
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
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