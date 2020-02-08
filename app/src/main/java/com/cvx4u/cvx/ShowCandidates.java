package com.cvx4u.cvx;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


class Candidate
{
    String name;
    String office;
    String party;
    String phoneNumber;
    String website;
    String email;
    String candidateID;
    String photoURL;
    Bitmap candidatePhoto;
}

class GetCandidateData extends AsyncTask<String,Void,ArrayList<Candidate>>
{

    HttpURLConnection connector=null;
    String electionNumber;
    public DeliverCandidateResults thisDelegate=null;

    public GetCandidateData(DeliverCandidateResults thisDel)
    {
        this.thisDelegate=thisDel;
    }

    ArrayList<Candidate> candidateList=new ArrayList<>();

    String voteSmartURL="http://api.votesmart.org/";
    String candidatesByElection="Candidates.getByElection";
    String candidatesInfo="Address.getCampaign";
    String candidateWebString="Address.getCampaignWebAddress";
    String candidateWebInfo="Address.getOfficeWebAddress";
    String candidatePhoto="CandidateBio.getBio";

    String voteSmartKey="key=6b633e49549b6c63c659be06c2ee75f9";

    String currentURL;

    BufferedReader reader;


    @Override
    protected void onPostExecute(ArrayList<Candidate> candidates) {
        super.onPostExecute(candidates);

        thisDelegate.deliverData(candidates);


    }

