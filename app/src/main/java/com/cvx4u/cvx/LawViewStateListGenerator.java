package com.cvx4u.cvx;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by laurenpiera on 1/31/18.
 */







public class LawViewStateListGenerator extends BaseAdapter {

    String[][] stateList={{"US","US House & Senate"},{"AL","Alabama"},{"AK","Alaska"},{"AR","Arkansas"},{"AZ","Arizona"},{"CA","California"},{"CO","Colorado"},{"CT","Connecticut"},{"DE","Delaware"},{"FL","Florida"},{"GA","Georgia"},{"HI","Hawaii"},{"ID","Idaho"},{"IL","Illinois"},{"IN","Indiana"},{"IA","Iowa"},{"KS","Kansas"},{"KY","Kentucky"},{"LA","Louisiana"},{"ME","Maine"},{"MD","Maryland"},{"MA","Massachusetts"},{"MI","Michigan"},{"MN","Minnesota"},{"MO","Missouri"},{"MS","Mississippi"},{"MT","Montana"},{"NC","North Carolina"},{"ND","North Dakota"},{"NE","Nebraska"},{"NH","New Hampshire"},{"NJ","New Jersey"},{"NM","New Mexico"},{"NV","Nevada"},{"NY","New York"},{"OH","Ohio"},{"OK","Oklahoma"},{"OR","Oregon"},{"PA","Pennsylvania"},{"RI","Rhode Island"},{"SC","South Carolina"},{"SD","South Dakota"},{"TN","Tennessee"},{"TX","Texas"},{"UT","Utah"},{"VA","Virginia"},{"VT","Vermont"},{"WA","Washington"},{"DC","Washington, DC"},{"WI","Wisconsin"},{"WV","West Virginia"},{"WY","Wyoming"}};

    private Context mContext;
    private LayoutInflater mInflater;

    String legiScanURL="https://api.legiscan.com/?key=438bd89d24f6339ea0adcc17fd06cd58&op=getMasterList&state=";

    public LawViewStateListGenerator(Context context)
    {
        mContext=context;
        //mElectionInfo=data;
        mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
}




    @Override
    public int getCount() {
        return 52;
    }

    @Override
    public Object getItem(int i) {
        return stateList[i][1];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View stateButtonRow=mInflater.inflate(R.layout.state_selection_button, viewGroup,false);

        Button stateButtonItem=(Button) stateButtonRow.findViewById(R.id.stateButton);

        stateButtonItem.setText(stateList[i][1]);

        stateButtonItem.setId(i);

        stateButtonItem.setOnClickListener(goToScreen);

        return stateButtonRow;
    }

    View.OnClickListener goToScreen=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent goToLegScreen=new Intent(mContext, LegislationDisplay.class);



            goToLegScreen.putExtra("state_name", stateList[view.getId()][0]);



            mContext.startActivity(goToLegScreen);

        }
    };
}
