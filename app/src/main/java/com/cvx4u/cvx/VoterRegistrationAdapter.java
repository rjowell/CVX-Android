package com.cvx4u.cvx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by laurenpiera on 1/26/18.
 */

public class VoterRegistrationAdapter extends BaseAdapter {

   private Context mContext;
   private LayoutInflater mInflater;
   private String[][] mElectionInfo;
   private VoterRegistrationInfo arrayGet=new VoterRegistrationInfo();
   String[][] voterInfo=arrayGet.voterInformation;

           public VoterRegistrationAdapter(Context context, String[][] data)
           {
               mContext=context;
               mElectionInfo=data;
               mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           }


    @Override
    public int getCount() {
        return mElectionInfo.length;
    }

    @Override
    public Object getItem(int i)
    {
        return mElectionInfo[i];
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

       View rowView=mInflater.inflate(R.layout.registration_info_cell, viewGroup, false);

       Button voterInfoButton=(Button) rowView.findViewById(R.id.voterInfo);

       Button onlineRegButton=(Button) rowView.findViewById(R.id.onlineReg);



       TextView stateName=(TextView) rowView.findViewById(R.id.stateName);

        //TextView linkInfo=(TextView) rowView.findViewById(R.id.linkInfo);

        String[] stateInfo=(String[]) getItem(i);

        stateName.setText(stateInfo[1]);

        voterInfoButton.setId(i);
        if(voterInfoButton.getId()==33)
        {
            voterInfoButton.setText("No Voter Registration");
        }

        if(voterInfo[i][3]==null)
        {
            onlineRegButton.setVisibility(View.INVISIBLE);
        }

        onlineRegButton.setId(100+i);


        //linkInfo.setText(stateInfo[2]);

        View.OnClickListener buttonProcess= new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {


                String url;
                Log.d("stateno",i+"");
                //Voter Info Button
                if(view.getId()<1)
                {
                    //North Dakota
                    if(i==33)
                    {

                    }

                    url=voterInfo[i][2];
                    Log.d("url",url);
                }
                //Online Registration Button
                else
                {


                    url=voterInfo[i][3];
                    Log.d("url-1",url);
                }

            }
        };

        voterInfoButton.setOnClickListener(buttonProcess);

        onlineRegButton.setOnClickListener(buttonProcess);



        return rowView;

    }
}