    @Override
    protected ArrayList<Candidate> doInBackground(String... strings) {

        String rawData;

        electionNumber=strings[0];

        System.out.println("Background ID is "+electionNumber);

        currentURL=voteSmartURL+candidatesByElection+"?"+voteSmartKey+"&o=JSON&electionId="+electionNumber;
        System.out.println(currentURL.toString());

        try {
            URL candidateListURL = new URL(currentURL);
            connector=(HttpURLConnection) candidateListURL.openConnection();
            connector.setRequestMethod("GET");
            connector.connect();

            reader=new BufferedReader(new InputStreamReader(connector.getInputStream()));

            StringBuilder builder=new StringBuilder();
            builder.append(reader.readLine());

            rawData=builder.toString();

            reader.close();

            JSONObject listOfElectionCandidates=new JSONObject(rawData);
            System.out.println(listOfElectionCandidates.length());
            //Array of candidates
            if(rawData.contains("\"candidate\":["))
            {
                JSONArray rawCandidateArray=listOfElectionCandidates.getJSONObject("candidateList").getJSONArray("candidate");

                for(int iterate=0; iterate<rawCandidateArray.length(); iterate++)
                {
                    Candidate thisCandidateHere=new Candidate();
                    JSONObject currentCandidate=rawCandidateArray.getJSONObject(iterate);
                    System.out.println(currentCandidate.getString("candidateId"));
                    thisCandidateHere.candidateID=currentCandidate.getString("candidateId");
                    thisCandidateHere.name=currentCandidate.getString("firstName")+" "+currentCandidate.getString("lastName");
                    thisCandidateHere.office=currentCandidate.getString("electionOffice");
                    thisCandidateHere.party=currentCandidate.getString("electionParties");

                    candidateList.add(thisCandidateHere);

                }
            }
            else
            {
                JSONObject rawCandidateArray=listOfElectionCandidates.getJSONObject("candidateList").getJSONObject("candidate");

                Candidate thisCandidateHere=new Candidate();
                //JSONObject currentCandidate=rawCandidateArray.getJSONObject(iterate);
                System.out.println(rawCandidateArray.getString("candidateId"));
                thisCandidateHere.candidateID=rawCandidateArray.getString("candidateId");
                thisCandidateHere.name=rawCandidateArray.getString("firstName")+" "+rawCandidateArray.getString("lastName");
                thisCandidateHere.party=rawCandidateArray.getString("electionParties");
                thisCandidateHere.office=rawCandidateArray.getString("electionOffice");
                candidateList.add(thisCandidateHere);

            }



           /* JSONArray rawCandidateArray=listOfElectionCandidates.getJSONObject("candidateList").getJSONArray("candidate");
            System.out.println("this russ 123-111");

            System.out.println("JSON ARRAY:"+rawCandidateArray.length());

            for(int iterate=0; iterate<rawCandidateArray.length(); iterate++)
            {
                Candidate thisCandidateHere=new Candidate();
                JSONObject currentCandidate=rawCandidateArray.getJSONObject(iterate);
                System.out.println(currentCandidate.getString("candidateId"));
                thisCandidateHere.candidateID=currentCandidate.getString("candidateId");
                thisCandidateHere.name=currentCandidate.getString("firstName")+" "+currentCandidate.getString("lastName");
                thisCandidateHere.party=currentCandidate.getString("electionParties");
                candidateList.add(thisCandidateHere);

            }*/

            for (Candidate thisGuy:candidateList)
            {
                currentURL=voteSmartURL+candidatesInfo+"?"+voteSmartKey+"&o=JSON&candidateId="+thisGuy.candidateID;
                System.out.println(currentURL);
                URL candidatePhoneURL=new URL(currentURL);
                connector=(HttpURLConnection) candidatePhoneURL.openConnection();

                reader=new BufferedReader(new InputStreamReader(connector.getInputStream()));

                builder=new StringBuilder();
                builder.append(reader.readLine());

                rawData=builder.toString();

                reader.close();
                System.out.println("here you gooo");

                if(rawData.contains("error")==false) {

                    System.out.println("here you gooo-0099");
                    JSONObject phoneInfo = new JSONObject(rawData);

                    System.out.println("here you gooo-0099988");
                    if(rawData.contains("\"office\":["))
                    {
                        thisGuy.phoneNumber=phoneInfo.getJSONObject("address").getJSONArray("office").getJSONObject(0).getJSONObject("phone").getString("phone1");
                    }
                    else
                    {
                        JSONObject phoneList = phoneInfo.getJSONObject("address").getJSONObject("office").getJSONObject("phone");

                        thisGuy.phoneNumber=phoneList.getString("phone1");
                    }


                   // System.out.println(phoneList.keys());
                }

                currentURL=voteSmartURL+candidateWebString+"?"+voteSmartKey+"&o=JSON&candidateId="+thisGuy.candidateID;

                System.out.println(currentURL);
                URL candidateWebURL=new URL(currentURL);
                connector=(HttpURLConnection) candidateWebURL.openConnection();

                reader=new BufferedReader(new InputStreamReader(connector.getInputStream()));

                builder=new StringBuilder();
                builder.append(reader.readLine());

                rawData=builder.toString();

                reader.close();
                System.out.println(rawData);

                if(rawData.contains("error")==false)
                {
                    System.out.println("choooooose-asbcde");
                    JSONObject webInfo=new JSONObject(rawData);
                    System.out.println("choooooose");

                    if(webInfo.getJSONObject("webaddress").toString().contains("\"address\":["))
                    {
                        System.out.println("choooooose-000");
                        JSONArray websiteList=webInfo.getJSONObject("webaddress").getJSONArray("address");
                        System.out.println("choooooose-111222");
                        for(int iterator=0;iterator<websiteList.length();iterator++)
                        {
                            if(websiteList.getJSONObject(iterator).getString("webAddressType")=="Website")
                            {
                                thisGuy.website=websiteList.getJSONObject(iterator).getString("webAddress");
                            }
                            if(websiteList.getJSONObject(iterator).getString("webAddressType")=="Email")
                            {
                                thisGuy.email=websiteList.getJSONObject(iterator).getString("webAddress");
                            }
                        }
                    }
                    else
                    {
                        JSONObject websiteList=webInfo.getJSONObject("webaddress").getJSONObject("address");

                        if(websiteList.getString("webAddressType")=="Website")
                        {
                            thisGuy.website=websiteList.getString("webAddress");
                        }
                        if(websiteList.getString("webAddressType")=="Email")
                        {
                            thisGuy.email=websiteList.getString("webAddress");
                        }

                    }



                }

                currentURL=voteSmartURL+candidatePhoto+"?"+voteSmartKey+"&o=JSON&candidateId="+thisGuy.candidateID;

                URL candidatePhotoURL=new URL(currentURL);
                connector=(HttpURLConnection) candidatePhotoURL.openConnection();

                reader=new BufferedReader(new InputStreamReader(connector.getInputStream()));

                builder=new StringBuilder();
                builder.append(reader.readLine());

                rawData=builder.toString();

                reader.close();
                System.out.println("photo data here");
                System.out.println(rawData);

                if(rawData.contains("error")==false)
                {
                    JSONObject photoInfo=new JSONObject(rawData);
                    thisGuy.photoURL=photoInfo.getJSONObject("bio").getJSONObject("candidate").getString("photo");


                    InputStream in=new java.net.URL(thisGuy.photoURL).openStream();
                    thisGuy.candidatePhoto=BitmapFactory.decodeStream(in);


                    //Bitmap candidatePicture= BitmapFactory.decodeStream(in);
                    //candidatePhoto.setImageBitmap(candidatePicture);




                }

            }

            /*for(Candidate currentThingg:candidateList)
            {
                System.out.println(currentThingg.candidateID);
                System.out.println(currentThingg.email);
                System.out.println(currentThingg.name);
                System.out.println(currentThingg.office);
                System.out.println(currentThingg.party);
                System.out.println(currentThingg.phoneNumber);
                System.out.println(currentThingg.photoURL);
                System.out.println(currentThingg.website);
            }*/






        }
        catch(Exception iiee)
        {
            System.out.println(iiee.toString());
        }

        //System.out.println(currentURL);




        return candidateList;
    }








}







