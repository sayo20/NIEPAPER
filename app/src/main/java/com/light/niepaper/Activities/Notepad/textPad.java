package com.light.niepaper.Activities.Notepad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.light.niepaper.Activities.Authentication.LoginActivity;
import com.light.niepaper.Model.NewNoteModel;
import com.light.niepaper.R;
//import com.light.niepaper.Model.NewBook;
//
import java.util.HashMap;
import java.util.Map;


public class textPad extends AppCompatActivity  {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser muser ;
    DatabaseReference mdbref ;
    EditText  bookcontent;
    EditText booktitle ;
    private ProgressDialog mProgress;
    private String mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_pad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Text Pad");
        toolbar.setNavigationIcon(R.drawable.icarrow);
        toolbar.showOverflowMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(textPad.this, makeNote.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

        });

        mProgress = new ProgressDialog(this);
        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(extras != null){
            if (muser == null) {
                loadLogin();
            } else {
                String useremail = muser.getEmail();
                String uemailnodot = useremail.replace(".","");
                mdbref = FirebaseDatabase.getInstance().getReference("users/"+uemailnodot);
                Bundle bundle=getIntent().getExtras();
                String note = bundle.getString("note");
                String note_nodot = note.replace(".","");
                DatabaseReference tit =mdbref.child("notes/"+note_nodot);
                bookcontent =(EditText)findViewById(R.id.edit_text_note);
                booktitle =(EditText)findViewById(R.id.edit_text_title);
                String bookt = booktitle .getText().toString();
                int index = note.length() - 3;
                StringBuilder note_withdot = new StringBuilder(note);
                note_withdot.insert(index,".");
                booktitle.setText(note_withdot);
                String bookcont = bookcontent.getText().toString();
                tit.orderByChild("notetitle")
                        .equalTo(note_nodot)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                    String title = (String) messageSnapshot.child("notetitle").getValue();
                                    String content = (String) messageSnapshot.child("notecontent").getValue();
                                    //booktitle.setText(title);
                                    bookcontent.setText(content);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        }



    }
    //copy the tit part add listner and try using the children update there nd how to set itemid on listview
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.makenote, menu);
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
        muser = firebaseAuth.getCurrentUser();

        switch (item.getItemId()){
            case R.id.action_logout:
                firebaseAuth.signOut();
                loadLogin();
                return true;
            case R.id.savetext:
                if (muser == null) {
                    loadLogin();
                }else {
                    String useremail = muser.getEmail();
                    String uemailnodot = useremail.replace(".","");
                    mdbref = FirebaseDatabase.getInstance().getReference("users/"+uemailnodot);
                    final DatabaseReference note_ =mdbref.child("notes");
                    bookcontent = (EditText) findViewById(R.id.edit_text_note);
                    booktitle = (EditText) findViewById(R.id.edit_text_title);
                    final String bookt = booktitle .getText().toString();
                    final String bookcont = bookcontent.getText().toString();
                    String id = bookt + bookcont;
                    final String filt = bookt.replace(".","") ;
                    final DatabaseReference note =note_.child(filt);
                    final String notekey = note.push().getKey();
                    final NewNoteModel bookcontentx =new NewNoteModel(filt , bookcont);
                    Map<String,Object>result = new HashMap<String, Object>();
                    result.put(filt,"");
                    note_.updateChildren(result);
                    note.child(notekey).setValue(bookcontentx);

                    bookcontent.setText("");
                    booktitle.setText("");
                    Toast.makeText(this, "Note was successfully saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this ,makeNote.class);
                    Bundle bundle = new Bundle();
                    intent.putExtras(bundle);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            default:
                return super.onOptionsItemSelected(item);

        }

    }

}

