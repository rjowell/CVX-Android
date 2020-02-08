package com.cvx4u.cvx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cvx4u.cvx.FollowItem;
import com.cvx4u.cvx.R;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by laurenpiera on 3/14/18.
 */

public class HomeScreenGenerator extends BaseAdapter {


    private Context localContext;
    private LayoutInflater localInflater;
    private ArrayList<FollowItem> localDataArray;


    public HomeScreenGenerator(ArrayList<FollowItem> dataInput, Context thisContext)
    {
        localContext=thisContext;
        localInflater=(LayoutInflater) localContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        localDataArray=dataInput;

    }



    @Override
    public int getCount() {
        if (localDataArray.size() == 0) {
            return 1;
        } else {
            return localDataArray.size();
        }
    }

    @Override
    public Object getItem(int i)
    {
        if(localDataArray.size()==0)
        {
            return null;
        }
        else {
            return localDataArray.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceType")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View mainScreenRow;

        if(localDataArray.size()==0)
        {
            mainScreenRow=localInflater.inflate(R.layout.main_screen_intro_cell, viewGroup, false);
        }
        else
        {
        FollowItem currentItem=(FollowItem) localDataArray.get(i);

        if(currentItem.isLegislation==false)
        {
            mainScreenRow=localInflater.inflate(R.layout.main_screen_election_cell, viewGroup, false);

            TextView elecName=mainScreenRow.findViewById(R.id.electionName);
            TextView stateName=mainScreenRow.findViewById(R.id.stateName);
            TextView stageName=mainScreenRow.findViewById(R.id.stageName);
            TextView elecDate=mainScreenRow.findViewById(R.id.electionDate);
            Button unfollowButton=mainScreenRow.findViewById(R.id.unfollowButton);
            unfollowButton.setOnClickListener(buttonProcess);
            unfollowButton.setId(25);
            unfollowButton.setTag(currentItem.electionName+"_"+currentItem.electionStage);
            elecName.setText(currentItem.electionName);
            stateName.setText(currentItem.electionStateAbbrev);
            stageName.setText(currentItem.electionStage);
            elecDate.setText(currentItem.electionDate);
        }


        else {


            //FollowItem currentItem = (FollowItem) localDataArray.get(i);


            mainScreenRow = localInflater.inflate(R.layout.main_screen_legislation_cell, viewGroup, false);
            TextView stateName = (TextView) mainScreenRow.findViewById(R.id.stateName);
            stateName.setText((CharSequence) currentItem.electionStateAbbrev);
            TextView billNumber = (TextView) mainScreenRow.findViewById(R.id.billNumber);
            billNumber.setText((CharSequence) currentItem.billNumber);
            TextView billTitle = (TextView) mainScreenRow.findViewById(R.id.billTitle);
            billTitle.setText((CharSequence) currentItem.billTitle);
            TextView lastStatusDate = (TextView) mainScreenRow.findViewById(R.id.billStatusDate);
            Button fullText = (Button) mainScreenRow.findViewById(R.id.homeScreenFullText);
            Button unfollow = (Button) mainScreenRow.findViewById(R.id.homeScreenUnfollow);
            fullText.setTag(currentItem.billId);
            unfollow.setTag(currentItem.billId);
            fullText.setId(0);
            unfollow.setId(1);
            fullText.setOnClickListener(buttonProcess);
            unfollow.setOnClickListener(buttonProcess);
            lastStatusDate.setText("Current Status As Of: " + currentItem.lastStatusDate);
            TextView lastStatus = (TextView) mainScreenRow.findViewById(R.id.billLatestStatus);
            lastStatus.setText(currentItem.lastStatusChange);


        }}


        return mainScreenRow;
    }

    public void saveFollowList()
    {
        try {
            FileOutputStream fos = localContext.openFileOutput("followListData", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);


            oos.writeObject(localDataArray);
            oos.close();
            fos.close();
        }
        catch(Exception iiee)
        {
            System.out.println(iiee.toString());
        }
        System.out.println("Save length is");
        System.out.println(localDataArray.size());
    }




    View.OnClickListener buttonProcess=new View.OnClickListener()
    {

        @SuppressLint("ResourceType")
        @Override
        public void onClick(View view) {

            if(view.getId()==0)
            {
                new OpenBillLink(view.getTag().toString(), localContext).execute();
            }
            else if(view.getId()==25)
            {
                for(FollowItem thisItem:localDataArray)
                {
                    if((thisItem.electionName+"_"+thisItem.electionStage).compareTo((String) view.getTag()) == 0)
                    {
                        localDataArray.remove(thisItem);
                        notifyDataSetChanged();
                    }
                }
            }
            else
            {
                for(FollowItem thisItem:localDataArray)
                {
                    if(thisItem.billId.compareTo(view.getTag().toString())==0)
                    {
                        localDataArray.remove(thisItem);
                        notifyDataSetChanged();
                    }
                }
            }

        }
    };
}
