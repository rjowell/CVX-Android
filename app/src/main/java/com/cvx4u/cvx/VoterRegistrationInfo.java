package com.cvx4u.cvx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class VoterRegistrationInfo extends AppCompatActivity {

    String[][] voterInformation={
            {"AL","Alabama","http://sos.alabama.gov/alabama-votes/voter/register-to-vote","https://www.alabamainteractive.org/sos/voter_registration/voterRegistrationWelcome.action"},
            {"AK","Alaska","http://www.elections.alaska.gov/Core/voterregistrationinformation.php","https://voterregistration.alaska.gov"},
            {"AR","Arkansas","https://www.sos.arkansas.gov/elections/voter-information/voter-registration-information",null},
            {"AZ","Arizona","https://www.azsos.gov/elections/voting-election/register-vote-or-update-your-current-voter-information","https://servicearizona.com/voterRegistration"},
            {"CA","Calfornia","http://www.sos.ca.gov/elections/voter-registration/","http://registertovote.ca.gov"},
            {"CO","Colorado","http://www.sos.state.co.us/pubs/elections/vote/VoterHome.html","https://www.sos.state.co.us/voter/pages/pub/olvr/verifyNewVoter.xhtml"},
            {"CT","Connecticut","http://portal.ct.gov/SOTS/Election-Services/Voter-Information/Voter-Registration-Information","https://voterregistration.ct.gov/OLVR/welcome.do"},
            {"DE","Delaware","http://electionsncc.delaware.gov/votreg.shtml","https://ivote.de.gov"},
            {"FL","Florida","http://dos.myflorida.com/elections/for-voters/voter-registration/register-to-vote-or-update-your-information/","https://registertovoteflorida.gov"},
            {"GA","Georgia","https://www.mvp.sos.ga.gov/MVP/mvp.do","https://registertovote.sos.ga.gov/GAOLVR/welcometoga.do#no-back-button"},
            {"HI","Hawaii","http://elections.hawaii.gov/voters/registration/","https://olvr.hawaii.gov"},
            {"ID","Idaho","http://www.idahovotes.gov/voter_info.shtml","https://apps.idahovotes.gov/OnlineVoterRegistration"},
            {"IL","Illinois","https://www.elections.il.gov/votinginformation/register.aspx","https://ova.elections.il.gov"},
            {"IN","Indiana","http://www.in.gov/sos/elections/2403.htm","https://indianavoters.in.gov"},
            {"IA","Iowa","https://sos.iowa.gov/elections/voterinformation/voterregistration.html","https://mymvd.iowadot.gov/Account/Login?ReturnUrl=%2fVoterRegistration"},
            {"KS","Kansas","http://www.kssos.org/elections/elections_registration.html","https://www.kdor.ks.gov/apps/voterreg/default.aspx"},
            {"KY","Kentucky","https://elect.ky.gov/registertovote/Pages/default.aspx","https://vrsws.sos.ky.gov/ovrweb/"},
            {"LA","Louisiana","https://www.sos.la.gov/ElectionsAndVoting/RegisterToVote/Pages/default.aspx","https://voterportal.sos.la.gov/VoterRegistration"},
            {"ME","Maine","http://www.maine.gov/sos/cec/elec/voter-info/votreg.html",null},
            {"MD","Maryland","http://www.elections.state.md.us/voter_registration/","https://voterservices.elections.maryland.gov/OnlineVoterRegistration/InstructionsStep1"},
            {"MA","Massachusetts","https://www.sec.state.ma.us/ele/eleifv/howreg.htm","https://www.sec.state.ma.us/ovr/"},
            {"MI","Michigan","http://www.michigan.gov/sos/0,4670,7-127-1633-49313--,00.html",null},
            {"MN","Minnesota","http://www.sos.state.mn.us/elections-voting/register-to-vote","https://mnvotes.sos.state.mn.us/VoterRegistration/VoterRegistrationMain.aspx"},
            {"MS","Mississippi","http://www.sos.ms.gov/elections-voting/pages/voter-registration-information.aspx",null},
            {"MO","Missouri","https://www.sos.mo.gov/elections/goVoteMissouri/register","https://s1.sos.mo.gov/votemissouri/request"},
            {"MT","Montana","https://sos.mt.gov/elections/vote/index",null},
            {"NE","Nebraska","https://www.nebraska.gov/apps-sos-voter-registration/","https://www.nebraska.gov/apps-sos-voter-registration/"},
            {"NV","Nevada","https://nvsos.gov/sosvoterservices/Registration/step1.aspx","https://nvsos.gov/sosvoterservices/Registration/step1.aspx"},
            {"NH","New Hampshire","http://sos.nh.gov/HowRegVote.aspx",null},
            {"NJ","New Jersey","http://www.njelections.org/voting-information.html",null},
            {"NM","New Mexico","hhttp://www.sos.state.nm.us/Voter_Information/Voter_Registration_Information.aspx","https://portal.sos.state.nm.us/OVR/WebPages/Instructionsstep1.aspx"},
            {"NY","New York","http://www.elections.ny.gov/votingregister.html",null},
            {"NC","North Carolina","http://www.ncsbe.gov/Voters/Registering-to-Vote",null},
            {"ND","North Dakota","No Voter Registration",null},
            {"OH","Ohio","https://www.sos.state.oh.us/elections/voters/register/#gref","https://olvr.sos.state.oh.us"},
            {"OK","Oklahoma","https://www.ok.gov/elections/Voter_Info/Register_to_Vote/",null},
            {"OR","Oregon","http://sos.oregon.gov/voting/Pages/updatevoterregistration.aspx","https://secure.sos.state.or.us/orestar/vr/register.do?lang=eng&source=SOS"},
            {"PA","Pennsylvania","https://www.pavoterservices.pa.gov/Pages/VoterRegistrationApplication.aspx","https://www.pavoterservices.pa.gov/Pages/VoterRegistrationApplication.aspx"},
            {"RI","Rhode Island","http://www.elections.state.ri.us/voting/registration.php","https://vote.sos.ri.gov"},
            {"SC","South Carolina","https://www.scvotes.org","https://info.scvotes.sc.gov/eng/ovr/start.aspx"},
            {"SD","South Dakota","https://sdsos.gov/elections-voting/voting/register-to-vote/default.aspx",null},
            {"TN","Tennessee","http://sos.tn.gov/products/elections/register-vote","https://ovr.govote.tn.gov"},
            {"TX","Texas","http://www.votetexas.gov/register-to-vote/",null},
            {"UT","Utah","https://secure.utah.gov/voterreg/index.html","https://secure.utah.gov/voterreg/index.html"},
            {"VT","Vermont","https://www.sec.state.vt.us/elections/voters/registration.aspx","https://olvr.sec.state.vt.us"},
            {"VA","Virginia","http://www.elections.virginia.gov/voter-outreach/","https://vote.elections.virginia.gov/Registration/Eligibility"},
            {"WA","Washington","http://www.dol.wa.gov/driverslicense/voter.html","https://weiapplets.sos.wa.gov/MyVoteOLVR/MyVoteOLVR"},
            {"DC","Washington, DC","https://www.vote4dc.com/ApplyInstructions/Register","https://www.vote4dc.com/ApplyInstructions/Register"},
            {"WV","West Virginia","https://ovr.sos.wv.gov/Register/Landing","https://ovr.sos.wv.gov/Register/Landing"},
            {"WI","Wisconsin","https://myvote.wi.gov/en-us/","https://myvote.wi.gov/en-us/VoterRegistration"},
            {"WY","Wyoming","http://soswy.state.wy.us/Elections/RegisteringToVote.aspx",null}
            };//end of array of arrays



    ListView voterInfo;

    Button homeButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_registration_info);
        //Intent intent=getIntent();
        //Log.d("data", voterInformation[1][2]);



        voterInfo=(ListView)findViewById(R.id.registrationInfo);

        VoterRegistrationAdapter theAdapter=new VoterRegistrationAdapter(this, voterInformation);

        voterInfo.setAdapter(theAdapter);

        homeButton=(Button) findViewById(R.id.homeButton);


        View.OnClickListener goHome=new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent openHomeWindow=new Intent(VoterRegistrationInfo.this, HomeScreen.class);

                startActivity(openHomeWindow);


            }
        };

        homeButton.setOnClickListener(goHome);


    }
}
