package planit.planit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.animateaddition.AnimateAdditionAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import planit.planit.event.EventAdapter;
import planit.planit.event.EventItem;
import planit.planit.vendor.TypefaceSpan;

//simple shit
public class EventActivity extends Activity {
    private static final String TAG = "PLANNIT";
    private String loggedInUser = "";
    private PostRequest postShit = null;
    public String eventDict = "";
    public HttpClient client = new DefaultHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("loggedInUser");
        }

        SpannableString s = new SpannableString("Plannit");
        s.setSpan(new TypefaceSpan(this, "ShadowsIntoLight.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);
        GetRequest getRequest = new GetRequest(loggedInUser, this);
        getRequest.execute((Void) null);

//        eventData.add(new EventItem(3, "hi"));
//        eventData.add(new EventItem(4, "hi"));
//        eventData.add(new EventItem(1, "hi"));
//        eventData.add(new EventItem(2, "hi"));
//        eventData.add(new EventItem(6, "hi"));



    }


    private ArrayList<EventItem> jsonParser(String jsonThing) throws JSONException {

        JSONObject obj = new JSONObject(jsonThing);
        JSONArray arr = obj.getJSONArray("" + 0);
        ArrayList<EventItem> eventData = new ArrayList<EventItem>();

        for (int i = 0; i < arr.length(); i++) {
            String eventHash = arr.getJSONObject(i).getString("event_hash");
            String title = arr.getJSONObject(i).getString("title");
            String description = arr.getJSONObject(i).getString("description");
            String time = arr.getJSONObject(i).getString("time");
            String location = arr.getJSONObject(i).getString("location");
            String creator = arr.getJSONObject(i).getString("creator");
            eventData.add(new EventItem(eventHash, title, description, time, location, creator));
        }
        return eventData;
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

        if (id == R.id.action_compose) {
            add_edit_to_listview();
        }

        return super.onOptionsItemSelected(item);
    }

    private void add_edit_to_listview() {

        if (postShit != null) {
            return;
        }

        AnimateAdditionAdapter<EventItem> ea = (AnimateAdditionAdapter<EventItem>) ((DynamicListView) findViewById(R.id.dynamiclistview)).getAdapter();
        EventItem ei = new EventItem("ajlfasd", "yolo", "adsjkfl", "adsjkfl", "adsjfkl", loggedInUser);
        ea.add(0, ei);

        postShit = new PostRequest("", "yolo", "", "", loggedInUser);
        postShit.execute((Void) null);
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


    class GetRequest extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private Context ctx;
        ArrayList<EventItem> eventData = new ArrayList<EventItem>();

        GetRequest(String email, Context ctx) {
            mEmail = email;
            this.ctx = ctx;
        }

        protected Boolean doInBackground(Void... params) {

            try {
                //Encode
                //String loginValue = URLEncoder.encode(mEmailView.toString(), "UTF-8");

                //Log.v(TAG, mEmail);
                String newURL = "http://192.241.239.59:8888/" + "get_events?email=" + mEmail; //Nick made me hardcode LOL
                //Log.v(TAG, newURL);
                HttpGet httpget = new HttpGet(newURL);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                eventDict = client.execute(httpget, responseHandler);
                Log.v(TAG, eventDict);

                try {
                    eventData = jsonParser(eventDict);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception ex) {
                Log.v(TAG, ex.toString());
            }

            if (!eventDict.equals(null)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                try{
                    EventAdapter ea = new EventAdapter(ctx, R.layout.event_row_item, eventData);
                    AlphaInAnimationAdapter aa = new AlphaInAnimationAdapter(ea);
                    aa.setAbsListView((DynamicListView) findViewById(R.id.dynamiclistview));
                    ((DynamicListView) findViewById(R.id.dynamiclistview)).setAdapter(ea);

                    ((DynamicListView) findViewById(R.id.dynamiclistview)).enableDragAndDrop();
                    ((DynamicListView) findViewById(R.id.dynamiclistview)).setDraggableManager(new TouchViewDraggableManager(R.id.eventItem));
                    Log.v(TAG, "Pass1");
                }
                catch (Exception ex)
                {
                    Log.v(TAG, ex.toString());
                }
            } else {
                Log.v(TAG, "fail");
            }
        }
    }

    class PostRequest extends AsyncTask<Void, Void, Boolean> {
        private final String mTitle;
        private final String mDescription;
        private final String mLocation;
        private final String mTime;
        private final String mCreator;

        PostRequest(String title, String description, String location, String time, String creator) {
            mTitle = title;
            mDescription = description;
            mLocation = location;
            mTime = time;
            mCreator = creator;
        }

        protected Boolean doInBackground(Void... params) {

            String setServerString = "";

            try {
                //Encode
                //String loginValue = URLEncoder.encode(mEmailView.toString(), "UTF-8");

                //Log.v(TAG, mEmail);
                String newURL = "http://192.241.239.59:8888/" + "create_event?title=" + mTitle + "&description:" + mDescription + "&location" + mLocation + "&time" + mTime + "&creator" + mCreator; //Nick made me hardcode LOL
                //Log.v(TAG, newURL);
                HttpPost httppost = new HttpPost(newURL);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                setServerString = client.execute(httppost, responseHandler);
                //Log.v(TAG, setServerString);

            } catch (Exception ex) {
                Log.v(TAG, ex.toString());
            }

            if (!setServerString.equals("-1")) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                //later might add event added
            } else {
                Log.v(TAG, "fail");
            }
        }
    }
}