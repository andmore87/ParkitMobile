package com.andmore.parkitmobile.image.util;

import com.andmore.parkitmobile.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridNodoAdapter extends BaseAdapter {
	
	 private Context mContext;
     private final String[] txtsNodoName;
     private final String[] txtsQtyNumber;
     private final int[] ImageId;
     
     
     public GridNodoAdapter(Context c,String[] txtsNodoName,String[] txtsQtyNumber,int[] ImageId ) {
         mContext = c;
         this.ImageId = ImageId;
         this.txtsNodoName = txtsNodoName;
         this.txtsQtyNumber = txtsQtyNumber;
     }
     
     
     @Override
     public int getCount() {
         // TODO Auto-generated method stub
         return txtsNodoName.length;
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
             grid = inflater.inflate(R.layout.fragment_section_nodo, null);
             TextView txtNodoName = (TextView) grid.findViewById(R.id.txtNodoName);
             TextView txtQtyNumber = (TextView) grid.findViewById(R.id.txtQtyNumber);
             ImageView imageNodo = (ImageView)grid.findViewById(R.id.imageNodo);
             txtNodoName.setText(txtsNodoName[position]);
             txtQtyNumber.setText(txtsQtyNumber[position]);
             imageNodo.setImageResource(ImageId[position]);
         } else {
             grid = (View) convertView;
         }

         return grid;
     }

}
