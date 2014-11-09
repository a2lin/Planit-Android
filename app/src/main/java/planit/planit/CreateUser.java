package planit.planit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;

import planit.planit.vendor.TypefaceSpan;


public class CreateUser extends Activity {

    private static final String TAG = "PLANNIT";
    protected String loggedInUser = "";
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
    private String image_path;
    private ArrayList<String> attendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Bundle b = getIntent().getExtras();
        if(b!= null) {
            this.loggedInUser = b.getString("loggedInUser");
        }
        // Set up the Event form.
        mTitleView = (EditText) findViewById(R.id.title);
        mDescriptionView = (EditText) findViewById(R.id.description);
        mTimeView = (EditText) findViewById(R.id.time);
        mLocationView = (EditText) findViewById(R.id.location);
        image_path = "";
        Button timButton = (Button) findViewById(R.id.timbutton);
        timButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
            }
        });
        SpannableString s = new SpannableString("Plannit");
        s.setSpan(new TypefaceSpan(this, "ArchitectsDaughter.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);


    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if(extras.getString("title") != null)
        {
            mTitleView.setText(extras.getString("title"));
        }
        if(extras.getString("description") != null)
        {
            mDescriptionView.setText(extras.getString("description"));
        }
        if(extras.getString("time") != null)
        {
            mTimeView.setText(extras.getString("time"));
        }
        if(extras.getString("location") != null)
        {
            mLocationView.setText(extras.getString("location"));
        }
        if(extras.getStringArrayList("list") != null)
        {
            this.attendees = extras.getStringArrayList("list");
        }
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

        String tempUser = loggedInUser;
        if (attendees != null) {
            for(String s : attendees)
            {
                tempUser += "___" + s;
            }
        }
        Log.v("WADDAFAK", tempUser);
        postShit = new PostRequest(mTitle, mDescription, mLocation, mTime, tempUser);
        postShit.execute((Void) null);



        //AnimateAdditionAdapter<EventItem> ea = (AnimateAdditionAdapter<EventItem>) ((DynamicListView) findViewById(R.id.dynamiclistview)).getAdapter();
        //EventItem ei = new EventItem(event_hash, mTitle, mDescription, mTime, mLocation, loggedInUser, image_path);

        //Does adding it this way actually work?
        //ea.add(0, ei);
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

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("loggedInUser");
        }

        if (id == R.id.action_add_friends) {
            Intent intent = new Intent(getApplicationContext(), FriendActivity.class);
            intent.putExtra("loggedInUser", loggedInUser);

            // add a tidbit to disable the check mark view if not on adding friends path
            intent.putExtra("expectingReturn", true);

            // save information typed in to fill back into the EditText
            intent.putExtra("time", mTimeView.getText().toString());
            intent.putExtra("location", mLocationView.getText().toString());
            intent.putExtra("description", mDescriptionView.getText().toString());
            intent.putExtra("title", mTitleView.getText().toString());
            startActivity(intent);
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
                String newURL = "http://54.68.34.231:8888/" + "create_event?title=" + mTitle + "&description=" + mDescription + "&location=" + mLocation + "&time=" + mTime + "&creator=" + mCreator; //Nick made me hardcode LOL
                Log.v("WADDAFAKO", newURL);
                HttpPost httppost = new HttpPost(newURL);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                event_hash = client.execute(httppost, responseHandler);
                Log.v("WADDAFAK", newURL);

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
