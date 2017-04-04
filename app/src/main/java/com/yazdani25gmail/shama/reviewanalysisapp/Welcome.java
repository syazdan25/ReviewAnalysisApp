package com.yazdani25gmail.shama.reviewanalysisapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome extends AppCompatActivity {

    //Create button Object
    public Button button2;
    public Button button3;
    public void init()
    {


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //Instantiate the Object
        button2= (Button)findViewById(R.id.button2);
        button3= (Button)findViewById(R.id.button3);

        //Go to single review analysis activity on clicking button2
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page1= new Intent(Welcome.this,MainActivity.class);
                startActivity(page1);
            }
        });

        //Go to single bulk analysis activity on clicking button3
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent page2= new Intent(Welcome.this,ReviewSentiment.class);
                startActivity(page2);
            }
        });
    }
}
