package com.yazdani25gmail.shama.reviewanalysisapp;

/**
 * Created by shama on 4/1/2017.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

//adding Watson Developer Cloud SDK for Java:
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;



public class FileOperations {

    String text="";
    String senti="test";

    public FileOperations() {

    }

    public Boolean write(String fname, String fcontent){
        try {

            String fpath = "/sdcard/"+fname+".txt";

            File file = new File(fpath);

            // If file does not exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();

            Log.d("Suceess","Sucess");
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public String read(String fname){

        BufferedReader br = null;
        String response = null;


        try {

            StringBuffer output = new StringBuffer();
            String fpath = "/sdcard/"+fname+".txt";

            br = new BufferedReader(new FileReader(fpath));
            String line = "";
            while ((line = br.readLine()) != null) {
                text=line;
                AskWatsonTask task = new AskWatsonTask();
                task.execute(new String[]{});
                output.append(text+": "+senti +"\n");
            }
            response = output.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
        return response;

    }


    private class AskWatsonTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... textsToAnalyse) {
            AlchemyLanguage service = new AlchemyLanguage();
            service.setApiKey("30d76f945d0b9cc098b98f4ee11b90777602cdea");

            Map<String, Object> params = new HashMap<String, Object>();
            params.put(AlchemyLanguage.TEXT, text);
            DocumentSentiment sentiment = service.getSentiment(params).execute();
            System.out.println(sentiment);
            //passing the result to be displayed at UI in the main tread
            //senti=sentiment.getSentiment().getType().name().toString();
            return sentiment.getSentiment().getType().name();

        }

        //setting the value of UI outside of the thread
        @Override
        protected void onPostExecute(String result) {
            senti=result;
        }
    }

}
