package com.example.jaykpatel.docsharing_t;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Fac_Dash extends AppCompatActivity {

    Button btn_SelectFile,btn_upload,buttonFetch;
   private TextView textViewFilename,userName;
    String url;
   private FirebaseStorage storage;//useed to stre file .
   private FirebaseDatabase database;//uwed to  save urls
   private Uri pdfUri;//url or path of file
    ProgressDialog progressDialog;
    Spinner spinner_Branch;
    String branch,item;
    Spinner spinnerFetchBranch;
    private FirebaseAuth mAuth;
    String faculty_name,uname_id,userNameStr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac__dash);

        mAuth=FirebaseAuth.getInstance();
        userName=findViewById(R.id.userName);
        storage=FirebaseStorage.getInstance();//return an object of firebase storage
        database=FirebaseDatabase.getInstance();//return firebase database obj

        btn_SelectFile=findViewById(R.id.btn_SelectFile);
        btn_upload=findViewById(R.id.btn_upload);
        //FacultyName

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        uname_id=currentFirebaseUser.getUid();
        DatabaseReference reference=database.getReference();
        reference.child("UserInfo").child(uname_id).child("Name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userNameStr=dataSnapshot.getValue(String.class);
                userName.setText("Hello Mr."+userNameStr);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        textViewFilename=findViewById(R.id.txtview_filename);

        buttonFetch=findViewById(R.id.button_Fetch);

        spinnerFetchBranch=findViewById(R.id.spinnerFatchBranch);
        spinner_Branch=findViewById(R.id.spinner_Branch_);
        spinner_Branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Nothing slected",Toast.LENGTH_LONG).show();
            }
        });


        spinnerFetchBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        buttonFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Fac_Dash.this,MyRecyclerViewActivity.class);
                intent.putExtra("Branch_Fetch_Item",item);
                startActivity(intent);
            }
        });

        btn_SelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(Fac_Dash.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    selectPdf();

                }
                else
                {

                    ActivityCompat.requestPermissions(Fac_Dash.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);

                }

            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfUri!=null)
                    uploadFile(pdfUri);
                else
                    Toast.makeText(Fac_Dash.this,"Select  a file",Toast.LENGTH_LONG).show();


            }
        });
    }

   /* @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }*/


    private void uploadFile(Uri pdfUri) {

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File..");
        progressDialog.setProgress(0);
        progressDialog.show();

       FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        final String userData=currentFirebaseUser.getUid();


        final String fileName=System.currentTimeMillis()+".pdf";
        final String fileName1=System.currentTimeMillis()+"";
        final String branch1=branch;
        StorageReference storageReference=storage.getReference();// root path
        storageReference.child(branch1).child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        url=taskSnapshot.getDownloadUrl().toString();//url of file
                        DatabaseReference reference=database.getReference();

                        reference.child(userData).child(branch1).child(fileName1).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(Fac_Dash.this,"File uploaded Successfully",Toast.LENGTH_LONG).show();

                                }
                                else
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(Fac_Dash.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                                }
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error",e.getMessage());
                Toast.makeText(Fac_Dash.this,e.getMessage(),Toast.LENGTH_LONG).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //track upload progress of file
                int currentProgress=(int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectPdf();
        }
        else
        {
            Toast.makeText(Fac_Dash.this,"Please Provide Permission",Toast.LENGTH_LONG).show();

        }
    }

    private void selectPdf() {

        //to open user to select file using file manager
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);//to fetch file
        startActivityForResult(intent,86);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //check user has selected file or not
        if (requestCode==86 &&resultCode==RESULT_OK && data!=null)
        {
            pdfUri=data.getData();
           String text= "A selected File:"+pdfUri.getPath();
            textViewFilename.setText(text);

        }
        else
        {
            Toast.makeText(Fac_Dash.this,"Please select file",Toast.LENGTH_LONG).show();
        }

    }
}
