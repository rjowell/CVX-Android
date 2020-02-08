package com.cvx4u.cvx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


class GetStateData extends AsyncTask<String,Void,String>
{

    public DeliverResults delegate=null;
    public String stateAbbreviation;


    public GetStateData(DeliverResults delegate)
    {
        this.delegate=delegate;
    }


    HttpURLConnection connector=null;
    String line="";

    @Override
    protected String doInBackground(String... strings)
    {
        
        stateAbbreviation=strings[1];
        String rawThisData="";

        try {
            URL legiScanURL = new URL("https://api.legiscan.com/?key=438bd89d24f6339ea0adcc17fd06cd58&op=getMasterList&state="+strings[1]);

            connector = (HttpURLConnection) legiScanURL.openConnection();

            connector.setRequestMethod("GET");


            connector.connect();

            BufferedReader reader=new BufferedReader(new InputStreamReader(connector.getInputStream()));


            String lineStuff;

            StringBuilder builder=new StringBuilder();
            builder.append(reader.readLine());

            rawThisData=builder.toString();



            reader.close();



            //JSONArray theArray=new JSONArray(line.toString());



        }
        catch (Exception ee)
        {
            ee.printStackTrace();

        }



        return rawThisData;


    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        System.out.println("Junk stuff is "+s);
        delegate.processData(s);

    }
}


public class LegislationDisplay extends AppCompatActivity implements DeliverResults{

    ProgressBar loadingBar;
    //String rawStateData="";






    Map<String,String[]> topicsList=new HashMap<>();
    Spinner topicChoose;



    ListView lawAdapter;
    TextView stateLabel;
    TextView fromDate;
    TextView toDate;
    ConstraintLayout errorWindow;
    TextView searchErrorDisplay;
    Button errorButton;
    String stateAbbr;
    Button stateSelection;
    Button cancelSearchButton;
    Button goHome;
    Button searchThisState;
    Button sortByTopic;
    ArrayList<LegislationItem> searchResults=new ArrayList<>();
    Button showAllBills;
    Button showFromPicker;
    Button searchWindowButton;
    Button showToPicker;
    Button toPickerDone;
    int searchFromMonth;
    int searchToMonth;
    int searchFromDay;
    int searchToDay;
    Button toPickerCancel;
    Button fromPickerDone;
    Button fromPickerCancel;
    ConstraintLayout searchWindow;
    ConstraintLayout fromPickerWindow;
    ConstraintLayout toPickerWindow;
    ConstraintLayout sortTopicWindow;
    ConstraintLayout loadingWindow;
    TextView fromDateDisplay;
    TextView toDateDisplay;
    String[] searchTerms;
    EditText searchTermField;
    LegislationDisplayGenerator currentGen;
    LegislationItem[] lawItemArray;

    CheckBox dateRange;

    int fromMonth;
    int fromDay;
    int toMonth;
    int toDay;

    String[] months={"January","February","March","April","May","June","July","August","September","October","November","December"};
    int[] daysInMonths={31,28,31,30,31,30,31,31,30,31,30,31};

    Long lastTime;

    LegislationItem[] lawData;

    FileInputStream fileRead;
    ObjectInputStream fileJunk;

    DatePicker fromPicker;
    DatePicker toPicker;




    String[] stateList={"US","US House & Senate","AL","Alabama","AK","Alaska","AR","Arkansas","AZ","Arizona","CA","California","CO","Colorado","CT","Connecticut","DE","Delaware","FL","Florida","GA","Georgia","HI","Hawaii","ID","Idaho","IL","Illinois","IN","Indiana","IA","Iowa","KS","Kansas","KY","Kentucky","LA","Louisiana","ME","Maine","MD","Maryland","MA","Massachusetts","MI","Michigan","MN","Minnesota","MO","Missouri","MS","Mississippi","MT","Montana","NC","North Carolina","ND","North Dakota","NE","Nebraska","NH","New Hampshire","NJ","New Jersey","NM","New Mexico","NV","Nevada","NY","New York","OH","Ohio","OK","Oklahoma","OR","Oregon","PA","Pennsylvania","RI","Rhode Island","SC","South Carolina","SD","South Dakota","TN","Tennessee","TX","Texas","UT","Utah","VA","Virginia","VT","Vermont","WA","Washington","DC","Washington, DC","WI","Wisconsin","WV","West Virginia","WY","Wyoming"};
    ArrayList<String> listOfStates=new ArrayList<String>();


