package com.technotech.technotechapplication.DashboardActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.technotech.technotechapplication.R;

import java.util.ArrayList;

class DashboardListAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final SharedPreferences common_mypref;
    ArrayList<String> myArray;
    DashboardActivity mcontext;
    boolean isEditing = false;



    public DashboardListAdapter(DashboardActivity contextOfApplication) {
        mcontext = contextOfApplication;
      //  myArray = array;
        common_mypref = contextOfApplication.getSharedPreferences(
                "user", 0);
        mInflater = (LayoutInflater) contextOfApplication
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;

        holder = new ViewHolder();
        view = mInflater.inflate(R.layout.dashboard_item_list_layout, null);

        view.setMinimumHeight(viewGroup.getMeasuredHeight());
        holder.ll = (LinearLayout) view.findViewById(R.id.ll);
        holder.ll.setMinimumHeight(viewGroup.getMeasuredHeight());

        holder.txt_follow = (TextView) view.findViewById(R.id.txt_follow);
        holder.txt_share = (TextView) view.findViewById(R.id.txt_share);
        holder.txt_name = (TextView) view.findViewById(R.id.txt_name);
        holder.imageView = (ImageView) view.findViewById(R.id.ImageView_image);
        holder.img_icon = (ImageView) view.findViewById(R.id.set_icon);
        holder.txt_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mcontext,"Following this vendor",Toast.LENGTH_LONG).show();
            }
        });
        holder.txt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mcontext,"Sharing this vendor",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    public static class ViewHolder {
        public TextView txt_follow,txt_share,txt_name;
        public ImageView imageView,img_icon;
        LinearLayout ll;
    }
}
