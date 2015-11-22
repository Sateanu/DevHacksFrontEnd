package exception.overdose.stack.devhacksapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.models.MenuModel;
import exception.overdose.stack.devhacksapp.models.POJO.Food;
import exception.overdose.stack.devhacksapp.models.POJO.Restaurant;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Adriana on 22/11/2015.
 */
public class FoodAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    private LayoutInflater layoutInflater;

    //    private List<Food> currentItems;
//    private HashMap<Long,Integer> quantities;
    private MenuModel menuModel;
    ArrayList<String> currentHeaders;
    private HashMap<String, Integer> indexer;

    private Context context;

    public FoodAdapter(Context context, MenuModel menuModel) {
        this.layoutInflater = LayoutInflater.from(context);
//        this.currentItems = items;
//        this.quantities=quantities;
        this.menuModel = menuModel;
        this.context = context;

        initIndexer();
    }

    private void initIndexer() {
        indexer = new HashMap<>();

        Collections.sort(menuModel.getFoods(), new Comparator<Food>() {
            @Override
            public int compare(Food lhs, Food rhs) {
                return lhs.getCategory().compareTo(rhs.getCategory());
            }
        });

        for (int i = 0; i < menuModel.getFoods().size(); i++) {
            if(indexer.get(menuModel.getFoods().get(i).getCategory()+ "") == null)
            {
                indexer.put(menuModel.getFoods().get(i).getCategory() + "", i);
            }
        }

        Set<String> keys = indexer.keySet();
        currentHeaders = new ArrayList<>(keys);
        Collections.sort(currentHeaders);
    }

    @Override
    public int getCount() {
        return menuModel.getFoods().size();
    }

    @Override
    public Object getItem(int position) {
        return menuModel.getFoods().get(position);
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

        holder.foodNameTextView.setText(menuModel.getFoods().get(position).getName());
        holder.descriptionTextView.setText(menuModel.getFoods().get(position).getDescription());
        holder.priceTextView.setText(menuModel.getFoods().get(position).getPrice() + " RON ");
        holder.minusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMinusImageViewClicked(position);
                holder.quantityTextView.setText(menuModel.getProductQuantities().get(menuModel.getFoods().get(position).getId()) + "");
            }
        });
        holder.plusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlusImageViewClicked(position);
                holder.quantityTextView.setText(menuModel.getProductQuantities().get(menuModel.getFoods().get(position).getId()) + "");
            }
        });
        holder.quantityTextView.setText(menuModel.getProductQuantities().get(menuModel.getFoods().get(position).getId()) + "");
        return convertView;
    }

    public void onMinusImageViewClicked(int position) {
        HashMap newHashMap = menuModel.getProductQuantities();
        newHashMap.put(menuModel.getFoods().get(position).getId(), Integer.valueOf(
                (Integer) newHashMap.get(menuModel.getFoods().get(position).getId()) - 1));
        menuModel.setProductQuantities(newHashMap, true);
    }

    public void onPlusImageViewClicked(int position) {
        HashMap newHashMap = menuModel.getProductQuantities();
        newHashMap.put(menuModel.getFoods().get(position).getId(), Integer.valueOf(
                (Integer) newHashMap.get(menuModel.getFoods().get(position).getId()) + 1));
        menuModel.setProductQuantities(newHashMap, true);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public Object[] getSections() {
        return currentHeaders.toArray();
    }

    @Override
    public int getPositionForSection(int section) {
        try {
            return indexer.get(currentHeaders.get(section));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < currentHeaders.size(); i++) {
            if (position < indexer.get(currentHeaders.get(i))) {
                return i - 1;
            }
        }
        return currentHeaders.size() - 1;
    }

    @Override
    public View getHeaderView(int i, View convertView, ViewGroup parent) {
        DividerViewHolder holder;

        if (convertView == null) {
            holder = new DividerViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout_restaurant_header_view, parent, false);
            holder.groupName = (TextView) convertView.findViewById(R.id.list_view_header);
            convertView.setTag(holder);
        } else {
            holder = (DividerViewHolder) convertView.getTag();
        }

        holder.groupName.setText("Header: " + menuModel.getFoods().get(i).getCategory());

        return convertView;
    }

    @Override
    public long getHeaderId(int i) {
        return indexer.get(menuModel.getFoods().get(i).getCategory());//currentItems.get(i).getSpecific();
    }

    public static class DividerViewHolder {
        TextView groupName;
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