    @Override
    protected void onPause() {
        super.onPause();

        //new LegislationDisplayGenerator().saveFollowList();
        System.out.println("save success");
        currentGen.saveFollowList();
        System.out.println("save success--really");


    }



    View.OnClickListener topicWindowButtonProcess=new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {

            if(view.getId()==0)
            {

            }
            else
            {
                System.out.println(topicChoose.getSelectedItem().toString());
                for(String item:topicsList.get(topicChoose.getSelectedItem().toString()))
                {
                    System.out.println(item);
                    for(LegislationItem thisThing:lawItemArray)
                    {
                        System.out.println("Billlls here");
                        if(thisThing.billDescription.contains(item) || thisThing.billTitle.contains(item))
                        {
                            searchResults.add(thisThing);
                        }
                    }
                }

                LegislationItem[] newThingArray=new LegislationItem[searchResults.size()];
                for(int i=0;i<searchResults.size();i++)
                {
                    newThingArray[i]=searchResults.get(i);
                }


                Collections.sort(Arrays.asList(newThingArray), new LegislationComparator(true));
                currentGen=new LegislationDisplayGenerator(newThingArray, stateAbbr, LegislationDisplay.this, LegislationDisplay.this);
                lawAdapter.setAdapter(currentGen);


            }
            sortTopicWindow.setVisibility(View.INVISIBLE);

        }
    };




    View.OnClickListener buttonProcess=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Button currentButton=(Button) view;
            Intent legislateIntent=null;
            System.out.println(currentButton.getText());

            if(currentButton.getText().equals("State Selection"))
            {
                System.out.println("option 1");
                legislateIntent=new Intent(LegislationDisplay.this,LegislationStateSelector.class);
            }
            else if(currentButton.getText().equals("Go Home"))
            {
                System.out.println("option 2");
                currentGen.saveFollowList();
                legislateIntent=new Intent(LegislationDisplay.this,HomeScreen.class);
            }
            else if(currentButton.getText().equals("Sort By Topic"))
            {
                sortTopicWindow.setVisibility(View.VISIBLE);
            }
            else if(currentButton.getText().equals("View All Bills"))
            {
                System.out.println("option 3");
                Collections.sort(Arrays.asList(lawItemArray), new LegislationComparator(true));
                currentGen=new LegislationDisplayGenerator(lawItemArray, stateAbbr, LegislationDisplay.this, LegislationDisplay.this);
                lawAdapter.setAdapter(currentGen);
            }
            else//Search
            {
                System.out.println("option 4");
                //searchWindow.setVisibility(View.VISIBLE);
            }

            if(legislateIntent != null) {
                startActivity(legislateIntent);
            }

        }
    };


    View.OnClickListener searchButtonProcess=new View.OnClickListener() {

        Button currentButton;


        @Override
        public void onClick(View view) {

           currentButton=(Button) view;
           //System.out.println(currentButton.getText().toString()=="FROM");

           if(currentButton.getId()==0)
           {
               System.out.println("the rights button");
               if(fromPickerWindow.getVisibility()==View.INVISIBLE)
                    fromPickerWindow.setVisibility(View.VISIBLE);
               else
                   fromPickerWindow.setVisibility(View.INVISIBLE);
           }

           else
           {
               if(toPickerWindow.getVisibility()==View.INVISIBLE)
                   toPickerWindow.setVisibility(View.VISIBLE);
               else
                   toPickerWindow.setVisibility(View.INVISIBLE);
           }

        }
    };



    View.OnClickListener searchWindowProcess=new View.OnClickListener() {
        @SuppressLint("ResourceType")
        @Override
        public void onClick(View view) {


            Button currentButton=(Button) view;
            ArrayList<String> newSearchTerms=new ArrayList<>();

            if(currentButton.getId()==125)
            {
                errorWindow.setVisibility(View.INVISIBLE);
            }
            else if(currentButton.getId()==10)
            {
                searchWindow.setVisibility(View.INVISIBLE);
            }
            else if(currentButton.getId()==100)
            {
            searchTerms=searchTermField.getText().toString().split(",");
            for (String items:searchTerms) {
                System.out.println(items);
                newSearchTerms.add(items);
            }
            System.out.println(dateRange.isChecked());
            if(dateRange.isChecked()==true)
            {
                System.out.println(fromDateDisplay.getText().length());
                System.out.println(toDateDisplay.getText().length());
                if(fromDateDisplay.getText().length() == 0 && toDateDisplay.getText().length() == 0)
                {
                    searchErrorDisplay.setText(R.string.noDatesPicked);
                    errorWindow.setVisibility(View.VISIBLE);
                }
                else if(searchFromMonth>searchToMonth)
                {
                    searchErrorDisplay.setText(R.string.dateRangeError);
                    errorWindow.setVisibility(View.VISIBLE);
                }
                else if(searchFromMonth==searchToMonth && searchFromDay>searchToDay)
                {
                    searchErrorDisplay.setText(R.string.dateRangeError);
                    errorWindow.setVisibility(View.VISIBLE);
                }

            }
            searchResults.clear();
            for(LegislationItem thisStuff:lawItemArray)
            {
                for(String terms:newSearchTerms)
                {
                    if(thisStuff.billTitle.contains(terms) && searchResults.contains(thisStuff) == false)
                    {
                           searchResults.add(thisStuff);
                    }
                    else if(thisStuff.billDescription.contains(terms) && searchResults.contains(thisStuff) ==false)
                    {
                        searchResults.add(thisStuff);
                    }
                }
            }

           LegislationItem[] doDaArray=new LegislationItem[searchResults.size()];
            for(int index=0;index<searchResults.size();index++)
            {
                doDaArray[index]=searchResults.get(index);
            }



                Collections.sort(Arrays.asList(doDaArray), new LegislationComparator(true));
            currentGen=new LegislationDisplayGenerator(doDaArray, stateAbbr, LegislationDisplay.this, LegislationDisplay.this);
            lawAdapter.setAdapter(currentGen);




            }
            else {

            }


        }
    };





    View.OnClickListener showSearchWindow=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          if(searchWindow.getVisibility()==View.INVISIBLE)
          {
              searchWindow.setVisibility(View.VISIBLE);
          }
          else
          {
              searchWindow.setVisibility(View.INVISIBLE);
          }

        }
    };


    View.OnClickListener datePickerProcess=new View.OnClickListener() {

        Button thisButton;

        @SuppressLint("ResourceType")
        @Override
        public void onClick(View view) {

           thisButton=(Button) view;

           //From Cancel
           if(thisButton.getId()==0)
           {
               fromPickerWindow.setVisibility(View.INVISIBLE);
           }
           //From Done
           else if(thisButton.getId()==1)
           {
               fromMonth=fromPicker.getMonth();
               fromDay=fromPicker.getDayOfMonth();
               fromDateDisplay.setText(months[fromMonth]+" "+fromDay);
               fromPickerWindow.setVisibility(View.INVISIBLE);
               searchFromMonth=fromPicker.getMonth();
               searchFromDay=fromPicker.getDayOfMonth();
               System.out.println("From date: "+searchFromMonth+" "+searchFromDay);
           }
           //To Cancel
           else if(thisButton.getId()==2)
           {
               toPickerWindow.setVisibility(View.INVISIBLE);
           }
           //To Done
           else
           {
               toMonth=toPicker.getMonth();
               toDay=toPicker.getDayOfMonth();
               toDateDisplay.setText(months[toMonth]+" "+toDay);
               toPickerWindow.setVisibility(View.INVISIBLE);
               searchToMonth=toPicker.getMonth();
               searchToDay=toPicker.getDayOfMonth();
               System.out.println("To date: "+searchToMonth+" "+searchToDay);
           }




        }
    };

    public void useDateRange(View view)
    {
        CheckBox currentBox=(CheckBox) view;
        System.out.println(((CheckBox) view).isChecked());
        if(((CheckBox) view).isChecked()==false)
        {
            showFromPicker.setVisibility(View.INVISIBLE);
            showToPicker.setVisibility(View.INVISIBLE);
            fromDateDisplay.setVisibility(View.INVISIBLE);
            toDateDisplay.setVisibility(View.INVISIBLE);
        }
        else
        {
            showFromPicker.setVisibility(View.VISIBLE);
            showToPicker.setVisibility(View.VISIBLE);
            fromDateDisplay.setVisibility(View.VISIBLE);
            toDateDisplay.setVisibility(View.VISIBLE);
        }
    }




    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legislation_display);

        loadingBar = (ProgressBar) findViewById(R.id.loadingProgress);
        loadingBar.setVisibility(View.VISIBLE);
        System.out.println("spiiiiiiining");

        Intent intent = getIntent();
        sortTopicWindow=findViewById(R.id.topicSelectionWindow);
        Button topicCancel=findViewById(R.id.topicCancelButton);
        Button topicGo=findViewById(R.id.topicGoButton);
        topicCancel.setId(0);
        topicGo.setId(1);
        loadingWindow=findViewById(R.id.loadingWindow);
        loadingWindow.setVisibility(View.VISIBLE);

        /*File currentFile=new File("followListData");
        currentFile.delete();*/



        topicCancel.setOnClickListener(topicWindowButtonProcess);
        topicGo.setOnClickListener(topicWindowButtonProcess);
        sortTopicWindow.setVisibility(View.INVISIBLE);
        String[] educationTerms={"school","college","education","teacher","student","university","textbook","curriculum","teaching","classrorom"};
        topicsList.put("Education", educationTerms);
        String[] healthcareTerms={"doctor","patient","healthcare","health care","prescription","medic","hospital","pharma","health","abortion","birth control","contracept","reproduct"};
        topicsList.put("Healthcare",healthcareTerms);
        String[] publicSafetyTerms={"police","firefighter","jail","prison","crime","gun control","terror","arrest","sheriff","constable","trooper","criminal","violence","public safety"};
        topicsList.put("Public Safety",publicSafetyTerms);
        String[] economyTerms={"economy","jobs","wage","bank","financ","consumer","business"};
        topicsList.put("Economy",economyTerms);
        String[] environmentTerms={"pollut","eco","sustainab","conservation","environment","natur","organic","preserv"};
        topicsList.put("Environment",environmentTerms);
        String[] womensIssuesTerms={"abortion","pro-choice","pro choice","pro-life","pro life","gyno","pregnan","maternity","equal pay"};
        topicsList.put("Women's Issues", womensIssuesTerms);
        String[] lgbtIssues={"lesbian","gay","bisex","transge","transsex","same-sex","same sex"};
        topicsList.put("LGBT Issues", lgbtIssues);
        String[] governmentIssues={"term limit","legislator","lawmaker","districting","gerrymand"};
        topicsList.put("Government", governmentIssues);
        String[] immigrationTerms={"immigra","foreign nationals"};
        topicsList.put("Immigration", immigrationTerms);
        String[] foreignPolicyTerms={"isis","al qaeda","alqaeda","north korea","trade agreement","trade deal","pyongyang","defense","foreign policy","passport","green card"};
        topicsList.put("Foreign Policy", foreignPolicyTerms);

        topicChoose=findViewById(R.id.topicPicker);
        String[] testArray={"Education","Healthcare","Public Safety","Economy","Women's Issues","LGBT Issues","Government","Immigration","Foreign Policy"};







        ArrayAdapter<String> spinnerAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_item, testArray);

        //spinnerAdapter.setDropDownViewResource(R.layout.topic_chooser_cell);
        topicChoose.setAdapter(spinnerAdapter);

        errorWindow=findViewById(R.id.errorWindow);
        searchErrorDisplay=findViewById(R.id.errorText);
        errorButton=findViewById(R.id.errorDismiss);
        errorButton.setId(125);
        errorButton.setOnClickListener(searchWindowProcess);
        errorWindow.setVisibility(View.INVISIBLE);


        cancelSearchButton=findViewById(R.id.cancelSearchButton);
        cancelSearchButton.setId(10);
        cancelSearchButton.setOnClickListener(searchWindowProcess);



        fromPicker=(DatePicker) findViewById(R.id.fromDatePicker);
        toPicker=(DatePicker) findViewById(R.id.toDatePicker);
        fromPicker.init(2018, 0, 0, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });
        searchWindow = (ConstraintLayout) findViewById(R.id.searchWindow);
        searchWindow.setVisibility(View.INVISIBLE);
        stateSelection = (Button) findViewById(R.id.stateSelection);
        stateSelection.setOnClickListener(buttonProcess);
        fromPickerCancel=(Button) findViewById(R.id.fromCancelButton);
        fromPickerCancel.setId(0);
        fromPickerCancel.setOnClickListener(datePickerProcess);
        fromPickerDone=findViewById(R.id.fromDoneButton);
        fromPickerDone.setId(1);
        fromPickerDone.setOnClickListener(datePickerProcess);
        searchTermField=findViewById(R.id.searchTerms);

        searchTermField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_SEARCH)
                {



                    ArrayList<String> newSearchTerms=new ArrayList<>();


                    searchTerms=searchTermField.getText().toString().split(",");
                    for (String items:searchTerms) {
                        System.out.println(items);
                        newSearchTerms.add(items);
                    }
                    System.out.println(dateRange.isChecked());
                    if(dateRange.isChecked()==true)
                    {
                        System.out.println(fromDateDisplay.getText().length());
                        System.out.println(toDateDisplay.getText().length());
                        if(fromDateDisplay.getText().length() == 0 && toDateDisplay.getText().length() == 0)
                        {
                            searchErrorDisplay.setText(R.string.noDatesPicked);
                            errorWindow.setVisibility(View.VISIBLE);
                        }
                        else if(searchFromMonth>searchToMonth)
                        {
                            searchErrorDisplay.setText(R.string.dateRangeError);
                            errorWindow.setVisibility(View.VISIBLE);
                        }
                        else if(searchFromMonth==searchToMonth && searchFromDay>searchToDay)
                        {
                            searchErrorDisplay.setText(R.string.dateRangeError);
                            errorWindow.setVisibility(View.VISIBLE);
                        }

                    }
                    searchResults.clear();
                    for(LegislationItem thisStuff:lawItemArray)
                    {
                        for(String terms:newSearchTerms)
                        {
                            if(thisStuff.billTitle.contains(terms) && searchResults.contains(thisStuff) == false)
                            {
                                searchResults.add(thisStuff);
                            }
                            else if(thisStuff.billDescription.contains(terms) && searchResults.contains(thisStuff) ==false)
                            {
                                searchResults.add(thisStuff);
                            }
                        }
                    }

                    LegislationItem[] doDaArray=new LegislationItem[searchResults.size()];
                    for(int index=0;index<searchResults.size();index++)
                    {
                        doDaArray[index]=searchResults.get(index);
                    }



                    Collections.sort(Arrays.asList(doDaArray), new LegislationComparator(true));
                    currentGen=new LegislationDisplayGenerator(doDaArray, stateAbbr, LegislationDisplay.this, LegislationDisplay.this);
                    lawAdapter.setAdapter(currentGen);










                    InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchTermField.getWindowToken(), 0);
                    searchWindow.setVisibility(View.INVISIBLE);
                    searchTermField.setText("");
                    return true;
                }

                return false;
            }
        });
        searchWindowButton=findViewById(R.id.stateSearchButton);
        searchWindowButton.setOnClickListener(searchWindowProcess);
        searchWindowButton.setId(100);
        fromDateDisplay=findViewById(R.id.fromDateDisplay);
        toDateDisplay=findViewById(R.id.toDateDisplay);
        dateRange=findViewById(R.id.dateRange);
        toPickerCancel=findViewById(R.id.toCancelButton);
        toPickerCancel.setId(2);
        toPickerCancel.setOnClickListener(datePickerProcess);
        toPickerDone=findViewById(R.id.toDoneButton);
        toPickerDone.setId(3);
        toPickerDone.setOnClickListener(datePickerProcess);

        goHome = (Button) findViewById(R.id.goBack);
        goHome.setOnClickListener(buttonProcess);
        searchThisState = (Button) findViewById(R.id.searchThisState);
        searchThisState.setOnClickListener(showSearchWindow);
        sortByTopic = (Button) findViewById(R.id.sortByTopic);
        sortByTopic.setOnClickListener(buttonProcess);
        showAllBills = (Button) findViewById(R.id.showAllBills);
        showAllBills.setOnClickListener(buttonProcess);

        fromDate=(TextView) findViewById(R.id.fromDateDisplay);
        toDate=(TextView) findViewById(R.id.toDateDisplay);


        //fromPicker.setVisibility(View.INVISIBLE);

        //toPicker.setVisibility(View.INVISIBLE);

        showFromPicker=(Button) findViewById(R.id.showFromDate);
        showFromPicker.setOnClickListener(searchButtonProcess);
        showFromPicker.setId(0);
        showFromPicker.setVisibility(View.INVISIBLE);
        showToPicker=(Button) findViewById(R.id.showToDate);
        showToPicker.setId(1);
        showToPicker.setOnClickListener(searchButtonProcess);
        showToPicker.setVisibility(View.INVISIBLE);
        fromDateDisplay.setVisibility(View.INVISIBLE);
        toDateDisplay.setVisibility(View.INVISIBLE);

        fromPickerWindow=findViewById(R.id.fromPickerWindow);
        fromPickerWindow.setVisibility(View.INVISIBLE);
        toPickerWindow=findViewById(R.id.toPickerWindow);
        toPickerWindow.setVisibility(View.INVISIBLE);

        for (String item : stateList) {

            listOfStates.add(item);

        }

        lawAdapter = (ListView) findViewById(R.id.lawViewer);

        String currentStateData;

        stateAbbr = intent.getStringExtra("state_name");

        stateLabel = (TextView) findViewById(R.id.stateName);

        stateLabel.setText(stateList[listOfStates.indexOf(stateAbbr) + 1]);





        String[][] stateList = {{"US", "US House & Senate"}, {"AL", "Alabama"}, {"AK", "Alaska"}, {"AR", "Arkansas"}, {"AZ", "Arizona"}, {"CA", "California"}, {"CO", "Colorado"}, {"CT", "Connecticut"}, {"DE", "Delaware"}, {"FL", "Florida"}, {"GA", "Georgia"}, {"HI", "Hawaii"}, {"ID", "Idaho"}, {"IL", "Illinois"}, {"IN", "Indiana"}, {"IA", "Iowa"}, {"KS", "Kansas"}, {"KY", "Kentucky"}, {"LA", "Louisiana"}, {"ME", "Maine"}, {"MD", "Maryland"}, {"MA", "Massachusetts"}, {"MI", "Michigan"}, {"MN", "Minnesota"}, {"MO", "Missouri"}, {"MS", "Mississippi"}, {"MT", "Montana"}, {"NC", "North Carolina"}, {"ND", "North Dakota"}, {"NE", "Nebraska"}, {"NH", "New Hampshire"}, {"NJ", "New Jersey"}, {"NM", "New Mexico"}, {"NV", "Nevada"}, {"NY", "New York"}, {"OH", "Ohio"}, {"OK", "Oklahoma"}, {"OR", "Oregon"}, {"PA", "Pennsylvania"}, {"RI", "Rhode Island"}, {"SC", "South Carolina"}, {"SD", "South Dakota"}, {"TN", "Tennessee"}, {"TX", "Texas"}, {"UT", "Utah"}, {"VA", "Virginia"}, {"VT", "Vermont"}, {"WA", "Washington"}, {"DC", "Washington, DC"}, {"WI", "Wisconsin"}, {"WV", "West Virginia"}, {"WY", "Wyoming"}};
