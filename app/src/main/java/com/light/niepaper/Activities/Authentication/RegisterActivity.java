package com.light.niepaper.Activities.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.light.niepaper.R;

public class RegisterActivity extends AppCompatActivity {
    protected EditText name;
    protected EditText email;
    protected EditText passwd;
    private ProgressDialog mProgress;
    protected Button register ;
    protected FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener ;
    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity__signup);
        mProgress = new ProgressDialog(this);
    }

    public void onRegister(View view){
        firebaseAuth = FirebaseAuth.getInstance();
        mProgress.setMessage("Creating Account...");
        mProgress.show();
        name = (EditText) findViewById(R.id.et_name);
        email = (EditText) findViewById(R.id.et_email);
        passwd = (EditText) findViewById(R.id.et_passwd);



        String email_ = email.getText().toString();
        String passwd_ = passwd.getText().toString();
        email_ =email_.trim();
        passwd_ = passwd_.trim();


        if(email_.isEmpty() || passwd_.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage(R.string.signup_error)
                    .setTitle(R.string.login_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else if(passwd_.length() < 8){
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage(R.string.password_error)
                    .setTitle(R.string.login_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else{

            firebaseAuth.createUserWithEmailAndPassword(email_,passwd_)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mProgress.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, profileActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("username" , name.getText().toString());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage(task.getException().getMessage())
                                        .setTitle(R.string.login_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }

                        }
                    });

//            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                    .setDisplayName(name.getText().toString()).build();
//            user.updateProfile(profileUpdates)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Log.d(TAG, "User profile updated.");
//                            }
//                        }
//                    });
        }
    }

//    @Override
//    protected void onStop() {
//        firebaseAuth = FirebaseAuth.getInstance();
//        super.onStop();
//        if(mAuthListener != null){
//            firebaseAuth.removeAuthStateListener(mAuthListener);
//        }
//
//    }

//    @Override
//    protected void onResume() {
//        firebaseAuth = FirebaseAuth.getInstance();
//        super.onResume();
//        firebaseAuth.addAuthStateListener(mAuthListener);
//    }
}
