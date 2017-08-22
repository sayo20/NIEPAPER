package com.light.niepaper.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.light.niepaper.Activities.Authentication.LoginActivity;
import com.light.niepaper.Activities.Authentication.profileActivity;
import com.light.niepaper.Activities.Notepad.makeNote;
import com.light.niepaper.Activities.Notepad.textPad;
import com.light.niepaper.R;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;


public class makebook extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser muser ;
    DatabaseReference mdbref ;
    EditText namedEditTxt;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        setContentView(R.layout.activity_makebook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icarrow);
        toolbar.showOverflowMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),profileActivity.class));
            }
        });

        //setting up the overlay for the FAB
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(240);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();

        if(muser == null){
            loadLogin();
        }else {
            final ListView listView = (ListView) findViewById(R.id.listView);
            final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
            listView.setAdapter(adapter);
            mUserId = muser.getUid();
            //mdbref = FirebaseDatabase.getInstance().getReference("users/"+mUserId);
            //final String notekey = mdbref.push().getKey();
            com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_book);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog d = new Dialog(makebook.this);
                    d.setTitle("Create new book");
                    d.setContentView(R.layout.inputdialog);
                    namedEditTxt = (EditText) d.findViewById(R.id.bookEditText);
                    Button saveBtn = (Button) d.findViewById(R.id.saveBtn);
                    d.show();

                    saveBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String bookname = namedEditTxt.getText().toString();
//                            NewNoteModel booktitlex =new NewNoteModel(bookname);
                            //mdbref.child(notekey).setValue(booktitlex);
                            namedEditTxt.setText("");
                            Intent intent =new Intent(makebook.this , textPad.class);
                            Bundle bundle = new Bundle();
                            //bundle.putString("booktitlekey",notekey);
                            intent.putExtras(bundle);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    });


                }
            });
//            mdbref.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    adapter.add((String) dataSnapshot.child("contents/notetitle").getValue());
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    adapter.remove((String) dataSnapshot.child("contents/notetitle").getValue());
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(makebook.this , makeNote.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
        }
        //testing authentification before filling up the list

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


