package exception.overdose.stack.devhacksapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.database.RestaurantDataSource;
import exception.overdose.stack.devhacksapp.interfaces.mvc.OnChangeListener;
import exception.overdose.stack.devhacksapp.models.OrdersModel;
import exception.overdose.stack.devhacksapp.utils.ViewUtils;
import exception.overdose.stack.devhacksapp.views.adapters.OrdersAdapter;

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
        ViewUtils.setActionBarTitle(getContext(), getContext().getResources().getString(R.string.app_name), false);
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
    public void showOrderDetail(int position)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        RelativeLayout dialogLayout = (RelativeLayout) View.inflate(getContext(), R.layout.dialog_order_details, null);

        final ImageView restaurantImageView = (ImageView) dialogLayout.findViewById(R.id.dialog_order_details_restaurant_logo_imageview);

        alertDialog.setView(dialogLayout);
        alertDialog.setTitle("Order details");
        TextView restaurantTextView= (TextView) dialogLayout.findViewById(R.id.dialog_order_restaurant_textview);
        RestaurantDataSource restaurantDataSource=new RestaurantDataSource(getContext());
        restaurantDataSource.open();
        restaurantTextView.setText(restaurantDataSource.getRestaurant(getModel().getOrders().get(position).getRestaurantID()).getName());
        TextView priceTextView=(TextView) dialogLayout.findViewById(R.id.dialog_order_details_price_textview);
        priceTextView.setText(getModel().getOrders().get(position).getPrice() + "");
        TextView priceWithDiscountextView=(TextView) dialogLayout.findViewById(R.id.dialog_order_details_price_with_discount_textview);
   
        TextView date=(TextView) dialogLayout.findViewById(R.id.dialog_order_details_time_textview);
        restaurantDataSource.closeHelper();
    }
}
