package com.example.jaykpatel.docsharing_t;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText login_id,login_password;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        findViewById(R.id.sign_up_btn).setOnClickListener(this);
        findViewById(R.id.loginBtn).setOnClickListener(this);
        firebaseAuth=FirebaseAuth.getInstance();
        login_id=(EditText)findViewById(R.id.login_id);
        login_password=(EditText)findViewById(R.id.login_password);
    }

    private void userLogin(){

        String email=login_id.getText().toString().trim();
        String password= login_password.getText().toString().trim();
        if(email.isEmpty()){
            login_id.setError("Email is required");
            login_id.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            login_id.setError("Enter a Valid Email ID");
            login_id.requestFocus();
            return;
        }

        if(password.isEmpty()){
            login_password.setError("Password is Required");
            login_password.requestFocus();
            return;
        }

        if(password.length()<6){
            login_password.setError("Minimum length of password must be 6");
            login_password.requestFocus();
            return;
        }
        final ProgressDialog progressDialog=ProgressDialog.show(Login_Activity.this,"Please Wait","Processing..");

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    Intent intent=new  Intent(Login_Activity.this,Fac_Dash.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
                else {
                    Log.e("Error",task.getException().toString());
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.sign_up_btn:
                Intent intent=new Intent(this,RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.loginBtn:
                userLogin();
                break;
        }

    }
}
