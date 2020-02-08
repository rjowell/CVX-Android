package com.cvx4u.cvx;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
//import java.net.URLConnection;
//import java.net.URLEncoder;


public class AddMessageToken extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh()
    {
        String refreshedToken= FirebaseInstanceId.getInstance().getToken();
        System.out.println("dookie");
        System.out.println(refreshedToken);

        try {
            URL url = new URL("http://www.cvx4u.com/web_service/add_android_push_key.php");

            System.out.println("dookie-11");
            System.out.println("Token is"+refreshedToken);
            String testString="keyval=";
            System.out.println("dookie-12");
            System.out.println(testString);
            //String dataString = URLEncoder.encode("keyval", "UTF-8") + "=" + URLEncoder.encode(refreshedToken, "UTF-8");
            //System.out.println(dataString);
            //System.out.println("this here");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println("dookie-13");
            conn.setRequestMethod("POST");
            System.out.println("dookie-14");
            conn.getOutputStream().write(testString.getBytes());
            System.out.println("dookie-15");
            //conn.setDoOutput(true);
            System.out.println("dookie-16");
            System.out.println("step 11");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            System.out.println("dookie-17");
            wr.write(refreshedToken);
            System.out.println("dookie-18");
            //System.out.println("step 22");
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
        }

    }


