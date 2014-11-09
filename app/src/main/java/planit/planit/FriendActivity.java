package planit.planit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import planit.planit.friends.FriendAdapter;
import planit.planit.friends.FriendItem;
import planit.planit.vendor.TypefaceSpan;

public class FriendActivity extends Activity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "StaggeredGridActivity";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";

    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private FriendAdapter mAdapter;
    private ArrayList<FriendItem> friendData;
    private ArrayList<Integer> nctx;
    public HttpClient client = new DefaultHttpClient();
    public String loggedInUser = "";
    public String friendDict = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        mGridView = (StaggeredGridView) findViewById(R.id.friend_grid_view);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loggedInUser = extras.getString("loggedInUser");
        }

        LayoutInflater layoutInflater = getLayoutInflater();

        View header = layoutInflater.inflate(R.layout.friend_header_footer, null);
        View footer = layoutInflater.inflate(R.layout.friend_header_footer, null);
        TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
        txtHeaderTitle.setText("Friends List");

        SpannableString s = new SpannableString("Plannit");
        s.setSpan(new TypefaceSpan(this, "ArchitectsDaughter.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);

        mGridView.addHeaderView(header);
        mGridView.addFooterView(footer);
        mAdapter = new FriendAdapter(this, R.id.friend_grid_view);
        nctx = new ArrayList<Integer>();


//        if (mData == null) {
//            mData = new ArrayList<FriendItem>();
//            nctx = new ArrayList<Integer>();
//            mData.add(new FriendItem(0, "hi", "http://i.imgur.com/nBoIRaJ.jpg"));
//            nctx.add(0);
//            mData.add(new FriendItem(1, "hi", "http://i.imgur.com/mCGDR69.jpg"));
//            nctx.add(0);
//            mData.add(new FriendItem(2, "hi", "http://i.imgur.com/VwLNu.jpg"));
//            nctx.add(0);
//            mData.add(new FriendItem(3, "hi", "http://i.imgur.com/NSL24Vt.jpg"));
//            nctx.add(0);
//            mData.add(new FriendItem(3, "hi", "http://i.imgur.com/uAdkZjL.jpg"));
//            nctx.add(0);
//            mData.add(new FriendItem(3, "hi", "http://i.imgur.com/DvpvklR.png"));
//            nctx.add(0);
//            mData.add(new FriendItem(3, "hi", "http://i.imgur.com/EvYLq.jpg"));
//            nctx.add(0);
//            nctx.add(0);
//        }

        GetRequest getRequest = new GetRequest(loggedInUser, this);
        getRequest.execute((Void) null);

    }

    private ArrayList<FriendItem> jsonParser(String jsonThing) throws JSONException {

        JSONObject obj = new JSONObject(jsonThing);
        JSONArray arr = obj.getJSONArray("" + 0);
        ArrayList<FriendItem> friendData = new ArrayList<FriendItem>();

        for (int i = 0; i < arr.length(); i++) {
            String email = arr.getJSONObject(i).getString("email");
            String name = arr.getJSONObject(i).getString("name");
            int friend_hash = Integer.parseInt(arr.getJSONObject(i).getString("user_hash"));
            String imgURI = arr.getJSONObject(i).getString("img_uri");

            friendData.add(new FriendItem(friend_hash, email, name, imgURI));
        }
        return friendData;
    }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.menu_friend, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_search) {
                return true;
            }
            else if (id == R.id.action_accept_friend) {
                Intent intent = new Intent(getApplicationContext(), CreateEvent.class);
                intent.putExtra("loggedInUser", loggedInUser);
                intent.putExtra("ReturnFromFriend", true);

                Bundle extras = getIntent().getExtras();
                intent.putExtra("time", extras.getString("time"));
                intent.putExtra("location", extras.getString("location"));
                intent.putExtra("description", extras.getString("description"));
                intent.putExtra("title", extras.getString("title"));
                ArrayList<String> inviteEmails = new ArrayList<String>();

                for(int i = 1; i < nctx.size();i++)
                {
                    if(nctx.get(i) % 2 == 1)
                    {
                        inviteEmails.add(mAdapter.getItem(i-1).email);
                    }
                }

                intent.putStringArrayListExtra("list",inviteEmails);
                startActivity(intent);
            }
            return true;
        }

        @Override
        protected void onSaveInstanceState ( final Bundle outState){
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onScrollStateChanged ( final AbsListView view, final int scrollState){
            Log.d(TAG, "onScrollStateChanged:" + scrollState);
        }

        @Override
        public void onScroll ( final AbsListView view, final int firstVisibleItem,
        final int visibleItemCount, final int totalItemCount){
            Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                    " visibleItemCount:" + visibleItemCount +
                    " totalItemCount:" + totalItemCount);
            // our handling

            if (!mHasRequestedMore) {
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if (lastInScreen >= totalItemCount) {
                    Log.d(TAG, "onScroll lastInScreen - so load more");
                    mHasRequestedMore = true;
                }
            }
        }

        @Override
        public void onItemClick (AdapterView < ? > adapterView, View view,int position, long id){
            //Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
            //adapterView.getItemAtPosition(position);
            if (view.getTag() != null) {
                if (position <= nctx.size()) {
                    FriendAdapter.ViewHolder vh = (FriendAdapter.ViewHolder) view.getTag();

                    if (nctx.get(position) % 2 == 0) {
                        vh.txtLineTwo.setBackgroundColor(Color.rgb(0, 158, 96));
                        nctx.set(position, nctx.get(position) + 1);
                    } else {
                        vh.txtLineTwo.setBackgroundColor(Color.rgb(0, 121, 255));
                        nctx.set(position, nctx.get(position) + 1);
                    }
                }
            }
        }

        @Override
        public boolean onItemLongClick (AdapterView < ? > parent, View view,int position, long id)
        {
            Toast.makeText(this, "Item Long Clicked: " + position, Toast.LENGTH_SHORT).show();
            return true;
        }

        class GetRequest extends AsyncTask<Void, Void, Boolean> {
            private final String mEmail;
            private FriendActivity fa = null;

            ArrayList<FriendItem> friendData = new ArrayList<FriendItem>();

            GetRequest(String email,FriendActivity fa) {
                mEmail = email;
                this.fa = fa;
            }

            protected Boolean doInBackground(Void... params) {

                try {
                    //Encode
                    //String loginValue = URLEncoder.encode(mEmailView.toString(), "UTF-8");

                    //Log.v(TAG, mEmail);
                    String newURL = "http://54.68.34.231:8888/" + "get_friends?email=" + mEmail; //Nick made me hardcode LOL
                    Log.v(TAG, mEmail);
                    //Log.v(TAG, newURL);
                    HttpGet httpget = new HttpGet(newURL);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    friendDict = client.execute(httpget, responseHandler);
                    Log.v(TAG, friendDict);

                    try {
                        friendData = jsonParser(friendDict);
                    } catch (JSONException e) {
                        //e.printStackTrace();
                    }

                } catch (Exception ex) {
                    //Log.v(TAG, ex.toString());
                }

                if (!friendDict.equals(null)) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(final Boolean success) {

                if (success) {
                    try {
                        //pad start and end for funsies.
                        nctx.add(0);
                        for (FriendItem data : friendData) {
                            mAdapter.add(data);
                            nctx.add(0);
                        }
                        nctx.add(0);

                        mGridView.setAdapter(mAdapter);
                        mGridView.setOnScrollListener(fa);
                        mGridView.setOnItemClickListener(fa);
                        mGridView.setOnItemLongClickListener(fa);

                       // ((DynamicHeightTextView) findViewById(R.id.txt_line2)).setText();
                    }
                    catch (Exception e) {
                        //Log.v(TAG, e.toString());
                    }
                }

            }
        }

    }