package com.chkry.htmlurlconnection;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

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
            





        } else if(type.equals("upload")){

//            final String sourceFile = "/mnt/sdcard/sanit";
            String fileName = "sanity.xls";
            // Write something if login successs.
            String sourceFile = Environment.getExternalStorageDirectory().getPath();
            String sourceFileUri = sourceFile+"/"+fileName;
//            Toast.makeText(context, "URL IS : "+ sourceFileUri, Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, "URI: "+fileName, Toast.LENGTH_SHORT).show();
            try {
                int bytesRead,bytesAvailable,bufferSize;
                byte[] buffer;
                String lineEnd = "\r\n";
                int serverResponseCode = 0;
                String twoHyphens = "--";
                String boundary = "*****";
                int maxBufferSize = 1 * 1024 * 1024;
                FileInputStream fileInputStream = new FileInputStream(sourceFileUri);

                String upload_url = "http://10.221.31.54/uploadurl.php";
//                String iaddress = params[1];
//                String portip = params[2];
                URL url = new URL(upload_url);


                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                httpURLConnection.setRequestProperty("uploaded_file", fileName);

//                dataOutputStream = new DataOutputStream(HttpURLConnection.getOutputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
//                OutputStream outputStream = httpURLConnection.getOutputStream();
//                dataOutputStream =  httpURLConnection.getOutputStream();

//                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(dataOutputStream,"UTF-8"));

                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + sourceFileUri + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);



                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);




                serverResponseCode = httpURLConnection.getResponseCode();
                String serverResponseMessage = httpURLConnection.getResponseMessage();

//                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
//                if(serverResponseCode == 200){
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + "http://coderefer.com/extras/uploads/"+ fileName);
////                        }
////                    });
////                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
//                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();
                return serverResponseMessage;
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