public class ShowCandidates extends AppCompatActivity implements DeliverCandidateResults {


    //var keyString: String="key=6b633e49549b6c63c659be06c2ee75f9"
    String voteSmartURL="http://api.votesmart.org/";
    String candidatesByElection="Candidates.getByElection";
    String candidatesInfo="Address.getCampaign";
    String candidateWebString="Address.getCampaignWebAddress";
    String candidateWebInfo="Address.getOfficeWebAddress";
    String candidatePhoto="CandidateBio.getBio";
    Button backToCalendar;
    TextView electionName;
    ConstraintLayout loadingWindow;

    ListView candidateViewer;

    String voteSmartKey="key=6b633e49549b6c63c659be06c2ee75f9";
    String electionId;

    Intent pastIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_candidates);
        candidateViewer=findViewById(R.id.candidateList);
        pastIntent=getIntent();
        loadingWindow=findViewById(R.id.loadingWindow);
        loadingWindow.setVisibility(View.VISIBLE);
        electionName=findViewById(R.id.electionName);
        electionName.setText(pastIntent.getStringExtra("Election_Name"));
        backToCalendar=findViewById(R.id.backToCalendar);
        backToCalendar.setOnClickListener(backToCal);
        electionId=pastIntent.getStringExtra("ElectionID");
        System.out.println("Election ID is "+electionId);

        new GetCandidateData(this).execute(electionId);


    }

    View.OnClickListener backToCal= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent goHome=new Intent(ShowCandidates.this, ElectionCalendar.class);
            startActivity(goHome);
        }
    };

    @Override
    public void deliverData(ArrayList<Candidate> inputData) {

        System.out.println("candidiate data dheer");
        CandidateWindowGenerator currentGenerator=new CandidateWindowGenerator(inputData, this);
        candidateViewer.setAdapter(currentGenerator);
        loadingWindow.setVisibility(View.INVISIBLE);

    }
}
