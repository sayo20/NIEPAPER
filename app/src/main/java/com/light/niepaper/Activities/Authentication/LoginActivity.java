package com.light.niepaper.Activities.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.light.niepaper.R;

public class LoginActivity extends AppCompatActivity {
    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button logInButton;
    protected TextView signUpTextView;
    protected TextView forgotPasswd;
    private FirebaseAuth firebaseAuth ;
    private ProgressDialog mProgress;
    private FirebaseAuth.AuthStateListener mAuthListener ;

    private FirebaseUser user;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgress = new ProgressDialog(this);


    }
    public void onsignup(View view){
        Intent intent = new Intent(this , RegisterActivity.class);
        startActivity(intent);
    }

    public  void onLogin(View view){
        mProgress.setMessage("Login in Process...");
        mProgress.show();
        firebaseAuth = FirebaseAuth .getInstance();
        emailEditText = (EditText) findViewById(R.id.emaillogin);
        passwordEditText = (EditText) findViewById(R.id.passwordlogin);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        email = email.trim();
        password = password.trim();
        firebaseAuth = FirebaseAuth.getInstance();
        user= firebaseAuth.getCurrentUser();
        if(email.isEmpty() || password.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage(R.string.login_error_message)
                    .setTitle(R.string.login_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
//            Bundle bundle = getIntent().getExtras();
//            final String name = bundle.getString("username");
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mProgress.dismiss();
                                //mAuthListener = new FirebaseAuth.AuthStateListener(){
                                Intent intent = new Intent(LoginActivity.this, profileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);


                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage(task.getException().getMessage())
                                            .setTitle(R.string.login_error_title)
                                            .setPositiveButton(android.R.string.ok, null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();

                            }
                        }
                    });

        }

    }
    public void onForgotpwd(View view){
        firebaseAuth = FirebaseAuth .getInstance();
        emailEditText = (EditText) findViewById(R.id.emaillogin);
        passwordEditText = (EditText) findViewById(R.id.passwordlogin);
        user = firebaseAuth.getCurrentUser();
        String email = emailEditText.getText().toString();

        email = email.trim();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG ,"Password reset email sent");
                        }
                    }
                });
    }
}
