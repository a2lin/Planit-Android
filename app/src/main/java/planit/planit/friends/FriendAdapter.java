package planit.planit.friends;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;
import com.squareup.picasso.Picasso;

import java.util.Random;

import planit.planit.R;

/**
 * Created by alexanderlin on 11/8/14.
 */
public class FriendAdapter extends ArrayAdapter<FriendItem> {

    private static final String TAG = "SampleAdapter";

    public static class ViewHolder {
        public DynamicHeightImageView txtLineOne;
        public DynamicHeightTextView txtLineTwo;
    }
    private Random rand;

    private final LayoutInflater mLayoutInflater;
    //private final Random mRandom;
    //private final ArrayList<Integer> mBackgroundColors;
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    private Context ctx;

    public FriendAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        //mRandom = new Random();
        //mBackgroundColors = new ArrayList<Integer>();
        //mBackgroundColors.add(R.color.sky_blue);
        this.ctx = context;
        this.rand = new Random();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.friend_item, parent, false);
            vh = new ViewHolder();
            vh.txtLineOne = (DynamicHeightImageView) convertView.findViewById(R.id.txt_line1);
            vh.txtLineTwo = (DynamicHeightTextView) convertView.findViewById(R.id.txt_line2);
            convertView.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        double positionHeight = getPositionRatio(position);
        //int backgroundIndex = position >= mBackgroundColors.size() ?
         //       position % mBackgroundColors.size() : position;

        //convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

        Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

        vh.txtLineOne.setHeightRatio(positionHeight);
        Picasso.with(ctx).load(getItem(position).imgURI).resize(200, 200 ).placeholder(R.drawable.plannit).into(vh.txtLineOne);
        vh.txtLineTwo.setText(getItem(position).friendName);

        return convertView;
    }

    private double getPositionRatio(final int position) {
        double ratio = 1;//sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        /*
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }*/
        return ratio;
    }
/*
    private double getRandomHeightRatio() {
        return (new Random().nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }*/
}