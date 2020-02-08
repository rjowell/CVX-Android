package com.cvx4u.cvx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.analytics.FirebaseAnalytics;
import android.widget.Toast;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.android.gms.tasks.Task;




import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;




/*class AddDeviceToken extends AsyncTask<Void, Void, Void>
{

    @Override
    protected Void doInBackground(Void... voids) {

        String refreshedToken= FirebaseInstanceId.getInstance().getToken();
        System.out.println("dookie");
        System.out.println(refreshedToken);

        try {
            URL url = new URL("http://www.cvx4u.com/web_service/add_android_push_key.php");

            System.out.println("dookie-11");
            System.out.println("Token is"+refreshedToken);
            String testString="keyval=";
            String dataString = URLEncoder.encode("keyval", "UTF-8") + "=" + URLEncoder.encode(refreshedToken, "UTF-8");
            System.out.println(dataString);
            System.out.println("this here");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.getOutputStream().write(testString.getBytes());
            //conn.setDoOutput(true);
            System.out.println("step 11");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(refreshedToken);
            System.out.println("step 22");
            wr.flush();
            System.out.println(conn.getResponseCode());
            String text="";
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }


            text = sb.toString();
            System.out.println("tessst iss");
            System.out.println(text);
        }
        catch(Exception eexx)
        {
            System.out.println("errrror is");
            System.out.println(eexx);
        }

return null;

    }
}*/



class HomeScreenUpdater extends AsyncTask<String, Void, HashMap<String,String>>
{

    HashMap<String,String> jsonData;
    ArrayList<String> statesToGet;
    public DeliverHomeScreenData dataDeliver;

    public HomeScreenUpdater(ArrayList<String> states, DeliverHomeScreenData sendOut)
    {
        statesToGet=states;
        dataDeliver=sendOut;
        statesToGet=states;
        jsonData=new HashMap<>();
        System.out.println("Weere heere");
    }


    @Override
    protected void onPostExecute(HashMap<String,String> newData)
    {
        System.out.println(newData.size());
        dataDeliver.deliverUpdates(newData);
    }


    @Override
    protected HashMap<String,String> doInBackground(String... strings)
    {
        System.out.println("cheese---burger");
        System.out.println(statesToGet.size());

        for(String states:statesToGet)
        {
            System.out.println("stateees are"+states);
        }



        for(String states:statesToGet)
        {
            System.out.println("poopy"+states);


        try {




                URL stateMasterURL = new URL("https://api.legiscan.com/?key=438bd89d24f6339ea0adcc17fd06cd58&op=getMasterList&state="+states);
                HttpURLConnection connection=(HttpURLConnection) stateMasterURL.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder=new StringBuilder();
                System.out.println("poopy1");
                builder.append(reader.readLine());
                System.out.println("poopy2");
                System.out.println("Data is "+builder.toString());

                jsonData.put(states, builder.toString());

                reader.close();

                System.out.println("Your sizzze is "+jsonData.size());


                //return jsonData;


            }
            catch(Exception iiee)
            {
                System.out.println(iiee.toString());
            }
            System.out.println("Siizzeeee "+jsonData.size());}

        return jsonData;

    }


        //return jsonData;
    }






public class HomeScreen extends AppCompatActivity implements DeliverHomeScreenData {


    //Button voterRegistration=(Button) findViewById(R.id.homeVoteReg);

    Button meetYourGovernment;

    private FirebaseAnalytics mFirebaseAnalytics;

    Button trackLegislation;

    ListView followTable;

    ConstraintLayout loadingWindow;

    HomeScreenGenerator homeGen;

    Button registerToVote;

    Button seeUpcomingElections;

    Button openFeedback;

    ArrayList<FollowItem> currentFollowList;
    ArrayList<String> statesToFollow;

    @Override
    protected void onPause() {

        super.onPause();
        homeGen.saveFollowList();


    }






    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        System.out.println("Token IUSSSS");
       System.out.println(FirebaseInstanceId.getInstance().getToken());
        //new AddDeviceToken().execute();


        FirebaseMessaging.getInstance().subscribeToTopic("news");


