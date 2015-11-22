package exception.overdose.stack.devhacksapp.views.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.database.RestaurantDataSource;
import exception.overdose.stack.devhacksapp.database.SubOrderDataSource;
import exception.overdose.stack.devhacksapp.models.POJO.Orders;
import exception.overdose.stack.devhacksapp.utils.Constants;
import exception.overdose.stack.devhacksapp.utils.XUtils;

/**
 * Created by Adriana on 21/11/2015.
 */
public class OrdersAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;

    private List<Orders> currentItems;

    private Context context;

    private RestaurantDataSource restaurantDataSource;
    private SubOrderDataSource subOrderDataSource;

    public OrdersAdapter(Context context, List<Orders> items) {
        this.layoutInflater = LayoutInflater.from(context);
        this.currentItems = items;
        this.context = context;
        restaurantDataSource = new RestaurantDataSource(context);
        subOrderDataSource = new SubOrderDataSource(context);
        restaurantDataSource.open();
        subOrderDataSource.open();
    }

    @Override
    public int getCount() {
        return currentItems.size();
    }

    @Override
    public Object getItem(int position) {
        return currentItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
                   final ViewHolder holder;

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.row_order, parent, false);

            holder = new ViewHolder();
            holder.timeTextView = (TextView) convertView.findViewById(R.id.row_order_time_textview);
            holder.restaurantTextView = (TextView) convertView.findViewById(R.id.row_order_restaurant_textview);
            holder.priceTextView = (TextView) convertView.findViewById(R.id.row_order_price_textview);
            holder.priceWithDiscountTextView = (TextView) convertView.findViewById(R.id.row_order_price_with_discount_textview);
            holder.subordersTextView = (TextView) convertView.findViewById(R.id.row_order_suborders_textview);
            holder.priceTextView.setPaintFlags(holder.priceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (currentItems.get(position).getTime() < (new Date()).getTime()) {
            holder.timeTextView.setTextColor(getContext().getResources().getColor(R.color.red_dark));
        } else {
            holder.timeTextView.setTextColor(getContext().getResources().getColor(R.color.green_dark));
        }
        holder.timeTextView.setText(Constants.simpleDateTimeFormat.format(currentItems.get(position).getTime()));
        holder.restaurantTextView.setText(restaurantDataSource.getRestaurant(currentItems.get(position).getRestaurantID()).getName());
        holder.subordersTextView.setText(
                XUtils.getSubordersRepresentation(
                        subOrderDataSource.getSubOrders(
                                currentItems.get(position).getId()), getContext()));
        if (currentItems.get(position).getDiscount() != 0) {
            holder.priceTextView.setText(currentItems.get(position).getPrice() + "");
            holder.priceTextView.setVisibility(View.VISIBLE);
        }
        else{
            holder.priceTextView.setVisibility(View.GONE);
        }
        holder.priceWithDiscountTextView.setText(String.format("%.2f",
                ((1 - currentItems.get(position).getDiscount())
                        * currentItems.get(position).getPrice())));

        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setCurrentItems(ArrayList<Orders> items) {
        this.currentItems = items;
    }

    private static class ViewHolder {
        TextView timeTextView;
        TextView restaurantTextView;
        TextView priceTextView;
        TextView priceWithDiscountTextView;
        TextView subordersTextView;
    }
}
