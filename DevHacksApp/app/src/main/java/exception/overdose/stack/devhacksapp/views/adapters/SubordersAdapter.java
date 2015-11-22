package exception.overdose.stack.devhacksapp.views.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.database.FoodDataSource;
import exception.overdose.stack.devhacksapp.database.RestaurantDataSource;
import exception.overdose.stack.devhacksapp.database.SubOrderDataSource;
import exception.overdose.stack.devhacksapp.models.POJO.Orders;
import exception.overdose.stack.devhacksapp.models.POJO.SubOrder;

/**
 * Created by Adriana on 22/11/2015.
 */
public class SubordersAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;

    private List<SubOrder> currentItems;

    private Context context;
    private FoodDataSource foodDataSource;
    public SubordersAdapter(Context context, List<SubOrder> items) {
        this.layoutInflater = LayoutInflater.from(context);
        this.currentItems = items;
        this.context = context;
        foodDataSource = new FoodDataSource(context);
        foodDataSource.open();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_order, parent, false);

            holder = new ViewHolder();
            holder.quantityTextView = (TextView) convertView.findViewById(R.id.row_suborder_quantity_textview);
            holder.foodNameTextView = (TextView) convertView.findViewById(R.id.row_suborder_product_name_textview);
            holder.foodDescriptionTextView = (TextView) convertView.findViewById(R.id.row_suborder_details_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.quantityTextView.setText(currentItems.get(position).getQuantity());
        holder.foodNameTextView.setText(foodDataSource.getFood(currentItems.get(position).getFoodID()).getName());
        holder.foodDescriptionTextView.setText(foodDataSource.getFood(currentItems.get(position).getFoodID()).getDescription());
        return convertView;
    }
    private static class ViewHolder {
        TextView quantityTextView;
        TextView foodNameTextView;
        TextView foodDescriptionTextView;
    }
}
