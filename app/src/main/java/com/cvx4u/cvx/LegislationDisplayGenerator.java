package com.cvx4u.cvx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


class OpenBillLink extends AsyncTask<String,Void,String>
{
    URL billURL;
    Context localContext;
    String localBillNo;
    String legiScanURL="https://api.legiscan.com/?key=438bd89d24f6339ea0adcc17fd06cd58&op=getBill&id=";

    public OpenBillLink(String billNumber, Context inputContext){

        localContext=inputContext;
        localBillNo=billNumber;

    }


    @Override
    protected String doInBackground(String... strings) {




                try {
                    billURL = new URL("https://api.legiscan.com/?key=438bd89d24f6339ea0adcc17fd06cd58&op=getBill&id=" + localBillNo);
                    HttpURLConnection connector=(HttpURLConnection) billURL.openConnection();

                    connector.setRequestMethod("GET");

                    connector.connect();

                    BufferedReader reader=new BufferedReader(new InputStreamReader(connector.getInputStream()));

                    String lineStuff;

                    StringBuilder builder=new StringBuilder();
                    builder.append(reader.readLine());

                    lineStuff=builder.toString();

                    reader.close();

                    JSONObject billJSONData=new JSONObject(lineStuff);

                    String stateBillLink=billJSONData.getJSONObject("bill").getString("state_link");

                    Intent openFullBill=new Intent(Intent.ACTION_VIEW, Uri.parse(stateBillLink));
                    localContext.startActivity(openFullBill);


                }
                catch(Exception iiee)
                {
                    System.out.println(iiee.toString());
                }






        return null;
    }
}



public class LegislationDisplayGenerator extends BaseAdapter {


    String stateRawData="";
    LegislationDisplay dataSaver;
    JSONObject stateJSONData=null;
    String[] billNumbers;
    String[] billTitles;
    String[] billDescriptions;
    String[] lastStatusDates;
    ArrayList<FollowItem> currentFollowList=new ArrayList<>();
    String[] lastStatuses;
    String[] fullTextLinks;
    File file;
    LegislationItem[] lawItemArray;

    FileOutputStream fileStream;

    File saveFile;
    Boolean followListExists;

    private Context thisContext;
    private LayoutInflater thisInflater;
    String localStateName;
    //ArrayList<FollowItem> currentFollowList;

    int arrayCount;


    public LegislationDisplayGenerator(LegislationItem[] dataInput,String stateName, Context theContext, LegislationDisplay legisDispl)
    {
        thisContext=theContext;

        dataSaver=legisDispl;



        localStateName=stateName;

        thisInflater=(LayoutInflater) thisContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        lawItemArray=dataInput;
        file=thisContext.getFileStreamPath("followListData");
        System.out.println("flie size isss :"+file.toString());
        System.out.println("point 1..");
        if(file == null || !file.exists())
        {
            followListExists=false;
            System.out.println("point 2..");
            try {
                FileOutputStream fout = thisContext.openFileOutput("followListData", Context.MODE_PRIVATE);
                System.out.println("point 3..");
                OutputStreamWriter osw=new OutputStreamWriter(fout);

                //osw.write("");

                osw.flush();
                osw.close();
                fout.close();
            }
            catch(Exception iiee)
            {
                System.out.println("errorr russ 123");
                System.out.println(iiee.toString());
            }

            System.out.println("it doesn't exist");
        }
        //System.out.println("point 4..");
        else if(file.length()==0)
    {

    }
    else {
            System.out.println("flie size isss :"+file.length());

        try {
            System.out.println("cheese----");
            FileInputStream fis = thisContext.openFileInput("followListData");
            System.out.println("burger----");
            ObjectInputStream ois = new ObjectInputStream(fis);
            System.out.println("fries----");
            if(file.length() < 10)
            {
                currentFollowList=new ArrayList<>();
            }
            else {
                currentFollowList = (ArrayList<FollowItem>) ois.readObject();
            }
            System.out.println("shake----");
            ois.close();
            fis.close();
        } catch (Exception iiee) {
            System.out.println(iiee.toString());
        }
    }

        System.out.println("Saved array is "+currentFollowList.size());
        System.out.println("Your arrays count is "+lawItemArray.length);

        arrayCount=lawItemArray.length-1;






    }




    @Override
    public int getCount() {
        return arrayCount;
    }






