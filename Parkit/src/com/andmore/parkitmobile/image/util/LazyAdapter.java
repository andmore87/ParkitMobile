package com.andmore.parkitmobile.image.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.andmore.parkitmobile.activity.R;
//import com.andmore.parkitmobile.activity.R;
import com.andmore.parkitmobile.entity.Centro_Comercial;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private List<Centro_Comercial> data;
    //private String[] data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
   public LazyAdapter(Activity a, List<Centro_Comercial> d)
    //public LazyAdapter(Activity a, String[] d)
     {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
       return data.size();
        //return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.item, null);
      
        TextView nameParking=(TextView)vi.findViewById(R.id.nameParking);
        TextView schedule = (TextView)vi.findViewById(R.id.schedule);
        ImageView image=(ImageView)vi.findViewById(R.id.image);
        
       Centro_Comercial ccObject =  new Centro_Comercial();
       ccObject = data.get(position);
        
        nameParking.setText(ccObject.getNombre()+": "+ccObject.getDireccion());
        schedule.setText("Horario: "+ccObject.getHorario());
       
        
        ProgressBar pb = (ProgressBar)vi.findViewById(R.id.progressBar1);
        imageLoader.DisplayImage(ccObject.getFoto(), image,pb);
        //imageLoader.DisplayImage(data[position], image,pb);
        return vi;
    }
}