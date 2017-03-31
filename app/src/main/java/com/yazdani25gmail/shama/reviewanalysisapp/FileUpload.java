package com.yazdani25gmail.shama.reviewanalysisapp;

import android.app.ProgressDialog;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage;
import com.ibm.watson.developer_cloud.alchemy.v1.model.DocumentSentiment;

import java.util.HashMap;
import java.util.Map;

public class FileUpload extends AppCompatActivity {


    private static final String TAG = "FileChooser";
    public static final int REQUEST_CODE=6384;
    TextView textView3;
    Button bChoose;
    Button bUpload;
    String sentiment;
    private Uri filePath;
    private StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


                if (requestCode==REQUEST_CODE && resultCode== RESULT_OK && data != null && data.getData() != null) {
                    filePath= data.getData();
        }
    }

    //File Chooser Method
    private void showFilechooser(){



        Intent intentTxt = new Intent();
        intentTxt.setType("text/*");
        intentTxt.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intentTxt,"Select a File"),REQUEST_CODE);

        /*intentTxt.setType("text/*");
        intentTxt.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intentTxt,"Select .txt file"),PICKFILE_RESULT_CODE);*/


    }

    private void uploadFile(){


        if(filePath != null) {
            //Progress Dialog for uploading
            final ProgressDialog progressDialog= new ProgressDialog(this);
            progressDialog.setTitle("File Uploading..");

            StorageReference riversRef = storageReference.child("images/review.txt");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
        }else {
            //file path null
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_upload);

        //initializing the UI parameters
        textView3 = (TextView) findViewById(R.id.textView2);
        bChoose = (Button) findViewById(R.id.button4);
        bUpload = (Button) findViewById(R.id.button5);

        //storage reference
        storageReference = FirebaseStorage.getInstance().getReference();

        //File Chooser Button
        bChoose.setOnClickListener(new View.OnClickListener() {@Override

        public void onClick(View v) {
            //open file chooser
            showFilechooser();
        }
        });

        //File upload Button
        bUpload.setOnClickListener(new View.OnClickListener() {@Override

        public void onClick(View v) {
            //open file chooser
            uploadFile();
        }
        });



    }
}
