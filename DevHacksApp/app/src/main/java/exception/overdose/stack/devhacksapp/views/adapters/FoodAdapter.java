package exception.overdose.stack.devhacksapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.models.POJO.Food;

/**
 * Created by Adriana on 22/11/2015.
 */
public class FoodAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;

    private List<Food> currentItems;
    private HashMap<Long,Integer> quantities;

    private Context context;

    public FoodAdapter(Context context, List<Food> items,HashMap<Long,Integer> quantities) {
        this.layoutInflater = LayoutInflater.from(context);
        this.currentItems = items;
        this.quantities=quantities;
        this.context = context;
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
            convertView = layoutInflater.inflate(R.layout.row_food, parent, false);

            holder = new ViewHolder();
            holder.foodNameTextView = (TextView) convertView.findViewById(R.id.row_food_food_name_textview);
            holder.descriptionTextView = (TextView) convertView.findViewById(R.id.row_food_food_description_textview);
            holder.priceTextView = (TextView) convertView.findViewById(R.id.row_food_food_price_textview);
            holder.quantityTextView = (TextView) convertView.findViewById(R.id.row_food_quantity_edittext);
            holder.plusImageView = (ImageView) convertView.findViewById(R.id.row_food_plus_quantity_textview);
            holder.minusImageView = (ImageView) convertView.findViewById(R.id.row_food_minus_quantity_textview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.foodNameTextView.setText(currentItems.get(position).getName());
        holder.descriptionTextView.setText(currentItems.get(position).getDescription());
        holder.priceTextView.setText(currentItems.get(position).getPrice()+" RON ");
       // holder.quantityTextView.setText();
        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setCurrentItems(ArrayList<Food> items) {
        this.currentItems = items;
    }

    private static class ViewHolder {
        TextView foodNameTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        TextView quantityTextView;
        ImageView plusImageView;
        ImageView minusImageView;
    }
}