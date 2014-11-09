package planit.planit.event;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;

import java.util.ArrayList;

import planit.planit.R;

/**
 * Created by alexanderlin on 11/8/14.
 */
public class EventAdapter extends ExpandableListItemAdapter<EventItem> {

    Context mContext;
    int layoutResourceId;
    ArrayList<EventItem> data = null;

    public EventAdapter(Context mContext, int layoutResourceId, ArrayList<EventItem> data) {

       super(mContext, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public long getItemId(final int position) {
        return Long.parseLong(data.get(position).eventHash);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        EventItem eventItem = data.get(position);

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.eventItem);
        textViewItem.setText(eventItem.eventName);

        return convertView;

    }
*/
    @NonNull
    @Override
    public View getTitleView(int i, @Nullable View view, @NonNull ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, viewGroup, false);
        }
        EventItem eventItem = data.get(i);
        TextView textViewItem = (TextView) view.findViewById(R.id.eventItem);
        textViewItem.setText(eventItem.title);
        

        return view;
    }

    @NonNull
    @Override
    public View getContentView(int i, @Nullable View view, @NonNull ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = inflater.inflate(layoutResourceId, viewGroup, false);
        }
        EventItem eventItem = data.get(i);
        TextView textViewItem = (TextView) view.findViewById(R.id.eventItem);
        textViewItem.setText(eventItem.title);

        return view;
    }

}
