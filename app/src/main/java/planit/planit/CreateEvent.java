package planit.planit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.animateaddition.AnimateAdditionAdapter;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import planit.planit.event.EventItem;


public class CreateEvent extends Activity {

    private static final String TAG = "PLANNIT";
    private String loggedInUser = "";
    private PostRequest postShit = null;
    public HttpClient client = new DefaultHttpClient();
    private String mTitle = "";
    private String mDescription = "";
    private String mLocation = "";
    private String mTime = "";
    private String mCreator = "";
    private EditText mTitleView;
    private EditText mDescriptionView;
    private EditText mTimeView;
    private EditText mLocationView;
    private String event_hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Set up the Event form.
        mTitleView = (EditText) findViewById(R.id.title);
        mDescriptionView = (EditText) findViewById(R.id.description);
        mTimeView = (EditText) findViewById(R.id.time);
        mLocationView = (EditText) findViewById(R.id.location);

        Button timButton = (Button) findViewById(R.id.timbutton);
        timButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
            }

            //Alex please fix this UI stuff on the next few lines
//            SpannableString s = new SpannableString("Plannit");
//            s.setSpan(new TypefaceSpan(this,"ArchitectsDaughter.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            ActionBar actionBar = getActionBar();
//            actionBar.setTitle(s);

        });
    }


    public void createEvent()
    {
        if(mTitleView.getText().toString() != null) {
            mTitle = mTitleView.getText().toString();
        }
        if(mDescriptionView.getText().toString() != null) {
            mDescription = mDescriptionView.getText().toString();
        }
        if(mTimeView.getText().toString() != null) {
            mTime = mTimeView.getText().toString();
        }
        if(mLocationView.getText().toString() != null) {
            mLocation = mLocationView.getText().toString();
        }

        add_edit_to_listview();
    }

    public void add_edit_to_listview() {

        postShit = new PostRequest(mTitle, mDescription, mLocation, mTime, loggedInUser);
        postShit.execute((Void) null);

        AnimateAdditionAdapter<EventItem> ea = (AnimateAdditionAdapter<EventItem>) ((DynamicListView) findViewById(R.id.dynamiclistview)).getAdapter();
        EventItem ei = new EventItem(event_hash, mTitle, mDescription, mTime, mLocation, loggedInUser);

        //Does adding it this way actually work?
        ea.add(0, ei);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("loggedInUser");
        }

        return super.onOptionsItemSelected(item);
    }

    class PostRequest extends AsyncTask<Void, Void, Boolean> {

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
                event_hash = client.execute(httppost, responseHandler);
                //Log.v(TAG, setServerString);

            } catch (Exception ex) {
                Log.v(TAG, ex.toString());
            }

            return !setServerString.equals("-1");
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Intent intent = new Intent(getApplicationContext(), EventActivity.class);
                intent.putExtra("loggedInUser", loggedInUser);
                startActivity(intent);
            } else {
                Log.v(TAG, "fail");
            }
        }
        }

}