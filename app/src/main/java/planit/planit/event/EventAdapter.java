package planit.planit.event;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import planit.planit.R;

/**
 * Created by alexanderlin on 11/8/14.
 */
public class EventAdapter extends ArrayAdapter<EventItem> {

    Context mContext;
    int layoutResourceId;
    ArrayList<EventItem> data = null;

    public EventAdapter(Context mContext, int layoutResourceId, ArrayList<EventItem> data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

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
        textViewItem.setTag(eventItem.eventID);

        return convertView;

    }

}
