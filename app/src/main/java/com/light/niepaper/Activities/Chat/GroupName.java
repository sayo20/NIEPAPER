package com.light.niepaper.Activities.Chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.light.niepaper.Activities.Authentication.LoginActivity;
import com.light.niepaper.Activities.Authentication.profileActivity;
import com.light.niepaper.Activities.Scheduler.Schedules;
import com.light.niepaper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GroupName extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser muser;
    DatabaseReference mdbref;
    String mUserId;
    private String notetitle_;
    private String groupname;
    private String groupkey;
    private ListView listView;
    private DatabaseReference key;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_rooms = new ArrayList<>();
    private static final String TAG = "activity_profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name);
        setTitle("CHAT GROUP");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbargroupname);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icarrow);
        toolbar.showOverflowMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupName.this, profileActivity.class);
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
            String uemail= muser.getEmail();
            String refine= uemail.replace(".","");
            mdbref = FirebaseDatabase.getInstance().getReference("users/"+refine);
            final DatabaseReference gname = mdbref.child("groupname");
            listView = (ListView) findViewById(R.id.listViewgroupname);
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);
            listView.setAdapter(arrayAdapter);
            registerForContextMenu(listView);
            com.getbase.floatingactionbutton.FloatingActionButton fabbut = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_menugroupname);
            fabbut.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    request_group_details(gname , muser);
                }

            });
            gname.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Set<String> set = new HashSet<String>();
                    Iterator i = dataSnapshot.getChildren().iterator();

                    while (i.hasNext()) {
                        set.add(((DataSnapshot) i.next()).getKey());
                    }

                    list_of_rooms.clear();
                    list_of_rooms.addAll(set);

                    arrayAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String group_chat_name = (String) listView.getItemAtPosition(position);
                    Intent i = new Intent(GroupName.this, ChatRoom.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("groupN", group_chat_name);
                    i.putExtras(bundle);
                    startActivity(i);
                }
            });

        }
    }


    public void loadLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
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
        final DatabaseReference note = mdbref.child("groupname");
        final ListView listViewx = (ListView) findViewById(R.id.listViewgroupname);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.deletetextsch:
                final String info_ =  list_of_rooms.get(info.position);
                Log.d("TAG", info_);
                listViewx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        notetitle_ = (String) listViewx.getItemAtPosition(position);
                        Toast.makeText(GroupName.this, "Group chat was successfully Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                note.child(info_)//.orderByChild("title")
                        //.equalTo(info_)
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

    private void request_group_details(final DatabaseReference gname , final FirebaseUser user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        builder.setTitle("Enter Group name");
        final EditText group_name = new EditText(this);
        group_name.setHint("Group name");
        layout.addView(group_name);
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                groupname = group_name.getText().toString();
                groupkey = user.getEmail() + groupname;
                Map<String, Object> mapa = new HashMap<String, Object>();
                mapa.put(groupname, "");
                gname.updateChildren(mapa);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });

        builder.show();
    }
    public void existing_room_key(final FirebaseUser user_email , final DatabaseReference node){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText group_key = new EditText(this);
        final EditText group_name = new EditText(this);
        builder.setTitle("Enter key to existing room");
        group_key.setHint("Enter users email");
        layout.addView(group_key);
        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int e_length = user_email.getEmail().length();
                String key = group_key.getText().toString();
                String g_name = key.substring(e_length);
                node.child(g_name).orderByChild("messageKey")
                        .equalTo(key)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                                    String title = (String) messageSnapshot.child("groupName").getValue();
                                    arrayAdapter.add(title);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();

            }
        });

        builder.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();

        switch (item.getItemId()) {
            case R.id.action_logout:
                firebaseAuth.signOut();
                loadLogin();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
