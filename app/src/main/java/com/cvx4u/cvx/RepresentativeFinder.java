package com.cvx4u.cvx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



class OfficerIndex
{
    String name;
    String ocdDivision;
    ArrayList<Integer> officeIndex;
}


class GetRepPhotos extends AsyncTask<Representative,Void,ArrayList<Representative>>
        {

            ArrayList<Representative> theListOfStuff;
            public DeliverRepresentativePhotos delRepPics=null;

            public GetRepPhotos(DeliverRepresentativePhotos thisDel, ArrayList<Representative> inputReps)
            {
                theListOfStuff=inputReps;
                delRepPics=thisDel;
            }


            @Override
            protected void onPostExecute(ArrayList<Representative> input)
            {
                System.out.println("your russ side is");
                System.out.println(input.size());
                delRepPics.deliverPhotos(input);
            }


            @Override
            protected ArrayList<Representative> doInBackground(Representative... representatives) {

                System.out.println(representatives.length);


                for(Representative things:theListOfStuff)
                {

                   System.out.println("pictures");
                    System.out.println(things.photoURL);

                    if(things.photoURL != null) {
                        try {
                            InputStream in = new java.net.URL(things.photoURL).openStream();
                            things.repPhoto = BitmapFactory.decodeStream(in);
                            System.out.println(things.repPhoto.getHeight());
                        } catch (Exception iiee) {
                            System.out.println(iiee.toString());
                        }
                    }

                }
                System.out.println(theListOfStuff.size());
                return theListOfStuff;
            }
        }








class GetRepresentatives extends AsyncTask<String,Void,String>
{

    HttpURLConnection connector=null;
    JSONObject rawRepresentativeData;
    String googleURL="https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyDfeiCRXoUEb2ZNaq9WmgadSmeEKAiCIlw&address=";
    public DeliverRepresentativeResults repSend=null;
    String addressInput;

    public GetRepresentatives(DeliverRepresentativeResults delegate, String input)
    {
        this.repSend=delegate;
        addressInput=input.replace(" ","%20");
        System.out.println("Address is "+input);
    }





    @Override
    protected void onPostExecute(String s)
    {

        repSend.deliverRawRepData(s);
    }


    @Override
    protected String doInBackground(String... strings) {

        System.out.println("you made it");

        String rawData="";
        String rawFullData="";

        System.out.println("Raw data isssss");
        System.out.println(rawFullData);

        try {
            URL currentInput = new URL(googleURL + addressInput);
            System.out.println("URs iis");
            System.out.println(currentInput.toString());
            connector=(HttpURLConnection) currentInput.openConnection();

            connector.connect();

            BufferedReader reader=new BufferedReader(new InputStreamReader(connector.getInputStream()));

            StringBuilder builder=new StringBuilder();

            while((rawData=reader.readLine()) != null) {
                  //reader.readLine();
                rawFullData=rawFullData+rawData;
            }



            reader.close();



        }
        catch(Exception iiee)
        {
            System.out.println(iiee.toString());
        }

        System.out.println(rawFullData);
        return rawFullData;
    }
}





public class RepresentativeFinder extends AppCompatActivity implements DeliverRepresentativeResults, DeliverRepresentativePhotos {



    ArrayList<Representative> representativeList=new ArrayList<>();
    ArrayList<OfficerIndex> officerIndexList=new ArrayList<>();
    ListView representativeDisplay;
    EditText addressInput;
    Spinner stateSelector;
    ConstraintLayout loadingWindow;
    Button cancelButton;
    TextView addressWrong;
    Button searchButton;
    RepresentativeDisplayGenerator displayGenerator;
    Button showSearch;
    Button goHome;
    Button switchStateLocalButton;
    Button mainMenuButton;
    ConstraintLayout searchWindow;
    TextView repSearchDescription;
    String searchTerm;
    String[] states={"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina","South Dakota","Tennessee","Texas","Utah","Vermont","Virginia","Washington","Washington, DC","West Virginia","Wisconsin","Wyoming"};
    String[] stateOfficeList={"Governor","Lieutenant Governor","United States Senate","United States House of Representatives"};



    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("this program here");
        setContentView(R.layout.activity_meet_your_government);
        searchWindow=findViewById(R.id.representSearchWindow);
        repSearchDescription=findViewById(R.id.repFinderDesc);
        repSearchDescription.setText(R.string.repFinder_description);
        addressWrong=findViewById(R.id.addressInvalid);
        addressWrong.setVisibility(View.INVISIBLE);
        loadingWindow=findViewById(R.id.loadingWindow);
        loadingWindow.setVisibility(View.INVISIBLE);




