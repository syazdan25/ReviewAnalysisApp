package com.yazdani25gmail.shama.reviewanalysisapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.AsyncTask;
import android.view.View;
import java.util.Map;
import java.util.HashMap;
import android.webkit.WebChromeClient;

//adding Watson Developer Cloud SDK for Java:
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    TextView textView1;
    EditText editText;
    Button button;
    String sentiment;
    ImageView thumb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //initializing the UI parameters
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);


        //fire action when button is pressed

        button.setOnClickListener(new View.OnClickListener() {@Override

            public void onClick(View v) {
                System.out.println("Logging to the console that the button pressed for the text : " + editText.getText());
                textView.setText("Displaying at UI the sentiment to be checked for : " + editText.getText());

                AskWatsonTask task = new AskWatsonTask();
                task.execute(new String[]{});

            }

        });
    }


    private class AskWatsonTask extends AsyncTask<String, Void, String> {

        int posval,negval;
        @Override
        protected String doInBackground(String... textsToAnalyse) {
            System.out.println(editText.getText());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Fetching Sentiment from Watson AlchemyAPI");
                }
            });

            AlchemyLanguage service = new AlchemyLanguage();
            service.setApiKey("30d76f945d0b9cc098b98f4ee11b90777602cdea");

            Map<String, Object> params = new HashMap<String, Object>();
            params.put(AlchemyLanguage.TEXT, editText.getText());
            DocumentSentiment sentiment = service.getSentiment(params).execute();
            System.out.println(sentiment);

            //passing the result to be displayed at UI in the main tread
            posval=sentiment.getSentiment().getScore().intValue();
            return sentiment.getSentiment().getType().name();

        }

        //setting the value of UI outside of the thread
        @Override
        protected void onPostExecute(String result) {

            thumb = (ImageView) findViewById(R.id.imageView);
            if(result== "POSITIVE"){
                thumb.setImageResource(R.drawable.thumbup);
            }
            else if(result== "NEGATIVE"){
                thumb.setImageResource(R.drawable.thumbdown);
            }
            else if(result== "NEUTRAL"){
                thumb.setImageResource(R.drawable.neutral);
            }
            textView.setText("Enter Review for analysis");
            textView1 = (TextView) findViewById(R.id.textView1);
            textView1.setText("Your Review is: " + result);
        }
    }

}
