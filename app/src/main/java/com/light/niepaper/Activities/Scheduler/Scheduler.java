package com.light.niepaper.Activities.Scheduler;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.light.niepaper.Activities.Authentication.LoginActivity;
import com.light.niepaper.Model.SchedulerModel;
import com.light.niepaper.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Scheduler extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser muser;
    DatabaseReference mdbref;
    String mUserId;
    private int i =0;
    private String tit;
    private ListView listOfTodo;
    private List<String>  keylist = new ArrayList<>();
    private List<String>  keylistx = new ArrayList<>();
    //private ArrayList<String>= new ArrayList<>();
    private EditText schedtitile;
    private EditText schesmsg;
    private FirebaseListAdapter<SchedulerModel> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarscheduler);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icarrow);
        toolbar.showOverflowMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Scheduler.this, Schedules.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

            if (muser == null) {
                loadLogin();
            } else {
                String useremail = muser.getEmail();
                String uemailnodot = useremail.replace(".", "");
                mdbref = FirebaseDatabase.getInstance().getReference("users/" + uemailnodot);
                final DatabaseReference schednode = mdbref.child("Schedules");
                mUserId = muser.getUid();
                com.getbase.floatingactionbutton.FloatingActionButton fabbut = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabscheduler);

                fabbut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        schedtitile = (EditText) findViewById(R.id.edit_sched_titles);
                        String titlel = schedtitile.getText().toString();
                        schesmsg = (EditText) findViewById(R.id.schedmessage);
                        String sched = schesmsg.getText().toString();
                        schesmsg.setText("");
                        SchedulerModel schedulerModel = new SchedulerModel(sched,"u" , titlel);
                        DatabaseReference schedtit = schednode.child(titlel);
                        String key = schedtit.push().getKey();
                        DatabaseReference lstnode = schedtit.child(key);
                        lstnode.setValue(schedulerModel);
                        displaySchedules(titlel);
                    }
                });

                if (extras != null) {
                    Bundle bundle = getIntent().getExtras();
                    final String note = bundle.getString("schedtitle");
                    schedtitile = (EditText) findViewById(R.id.edit_sched_titles);
                    schedtitile.setText(note);
                    final ListView listOfts = (ListView)findViewById(R.id.listViewscheduler);
                    CheckedTextView ctvir = (CheckedTextView)listOfts.findViewById(R.id.txt_title);

                    displaySchedules(note);

                }


            }
        }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.makenote, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    public void loadLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void displaySchedules(final String g_name){
        final ListView listOfMessages = (ListView)findViewById(R.id.listViewscheduler);
        listOfMessages.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        String uemail= muser.getEmail();
        String refine= uemail.replace(".","");
        mdbref = FirebaseDatabase.getInstance().getReference("users/"+refine);
        final DatabaseReference groupname = mdbref.child("Schedules/"+g_name);
        final DatabaseReference schednode = mdbref.child("Schedules");
        adapter = new FirebaseListAdapter<SchedulerModel>(this,SchedulerModel.class,
                R.layout.todostructure, groupname) {
            @Override
            protected void populateView(View v, SchedulerModel model, int position) {
                final CheckedTextView td = (CheckedTextView)v.findViewById(R.id.txt_title);
                final CheckedTextView todos = (CheckedTextView)v.findViewById(R.id.txt_title);
                todos.setText(model.getTodo());
                retainCheck(g_name, td);
                listOfMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SchedulerModel post = (SchedulerModel)parent.getItemAtPosition(position);
                        final String current_item = post.getTodo();
                        final CheckedTextView to_do = (CheckedTextView)view.findViewById(R.id.txt_title);

                        // Toast.makeText(Scheduler.this,"curent item is "+current_item,Toast.LENGTH_SHORT).show();
                        schednode.child(g_name).orderByChild("title")
                                .equalTo(g_name)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //if(to_do.isChecked()){
                                        final DatabaseReference todotitle = schednode.child(g_name);
                                        todotitle.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot keys : dataSnapshot.getChildren()){
                                                    String key = (String) keys.getKey();
                                                    keylist.add(key);

                                                    for(int j=0; j < keylist.size();j++){
                                                        final String k = keylist.get(j);
                                                        // final DatabaseReference key_node = todotitle.child(k);
                                                        todotitle.child(k).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                String todox =(String) dataSnapshot.child("todo").getValue();
                                                                final DatabaseReference key_node = todotitle.child(k);

                                                                //Toast.makeText(Scheduler.this,"toddo from uknw is "+titletodo,Toast.LENGTH_SHORT).show();
                                                                if(todox == current_item && to_do.isChecked()){
                                                                    key_node.child("checkstate").setValue("t");
                                                                    to_do.setPaintFlags(to_do.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                                                }else if(todox == current_item && !to_do.isChecked()){
                                                                    key_node.child("checkstate").setValue("f");
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }

                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                    }
                });

            }
        };


        listOfMessages.setAdapter(adapter);
    }


    public void retainCheck(final String name, final CheckedTextView todoss){
        String uemail = muser.getEmail();
        String refine = uemail.replace(".", "");
        mdbref = FirebaseDatabase.getInstance().getReference("users/" + refine);
        final DatabaseReference schednodex = mdbref.child("Schedules");
        final DatabaseReference testchst = schednodex.child(name);
        final EditText smsg = (EditText) findViewById(R.id.schedmessage);
        schednodex.child(name).orderByChild("title")
                .equalTo(name)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final DatabaseReference todotitlex = schednodex.child(name);
                        todotitlex.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot keyx : dataSnapshot.getChildren()){
                                    String keyy = (String) keyx.getKey();
                                    DatabaseReference kk_node = todotitlex.child(keyy);
                                    kk_node.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String check_state = (String) dataSnapshot.child("checkstate").getValue();
                                            //Toast.makeText(Scheduler.this, "checkstae is "+ check_state, Toast.LENGTH_SHORT).show();
                                            if(check_state == "t"){
                                                String t_name = (String)dataSnapshot.child("todo").getValue();
                                                todoss.setPaintFlags(todoss.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                                //todoss.setChecked(true);
                                            }else if(check_state == "f"){
                                                todoss.setEnabled(true);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