    public void saveFollowList()
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
    public Object getItem(int i) {
        return lawItemArray[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View legislativeRow=thisInflater.inflate(R.layout.legislation_info_cell, viewGroup,false);

        TextView viewBillNumber=(TextView) legislativeRow.findViewById(R.id.billNumber);
        viewBillNumber.setText((CharSequence) lawItemArray[i].billNumber);

        TextView viewBillTitle=(TextView) legislativeRow.findViewById(R.id.billTitle);
        viewBillTitle.setText((CharSequence) lawItemArray[i].billTitle);

        TextView viewBillDesc=(TextView) legislativeRow.findViewById(R.id.billDescription);
        viewBillDesc.setText((CharSequence) lawItemArray[i].billDescription);

        TextView viewStatusDate=(TextView) legislativeRow.findViewById(R.id.statusDate);
        viewStatusDate.setText((CharSequence) lawItemArray[i].billLastActionDate);

        TextView viewStatus=(TextView) legislativeRow.findViewById(R.id.statusDesc);
        viewStatus.setText((CharSequence) lawItemArray[i].billLastAction);

        Button fullTextButton=(Button) legislativeRow.findViewById(R.id.fullText);
        fullTextButton.setId(Integer.parseInt(lawItemArray[i].billID));
        fullTextButton.setOnClickListener(openFullText);


        Button followThisButton=(Button) legislativeRow.findViewById(R.id.followthis);
        followThisButton.setId(Integer.parseInt(lawItemArray[i].billID));
        followThisButton.setTag(i);
        followThisButton.setOnClickListener(followThis);



           for(FollowItem thisJunk:currentFollowList)
           {

               System.out.println("numbers here");
               System.out.println(thisJunk.billNumber);
               System.out.println(lawItemArray[i].billID);


               if(thisJunk.billId.compareTo(lawItemArray[i].billID)==0)
               {
                   System.out.println("true that");
                   System.out.println(thisJunk.billNumber);
                   System.out.println(lawItemArray[i].billID);
                   followThisButton.setText(R.string.unfollow);
               }
           }









        return legislativeRow;
    }




    View.OnClickListener openFullText=new View.OnClickListener()
    {

        @Override
        public void onClick(View view) {
            new OpenBillLink(Integer.toString(view.getId()), thisContext).execute();
        }
    };



    View.OnClickListener followThis=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Button currentButton=(Button)view;
            System.out.println("label is");
            System.out.println(currentButton.getText());

            if(currentButton.getText().toString().compareTo("Unfollow")==0)
            {
                System.out.println("this string heeer");
                currentButton.setText(R.string.followThis);
                try
                {
                    //FileInputStream fis=thisContext.openFileInput("followListData");
                    //ObjectInputStream ois=new ObjectInputStream(fis);
                    //currentFollowList=(ArrayList<FollowItem>) ois.readObject();
                    for(FollowItem thisThing:currentFollowList)
                    {
                        if(Integer.parseInt(thisThing.billNumber)==view.getId())
                        {
                            currentFollowList.remove(thisThing);
                        }

                    }
                    System.out.println("new size is");
                    System.out.println(currentFollowList.size());
                    /*FileOutputStream fos=thisContext.openFileOutput("followListData", Context.MODE_PRIVATE);
                    ObjectOutputStream oos=new ObjectOutputStream(fos);
                    oos.writeObject(currentFollowList);
                    oos.close();
                    fos.close();*/


                }
                catch(Exception iiee)
                {
                    System.out.println(iiee.toString());
                }
            }
            else
            {

            System.out.println("ID is---- "+view.getId());

            currentButton.setText(R.string.unfollow);
                //File thisFile=thisContext.getFileStreamPath("followListData");
                //ArrayList<FollowItem> currentFollowList=new ArrayList<>();



                FollowItem thisItem=new FollowItem();
                thisItem.billNumber=lawItemArray[Integer.parseInt(view.getTag().toString())].billNumber;
                thisItem.billId=Integer.toString(view.getId());
                thisItem.isLegislation=true;
                thisItem.lastStatusDate=lawItemArray[Integer.parseInt(view.getTag().toString())].billLastActionDate;
                thisItem.billTitle=lawItemArray[Integer.parseInt(view.getTag().toString())].billTitle;
                thisItem.electionStateAbbrev=localStateName;
                thisItem.lastStatusChange=lawItemArray[Integer.parseInt(view.getTag().toString())].billLastAction;
                boolean itemExists=false;
                for(FollowItem thisThing:currentFollowList)
                {
                    if(Integer.parseInt(thisThing.billId)==view.getId())
                    {
                        itemExists=true;
                        break;
                    }
                }



                if(itemExists==false) {
                    currentFollowList.add(thisItem);
                }

                System.out.println("Array here");
                System.out.println(currentFollowList.size());
                for(FollowItem thiese:currentFollowList)
                {
                    System.out.println("IS is "+thiese.billNumber);
                }

            /*try {

                System.out.println(thisContext.getFileStreamPath("followListData").exists());

                FileOutputStream fos=thisContext.openFileOutput("followListData", Context.MODE_PRIVATE);
                ObjectOutputStream oos=new ObjectOutputStream(fos);
                oos.writeObject(currentFollowList);
                oos.close();
                fos.close();
            }
            catch(Exception iiee)
            {
                System.out.println(iiee.toString());
            }*/

        }}
    };

    /*
    //save
    fileStream=this.openFileOutput(stateAbbr+"stateInfo", Context.MODE_PRIVATE);
            ObjectOutputStream oos=new ObjectOutputStream(fileStream);
            oos.writeObject(lawItemArray);
            oos.close();
            fileStream.close();

            SharedPreferences timeKeeper=getSharedPreferences(stateAbbr+"lastUpdateTime",0);
            SharedPreferences.Editor timeEditorA=timeKeeper.edit();

            timeEditorA.putLong(stateAbbr+"lastUpdateTime",Calendar.getInstance().getTimeInMillis());

            timeEditorA.commit();



            //read out
            FileInputStream fileIn = this.openFileInput(stateAbbr+"stateInfo");
                ObjectInputStream ois = new ObjectInputStream(fileIn);

                //ObjectOutputStream oos=new ObjectOutputStream(fileStream);
                System.out.println("hello");

                LegislationItem[] dataArray=(LegislationItem[]) ois.readObject();
                ois.close();
                fileIn.close();
     */
}
