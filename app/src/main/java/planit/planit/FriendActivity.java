package planit.planit;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
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
    private ArrayList<FriendItem> mData;
    private ArrayList<Integer> nctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        mGridView = (StaggeredGridView) findViewById(R.id.friend_grid_view);

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


        if (mData == null) {
            mData = new ArrayList<FriendItem>();
            nctx = new ArrayList<Integer>();
            mData.add(new FriendItem(0, "hi", "http://i.imgur.com/nBoIRaJ.jpg"));
            nctx.add(0);
            mData.add(new FriendItem(1, "hi", "http://i.imgur.com/mCGDR69.jpg"));
            nctx.add(0);
            mData.add(new FriendItem(2, "hi", "http://i.imgur.com/VwLNu.jpg"));
            nctx.add(0);
            mData.add(new FriendItem(3, "hi", "http://i.imgur.com/NSL24Vt.jpg"));
            nctx.add(0);
            mData.add(new FriendItem(3, "hi", "http://i.imgur.com/uAdkZjL.jpg"));
            nctx.add(0);
            mData.add(new FriendItem(3, "hi", "http://i.imgur.com/DvpvklR.png"));
            nctx.add(0);
            mData.add(new FriendItem(3, "hi", "http://i.imgur.com/EvYLq.jpg"));
            nctx.add(0);
        }

        for (FriendItem data : mData) {
            mAdapter.add(data);
        }

        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
        mGridView.setOnItemLongClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
        adapterView.getItemAtPosition(position);
        if(view.getTag() != null)
        {
            FriendAdapter.ViewHolder vh = (FriendAdapter.ViewHolder) view.getTag();
            if (nctx.get(position)%2 == 0) {
                vh.txtLineTwo.setBackgroundColor(Color.rgb(0, 158, 96));
                nctx.set(position, nctx.get(position)+1);
            }
            else {
                vh.txtLineTwo.setBackgroundColor(Color.rgb(0, 121, 255));
                nctx.set(position, nctx.get(position)+1);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        Toast.makeText(this, "Item Long Clicked: " + position, Toast.LENGTH_SHORT).show();
        return true;
    }


}