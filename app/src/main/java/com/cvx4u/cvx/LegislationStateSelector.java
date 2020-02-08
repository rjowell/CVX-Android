package com.cvx4u.cvx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;








public class LegislationStateSelector extends AppCompatActivity {


    String[] stateList={"US","US House & Senate","AL","Alabama","AK","Alaska","AR","Arkansas","AZ","Arizona","CA","California","CO","Colorado","CT","Connecticut","DE","Delaware","FL","Florida","GA","Georgia","HI","Hawaii","ID","Idaho","IL","Illinois","IN","Indiana","IA","Iowa","KS","Kansas","KY","Kentucky","LA","Louisiana","ME","Maine","MD","Maryland","MA","Massachusetts","MI","Michigan","MN","Minnesota","MO","Missouri","MS","Mississippi","MT","Montana","NC","North Carolina","ND","North Dakota","NE","Nebraska","NH","New Hampshire","NJ","New Jersey","NM","New Mexico","NV","Nevada","NY","New York","OH","Ohio","OK","Oklahoma","OR","Oregon","PA","Pennsylvania","RI","Rhode Island","SC","South Carolina","SD","South Dakota","TN","Tennessee","TX","Texas","UT","Utah","VA","Virginia","VT","Vermont","WA","Washington","DC","Washington, DC","WI","Wisconsin","WV","West Virginia","WY","Wyoming"};


    ListView stateButtons;

    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legislation_state_selector);

        stateButtons=(ListView) findViewById(R.id.stateSelection);

        cancelButton=(Button) findViewById(R.id.cancelButton);

        View.OnClickListener cancelProcess=new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goHome=new Intent(LegislationStateSelector.this, HomeScreen.class);
                startActivity(goHome);
            }
        };

        cancelButton.setOnClickListener(cancelProcess);

        LawViewStateListGenerator adapter=new LawViewStateListGenerator(this);

        stateButtons.setAdapter(adapter);
    }
}
