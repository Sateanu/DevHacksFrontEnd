package exception.overdose.stack.devhacksapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.database.RestaurantDataSource;
import exception.overdose.stack.devhacksapp.database.SubOrderDataSource;
import exception.overdose.stack.devhacksapp.interfaces.mvc.OnChangeListener;
import exception.overdose.stack.devhacksapp.models.OrdersModel;
import exception.overdose.stack.devhacksapp.utils.Constants;
import exception.overdose.stack.devhacksapp.utils.ViewUtils;
import exception.overdose.stack.devhacksapp.utils.XUtils;
import exception.overdose.stack.devhacksapp.views.adapters.OrdersAdapter;
import exception.overdose.stack.devhacksapp.views.adapters.SubordersAdapter;

/**
 * Created by Adriana on 21/11/2015.
 */
public class OrdersLayout  extends RelativeLayout implements OnChangeListener<OrdersModel> {

    private final String TAG = "OrdersLayout";

    private OrdersModel model;
    private ViewListener viewListener;

    private ListView ordersListView;
    private OrdersAdapter ordersAdapter;
    private Toolbar toolbar;


    public interface ViewListener {
        void onOrderClicked(int position);
    }

    public OrdersLayout(Context context) {
        super(context);
    }

    public OrdersLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OrdersLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initLayout();
        initToolbar();
    }

    private void initLayout() {
        ordersListView = (ListView) findViewById(R.id.activity_orders_orders_listview);
        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getViewListener().onOrderClicked(position);
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.activity_orders_toolbar);
        ((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        ViewUtils.setActionBarTitle(getContext(), getContext().getResources().getString(R.string.app_name), true);
    }

    private void updateView() {
        if(ordersAdapter == null)
        {
            ordersAdapter = new OrdersAdapter(getContext(), getModel().getOrders());
            ordersListView.setAdapter(ordersAdapter);
        }
        else
        {
            ordersAdapter.setCurrentItems(getModel().getOrders());
            ordersAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChange() {
        updateView();
    }

    public OrdersModel getModel() {
        return model;
    }

    public void setModel(OrdersModel model) {
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
    public void showOrderDetails(int position)
    {
        RestaurantDataSource restaurantDataSource=new RestaurantDataSource(getContext());
        SubOrderDataSource subOrderDataSource=new SubOrderDataSource(getContext());
        subOrderDataSource.open();
        restaurantDataSource.open();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        LinearLayout dialogLayout = (LinearLayout) View.inflate(getContext(), R.layout.dialog_order_details, null);

        final ImageView restaurantImageView = (ImageView) dialogLayout.findViewById(R.id.dialog_order_details_restaurant_logo_imageview);
        TextView restaurantTextView= (TextView) dialogLayout.findViewById(R.id.dialog_order_restaurant_textview);
        TextView priceTextView=(TextView) dialogLayout.findViewById(R.id.dialog_order_details_price_textview);
        TextView priceWithDiscountextView=(TextView) dialogLayout.findViewById(R.id.dialog_order_details_price_with_discount_textview);
        ListView subordersListView= (ListView) dialogLayout.findViewById(R.id.dialog_order_details_listview);
        TextView timeTextView=(TextView) dialogLayout.findViewById(R.id.dialog_order_details_time_textview);

        alertDialog.setView(dialogLayout);
        alertDialog.setTitle("Order details");

        restaurantTextView.setText(restaurantDataSource.getRestaurant(getModel().getOrders().get(position).getRestaurantID()).getName());
        priceTextView.setText(getModel().getOrders().get(position).getPrice() + "");
        if (getModel().getOrders().get(position).getDiscount() != 0) {
            priceTextView.setText(getModel().getOrders().get(position).getPrice() + "");
        }
        priceTextView.setPaintFlags(priceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        priceWithDiscountextView.setText(String.format("%.2f",
                ((1 - getModel().getOrders().get(position).getDiscount()/100)
                        * getModel().getOrders().get(position).getPrice())));
        timeTextView.setText(Constants.simpleDateTimeFormat.format(getModel().getOrders().get(position).getTime()));
        subordersListView.setAdapter(new SubordersAdapter(getContext(),subOrderDataSource.getSubOrders(getModel().getOrders().get(position).getId())));
        restaurantDataSource.closeHelper();
        subOrderDataSource.closeHelper();
        try {
            alertDialog.create().show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
