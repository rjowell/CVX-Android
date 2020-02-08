package com.cvx4u.cvx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by laurenpiera on 3/12/18.
 */

public class RepresentativeDisplayGenerator extends BaseAdapter
{

    Context localContext;
    ArrayList<Representative> repList;
    LayoutInflater thisInflater;

    public RepresentativeDisplayGenerator(Context thisContext, ArrayList<Representative> listOfReps)
    {
        localContext=thisContext;
        thisInflater=(LayoutInflater) localContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        repList=listOfReps;

    }




    @Override
    public int getCount() {
        return repList.size();
    }

    @Override
    public Object getItem(int i) {
        return repList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceType")



    View.OnClickListener buttonProcessor=new View.OnClickListener()
    {


        @Override
        public void onClick(View view)
        {
            //Phone
            if(view.getId()==0)
            {
                Intent callCandidate=new Intent(Intent.ACTION_CALL);
                callCandidate.setData(Uri.parse("tel:"+view.getTag().toString().replace("(","").replace(")","").replace("-","").replace(" ","")));
                if(localContext.checkCallingPermission(Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED)
                {
                    localContext.startActivity(callCandidate);
                }
            }
            //Email
            else if(view.getId()==1)
            {
                Intent sendEmail=new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", view.getTag().toString(),null));
                localContext.startActivity(Intent.createChooser(sendEmail, "Choose an e-mail service"));
            }
            else
            {
                Intent launchBrowser=new Intent(Intent.ACTION_VIEW, Uri.parse(view.getTag().toString()));
                localContext.startActivity(launchBrowser);
            }
        }
    };


    @SuppressLint("ResourceType")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View currentCell=thisInflater.inflate(R.layout.representative_cell, viewGroup, false);
        Representative currentRepre=repList.get(i);

        TextView officeLabel=currentCell.findViewById(R.id.officeLabel);
        ImageView repPhoto=currentCell.findViewById(R.id.repPhoto);
        TextView repName=currentCell.findViewById(R.id.repName);
        TextView repParty=currentCell.findViewById(R.id.repParty);
        Button repPhone=currentCell.findViewById(R.id.repPhone);
        Button repEmail=currentCell.findViewById(R.id.repEmail);
        Button repWebsite=currentCell.findViewById(R.id.repWebsite);

        officeLabel.setText((CharSequence) currentRepre.officeName);
        if(currentRepre.repPhoto==null)
        {
            repPhoto.setImageResource(R.drawable.nophoto1);
        }
        else {
            repPhoto.setImageBitmap(currentRepre.repPhoto);
        }
        repName.setText((CharSequence) currentRepre.repName);
        repParty.setText((CharSequence) currentRepre.party);
        repPhone.setTag(currentRepre.phoneNumber);
        repPhone.setText(currentRepre.phoneNumber);
        repPhone.setOnClickListener(buttonProcessor);
        repPhone.setId(0);
        repEmail.setTag(currentRepre.eMail);
        //repEmail.setText(currentRepre.eMail);
        repEmail.setId(1);
        repEmail.setOnClickListener(buttonProcessor);
        //repWebsite.setText(currentRepre.website);
        repWebsite.setTag(currentRepre.website);
        repWebsite.setId(2);
        repWebsite.setOnClickListener(buttonProcessor);















        return currentCell;
    }
}
