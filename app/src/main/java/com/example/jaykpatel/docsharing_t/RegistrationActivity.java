package com.example.jaykpatel.docsharing_t;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText remail_id,regpassword,faculty_name,college_id,mob_no;
    FirebaseAuth firebaseAuth;
     Spinner spinner_Branch;
     String f_branch,email,password;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


     faculty_name=(EditText)findViewById(R.id.faculty_Name);
     college_id=findViewById(R.id.college_id);
     mob_no=findViewById(R.id.mob_no);


        remail_id=(EditText)findViewById(R.id.remail_id);
        regpassword=(EditText)findViewById(R.id.regpassword);
        firebaseAuth = FirebaseAuth.getInstance();
          database=FirebaseDatabase.getInstance();
        spinner_Branch=findViewById(R.id.spinner_branch);
        spinner_Branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                f_branch=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"Nothing slected",Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btnRegistrationUser).setOnClickListener(this);
    }


    private void registerUser(){
        email=remail_id.getText().toString().trim();
         password= regpassword.getText().toString().trim();
        if(email.isEmpty()){
            remail_id.setError("Email is required");
            remail_id.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            remail_id.setError("Enter a Valid Email ID");
            remail_id.requestFocus();
            return;
        }

        if(password.isEmpty()){
            regpassword.setError("Password is Required");
            regpassword.requestFocus();
            return;
        }

        if(password.length()<6){
            regpassword.setError("Minimun length of password must be 6");
            regpassword.requestFocus();
            return;
        }


        final ProgressDialog progressDialog=ProgressDialog.show(RegistrationActivity.this,"Please Wait","Processing..");
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {

                    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

                    final String uid=firebaseUser.getUid();
                    final String fac_name=faculty_name.getText().toString().trim();
                    final String col_id=college_id.getText().toString().trim();
                    final String mobile=mob_no.getText().toString().trim();

                    DatabaseReference reference=database.getReference();

                    reference.child("UserInfo").child(uid).child("Name").setValue(fac_name).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Name Uploaded",Toast.LENGTH_LONG).show();
                        }
                    });
                   reference.child("UserInfo").child(uid).child("CollegeId").setValue(col_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           Toast.makeText(getApplicationContext(),"Id Uploaded",Toast.LENGTH_LONG).show();

                       }
                   });
                     reference.child("UserInfo").child(uid).child("MobileNo").setValue(mobile).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             Toast.makeText(getApplicationContext(),"MobileNo Uploaded",Toast.LENGTH_LONG).show();

                         }
                     });
                    reference.child("UserInfo").child(uid).child("Branch").setValue(f_branch).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Branch Uploaded",Toast.LENGTH_LONG).show();

                        }
                    });
                    reference.child("UserInfo").child(uid).child("Email").setValue(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Email Uploaded",Toast.LENGTH_LONG).show();

                        }
                    });
                    reference.child("UserInfo").child(uid).child("EmailUid").setValue(uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Uid Uploaded",Toast.LENGTH_LONG).show();

                        }
                    });

                    Toast.makeText(getApplicationContext(), "User Registered Successfull", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegistrationActivity.this,Login_Activity.class);
                    startActivity(intent);

                }
                else {
                    Log.e("Error",task.getException().toString());
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
/*public void uploadData(FirebaseUser fu)
{

}*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistrationUser:
                registerUser();
        }
    }


}
