package com.example.ogadrive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Footkar on 7/22/2015.
 */
public class NavigationAdapter extends ArrayAdapter {

    String list[] ;
    public NavigationAdapter(Context context, String[] list) {
        super(context, android.R.id.text1);
    this.list = list;
    }




    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.length;
    }

    @Override
    public String getItem(int arg0) {
        // TODO Auto-generated method stub
        return list[arg0];
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View row, ViewGroup arg2) {
        // TODO Auto-generated method stub
        Holder holder ;
        if(row == null) {
            LayoutInflater layoutInflater=LayoutInflater.from(getContext());
            row=layoutInflater.inflate(R.layout.navigation_row,null,false);
            holder = new Holder();
            holder.txtViewHeading =  (TextView) row.findViewById(R.id.txtHeading);
            holder.imgLogo =  (ImageView) row.findViewById(R.id.imgLogo);
            holder.lytLinear = (LinearLayout) row.findViewById(R.id.lytLinear);
            row.setTag(holder);

        } else {
            holder = (Holder) row.getTag();
        }



        holder.txtViewHeading.setText(list[position]);
        if(position == 0) {
            holder.imgLogo.setImageResource(R.drawable.user_profile);
            holder.lytLinear.setPadding(10, 40, 10, 60);
        } else if(position == 1) {
            holder.imgLogo.setImageResource(R.drawable.home);

        } else if(position == 2) {
            holder.imgLogo.setImageResource(R.drawable.book_vehicle);

        } else if(position == 4) {
        holder.imgLogo.setImageResource(R.drawable.contactus);

    }  else if(position == 5) {
            holder.imgLogo.setImageResource(R.drawable.support);

        }  else if(position == 6) {
        holder.imgLogo.setImageResource(R.drawable.about_us);

    } else {
            holder.imgLogo.setImageResource(R.drawable.history_icon);
        }





        return row;
    }

    static class Holder {
        TextView txtViewHeading;
        ImageView imgLogo;
        LinearLayout lytLinear;

    }


}
