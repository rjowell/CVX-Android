package com.cvx4u.cvx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.zip.Inflater;



/**
 * Created by laurenpiera on 2/21/18.
 */

public class ElectionCalStateGenerator extends BaseAdapter {








    String[][] stateList={{"US","US House & Senate"},{"AL","Alabama"},{"AK","Alaska"},{"AR","Arkansas"},{"AZ","Arizona"},{"CA","California"},{"CO","Colorado"},{"CT","Connecticut"},{"DE","Delaware"},{"FL","Florida"},{"GA","Georgia"},{"HI","Hawaii"},{"ID","Idaho"},{"IL","Illinois"},{"IN","Indiana"},{"IA","Iowa"},{"KS","Kansas"},{"KY","Kentucky"},{"LA","Louisiana"},{"ME","Maine"},{"MD","Maryland"},{"MA","Massachusetts"},{"MI","Michigan"},{"MN","Minnesota"},{"MO","Missouri"},{"MS","Mississippi"},{"MT","Montana"},{"NC","North Carolina"},{"ND","North Dakota"},{"NE","Nebraska"},{"NH","New Hampshire"},{"NJ","New Jersey"},{"NM","New Mexico"},{"NV","Nevada"},{"NY","New York"},{"OH","Ohio"},{"OK","Oklahoma"},{"OR","Oregon"},{"PA","Pennsylvania"},{"RI","Rhode Island"},{"SC","South Carolina"},{"SD","South Dakota"},{"TN","Tennessee"},{"TX","Texas"},{"UT","Utah"},{"VA","Virginia"},{"VT","Vermont"},{"WA","Washington"},{"DC","Washington, DC"},{"WI","Wisconsin"},{"WV","West Virginia"},{"WY","Wyoming"}};

    ArrayList<String> selectedStates;
    Set<String> testArray=new HashSet<String>();


    //String[] selectedStates={};


    private Context localContext;
    private LayoutInflater localInflater;
    SharedPreferences stateListSave;
    SharedPreferences.Editor stateListEditor;




    public ElectionCalStateGenerator(Context context)
    {
        localContext=context;
        localInflater=(LayoutInflater) localContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        stateListSave=context.getSharedPreferences("ElectionCalStates",0);
        stateListEditor=stateListSave.edit();
        selectedStates=new ArrayList<String>();
        Set<String> currentStuff=stateListSave.getStringSet("ElectionSaveStates", null);
        if(currentStuff != null) {
            selectedStates.addAll(currentStuff);
        }
        System.out.println("statesssss");
        System.out.println(selectedStates);
        for (String state:selectedStates) {
            System.out.println(state);
        }




    }

    public void closeEditing()
    {
        stateListEditor.commit();
    }


    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int i) {
        return stateList[i+1][1];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View stateSelectionRow=localInflater.inflate(R.layout.election_cal_state_cell, viewGroup,false);

        CheckBox currentState=(CheckBox) stateSelectionRow.findViewById(R.id.stateName);

        System.out.println(currentState.getText());

        currentState.setText(stateList[i+1][1]);
        currentState.setTag(stateList[i+1][0]);


        if(selectedStates.contains(stateList[i+1][0]))
        {
            currentState.setChecked(true);
        }



        currentState.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked())
                {
                    System.out.println("State added");
                    //System.out.println(compoundButton.getText()+" is checked");
                    if(selectedStates.contains(compoundButton.getTag())==false)
                    {
                        selectedStates.add((String) compoundButton.getTag());
                        testArray.addAll(selectedStates);
                        stateListEditor.putStringSet("ElectionSaveStates", testArray);
                    }
                }
                else
                {
                    System.out.println("State removed");
                    if(selectedStates.contains(compoundButton.getTag())==true)
                    {
                        selectedStates.remove(compoundButton.getTag());
                        testArray.addAll(selectedStates);
                        stateListEditor.putStringSet("ElectionSaveStates", testArray);
                    }


                    //System.out.println(compoundButton.getText()+" is not checked");
                }

                System.out.println(selectedStates);

            }
        });



        if(currentState.isChecked())
        {
            System.out.println("hello doobie");
        }

        return stateSelectionRow;
    }


}
