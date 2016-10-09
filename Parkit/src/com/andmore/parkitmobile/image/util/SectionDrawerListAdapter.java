package com.andmore.parkitmobile.image.util;

import java.util.ArrayList;

import com.andmore.parkitmobile.activity.R;
import com.andmore.parkitmobile.entity.SectionDrawerItem;

 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SectionDrawerListAdapter extends BaseAdapter  {
	
	private Context context;
    private ArrayList<SectionDrawerItem> sectionDrawerItems;
     
    public SectionDrawerListAdapter(Context context, ArrayList<SectionDrawerItem> navDrawerItems){
        this.context = context;
        this.sectionDrawerItems = navDrawerItems;
    }
    
    @Override
    public int getCount() {
        return sectionDrawerItems.size();
    }
 
    @Override
    public Object getItem(int position) {       
        return sectionDrawerItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
          
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
          
        imgIcon.setImageResource(sectionDrawerItems.get(position).getIcon());        
        txtTitle.setText(sectionDrawerItems.get(position).getTitle());
         
        // displaying count
        // check whether it set visible or not
        if(sectionDrawerItems.get(position).getCounterVisibility()){
            txtCount.setText(sectionDrawerItems.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }
         
        return convertView;
    }

}
