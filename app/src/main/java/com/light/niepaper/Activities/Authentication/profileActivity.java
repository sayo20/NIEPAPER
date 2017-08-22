package com.light.niepaper.Activities.Authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.light.niepaper.Activities.Chat.GroupName;
import com.light.niepaper.Activities.Notepad.makeNote;
import com.light.niepaper.Activities.Scheduler.Schedules;
import com.light.niepaper.R;

public class profileActivity extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    Button notebut;
    Button chatroom;
    Button schedules;
    Button existing_note;
    ImageView imageView;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("NIEPAPER");
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
        schedules = (Button)findViewById(R.id.schedulder);
        chatroom = (Button) findViewById(R.id.chat);
        notebut = (Button) findViewById(R.id.notebook);
        notebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(profileActivity.this, makeNote.class);
                startActivity(intent);
            }

        });
        schedules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profileActivity.this, Schedules.class);
                startActivity(intent);
            }
        });
        imageView = (ImageView)findViewById(R.id.textView2) ;
        imageView.setEnabled(false);
        chatroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten =getIntent();
                Bundle extras = inten.getExtras();
                Intent intent = new Intent(profileActivity.this, GroupName.class);
                if(extras != null){
                    final Bundle bundle = getIntent().getExtras();
                    String usern = bundle.getString("username");
                    Bundle b = new Bundle();
                    b.putString("usern",usern);
                    inten.putExtras(b);
                }
                startActivity(intent);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();






//        existing_note = (Button)findViewById(R.id.existing_note);
//        existing_note.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(profileActivity.this , makebook.class);
//                startActivity(intent);
//            }
//        });
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    public void loadLogin(){
        Intent intent = new Intent(this , LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = FirebaseAuth.getInstance();
        switch (item.getItemId()){
            case R.id.action_logout:
                firebaseAuth.signOut();
                loadLogin();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
