package com.andmore.parkitmobile.image.util;

import com.andmore.parkitmobile.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridLocalInfoAdapter extends BaseAdapter {
	
	 private Context mContext;
     private final String[] txtsLocalName;
     private final String[] txtsLocalNumber;
     private final int[] ImageId;
     
     
     public GridLocalInfoAdapter(Context c,String[] txtsLocalName,String[] txtsLocalNumber,int[] ImageId ) {
         mContext = c;
         this.ImageId = ImageId;
         this.txtsLocalName = txtsLocalName;
         this.txtsLocalNumber = txtsLocalNumber;
     }
     
     
     @Override
     public int getCount() {
         // TODO Auto-generated method stub
         return txtsLocalName.length;
     }

     @Override
     public Object getItem(int position) {
         // TODO Auto-generated method stub
         return null;
     }

     @Override
     public long getItemId(int position) {
         // TODO Auto-generated method stub
         return 0;
     }
     
     
     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
         // TODO Auto-generated method stub
         View grid;
         LayoutInflater inflater = (LayoutInflater) mContext
             .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         if (convertView == null) {

             grid = new View(mContext);
             grid = inflater.inflate(R.layout.fragment_section_localinfo, null);
             TextView txtLocalName = (TextView) grid.findViewById(R.id.txtLocalName);
             
             ImageView imageLocalInfo = (ImageView)grid.findViewById(R.id.imageLocalInfo);
             txtLocalName.setText(txtsLocalNumber[position]+" "+txtsLocalName[position]);
             imageLocalInfo.setImageResource(ImageId[position]);
         } else {
             grid = (View) convertView;
         }

         return grid;
     }

}
