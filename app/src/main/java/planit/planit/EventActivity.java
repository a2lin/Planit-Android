package planit.planit;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
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
        s.setSpan(new TypefaceSpan(this, "ArchitectsDaughter.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);

        Log.v(TAG, "pass1");
        GetRequest getRequest = new GetRequest(loggedInUser, this);
        getRequest.execute((Void) null);

//        Log.v(TAG, "pass2");
//        Button addEventButton = (Button) findViewById(R.id.action_compose);
//        addEventButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            Log.v(TAG, "pass3");
//            addEvent();
//            }
//        });

//        eventData.add(new EventItem(3, "hi"));
//        eventData.add(new EventItem(4, "hi"));
//        eventData.add(new EventItem(1, "hi"));
//        eventData.add(new EventItem(2, "hi"));
//        eventData.add(new EventItem(6, "hi"));
    }

    public void addEvent(){
        Intent intent = new Intent(getApplicationContext(), CreateEvent.class);
        intent.putExtra("loggedInUser", loggedInUser);
        startActivity(intent);
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
            String image = arr.getJSONObject(i).getString("image_path");
            eventData.add(new EventItem(eventHash, title, description, time, location, creator, image));
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
        else if (id == R.id.action_compose) {
            addEvent();
        }

        return super.onOptionsItemSelected(item);
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
                    Log.v("WTF", eventData.toString());
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
}
