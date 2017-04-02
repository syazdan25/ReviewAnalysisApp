package com.yazdani25gmail.shama.reviewanalysisapp;

import android.app.ProgressDialog;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FileUpload extends AppCompatActivity {


    private static final String TAG = "FileChooser";
    private static final int READ_REQUEST_CODE = 42;
    public static final int REQUEST_CODE=6384;
    private static final int EDIT_REQUEST_CODE = 44;
    TextView textView3;
    Button bChoose;
    Button bUpload;
    Button bAnalyze;

    String str;
    private Uri filePath;
    private StorageReference storageReference;
    String convertedPath;
    String line;
    String sentiment;
    String result;



    private void editDocument() {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's
        // file browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones).
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only text files.
        intent.setType("text/plain");

        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }

    private void alterDocument(Uri uri) {
        try {
            ParcelFileDescriptor pfd = this.getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write(("Overwritten by MyCloud at " +
                    System.currentTimeMillis() + "\n").getBytes());
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


                if (requestCode==REQUEST_CODE && resultCode== RESULT_OK && data != null && data.getData() != null) {
                    filePath= data.getData();
                    alterDocument(filePath);


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

    //File upload
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
            Toast.makeText(getApplicationContext(), "File path not found", Toast.LENGTH_LONG).show();
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
        bAnalyze = (Button) findViewById(R.id.button6);

        //storage reference
        storageReference = FirebaseStorage.getInstance().getReference();

        //File Chooser Button
        bChoose.setOnClickListener(new View.OnClickListener() {@Override

        public void onClick(View v) {
            //open file chooser
            //
            // showFilechooser();
            //performFileSearch();
            editDocument();
        }
        });

        //File upload Button
        bUpload.setOnClickListener(new View.OnClickListener() {@Override

        public void onClick(View v) {
            //open file chooser
            uploadFile();
        }
        });


        //Analyze
        bAnalyze.setOnClickListener(new View.OnClickListener() {@Override

        public void onClick(View v) {
            //open file chooser
            //analyze();

        }
        });







    }
}
