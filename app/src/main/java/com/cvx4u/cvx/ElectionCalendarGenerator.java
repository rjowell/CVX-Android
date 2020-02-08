package com.cvx4u.cvx;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by laurenpiera on 3/2/18.
 */

public class ElectionCalendarGenerator extends BaseAdapter {


    ArrayList<ArrayList<CalendarItem>> currentData;
    private Context thisContext;
    private LayoutInflater thisInflater;
    ArrayList<FollowItem> currentFollowList;

    String[][] months={{"1","January"},{"2","February"},{"3","March"},{"4","April"},{"5","May"},{"6","June"},{"7","July"},{"8","August"},{"9","September"},{"10","October"},{"11","November"},{"12","December"}};

    ArrayList<ArrayList<String>> calendarDisplayData;

    public ElectionCalendarGenerator(ArrayList<ArrayList<CalendarItem>> calendarDataIn, Context context)
    {
        currentData=calendarDataIn;
        thisContext=context;
        thisInflater=(LayoutInflater) thisContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        System.out.println("There are items: "+currentData.size());

        calendarDisplayData=new ArrayList<>();



        for(int index=1;index<13;index++)
        {
            ArrayList<String> thisThingHere=new ArrayList<>();
            thisThingHere.add(months[index-1][1]);
            calendarDisplayData.add(thisThingHere);

            if(currentData.get(index-1).size() != 0)
            {
                ArrayList<CalendarItem> currentCalItem=currentData.get(index-1);
                for (CalendarItem thingsHere:currentCalItem)
                {
                    thisThingHere=new ArrayList<>();
                    /* [Election Name, Election Stage, Election State, Election ID, Election Day]*/
                    thisThingHere.add(thingsHere.electionName);
                    thisThingHere.add(thingsHere.electionStage);
                    thisThingHere.add(thingsHere.electionState);
                    thisThingHere.add(thingsHere.electionID);
                    thisThingHere.add(Integer.toString(thingsHere.electionDay));
                    thisThingHere.add(Integer.toString(thingsHere.electionMonth));
                    calendarDisplayData.add(thisThingHere);
                }
            }
        }

        try
        {
            System.out.println("interior loop");
            FileInputStream fis = thisContext.openFileInput("followListData");
            System.out.println("interior loop-1");
            ObjectInputStream ois = new ObjectInputStream(fis);
            System.out.println("interior loop-2");
            currentFollowList = (ArrayList<FollowItem>) ois.readObject();
            System.out.println("interior loop-3");
            System.out.println(currentFollowList.size());

            ois.close();
            fis.close();
        }
        catch(Exception iiee)
        {
            System.out.println(iiee.toString());
            currentFollowList=new ArrayList<>();
        }









    }



    public void saveElectionFollowList()
    {


        try {
            FileOutputStream fos = thisContext.openFileOutput("followListData", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);


            oos.writeObject(currentFollowList);
            oos.close();
            fos.close();
        }
        catch(Exception iiee)
        {
            System.out.println(iiee.toString());
        }
        System.out.println("Save length is");
        System.out.println(currentFollowList.size());
    }


    @Override
    public int getCount() {

        int currentCount=0;

        for(ArrayList<CalendarItem> things:currentData)
        {
            currentCount=currentCount+things.size();
        }

        return currentCount+12;
    }

