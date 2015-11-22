package exception.overdose.stack.devhacksapp.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.models.POJO.Restaurant;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by alexbuicescu on 20.08.2015.
 */
public class RestaurantsAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

    private LayoutInflater inflater;

    private List<Restaurant> currentItems;

    ArrayList<String> currentHeaders;
    private HashMap<String, Integer> indexer;

    private Context context;

    public RestaurantsAdapter(Context context, List<Restaurant> items) {
        this.inflater = LayoutInflater.from(context);
        this.currentItems = items;
        this.context = context;

        initIndexer();
    }

    private void initIndexer() {
        indexer = new HashMap<>();

        Collections.sort(currentItems, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant lhs, Restaurant rhs) {
                return lhs.getSpecific().compareTo(rhs.getSpecific());
            }
        });

        for (int i = 0; i < currentItems.size(); i++) {
            if(indexer.get(currentItems.get(i).getSpecific()+ "") == null)
            {
                indexer.put(currentItems.get(i).getSpecific() + "", i);
            }
        }

        Set<String> keys = indexer.keySet();
        currentHeaders = new ArrayList<>(keys);
        Collections.sort(currentHeaders);
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
            convertView = inflater.inflate(R.layout.row_restaurant, parent, false);

            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.row_restaurant_name_textview);
            holder.specificTextView = (TextView) convertView.findViewById(R.id.row_restaurant_specific_textview);
            holder.locationTextView = (TextView) convertView.findViewById(R.id.row_restaurant_location_textview);
            holder.restaurantLogoImageView= (ImageView) convertView.findViewById(R.id.row_restaurant_restaurant_logo_imageview);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(currentItems.get(position).getUrl(), holder.restaurantLogoImageView);
        holder.nameTextView.setText(currentItems.get(position).getName());
        holder.specificTextView.setText(currentItems.get(position).getSpecific());
        holder.locationTextView.setText(currentItems.get(position).getLocation());

        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setCurrentItems(ArrayList<Restaurant> items)
    {
        this.currentItems = items;
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
            convertView = inflater.inflate(R.layout.layout_restaurant_header_view, parent, false);
            holder.groupName = (TextView) convertView.findViewById(R.id.list_view_header);
            convertView.setTag(holder);
        } else {
            holder = (DividerViewHolder) convertView.getTag();
        }

        holder.groupName.setText(currentItems.get(i).getSpecific());

        return convertView;
    }

    @Override
    public long getHeaderId(int i) {
        return i;//currentItems.get(i).getSpecific();
    }
    private static class ViewHolder {
        TextView nameTextView;
        TextView specificTextView;
        TextView locationTextView;
        ImageView restaurantLogoImageView;
    }

    public static class DividerViewHolder {
        TextView groupName;
    }
}
