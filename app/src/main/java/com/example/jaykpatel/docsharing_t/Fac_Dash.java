package com.example.jaykpatel.docsharing_t;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.io.File;
import java.util.ArrayList;

public class Fac_Dash extends AppCompatActivity {

    Button btn_SelectFile,btn_upload,buttonFetch;
   private TextView textViewFilename,userName;
    String url;
   private FirebaseStorage storage;//useed to stre file .
   private FirebaseDatabase database;//uwed to  save urls
   private Uri pdfUri;//url or path of file
    ProgressDialog progressDialog;
    Spinner spinner_Branch,spinner_Sem,spinner_sub,spinner_Sub_fetch;
    String branch,item,fetch_sem,sem,sub_fetch,sub;
    Spinner spinnerFetchBranch;
    private FirebaseAuth mAuth;
    String faculty_name,uname_id,userNameStr;
    RecyclerView recyclerView;
    final ArrayList<String> sub_options= new ArrayList<String>();
    final ArrayList<String> sub_upload_options= new ArrayList<String>();


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



        spinnerFetchBranch=findViewById(R.id.spinnerFatchBranch);
        spinner_Branch=findViewById(R.id.spinner_Branch_);
        spinner_Sub_fetch=findViewById(R.id.spinner_sub_fetch);

        spinner_sub=findViewById(R.id.spinner_sub);

        final ArrayAdapter<String> subAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,sub_upload_options);
        spinner_sub.setAdapter(subAdapter);
       //upload spinner sem updation
        spinner_Sem=findViewById(R.id.spinner_sem);
        spinner_Sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sem=parent.getItemAtPosition(position).toString();
                subSpinUpdate(branch,sem);

            }
            private void subSpinUpdate(String branch,String sem)
            {
                sub_upload_options.removeAll(sub_upload_options);
                switch (branch) {


                    case "CE(Computer Eng)":
                        switch (sem) {
                            case "Sem-1":
                                sub_upload_options.add("ELCP");
                                sub_upload_options.add("BEEE");
                                sub_upload_options.add("Maths1");
                                break;
                            case "Sem-2":
                               sub_upload_options.add("ELCP2");
                               sub_upload_options.add("BEEE2");
                               sub_upload_options.add("Maths21");
                                break;
                            case "Sem-3":
                               sub_upload_options.add("ELCP3");
                               sub_upload_options.add("BEEE3");
                               sub_upload_options.add("Maths31");
                                break;
                            case "Sem-4":
                                sub_upload_options.add("ELCP4");
                                sub_upload_options.add("BEEE4");
                                sub_upload_options.add("Maths41");
                                break;
                            case "Sem-5":
                                sub_upload_options.add("ELCP5");
                                sub_upload_options.add("BEEE5");
                                sub_upload_options.add("Maths15");
                                break;
                            case "Sem-6":
                                sub_upload_options.add("ELCP6");
                                sub_upload_options.add("BEEE6");
                                sub_upload_options.add("Maths16");
                                break;
                            case "Sem-7":
                                sub_upload_options.add("ELCP7");
                                sub_upload_options.add("BEEE7");
                                sub_upload_options.add("Maths17");
                                break;
                        }
                        break;

                    case "IT(Information Tech)":
                        switch (sem){
                            case "Sem-1":
                                sub_upload_options.add("1ELCP");
                                sub_upload_options.add("1BEEE");
                                sub_upload_options.add("1Maths1");
                                break;
                            case "Sem-2":
                               sub_upload_options.add("2ELCP");
                               sub_upload_options.add("2BEEE");
                               sub_upload_options.add("2Maths1");
                                break;
                            case "Sem-3":
                               sub_upload_options.add("3ELCP");
                               sub_upload_options.add("3BEEE");
                               sub_upload_options.add("3Maths1");
                                break;
                            case "Sem-4":
                               sub_upload_options.add("4ELCP");
                               sub_upload_options.add("4BEEE");
                               sub_upload_options.add("4Maths1");
                                break;
                            case "Sem-5":
                                sub_upload_options.add("5ELCP");
                                sub_upload_options.add("5BEEE");
                                sub_upload_options.add("5Maths1");
                                break;
                            case "Sem-6":
                               sub_upload_options.add("6ELCP");
                               sub_upload_options.add("6BEEE");
                               sub_upload_options.add("6Maths1");
                                break;
                            case "Sem-7":
                               sub_upload_options.add("7ELCP");
                               sub_upload_options.add("7BEEE");
                               sub_upload_options.add("7Maths1");
                                break;

                        }
                        break;
                    case "CH(Chemical Eng)":
                        switch (sem){
                            case "Sem-1":
                               sub_upload_options.add("ELCP");
                               sub_upload_options.add("BEEE");
                               sub_upload_options.add("Maths1");
                                break;
                            case "Sem-2":
                                sub_upload_options.add("ELCP2");
                                sub_upload_options.add("BEEE2");
                                sub_upload_options.add("Maths21");
                                break;
                            case "Sem-3":
                               sub_upload_options.add("ELCP3");
                               sub_upload_options.add("BEEE3");
                               sub_upload_options.add("Maths31");
                                break;
                            case "Sem-4":
                               sub_upload_options.add("ELCP4");
                               sub_upload_options.add("BEEE4");
                               sub_upload_options.add("Maths41");
                                break;
                            case "Sem-5":
                               sub_upload_options.add("ELCP5");
                               sub_upload_options.add("BEEE5");
                               sub_upload_options.add("Maths15");
                                break;
                            case "Sem-6":
                               sub_upload_options.add("ELCP6");
                               sub_upload_options.add("BEEE6");
                               sub_upload_options.add("Maths16");
                                break;
                            case "Sem-7":
                                sub_upload_options.add("ELCP7");
                                sub_upload_options.add("BEEE7");
                                sub_upload_options.add("Maths17");
                                break;

                        }
                        break;
                }
                final ArrayAdapter<String> subAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,sub_upload_options);
                spinner_sub.setAdapter(subAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




   

        spinner_Branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Nothing selected",Toast.LENGTH_LONG).show();
            }
        });