    @Override
    public Object getItem(int i) {
        return calendarDisplayData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View currentRow;
        ArrayList<String> currentDataString=calendarDisplayData.get(i);

        if(currentDataString.size()==1)
        {
            currentRow=thisInflater.inflate(R.layout.election_month_cell, viewGroup, false);
            TextView monthLabel=currentRow.findViewById(R.id.monthLabel);
            System.out.println(currentDataString.get(0));
            monthLabel.setText(currentDataString.get(0));
        }
        else
        {
            currentRow=thisInflater.inflate(R.layout.election_cell, viewGroup, false);
            TextView cellDay=currentRow.findViewById(R.id.dayLabel);
            TextView cellState=currentRow.findViewById(R.id.stateLabel);
            TextView cellStage=currentRow.findViewById(R.id.stageLabel);
            Button goToCandidates=currentRow.findViewById(R.id.electionButton);
            Button addToCalButton=currentRow.findViewById(R.id.addToCalButton);
            addToCalButton.setId(i);
            for(FollowItem theeseThings:currentFollowList)
            {
                if(theeseThings.isLegislation==false) {


                    String[] monthDay = theeseThings.electionDate.split("-");
                    if (theeseThings.electionName.compareTo(currentDataString.get(0)) == 0 && theeseThings.electionStage.compareTo(currentDataString.get(1)) == 0 && Integer.parseInt(monthDay[1]) - Integer.parseInt(currentDataString.get(4)) == 0) {
                        addToCalButton.setText("Unfollow");
                    }
                }
            }
            addToCalButton.setOnClickListener(addToCalendar);
            /* [Election Name, Election Stage, Election State, Election ID, Election Day]*/
            goToCandidates.setText(currentDataString.get(0));
            goToCandidates.setId(Integer.parseInt(currentDataString.get(3)));
            goToCandidates.setOnClickListener(goToCandidateWindow);
            goToCandidates.setTag(currentDataString.get(0));
            cellStage.setText(currentDataString.get(1));
            cellState.setText(currentDataString.get(2));
            cellDay.setText(currentDataString.get(4));
            //cellDay.setText(currentDataString.get(4));

        }



        return currentRow;
    }


    View.OnClickListener addToCalendar= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Button currentButton=(Button) view;

            if(currentButton.getText().toString().compareTo("Unfollow") != 0) {

                FollowItem currentElection = new FollowItem();
                currentElection.electionStateAbbrev = calendarDisplayData.get(view.getId()).get(2);
                currentElection.electionDate = calendarDisplayData.get(view.getId()).get(5) + "-" + calendarDisplayData.get(view.getId()).get(4);
                currentElection.electionName = calendarDisplayData.get(view.getId()).get(0);
                currentElection.electionStage = calendarDisplayData.get(view.getId()).get(1);
                currentElection.isLegislation = false;
                currentButton.setText("Unfollow");
                currentFollowList.add(currentElection);
                String startDate = "2018/" + calendarDisplayData.get(view.getId()).get(5) + "/" + calendarDisplayData.get(view.getId()).get(4) + " 00:00:01";
                String endDate = "2018/" + calendarDisplayData.get(view.getId()).get(5) + "/" + calendarDisplayData.get(view.getId()).get(4) + " 23:59:00";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                try {
                    Date electionStart = sdf.parse(startDate);
                    Date electionEnd = sdf.parse(endDate);


                    Intent addToCalendar = new Intent(Intent.ACTION_EDIT);
                    addToCalendar.setType("vnd.android.cursor.item/event");

                    addToCalendar.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, electionStart.getTime());
                    addToCalendar.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, electionEnd.getTime());
                    addToCalendar.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                    addToCalendar.putExtra(CalendarContract.Events.TITLE, currentElection.electionName);
                    addToCalendar.putExtra(CalendarContract.Events.DESCRIPTION, currentElection.electionName + ": " + currentElection.electionStage);
                    thisContext.startActivity(addToCalendar);


                } catch (Exception iiee) {
                    System.out.println("Exception" + iiee.toString());
                }

            }
            else
            {
                System.out.println("Unfollow");
            }

        }
    };



    View.OnClickListener goToCandidateWindow=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent openCandidateWindow=new Intent (thisContext, ShowCandidates.class);
            openCandidateWindow.putExtra("ElectionID",Integer.toString(view.getId()));
            openCandidateWindow.putExtra("Election_Name",(String) view.getTag());
            thisContext.startActivity(openCandidateWindow);



            //System.out.println(view.getId());

        }
    };
}
