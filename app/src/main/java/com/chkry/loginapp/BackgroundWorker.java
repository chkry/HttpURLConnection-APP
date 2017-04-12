package com.chkry.loginapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * Created by chakradhar.reddy on 2/12/2016.
 */
public class BackgroundWorker extends AsyncTask<String,String,String> {

    Context context;
    AlertDialog alertDialog;
    BackgroundWorker(Context ctx){
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://10.221.31.54/automator/androidtest1.php";
        if(type.equals("login")){
            // Write something if login successs.
            try {
                String iaddress = params[1];
                String portip = params[2];
                URL url = new URL(login_url);


                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("iaddress","UTF-8")+"="+URLEncoder.encode(iaddress,"UTF-8")+"&"
                        +URLEncoder.encode("portip","UTF-8")+"="+URLEncoder.encode(portip,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result+= line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }


            }
        return null;

    }

    protected void onPreExecute(){
//        alertDialog = new AlertDialog.Builder(context).create();
//        alertDialog.setTitle("Login Status");
        Toast.makeText(context,"Installation is in progress",Toast.LENGTH_SHORT).show();
    }
    protected  void onPostExecute(String result){

//        alertDialog.setMessage(result);
//        alertDialog.show();

        Toast.makeText(context,result,Toast.LENGTH_LONG).show();
//
//        if (Objects.equals(result, "Success")){
//            Intent i = new Intent(context,SuccessActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//
//        }else{
//
//
//            Toast.makeText(context,"Not Success",Toast.LENGTH_LONG).show();
//        }


    }

    protected void onProgressUpdate(){
        super.onProgressUpdate();

    }

}
