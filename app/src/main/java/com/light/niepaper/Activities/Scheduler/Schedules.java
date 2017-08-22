package com.light.niepaper.Activities.Scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.light.niepaper.Activities.Authentication.LoginActivity;
import com.light.niepaper.Activities.Authentication.profileActivity;
import com.light.niepaper.Model.ChatMessageModel;
import com.light.niepaper.Model.SchedulerModel;
import com.light.niepaper.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static android.support.v7.appcompat.R.id.info;

public class Schedules extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser muser;
    DatabaseReference mdbref;
    String mUserId;
    private String notetitle_;
    private String tt;
    private List<String>titles = new ArrayList<>();
    private ArrayList<String> notelist= new ArrayList<>();
    private FirebaseListAdapter<SchedulerModel> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarschedules);
        setSupportActionBar(toolbar);
        setTitle("SCHEDULES");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icarrow);
        toolbar.showOverflowMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Schedules.this, profileActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();
        if (muser == null) {
            loadLogin();
        } else {
            mUserId = muser.getUid();
            String useremail = muser.getEmail();
            String uemailnodot = useremail.replace(".","");
            mdbref = FirebaseDatabase.getInstance().getReference("users/"+uemailnodot);
            DatabaseReference note = mdbref.child("Schedules");
            mUserId = muser.getUid();
            final ListView listOftodos = (ListView)findViewById(R.id.listViewschedule);
            final ArrayAdapter<String> adaptert = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notelist);
            listOftodos.setAdapter(adaptert);
            registerForContextMenu(listOftodos);
            com.getbase.floatingactionbutton.FloatingActionButton fabbut = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_menuscheduler);
            fabbut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Schedules.this,Scheduler.class);
                    startActivity(intent);
                }
            });
            note.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Set<String> set = new HashSet<String>();
                    Iterator i = dataSnapshot.getChildren().iterator();

                    while (i.hasNext()){
                        set.add(((DataSnapshot)i.next()).getKey());
                        //tt=((DataSnapshot)i.next()).getKey();
                       // titles.add(((DataSnapshot)i.next()).getKey());
                    }
                    //for(int j =0;j<set.size();j++){
                         //String tt_ = titles.get(j);
                        //displayChatMessages(tt);
                    //}

                    notelist.clear();
                    notelist.addAll(set);

                    adaptert.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            listOftodos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String notetitle = (String) listOftodos.getItemAtPosition(position);
                    openScheduler(notetitle);
                }
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }
    public void openScheduler(String tit){
        Intent intent = new Intent(Schedules.this,Scheduler.class);
        Bundle bundle = new Bundle();
        bundle.putString("schedtitle",tit);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void loadLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void displayChatMessages(String g_name){
        ListView listOfMessages = (ListView)findViewById(R.id.listViewschedule);
        String uemail= muser.getEmail();
        String refine= uemail.replace(".","");
        mdbref = FirebaseDatabase.getInstance().getReference("users/"+refine);
        final DatabaseReference groupname = mdbref.child("Schedules/"+g_name);
        adapter = new FirebaseListAdapter<SchedulerModel>(this,SchedulerModel.class,
                R.layout.schedules, groupname) {
            @Override
            protected void populateView(View v, SchedulerModel model, int position) {
                TextView messageText = (TextView)v.findViewById(R.id.schedule_text);
                TextView messageTime = (TextView)v.findViewById(R.id.schedule_time);

                messageText.setText(model.getTitle());

                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm)",
                        model.getSchedTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.schedulescontext, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();
        String useremail = muser.getEmail();
        final String uemailnodot = useremail.replace(".","");
        mdbref = FirebaseDatabase.getInstance().getReference("users/"+uemailnodot);
        final DatabaseReference note = mdbref.child("Schedules");
        final ListView listViewx = (ListView) findViewById(R.id.listViewschedule);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.deletetextsch:
                final String info_ =  notelist.get(info.position);
                Log.d("TAG", info_);
                listViewx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        notetitle_ = (String) listViewx.getItemAtPosition(position);
                        Toast.makeText(Schedules.this, "Todo was successfully Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                note.child(info_).orderByChild("title")
                        .equalTo(info_)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                    firstChild.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
        }
        return super.onContextItemSelected(item);
    }
}
