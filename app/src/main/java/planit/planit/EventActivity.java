package planit.planit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.animateaddition.AnimateAdditionAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;

import java.util.ArrayList;

import planit.planit.event.EventAdapter;
import planit.planit.event.EventItem;
import planit.planit.vendor.TypefaceSpan;


public class EventActivity extends Activity {
    int counter = 6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        SpannableString s = new SpannableString("Plannit");
        s.setSpan(new TypefaceSpan(this, "ShadowsIntoLight.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);
        ArrayList<EventItem> eventData = new ArrayList<EventItem>();
        eventData.add(new EventItem(3, "hi"));
        eventData.add(new EventItem(4, "hi"));
        eventData.add(new EventItem(1, "hi"));
        eventData.add(new EventItem(2, "hi"));
        eventData.add(new EventItem(6, "hi"));


        EventAdapter ea = new EventAdapter(this, R.layout.event_row_item, eventData);
        AlphaInAnimationAdapter aa = new AlphaInAnimationAdapter(ea);
        aa.setAbsListView((DynamicListView)findViewById(R.id.dynamiclistview));
        ((DynamicListView)findViewById(R.id.dynamiclistview)).setAdapter(ea);

        ((DynamicListView)findViewById(R.id.dynamiclistview)).enableDragAndDrop();
        ((DynamicListView)findViewById(R.id.dynamiclistview)).setDraggableManager(new TouchViewDraggableManager(R.id.eventItem));
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
        AnimateAdditionAdapter<EventItem> ea = (AnimateAdditionAdapter<EventItem>)((DynamicListView)findViewById(R.id.dynamiclistview)).getAdapter();
        EventItem ei = new EventItem(++counter, "yolo");
        ea.add(0,ei);
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