        View.OnClickListener buttonProcess=new View.OnClickListener()
        {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                System.out.println("wine & cheese");
                //Cancel
                if(view.getId()==0)
                {
                    searchWindow.setVisibility(View.INVISIBLE);
                    InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(addressInput.getWindowToken(), 0);

                }
                else if(view.getId()==1)
                {
                    representativeList.clear();
                    officerIndexList.clear();
                    System.out.println("Arrayyyy "+representativeList.size());
                    if(stateSelector.getVisibility()==View.INVISIBLE)
                    {
                        System.out.println("heeeree 00111");
                        loadingWindow.setVisibility(View.VISIBLE);
                        searchTerm = addressInput.getText().toString().replace(" ", "%20");
                        new GetRepresentatives(RepresentativeFinder.this, searchTerm).execute();
                    }
                    else
                    {
                        System.out.println("heeeree 00111-------");
                        loadingWindow.setVisibility(View.VISIBLE);
                        searchTerm=stateSelector.getSelectedItem().toString();
                        new GetRepresentatives(RepresentativeFinder.this, searchTerm).execute();
                    }

                }
                else if(view.getId()==2)
                {
                    InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                    //switchButton
                    if(stateSelector.getVisibility()==View.INVISIBLE)
                    {
                        stateSelector.setVisibility(View.VISIBLE);
                        repSearchDescription.setText(R.string.stateRepInstructions);
                        addressInput.setVisibility(View.INVISIBLE);
                        switchStateLocalButton.setText(R.string.switchToLocalSearch);
                        imm.hideSoftInputFromWindow(addressInput.getWindowToken(), 0);
                    }
                    else
                    {
                        stateSelector.setVisibility(View.INVISIBLE);
                        addressInput.setVisibility(View.VISIBLE);
                        repSearchDescription.setText(R.string.repFinder_description);
                        switchStateLocalButton.setText(R.string.switchToStateSearch);
                        addressInput.setFocusable(true);
                        imm.showSoftInputFromInputMethod(addressInput.getWindowToken(), 0);
                    }
                }
                else
                {
                    Intent goToMain=new Intent(RepresentativeFinder.this, HomeScreen.class);
                    startActivity(goToMain);
                }

            }
        };





        representativeDisplay=findViewById(R.id.representativeListing);

        displayGenerator=new RepresentativeDisplayGenerator(this, representativeList);
        representativeDisplay.setAdapter(displayGenerator);



        stateSelector=findViewById(R.id.stateSelector);
        ArrayAdapter<String> stateList=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);
        stateSelector.setAdapter(stateList);
        stateSelector.setVisibility(View.INVISIBLE);
        addressInput=findViewById(R.id.addressInput);
        addressInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {


                if (i == EditorInfo.IME_ACTION_SEARCH)
                {


                    loadingWindow.setVisibility(View.VISIBLE);
                    searchTerm = addressInput.getText().toString().replace(" ", "%20");
                    InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(addressInput.getWindowToken(), 0);
                    new GetRepresentatives(RepresentativeFinder.this, searchTerm).execute();

                    return true;
                }


                return false;
            }
        });
        cancelButton=findViewById(R.id.repCancelButton);
        cancelButton.setId(0);
        cancelButton.setOnClickListener(buttonProcess);
        searchButton=findViewById(R.id.repSearchButton);
        searchButton.setId(1);
        searchButton.setOnClickListener(buttonProcess);
        showSearch=findViewById(R.id.searchButton);
        showSearch.setOnClickListener(showSearchGoHome);
        showSearch.setId(200);
        goHome=findViewById(R.id.goHomeButton);
        goHome.setId(100);
        goHome.setOnClickListener(showSearchGoHome);
        switchStateLocalButton=findViewById(R.id.stateLocalSwitch);
        switchStateLocalButton.setId(2);
        switchStateLocalButton.setOnClickListener(buttonProcess);
        switchStateLocalButton.setText(R.string.switchToStateSearch);
        mainMenuButton=findViewById(R.id.goToMainMenu);
        mainMenuButton.setId(3);
        mainMenuButton.setOnClickListener(buttonProcess);









    }

    View.OnClickListener showSearchGoHome=new View.OnClickListener()
    {

        @SuppressLint("ResourceType")
        @Override
        public void onClick(View view) {

            if(view.getId()==100)
            {
                //goHome
                Intent goToMain=new Intent(RepresentativeFinder.this, HomeScreen.class);
                startActivity(goToMain);
            }
            else
            {
                searchWindow.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    public void deliverRawRepData(String input) {

        System.out.println("data is this");
        System.out.println(input);

        if(input.contains("error"))
        {
            System.out.println("errorrrrr");
            addressWrong.setVisibility(View.VISIBLE);
            //TextView addressWrong=findViewById(R.id.addressInvalid);

        }
    else {
            try {

                Map<String, ArrayList<Integer>> divisionsList = new HashMap<>();

                JSONObject rawInputData = new JSONObject(input);

                JSONObject divisions = rawInputData.getJSONObject("divisions");
                JSONArray officerListing = rawInputData.getJSONArray("offices");
                JSONArray listOfPeople = rawInputData.getJSONArray("officials");


                Iterator<String> divisionKeys = divisions.keys();

                while (divisionKeys.hasNext()) {
                    String currentKey = divisionKeys.next();
                    JSONObject currentOfficeDivIndex = divisions.getJSONObject(currentKey);

                    JSONArray currentDivArray = currentOfficeDivIndex.getJSONArray("officeIndices");
                    ArrayList<Integer> currentDivIndexes = new ArrayList<>();

                    for (int h = 0; h < currentDivArray.length(); h++) {
                        currentDivIndexes.add(Integer.parseInt(currentDivArray.get(h).toString()));
                    }

                    divisionsList.put(currentOfficeDivIndex.getString("name"), currentDivIndexes);
                }


                for (Map.Entry<String, ArrayList<Integer>> thisItem : divisionsList.entrySet()) {
                    Object[] currentOfficeList = thisItem.getValue().toArray();
                    String currentDivision = thisItem.getKey();

                    for (Object item : currentOfficeList) {
                        JSONObject currentOfficer = officerListing.getJSONObject(Integer.parseInt(item.toString()));
                        JSONArray personIndex = currentOfficer.getJSONArray("officialIndices");
                        OfficerIndex currentIndex = new OfficerIndex();
                        ArrayList<Integer> currentPersons = new ArrayList<>();
                        currentIndex.name = currentOfficer.getString("name");
                        currentIndex.ocdDivision = thisItem.getKey();
                        for (int index = 0; index < personIndex.length(); index++) {
                            currentPersons.add(Integer.parseInt(personIndex.get(index).toString()));
                        }
                        currentIndex.officeIndex = currentPersons;
                        officerIndexList.add(currentIndex);

                    }

                    System.out.println(thisItem.getKey() + " " + thisItem.getValue());
                }

                for (int thisJunk = 0; thisJunk < officerIndexList.size(); thisJunk++) {
                    Object[] personList = officerIndexList.get(thisJunk).officeIndex.toArray();
                    String division = officerIndexList.get(thisJunk).ocdDivision;
                    for (Object number : personList) {
                        Representative currentPerson = new Representative();

                        JSONObject currentGuy = listOfPeople.getJSONObject((Integer) number);
                        System.out.println("Persssson indexxx " + number);
                        currentPerson.repName = currentGuy.getString("name");
                        System.out.println("step 1");
                        currentPerson.ocdDivision = division;
                        System.out.println("step 2");
                        currentPerson.officeName = officerIndexList.get(thisJunk).name;
                        System.out.println("step 3");
                        if (currentGuy.has("party")) {
                            currentPerson.party = currentGuy.getString("party");
                        }
                        System.out.println("step 4");
                        if (currentGuy.has("phones")) {
                            currentPerson.phoneNumber = currentGuy.getJSONArray("phones").getString(0);
                        }
                        System.out.println("step 5");
                        if (currentGuy.has("urls")) {
                            currentPerson.website = currentGuy.getJSONArray("urls").getString(0);
                        }
                        System.out.println("step 6");
                        if (currentGuy.has("emails")) {
                            currentPerson.eMail = currentGuy.getJSONArray("emails").getString(0);
                            System.out.println("step 7");
                        }
                        System.out.println(currentGuy.getString("name"));
                        System.out.println("step 8");
                        currentPerson.officeIndex = (Integer) number;
                        if (currentGuy.has("photoUrl")) {

                            currentPerson.photoURL = currentGuy.getString("photoUrl");
                            System.out.println("step 9");

                        /*InputStream in = new java.net.URL(currentGuy.getString("photoUrl")).openStream();
                        currentPerson.repPhoto= BitmapFactory.decodeStream(in);*/
                        }


                        System.out.println(officerIndexList.get(thisJunk).name);
                        System.out.println((Integer) number);
                        System.out.println("Pres VP");
                        System.out.println(currentPerson.officeName.compareTo("President of the United States"));
                        System.out.println(currentPerson.officeName.compareTo("Vice-President of the United States"));



                        if(currentPerson.officeName.compareTo("President of the United States")==0)
                        {
                            System.out.println("Goooober");
                        }
                        if(currentPerson.officeName.compareTo("Vice-President of the United States")==0)
                        {
                            System.out.println("Goooober-111");
                        }




                            if(currentPerson.officeName.compareTo("President of the United States") != 0 && currentPerson.officeName.compareTo("Vice-President of the United States") != 0) {
                                representativeList.add(currentPerson);


                        }


                    }


                    //System.out.println(officerIndexList.get(thisJunk).name+" "+officerIndexList.get(thisJunk).ocdDivision+" "+officerIndexList.get(thisJunk).officeIndex);
                }


            } catch (Exception iiee) {
                System.out.println(iiee.toString());
            }






            new GetRepPhotos(this, representativeList).execute();


            for (Representative peeeple : representativeList) {
                System.out.println(peeeple.repName);
                System.out.println(peeeple.officeName);
                System.out.println(peeeple.party);
            }
        }

    }




    @Override
    public void deliverPhotos(ArrayList<Representative> data) {

        //ArrayList<Representative> currentData=data;
        /*for(Representative items:data)
        {
            if(items.officeIndex==0 || items.officeIndex==1)
            {
                data.remove(items);
            }
        }*/

        //Remove President & VP


        for(Representative thisStuff:data)
        {


            System.out.println("Russ juknksss");
            System.out.println(thisStuff.officeName);
            System.out.println(data.indexOf(thisStuff));
        }




        Collections.sort(data, new RepresentativeComparator());

        //Remove President & VP
        /*data.remove(0);
        data.remove(0);
        */

        int governorPosition=0;
        int ltGovPosition=0;
        for(int index=0;index<data.size();index++)
        {
            if(data.get(index).officeName.compareTo("Governor")==0)
            {
                governorPosition=index;
            }
            if(data.get(index).officeName.compareTo("Lieutenant Governor")==0)
            {
                ltGovPosition=index;
            }
        }

        if(governorPosition != 0) {
            Collections.swap(data, governorPosition, 0);
        }
        if(ltGovPosition != 0)
        {
            Collections.swap(data,ltGovPosition,1);
        }




        representativeList=data;
        displayGenerator.notifyDataSetChanged();

        //RepresentativeDisplayGenerator thisGen=new RepresentativeDisplayGenerator(this, data);
        //representativeDisplay.setAdapter(thisGen);
        searchWindow.setVisibility(View.INVISIBLE);
        loadingWindow.setVisibility(View.INVISIBLE);




    }


}
