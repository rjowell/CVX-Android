package com.cvx4u.cvx;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by laurenpiera on 3/2/18.
 */

public class CandidateWindowGenerator extends BaseAdapter {


    ArrayList<Candidate> candidateList=new ArrayList<>();

    private Context localContext;
    private LayoutInflater localInflater;

    public CandidateWindowGenerator(ArrayList<Candidate> listOfCandidates, Context context)
    {
        localContext=context;
        localInflater=(LayoutInflater) localContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        candidateList=listOfCandidates;
    }



    @Override
    public int getCount() {
        return candidateList.size();
    }

    @Override
    public Object getItem(int i) {
        return candidateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @SuppressLint("ResourceType")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View.OnClickListener buttonProcess=new View.OnClickListener()
        {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view)
            {
               //System.out.println(view.getTag().toString());

                //Phone
                if(view.getId()==0)
                {
                    Intent callCandidate=new Intent(Intent.ACTION_CALL);
                    callCandidate.setData(Uri.parse("tel:"+view.getTag().toString().replace("(","").replace(")","").replace("-","").replace(" ","")));
                    if(localContext.checkCallingPermission(Manifest.permission.CALL_PHONE)== PackageManager.PERMISSION_GRANTED) {
                        localContext.startActivity(callCandidate);
                    }
                }
                //Email
                else if(view.getId()==1)
                {
                    Intent sendEmail=new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",view.getTag().toString(),null));
                    localContext.startActivity(Intent.createChooser(sendEmail, "Choose an e-mail service"));
                }
                else
                {
                    Intent launchBrowser=new Intent(Intent.ACTION_VIEW, Uri.parse(view.getTag().toString()));
                    localContext.startActivity(launchBrowser);
                }
            }
        };





        View currentCell=localInflater.inflate(R.layout.candidate_info_cell, viewGroup, false);

        TextView candidateName=currentCell.findViewById(R.id.candidateName);
        candidateName.setText(candidateList.get(i).name);
        TextView candidateOffice=currentCell.findViewById(R.id.candidateOffice);
       candidateOffice.setText(candidateList.get(i).office);
        TextView candidateParty=currentCell.findViewById(R.id.candidateParty);
        candidateParty.setText(candidateList.get(i).party);
        ImageView candidatePhoto=(ImageView) currentCell.findViewById(R.id.candidatePhoto);
        Button candidatePhone=currentCell.findViewById(R.id.candidatePhone);
        Button candidateEmail=currentCell.findViewById(R.id.candidateEmail);
        Button candidateWebsite=currentCell.findViewById(R.id.candidateWebsite);
        if(candidateList.get(i).phoneNumber==null)
        {
            candidatePhone.setVisibility(View.INVISIBLE);
        }
        else {
            candidatePhone.setText((CharSequence) candidateList.get(i).phoneNumber);
            candidatePhone.setTag((CharSequence) candidateList.get(i).phoneNumber);
            candidatePhone.setId(0);
            candidatePhone.setOnClickListener(buttonProcess);
        }
        if(candidateList.get(i).email==null)
        {
            candidateEmail.setVisibility(View.INVISIBLE);
        }
        else
        {
            candidateEmail.setText((CharSequence) candidateList.get(i).email);
            candidateEmail.setTag((CharSequence) candidateList.get(i).email);
            candidateEmail.setId(1);
            candidateEmail.setOnClickListener(buttonProcess);
        }
        if(candidateList.get(i).website==null)
        {
            candidateWebsite.setVisibility(View.INVISIBLE);
        }
        else
        {
            candidateWebsite.setTag(candidateList.get(i).website);
            candidateWebsite.setId(2);
            candidateWebsite.setOnClickListener(buttonProcess);
        }



        if(candidateList.get(i).candidatePhoto==null)
        {
            candidatePhoto.setImageResource(R.drawable.nophoto1);
        }
        else {
            candidatePhoto.setImageBitmap(candidateList.get(i).candidatePhoto);
        }





        return currentCell;
    }
}
