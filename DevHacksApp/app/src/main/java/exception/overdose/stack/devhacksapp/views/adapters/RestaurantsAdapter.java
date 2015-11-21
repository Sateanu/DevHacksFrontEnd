package exception.overdose.stack.devhacksapp.views.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import exception.overdose.stack.devhacksapp.R;
import exception.overdose.stack.devhacksapp.models.POJO.Restaurant;

/**
 * Created by alexbuicescu on 20.08.2015.
 */
public class RestaurantsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private List<Restaurant> currentItems;

    private Context context;

    public RestaurantsAdapter(Context context, List<Restaurant> items) {
        this.layoutInflater = LayoutInflater.from(context);
        this.currentItems = items;
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
            convertView = layoutInflater.inflate(R.layout.row_event, parent, false);

            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.row_event_title_textview);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

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

    private static class ViewHolder {
        TextView nameTextView;
        TextView specificTextView;
        TextView locationTextView;
    }
}
