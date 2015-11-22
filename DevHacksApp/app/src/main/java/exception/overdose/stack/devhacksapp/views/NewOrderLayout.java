package exception.overdose.stack.devhacksapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.database.RestaurantDataSource;
import exception.overdose.stack.devhacksapp.database.SubOrderDataSource;
import exception.overdose.stack.devhacksapp.interfaces.mvc.OnChangeListener;
import exception.overdose.stack.devhacksapp.managers.RestaurantsManager;
import exception.overdose.stack.devhacksapp.models.MainModel;
import exception.overdose.stack.devhacksapp.models.MenuModel;
import exception.overdose.stack.devhacksapp.models.NewOrderModel;
import exception.overdose.stack.devhacksapp.models.OrdersModel;
import exception.overdose.stack.devhacksapp.models.POJO.SubOrder;
import exception.overdose.stack.devhacksapp.utils.Constants;
import exception.overdose.stack.devhacksapp.utils.ViewUtils;
import exception.overdose.stack.devhacksapp.views.adapters.OrdersAdapter;
import exception.overdose.stack.devhacksapp.views.adapters.RestaurantsAdapter;
import exception.overdose.stack.devhacksapp.views.adapters.SubordersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by alexbuicescu on 22.11.2015.
 */
public class NewOrderLayout extends LinearLayout implements OnChangeListener<MainModel> {

    private final String TAG = "NewOrderLayout";


    private NewOrderModel model;
    private ViewListener viewListener;

    private ListView subordersListView;
    private TextView priceTextView;
    private TextView timeTextView;
    private LinearLayout timeLinearLayout;
    private EditText addressEditText;
    private RelativeLayout goToMapsRelativeLayout;
    private Toolbar toolbar;
    private SubordersAdapter subordersAdapter;


    public interface ViewListener {
        void onMapsClicked();
        void onSendOrderClicked();
        void onTimeClicked();
    }

    public NewOrderLayout(Context context) {
        super(context);
    }

    public NewOrderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewOrderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initLayout();
        initToolbar();
    }

    private void initLayout() {
        subordersListView = (ListView) findViewById(R.id.activity_neworder_suborders_listview);
        subordersListView.setAdapter(new SubordersAdapter(getContext(), RestaurantsManager.getRestaurantsManager().getSubOrders()));
        addressEditText= (EditText) findViewById(R.id.activity_neworder_address_edittext);
        goToMapsRelativeLayout= (RelativeLayout) findViewById(R.id.activity_neworder_map_relativelayout);
        goToMapsRelativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onMapsClicked();
            }
        });
        timeLinearLayout= (LinearLayout) findViewById(R.id.activity_neworder_time_linearlayout);
        timeLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getViewListener().onTimeClicked();
            }
        });
        timeTextView= (TextView) findViewById(R.id.activity_neworder_time_textview);
        priceTextView= (TextView) findViewById(R.id.activity_neworder_price_textview);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_new_order_toolbar);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        ViewUtils.setActionBarTitle(getContext(), getContext().getResources().getString(R.string.app_name), true);
    }

    private void updateView() {
        if(subordersAdapter == null)
        {
            subordersAdapter = new SubordersAdapter(getContext(), getModel().getSubOrders());
            subordersListView.setAdapter(subordersAdapter);
        }
        else
        {
           // subordersAdapter.setCurrentItems(getModel().getSubOrders());
           // subordersAdapter.notifyDataSetChanged();
        }

        if(getModel().getNewOrder()!= null && getModel().getNewOrder().getTime() != -1)
        {
            timeTextView.setText(Constants.simpleDateTimeFormat.format(new Date(getModel().getNewOrder().getTime())));
        }

        addressEditText.setText(getModel().getNewOrder().getLocation());
        priceTextView.setText(getModel().getNewOrder().getPrice() + " RON");
    }

    public String getLocation()
    {
        return addressEditText.getText().toString();
    }

    @Override
    public void onChange() {
        updateView();
    }

    public NewOrderModel getModel() {
        return model;
    }

    public void setModel(NewOrderModel model) {
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