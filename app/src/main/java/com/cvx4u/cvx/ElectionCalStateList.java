package com.cvx4u.cvx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;


public class ElectionCalStateList extends AppCompatActivity {


    String[][] stateList={{"US","US House & Senate"},{"AL","Alabama"},{"AK","Alaska"},{"AR","Arkansas"},{"AZ","Arizona"},{"CA","California"},{"CO","Colorado"},{"CT","Connecticut"},{"DE","Delaware"},{"FL","Florida"},{"GA","Georgia"},{"HI","Hawaii"},{"ID","Idaho"},{"IL","Illinois"},{"IN","Indiana"},{"IA","Iowa"},{"KS","Kansas"},{"KY","Kentucky"},{"LA","Louisiana"},{"ME","Maine"},{"MD","Maryland"},{"MA","Massachusetts"},{"MI","Michigan"},{"MN","Minnesota"},{"MO","Missouri"},{"MS","Mississippi"},{"MT","Montana"},{"NC","North Carolina"},{"ND","North Dakota"},{"NE","Nebraska"},{"NH","New Hampshire"},{"NJ","New Jersey"},{"NM","New Mexico"},{"NV","Nevada"},{"NY","New York"},{"OH","Ohio"},{"OK","Oklahoma"},{"OR","Oregon"},{"PA","Pennsylvania"},{"RI","Rhode Island"},{"SC","South Carolina"},{"SD","South Dakota"},{"TN","Tennessee"},{"TX","Texas"},{"UT","Utah"},{"VA","Virginia"},{"VT","Vermont"},{"WA","Washington"},{"DC","Washington, DC"},{"WI","Wisconsin"},{"WV","West Virginia"},{"WY","Wyoming"}};

    ListView stateListDisplay;

    Button goBackButton;
    Button cancelButton;
    SharedPreferences stateListSave;
    SharedPreferences.Editor stateListEditor;
    ElectionCalStateGenerator calGen;


    View.OnClickListener buttonProcess=new View.OnClickListener()
    {
        @Override
        public void onClick(View view) {

            Button currentButton=(Button)view;


                System.out.println("Dont button");
                calGen.closeEditing();
                Intent goToElectionCal=new Intent(ElectionCalStateList.this, ElectionCalendar.class);
                startActivity(goToElectionCal);




        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_election_cal_state_list);

        stateListDisplay=findViewById(R.id.stateListDisplay);

        stateListDisplay.setDivider(null);

        goBackButton=findViewById(R.id.goBack);
        cancelButton=findViewById(R.id.cancel);
        goBackButton.setOnClickListener(buttonProcess);
        goBackButton.setId(0);
        cancelButton.setOnClickListener(buttonProcess);

        calGen=new ElectionCalStateGenerator(this);

        stateListDisplay.setAdapter(calGen);



    }
}
