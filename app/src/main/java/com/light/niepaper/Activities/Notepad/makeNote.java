package com.light.niepaper.Activities.Notepad;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.light.niepaper.Activities.Authentication.LoginActivity;
import com.light.niepaper.Activities.Authentication.profileActivity;
import com.light.niepaper.R;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class makeNote extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser muser ;
    DatabaseReference mdbref ;
    String mUserId;
    FirebaseStorage storage;
    private String notetitle_;
    private String groupkey;
    private ArrayList<String> notelist= new ArrayList<>();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_note);
        setTitle("Notes");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmakenote);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icarrow);
        toolbar.showOverflowMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(makeNote.this , profileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menunotes);
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
        if (muser == null) {
            loadLogin();
        } else {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference drawing = storage.getReference();
            mUserId = muser.getUid();
            String useremail = muser.getEmail();
            String uemailnodot = useremail.replace(".","");
            mdbref = FirebaseDatabase.getInstance().getReference("users/"+uemailnodot);
            DatabaseReference note = mdbref.child("notes");
            final ListView listViewx = (ListView) findViewById(R.id.listViewa);
            final ArrayAdapter<String> adaptert = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notelist);
            listViewx.setAdapter(adaptert);
            registerForContextMenu(listViewx);
            com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_note);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(makeNote.this, textPad.class);
                    startActivity(intent);

                }
            });
            com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonsketch = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_sketch);
            floatingActionButtonsketch.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(makeNote.this, SketchRoom.class);
                    startActivity(intent);

                }
            });
            com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonaudio = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_audio);
            floatingActionButtonaudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudionameDialog();

                }
            });
            note.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Set<String> set = new HashSet<String>();
                    Iterator i = dataSnapshot.getChildren().iterator();

                    while (i.hasNext()){
                        set.add(((DataSnapshot)i.next()).getKey());
                    }

                    notelist.clear();
                    notelist.addAll(set);

                    adaptert.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            
            listViewx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String notetitle = (String) listViewx.getItemAtPosition(position);
                    if(notetitle.length() > 3){
                        if(notetitle.endsWith("txt")){
                            transfer(notetitle);
                        }else if(notetitle.endsWith("png")){
                            sketchroom(notetitle);
                        }else if(notetitle.endsWith("mp3")){
                            audioRoom(notetitle);
                        }
                    }

                    Log.d("TAG", notetitle);


                }
            });
    }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }
    public void transfer(String title ){
        Intent intent = new Intent(makeNote.this , textPad.class);
        Bundle bundle = new Bundle();
        bundle.putString("note",title);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void sketchroom(String title){
        Intent intent = new Intent(makeNote.this , SketchRoom.class);
        Bundle bundle = new Bundle();
        bundle.putString("sketchtit",title);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void audioRoom(String title){
        Intent intent = new Intent(makeNote.this , AudioRecorder.class);
        Bundle bundle = new Bundle();
        bundle.putString("audiotitle",title);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);

    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        storage=FirebaseStorage.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();
        mUserId = muser.getUid();
        String useremail = muser.getEmail();
        final String uemailnodot = useremail.replace(".","");
        mdbref = FirebaseDatabase.getInstance().getReference("users/"+uemailnodot);
        final DatabaseReference note = mdbref.child("notes");
        final ListView listViewx = (ListView) findViewById(R.id.listViewa);
        final String imgurl = "gs://niepaper.appspot.com";
        final StorageReference photoRef = storage.getReferenceFromUrl(imgurl);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.deletetext:
                final String info_ =  notelist.get(info.position);
                Log.d("TAG", info_);
                listViewx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        notetitle_ = (String) listViewx.getItemAtPosition(position);


                    }
                });
                note.child(info_).orderByChild("notetitle")
                        .equalTo(info_)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    if(info_.endsWith("png")){
                                        photoRef.child(info_+"/"+info_+".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(makeNote.this, "Sketch was successfully Deleted", Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                Toast.makeText(makeNote.this, "Uh-oh, an error occured", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                        firstChild.getRef().removeValue();
                                    }else if(info_.endsWith("txt")){
                                        DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                        firstChild.getRef().removeValue();
                                        Toast.makeText(makeNote.this, "Sketch was successfully Deleted", Toast.LENGTH_SHORT).show();
                                    }else if(info_.endsWith("mp3")){
                                        photoRef.child(info_+"/"+info_+".3gp").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(makeNote.this, "Audio was successfully Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                Toast.makeText(makeNote.this, "Uh-oh, an error occured", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                        firstChild.getRef().removeValue();
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            case R.id.sharetext:
                if(mUserId == null){
                    loadLogin();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LinearLayout linearLayout = new LinearLayout(this);
                    final EditText email = new EditText(this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.addView(email);
                    email.setHint("Enter recievers email");
                    builder.setTitle("Recievers email");
                    builder.setView(linearLayout);
                    final String note_to_share = notelist.get(info.position);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String recievers_email = email.getText().toString();
                            String recievers_enodot = recievers_email.replace(".","");
                            String current_user = muser.getEmail();
                            String cur_usernodot = current_user.replace(".", "");
                            DatabaseReference curr_user = FirebaseDatabase.getInstance().getReference("users/"+cur_usernodot);
                            DatabaseReference note_curr = curr_user.child("notes");
                            if(recievers_email.length() != 0){
                                DatabaseReference rrpath = FirebaseDatabase.getInstance().getReference("users/"+ recievers_enodot);
                                final DatabaseReference r_notesnode = rrpath.child("notes/"+note_to_share);
                                note_curr.child(note_to_share).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        r_notesnode.setValue(dataSnapshot.getValue());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                    });
                    builder.show();
                }

        }

        return super.onContextItemSelected(item);
    }
    public void AudionameDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText groupkey_ = new EditText(this);
        builder.setTitle("Enter Audio Title");
        groupkey_.setHint("Enter your title here, and add extension `.mp3`");
        layout.addView(groupkey_);
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(makeNote.this, AudioRecorder.class);
                    Bundle bundle = new Bundle();
                    groupkey = groupkey_.getText().toString();
                    bundle.putString("audiotitle" ,groupkey);
                    intent.putExtras(bundle);
                    startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
