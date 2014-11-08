package planit.planit;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import java.util.ArrayList;

import planit.planit.event.EventAdapter;
import planit.planit.event.EventItem;


public class EventActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        ArrayList<EventItem> eventData = new ArrayList<EventItem>();
        eventData.add(new EventItem(3, "hi"));
        eventData.add(new EventItem(4, "hi"));
        eventData.add(new EventItem(1, "hi"));
        eventData.add(new EventItem(2, "hi"));
        eventData.add(new EventItem(6, "hi"));


        EventAdapter ea = new EventAdapter(this, R.layout.event_row_item, eventData);
        ((DynamicListView)findViewById(R.id.dynamiclistview)).setAdapter(ea);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        if (id == R.id.action_compose){
            add_edit_to_listview();
        }

        return super.onOptionsItemSelected(item);
    }

    private void add_edit_to_listview(){
        EventAdapter ea = (EventAdapter)((DynamicListView)findViewById(R.id.dynamiclistview)).getAdapter();
        EventItem ei = new EventItem(5, "yolo");
        ea.add(ei);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event, container, false);
            return rootView;
        }
    }
}
