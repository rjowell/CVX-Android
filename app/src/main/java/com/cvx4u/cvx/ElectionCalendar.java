package com.cvx4u.cvx;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;



class CalendarItem
{
    int electionDay;
    int electionMonth;
    String electionName;
    String electionStage;
    String electionState;
    String electionStageID;
    String electionID;



}









class Election
{
    String electionID;
    String name;
    String state;
    String typeID;
    String special;
    String year;
    ArrayList<ElectionStage> stageArray;
    static class ElectionStage
    {
        String stageID;
        String electionStageID;
        String name;
        String state;
        String electionDate;
        int month;
        int day;
        //String npatMail;

    }

}







class FetchElectionCalendar extends AsyncTask<String[],Void,String>
{

    HttpURLConnection connector=null;
    private static String voteSmartURL="http://api.votesmart.org/Election.getElectionByYearState?o=JSON&key=6b633e49549b6c63c659be06c2ee75f9&year=2018&stateId=";
    private static final String ns=null;
    public DeliverElectionResults delegate=null;
    private String[] inputStates=null;



    public FetchElectionCalendar(DeliverElectionResults delegate, String[] statesInput)
    {
        this.delegate=delegate;
        inputStates=statesInput;
    }

    @Override
    protected String doInBackground(String[]... strings) {
        String rawThisData="";
        //ArrayList<String> rawStatesData={};

        //getElectionStates=getSharedPreferences("ElectionCalStates",0);


        for (String stateAbbr:inputStates) {

            System.out.println("state is " + stateAbbr);


            try {
                URL currentStateURL = new URL(voteSmartURL + stateAbbr);

                connector = (HttpURLConnection) currentStateURL.openConnection();

                connector.connect();


                BufferedReader reader = new BufferedReader(new InputStreamReader(connector.getInputStream()));

                StringBuilder builder = new StringBuilder();
            /*while(reader.readLine() != null) {*/
                builder.append(reader.readLine());
                //}
                //rawStatesData.add(builder.toString());
                rawThisData = rawThisData+builder.toString();
                if(stateAbbr != inputStates[inputStates.length-1])
                {
                    rawThisData=rawThisData+"%%--%%--%%";
                }
                reader.close();
            } catch (Exception thisJunk) {
                System.out.println(thisJunk.toString());
            }

        }

        //String[] finalData= (String[]) rawStatesData.toArray();





        return rawThisData;
    }

    @Override
    protected void onPostExecute(String s)
    {
        delegate.processData(s);
    }














}








public class ElectionCalendar extends AppCompatActivity implements DeliverElectionResults {



    Button goToStateList;
    Button goToMainMenu;
    Button help;
    Button welcomeDone;
    ConstraintLayout welcomeScreen;
    ConstraintLayout loadingWindow;
    ElectionCalendarGenerator currentGen;
    Button showHelpScreen;
    String[] electionStates={};
    Set<String> rawData;
    SharedPreferences getElectionStates;
    SharedPreferences.Editor electionStatesEdit;
    ArrayList<Election> electionData=new ArrayList<>();
    ArrayList<CalendarItem> calendarStuff=new ArrayList<>();

    ArrayList<ArrayList<CalendarItem>> calData=new ArrayList<>();

    ListView electionCalendar;

    HashMap<Integer, ArrayList<CalendarItem>> displayData=new HashMap<>();