/*
        for (String[] things : stateList) {

            SharedPreferences timeStoreA = getSharedPreferences(things[0] + "lastUpdateTime", 0);
            SharedPreferences.Editor timeEditorA = timeStoreA.edit();

            timeEditorA.remove(things[0] + "lastUpdateTime");

            timeEditorA.commit();

        }*/


        SharedPreferences timeStore = getSharedPreferences(stateAbbr + "lastUpdateTime", 0);
        SharedPreferences.Editor timeEditor = timeStore.edit();

        //timeEditor.putLong(stateAbbr+"lastUpdateTime",Calendar.getInstance().getTimeInMillis());

        //timeEditor.remove(stateAbbr + "lastUpdateTime");

            timeEditor.commit();




        lastTime=null;


        try {

            lastTime=timeStore.getLong(stateAbbr+"lastUpdateTime",00);

            System.out.println("Your time is "+stateAbbr+" "+lastTime);

            //Date lastUpdateTimeA = dateFormat.parse(timeStore.getString(stateAbbr + "lastUpdateTime", "None here"));

        }
        catch(Exception thisError)
        {
            System.out.println(thisError.toString());
        }

        System.out.println(stateAbbr+" "+lastTime);

        System.out.println(Calendar.getInstance().getTimeInMillis()-lastTime);








        if(lastTime != null && lastTime != 00 && Calendar.getInstance().getTimeInMillis()-lastTime < 3600000)
        {
            System.out.println("this loop place");
            try {
                FileInputStream fileIn = this.openFileInput(stateAbbr+"stateInfo");
                ObjectInputStream ois = new ObjectInputStream(fileIn);

                //ObjectOutputStream oos=new ObjectOutputStream(fileStream);
                System.out.println("hello");

                LegislationItem[] dataArray=(LegislationItem[]) ois.readObject();
                ois.close();
                fileIn.close();

                for (LegislationItem thing:dataArray)
                {
                    System.out.println(thing.billLastActionDate);
                }
                System.out.println("before line");
                Collections.sort(Arrays.asList(dataArray), new LegislationComparator(false));
                //Arrays.sort(dataArray);
                System.out.println("divider");
                //System.out.println(dataArray[3].billLastActionDate.replace("-",""));

                for (LegislationItem thing:dataArray)
                {
                    System.out.println(thing.billLastActionDate);
                }



                System.out.println("new orleans time");
                System.out.println(dataArray[143].billLastActionDate);
                lawItemArray=dataArray;
                System.out.println("hamburgers");
                Collections.sort(Arrays.asList(dataArray), new LegislationComparator(true));
                currentGen=new LegislationDisplayGenerator(dataArray, stateAbbr, this,this);
                lawAdapter.setAdapter(currentGen);
                loadingWindow.setVisibility(View.INVISIBLE);


            }
            catch(Exception thisEx)
            {
                System.out.println("error travis");
                System.out.println(thisEx.toString());
            }
        }
        else
        {
            System.out.println("this loop place-111");
            new GetStateData(this).execute("", stateAbbr);
        }





        loadingBar.setVisibility(ProgressBar.INVISIBLE);



    }

    @Override
    public void processData(String output) {

        //System.out.println("russ iss-123"+output);
        //loadingBar.setVisibility(ProgressBar.VISIBLE);

        JSONObject stateJSONData=null;

        int arrayCount;

        lawItemArray=null;

        FileOutputStream fileStream;




        try {
            stateJSONData = new JSONObject(output);

            JSONObject masterList=new JSONObject(stateJSONData.getString("masterlist"));
            System.out.println("data of "+masterList.getJSONObject("session").getString("session_id"));
            JSONObject lawData=new JSONObject(stateJSONData.getString("masterlist"));
            arrayCount=lawData.length()-1;
            System.out.println(arrayCount);

            lawItemArray=new LegislationItem[arrayCount];

            /*
            billNumbers=new String[arrayCount];
            billTitles=new String[arrayCount];
            billDescriptions=new String[arrayCount];
            lastStatusDates=new String[arrayCount];
            lastStatuses=new String[arrayCount];
            fullTextLinks=new String[arrayCount];
            */


            for(int count=0; count<arrayCount; count++)
            {
                System.out.println("loop is");
                lawItemArray[count]=new LegislationItem(lawData.getJSONObject(count+"").getString("number"),lawData.getJSONObject(count+"").getString("url"),lawData.getJSONObject(count+"").getString("last_action"),lawData.getJSONObject(count+"").getString("last_action_date"),lawData.getJSONObject(count+"").getString("title"),lawData.getJSONObject(count+"").getString("description"),lawData.getJSONObject(count+"").getString("bill_id"));


            }
            //System.out.println(lawData.getJSONObject("3144"));

            //System.out.println(lawItemArray[155].billDescription.toString());



            fileStream=this.openFileOutput(stateAbbr+"stateInfo", Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fileStream);
            oos.writeObject(lawItemArray);
            oos.close();
            fileStream.close();

            SharedPreferences timeKeeper=getSharedPreferences(stateAbbr+"lastUpdateTime",0);
            SharedPreferences.Editor timeEditorA=timeKeeper.edit();

            timeEditorA.putLong(stateAbbr+"lastUpdateTime",Calendar.getInstance().getTimeInMillis());

            timeEditorA.commit();

            long theTime=timeKeeper.getLong(stateAbbr+"lastUpdateTime",0);

            System.out.println("The long time is "+theTime);








        }
        catch(Exception abc)
        {
            System.out.println(abc.toString());

        }

        System.out.println("Count is "+lawItemArray[156].billLastActionDate);

        Collections.sort(Arrays.asList(lawItemArray), new LegislationComparator(true));
        currentGen=new LegislationDisplayGenerator(lawItemArray, stateAbbr, this, this);
        lawAdapter.setAdapter(currentGen);
        loadingWindow.setVisibility(View.INVISIBLE);

    }


}