//Sem Spinner
        final ArrayAdapter<String> subFetchAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,sub_options);
        spinner_Sub_fetch.setAdapter(subAdapter);


        spinner_Sem=findViewById(R.id.spinner_sem_fetch);
        spinner_Sem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fetch_sem=parent.getItemAtPosition(position).toString();
                resetSub(item,fetch_sem);

            }


            private void resetSub(String branch,String fetch_sem) {
                sub_options.removeAll(sub_options);
                if (branch.equals("CE(Computer Eng)")) {
                    if (fetch_sem.equals("Sem-1")) {
                        sub_options.add("ELCP");
                        sub_options.add("BEEE");
                        sub_options.add("Maths1");
                    } else if (fetch_sem.equals("Sem-2")) {
                        sub_options.add("ELCP2");
                        sub_options.add("BEEE2");
                        sub_options.add("Maths21");
                    } else if (fetch_sem.equals("Sem-3")) {
                        sub_options.add("ELCP3");
                        sub_options.add("BEEE3");
                        sub_options.add("Maths31");
                    } else if (fetch_sem.equals("Sem-4")) {
                        sub_options.add("ELCP4");
                        sub_options.add("BEEE4");
                        sub_options.add("Maths41");
                    } else if (fetch_sem.equals("Sem-5")) {
                        sub_options.add("ELCP5");
                        sub_options.add("BEEE5");
                        sub_options.add("Maths15");
                    } else if (fetch_sem.equals("Sem-6")) {
                        sub_options.add("ELCP6");
                        sub_options.add("BEEE6");
                        sub_options.add("Maths16");
                    } else if (fetch_sem.equals("Sem-7")) {
                        sub_options.add("ELCP7");
                        sub_options.add("BEEE7");
                        sub_options.add("Maths17");
                    }
                }
                else if(branch.equals("IT(Information Tech)")){
                    if (fetch_sem.equals("Sem-1") ){
                        sub_options.add("1ELCP");
                        sub_options.add("1BEEE");
                        sub_options.add("1Maths1");
                    } else if (fetch_sem.equals("Sem-2") ){
                        sub_options.add("2ELCP");
                        sub_options.add("2BEEE");
                        sub_options.add("2Maths1");
                    } else if (fetch_sem.equals("Sem-3") ){
                        sub_options.add("3ELCP");
                        sub_options.add("3BEEE");
                        sub_options.add("3Maths1");
                    } else if (fetch_sem.equals("Sem-4") ){
                        sub_options.add("4ELCP");
                        sub_options.add("4BEEE");
                        sub_options.add("4Maths1");
                    } else if (fetch_sem.equals("Sem-5") ){
                        sub_options.add("5ELCP");
                        sub_options.add("5BEEE");
                        sub_options.add("5Maths1");
                    } else if (fetch_sem.equals("Sem-6") ){
                        sub_options.add("6ELCP");
                        sub_options.add("6BEEE");
                        sub_options.add("6Maths1");
                    } else if (fetch_sem.equals("Sem-7") ){
                        sub_options.add("7ELCP");
                        sub_options.add("7BEEE");
                        sub_options.add("7Maths1");
                    }
                }
                else if (branch.equals("CH(Chemical Eng)"))
                {
                    if (fetch_sem.equals("Sem-1") ){
                        sub_options.add("ELCP");
                        sub_options.add("BEEE");
                        sub_options.add("Maths1");
                    } else if (fetch_sem.equals("Sem-2") ){
                        sub_options.add("ELCP");
                        sub_options.add("BEEE");
                        sub_options.add("Maths1");
                    } else if (fetch_sem.equals("Sem-3")) {
                        sub_options.add("ELCP");
                        sub_options.add("BEEE");
                        sub_options.add("Maths1");
                    } else if (fetch_sem.equals("Sem-4")) {
                        sub_options.add("ELCP");
                        sub_options.add("BEEE");
                        sub_options.add("Maths1");
                    } else if (fetch_sem.equals("Sem-5") ){
                        sub_options.add("ELCP");
                        sub_options.add("BEEE");
                        sub_options.add("Maths1");
                    } else if (fetch_sem.equals("Sem-6")) {
                        sub_options.add("ELCP");
                        sub_options.add("BEEE");
                        sub_options.add("Maths1");
                    } else if (fetch_sem.equals( "Sem-7")) {
                        sub_options.add("ELCP");
                        sub_options.add("BEEE");
                        sub_options.add("Maths1");
                    }

                }
                final ArrayAdapter<String> subFetchAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,sub_options);
                spinner_Sub_fetch.setAdapter(subAdapter);



            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinnerFetchBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();

                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(item);

                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String fileName=dataSnapshot.getKey();
                        String url=dataSnapshot.getValue(String.class);
                        ((MyAdapter)recyclerView.getAdapter()).update(fileName,url);

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                recyclerView=findViewById(R.id.recyclerView1);
                //custom adapter
                //populate recycler views
                recyclerView.setLayoutManager(new LinearLayoutManager(Fac_Dash.this));
                MyAdapter myAdapter=new MyAdapter(recyclerView,getApplicationContext(),new ArrayList<String>(),new ArrayList<String>());
                recyclerView.setAdapter(myAdapter);


            }
            public void onNothingSelected(AdapterView<?> parent) {
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


       /* final String fileName=System.currentTimeMillis()+".pdf";
        final String fileName1=System.currentTimeMillis()+"";
        */
        String filename_test=textViewFilename.getText().toString();
        final String fileName1=filename_test;
        final String fileName=filename_test+".pdf";
        final String branch1=branch;
        final String sub_db;
        final String sem_db;
        StorageReference storageReference=storage.getReference();// root path
        storageReference.child(branch1).child(fileName).putFile(pdfUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        url=taskSnapshot.getDownloadUrl().toString();//url of file
                        DatabaseReference reference=database.getReference();

                        reference.child(branch1).child(fileName1).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
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
/*
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
*/
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
        case 86:
            if (resultCode == RESULT_OK &&  requestCode==86   && data!=null) {
                // Get the Uri of the selected file
                pdfUri = data.getData();
                String uriString = pdfUri.toString();
                File myFile = new File(uriString);
                String path = myFile.getAbsolutePath();
                String displayName = null;
                String file_Name = null;
                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getApplicationContext().getContentResolver().query(pdfUri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                            if(displayName.indexOf(".")>0){
                                file_Name = displayName.substring(0,displayName.lastIndexOf("."));
                            }
                            textViewFilename.setText(file_Name.toString());
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    displayName = myFile.getName();
                    textViewFilename.setText(displayName);
                }
            }
            break;
    }
    super.onActivityResult(requestCode, resultCode, data);
}



}
