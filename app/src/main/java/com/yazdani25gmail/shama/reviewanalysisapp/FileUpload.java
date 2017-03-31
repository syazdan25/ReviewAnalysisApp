package com.yazdani25gmail.shama.reviewanalysisapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    public static int PICKFILE_RESULT_CODE=234;
    TextView textView3;
    Button bChoose;
    Button bUpload;
    String sentiment;
    private Uri filePath;
    private StorageReference mStorageRef;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICKFILE_RESULT_CODE && requestCode== RESULT_OK && data != null && data.getData() != null){
            filePath= data.getData();

        }
    }

    //File Chooser Method
    private void showFilechooser(){
        Intent intentTxt = new Intent(Intent.ACTION_GET_CONTENT);
        intentTxt.setType("text/plain");
        intentTxt.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intentTxt,"Select .txt file"),PICKFILE_RESULT_CODE);

    }

    private void uploadFile(){

        if(filePath !=null) {

            final ProgressDialog pd= new ProgressDialog(this);
            pd.setTitle("Uploading....");

            StorageReference riversRef = mStorageRef.child("file/review.txt");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"File Uploaded", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }else{
            //error
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
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