               /* .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });*/



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle=new Bundle();
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE,"app_open");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN,bundle);
        loadingWindow=findViewById(R.id.loadingWindow);
        loadingWindow.setVisibility(View.VISIBLE);
        meetYourGovernment=(Button) findViewById(R.id.homeMeetGovt);
        trackLegislation=(Button) findViewById(R.id.homeTrackLegis);
        registerToVote=(Button) findViewById(R.id.homeVoteReg);
        seeUpcomingElections=(Button) findViewById(R.id.homeUpcomingVotes2);
        openFeedback=(Button) findViewById(R.id.homeFeedback);
        followTable=findViewById(R.id.followUpdates);
        System.out.println("right here--11");
        meetYourGovernment.setId(0);
        trackLegislation.setId(1);
        registerToVote.setId(2);
        seeUpcomingElections.setId(3);
        statesToFollow=new ArrayList<>();
        openFeedback.setId(4);
        currentFollowList=new ArrayList<>();
        homeGen=new HomeScreenGenerator(currentFollowList, this);
        System.out.println("main screen");
        try {


            /*File testFile=new File("followListData");

            if(testFile.exists()==false) {
            FileOutputStream fos = openFileOutput("followListData", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);


            //oos.writeObject(currentFollowList);
            oos.close();
            fos.close();
            }*/

            //System.out.println("Size is "+testFile.length());

            //System.out.println(Files.size(new File("followListData").toPath()));


            System.out.println("interior loop");
            FileInputStream fis = openFileInput("followListData");
            System.out.println("interior loop-1");
            ObjectInputStream ois = new ObjectInputStream(fis);
            System.out.println("interior loop-2");


                currentFollowList = (ArrayList<FollowItem>) ois.readObject();
                System.out.println("interior loop-3");
                System.out.println(currentFollowList.size());

                ois.close();
                fis.close();
                System.out.println("right here--22");
                for (FollowItem theseStuff : currentFollowList) {
                    System.out.println("right here--33");
                    System.out.println(theseStuff.electionStateAbbrev);
                    statesToFollow.add(theseStuff.electionStateAbbrev);
                }
                System.out.println("right here--44");
                new HomeScreenUpdater(statesToFollow, this).execute();

            /*else
            {
                HomeScreenGenerator thisGen=new HomeScreenGenerator(new ArrayList<FollowItem>(0), this);
                followTable.setAdapter(thisGen);
            }*/

            /*for(FollowItem these:currentFollowList)
            {
                System.out.println("numberssss");
                System.out.println(these.billNumber);
            }

            followTable.setAdapter(new HomeScreenGenerator(currentFollowList, this));*/
        }
        catch(Exception iiee)
        {
            System.out.println("input error");
            System.out.println(iiee.toString());
            followTable.setAdapter(homeGen);
            loadingWindow.setVisibility(View.INVISIBLE);
        }


        /*
        Welcome To CVX
        CVX is an app that puts government in the palm of your hand. This is your dashboard, where you can track pieces of legislation making their way through the lawmaking process, or check the status of upcoming elections in your area. Try clicking on one of the options below and see what CVX can do for you!
         */




        View.OnClickListener buttonProcess= new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent openDestination;

                Intent[] arrayOfIntents={new Intent(HomeScreen.this, RepresentativeFinder.class),new Intent(HomeScreen.this, LegislationStateSelector.class), new Intent(HomeScreen.this, VoterRegistrationInfo.class), new Intent(HomeScreen.this, ElectionCalendar.class), new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto","russ@cvx4u.com",null))};

                openDestination=arrayOfIntents[view.getId()];

                if(view.getId()==4)
                {

                    openDestination.putExtra(Intent.EXTRA_SUBJECT, "App Feedback");
                    startActivity(Intent.createChooser(openDestination, "Send Feedback"));

                }
                else {
                    startActivity(openDestination);
                }

            }
        };

        meetYourGovernment.setOnClickListener(buttonProcess);
        trackLegislation.setOnClickListener(buttonProcess);
        registerToVote.setOnClickListener(buttonProcess);
        seeUpcomingElections.setOnClickListener(buttonProcess);
        openFeedback.setOnClickListener(buttonProcess);


    }

    @Override
    public void deliverUpdates(HashMap<String,String> newData)
    {


        System.out.println("destination 1");
        System.out.println(currentFollowList.size());

        ArrayMap<String, String> thisData;

        for(FollowItem currentThings:currentFollowList)
        {
            System.out.println(currentThings.electionStateAbbrev);
            System.out.println(newData.size());

            try
            {
                System.out.println("dest 2");
                //System.out.println(newData.get(currentThings.electionStateAbbrev));
                JSONObject masterListData = new JSONObject(newData.get(currentThings.electionStateAbbrev)).getJSONObject("masterlist");
                System.out.println("dest 3");
                Iterator<String> iter=masterListData.keys();
                System.out.println("dest 4");
                while(iter.hasNext())
                {
                    String key=iter.next();
                    //System.out.println("Key is "+key);

                    if(key.compareTo("session")!=0) {
                        JSONObject currentThing = masterListData.getJSONObject(key);

                        //System.out.println(currentThing.getString("bill_id"));

                        if(currentThing.getString("bill_id").compareTo(currentThings.billId)==0)
                        {
                            if(currentThings.lastStatusDate.compareTo(currentThing.getString("last_action_date"))!=0)
                            {
                                currentThings.lastStatusChange=currentThing.getString("last_action");
                                currentThings.lastStatusDate=currentThing.getString("last_action_date");
                            }


                        }


                    }




                }


            }
            catch(Exception iiee)
            {
                System.out.println(iiee.toString());
            }
        }
        homeGen=new HomeScreenGenerator(currentFollowList,this);
        followTable.setAdapter(homeGen);
        loadingWindow.setVisibility(View.INVISIBLE);

    }
}