    String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};

    View.OnClickListener openStateScreen= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent openDestination=new Intent(ElectionCalendar.this, ElectionCalStateList.class);

            startActivity(openDestination);

        }
    };

    View.OnClickListener openToMainMenu= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent goToMainMenu=new Intent(ElectionCalendar.this, HomeScreen.class);
            startActivity(goToMainMenu);
        }
    };

    View.OnClickListener showHideWelcome= new View.OnClickListener() {
        @SuppressLint("ResourceType")
        @Override
        public void onClick(View view) {

            if(view.getId()==27)
            {
                welcomeScreen.setVisibility(View.VISIBLE);
            }
            else {
                welcomeScreen.setVisibility(View.INVISIBLE);
            }

        }
    };


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_calendar);
        goToMainMenu=findViewById(R.id.mainMenu);
        welcomeScreen=findViewById(R.id.welcomeWindow);
        help=findViewById(R.id.help);
        help.setOnClickListener(showHideWelcome);
        help.setId(27);
        welcomeDone=findViewById(R.id.welcomeDoneButton);
        welcomeDone.setOnClickListener(showHideWelcome);
        welcomeDone.setId(72);
        goToMainMenu.setOnClickListener(openToMainMenu);
        goToStateList=findViewById(R.id.selectStates);
        electionCalendar=findViewById(R.id.electionCalendar);
        goToStateList.setOnClickListener(openStateScreen);
        //loadingWindow=findViewById(R.id.loadingWindow);
        //loadingWindow.setVisibility(View.VISIBLE);
        int monthCounter=1;
        while(monthCounter<13)
        {
            calData.add(new ArrayList<CalendarItem>());
            monthCounter++;
        }
        System.out.println("Month ARrays are "+calData.size());
        //Collections.sort(calendarStuff, new ElectionCalednarComparator());

        getElectionStates=getSharedPreferences("ElectionCalStates",0);
        rawData=getElectionStates.getStringSet("ElectionSaveStates", null);
        //System.out.println(rawData);


        System.out.println("states are");
        if(rawData != null) {
            electionStates = rawData.toArray(new String[rawData.size()]);
        }
        for (String item:electionStates)
        {
            System.out.println("States are this:  "+item);
        }


        System.out.println(electionStates);
        //System.out.println(electionStates[1]);

        new FetchElectionCalendar(this, electionStates).execute();


}




    @Override
    protected void onPause() {
        super.onPause();

        //new LegislationDisplayGenerator().saveFollowList();
        System.out.println("save success");
        currentGen.saveElectionFollowList();
        System.out.println("save success--really");


    }


    @Override
    public void processData(String input) {

    String[] currentData=input.split("%%--%%--%%");

        for (String currentStateData:currentData)
        {

            JSONObject stateJSONData;
            JSONArray thisArray;
            JSONObject currentElection;


            if(currentStateData.contains("No elections found"))
            {
                System.out.println("no elections");
            }//End No Elections
            else
            {
                JSONObject masterList;
                try {
                    stateJSONData=new JSONObject(currentStateData);
                    masterList = new JSONObject(stateJSONData.getString("elections"));

                    if(currentStateData.contains("\"election\":["))
                    {
                        thisArray=masterList.getJSONArray("election");
                        System.out.println("Count is "+thisArray.length());

                        for(int i=0;i<thisArray.length();i++)
                        {
                            Election currentItem=new Election();



                            JSONObject currentThing=thisArray.getJSONObject(i);

                            currentItem.name=currentThing.getString("name");
                            currentItem.electionID=currentThing.getString("electionId");
                            currentItem.special=currentThing.getString("special");
                            currentItem.state=currentThing.getString("stateId");
                            currentItem.typeID=currentThing.getString("officeTypeId");
                            currentItem.year="2018";

                            //multiple stages
                            if(thisArray.getJSONObject(i).toString().contains("\"stage\":["))
                            {
                                System.out.println("stage multiple");
                                JSONArray currentStages=currentThing.getJSONArray("stage");
                                for(int index=0;index<currentStages.length();index++)
                                {
                                    Election.ElectionStage currentStage=new Election.ElectionStage();
                                    JSONObject currentVoteStage=currentStages.getJSONObject(index);
                                    currentStage.electionDate=currentVoteStage.getString("electionDate");
                                    currentStage.electionStageID=currentVoteStage.getString("electionElectionstageId");
                                    currentStage.name=currentVoteStage.getString("name");
                                    currentStage.stageID=currentVoteStage.getString("stageId");
                                    currentStage.day=Integer.parseInt(currentStage.electionDate.substring(8,10));
                                    currentStage.month=Integer.parseInt(currentStage.electionDate.substring(5,7));
                                    System.out.println("point one");
                                    currentItem.stageArray=new ArrayList<>();
                                    currentItem.stageArray.add(currentStage);

                                }

                            }
                            //Only one stage
                            else
                            {

                                JSONObject currentStages=currentThing.getJSONObject("stage");
                                Election.ElectionStage currentStage=new Election.ElectionStage();
                                currentStage.electionDate=currentStages.getString("electionDate");
                                currentStage.electionStageID=currentStages.getString("electionElectionstageId");
                                currentStage.name=currentStages.getString("name");
                                currentStage.stageID=currentStages.getString("stageId");
                                currentStage.day=Integer.parseInt(currentStage.electionDate.substring(8,10));
                                currentStage.month=Integer.parseInt(currentStage.electionDate.substring(5,7));
                                currentItem.stageArray=new ArrayList<>();
                                System.out.println("point two");
                                if(currentItem==null)
                                {
                                    System.out.println("Current item null");
                                }
                                if(currentItem.stageArray==null)
                                {
                                    System.out.println("stage array null");
                                }
                                if(currentStage==null)
                                {
                                    System.out.println("stage----- array null");
                                }
                                currentItem.stageArray.add(currentStage);
                            }


                            //System.out.println(currentThing.getString("name"));

                            System.out.println("point three");
                            electionData.add(currentItem);
                        }



                    }
                    else
                    {


                        currentElection=masterList.getJSONObject("election");
                        Election currentItem=new Election();


                        currentItem.name=currentElection.getString("name");
                        currentItem.electionID=currentElection.getString("electionId");
                        currentItem.special=currentElection.getString("special");
                        currentItem.state=currentElection.getString("stateId");
                        currentItem.typeID=currentElection.getString("officeTypeId");
                        currentItem.year="2018";

                        //multiple stages
                        if(currentElection.toString().contains("\"stage\":["))
                        {
                            System.out.println("stage multiple");
                            currentItem.stageArray=new ArrayList<>();
                            JSONArray currentStages=currentElection.getJSONArray("stage");
                            for(int index=0;index<currentStages.length();index++)
                            {
                                Election.ElectionStage currentStage=new Election.ElectionStage();
                                JSONObject currentVoteStage=currentStages.getJSONObject(index);
                                currentStage.electionDate=currentVoteStage.getString("electionDate");
                                currentStage.electionStageID=currentVoteStage.getString("electionElectionstageId");
                                currentStage.name=currentVoteStage.getString("name");
                                currentStage.stageID=currentVoteStage.getString("stageId");
                                currentStage.day=Integer.parseInt(currentStage.electionDate.substring(8,10));
                                currentStage.month=Integer.parseInt(currentStage.electionDate.substring(5,7));
                                System.out.println("point four");

                                //Make sure stages are this year
                                if(currentStage.electionDate.contains("2018-")) {
                                    currentItem.stageArray.add(currentStage);
                                }


                            }

                        }
                        //Only one stage
                        else
                        {

                            JSONObject currentStages=currentElection.getJSONObject("stage");
                            Election.ElectionStage currentStage=new Election.ElectionStage();
                            currentStage.electionDate=currentStages.getString("electionDate");
                            currentStage.electionStageID=currentStages.getString("electionElectionstageId");
                            currentStage.name=currentStages.getString("name");
                            currentStage.stageID=currentStages.getString("stageId");
                            currentStage.day=Integer.parseInt(currentStage.electionDate.substring(8,10));
                            currentStage.month=Integer.parseInt(currentStage.electionDate.substring(5,7));
                            System.out.println("point five");
                            if(currentStage.electionDate.contains("2018-")) {
                                currentItem.stageArray.add(currentStage);
                            }
                        }


                        System.out.println("point six");
                        electionData.add(currentItem);



                        System.out.println("only one chum");
                    }


                }
                catch(Exception iiee)
                {
                    System.out.println(iiee.toString());
                }


            }



        }


        for (Election currentThing:electionData)
        {
            System.out.println(currentThing.name);

            for(Election.ElectionStage stages:currentThing.stageArray)
            {
                System.out.println(stages.name);
                System.out.println(stages.month);
                System.out.println(stages.day);
            }
        }







        for (Election currentThing:electionData)
        {
            /*
            System.out.println("NAme: "+currentThing.name);
            for (Election.ElectionStage currentStage:currentThing.stageArray)
            {
                System.out.println("Stage Name: "+currentStage.name);
                System.out.println("Month: "+currentStage.month);
                System.out.println("Day: "+currentStage.day);
            }*/








            for(Election.ElectionStage currentStage:currentThing.stageArray)
            {
                CalendarItem currentItem=new CalendarItem();
                currentItem.electionDay=currentStage.day;
                currentItem.electionMonth=currentStage.month;
                currentItem.electionID=currentThing.electionID;
                currentItem.electionName=currentThing.name;
                currentItem.electionStage=currentStage.name;
                currentItem.electionStageID=currentStage.electionStageID;
                currentItem.electionState=currentThing.state;
                //System.out.println("Russ monmth si"+currentItem.electionMonth);
                //System.out.println(currentItem.electionDay);

                calData.get(currentItem.electionMonth-1).add(currentItem);


            }
        }

        for (ArrayList<CalendarItem> currentThing:calData)
        {
            Collections.sort(currentThing,new ElectionCalednarComparator());
            //System.out.println(currentThing.size());
        }

        int currMonth=0;





        for (ArrayList<CalendarItem> calItems:calData)
        {
            System.out.println("Month "+currMonth);
            for (CalendarItem thingOne:calItems)
            {
                System.out.println("Day is "+thingOne.electionDay);
                System.out.println("Elec is "+thingOne.electionName);
                System.out.println("Stage is "+thingOne.electionStage);
            }
            currMonth++;
        }

        currentGen=new ElectionCalendarGenerator(calData, this);
        electionCalendar.setAdapter(currentGen);


    }
}
