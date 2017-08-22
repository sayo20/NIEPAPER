package com.light.niepaper.Activities.Chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.light.niepaper.Activities.Authentication.LoginActivity;
import com.light.niepaper.R;
import com.light.niepaper.Model.ChatMessageModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ChatRoom extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseUser muser;
    DatabaseReference mdbref;
    String mUserId;
    String temp_key;
    private List<String>useringroup = new ArrayList<>();
    private List<String>otherusers = new ArrayList<>();
    private String gname;
    private FirebaseListAdapter<ChatMessageModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarchatroom);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icarrow);
        toolbar.showOverflowMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatRoom.this, GroupName.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();



        //}
        firebaseAuth = FirebaseAuth.getInstance();

        muser = firebaseAuth.getCurrentUser();
        if(extras != null) {
        if (muser == null) {
            loadLogin();
        } else {
            Bundle b = getIntent().getExtras();
            gname = b.getString("groupN");
            setTitle(gname);
            final String uname= muser.getEmail();
            final String key = uname + gname;
            final EditText chatmessage = (EditText) findViewById(R.id.chatmessage);
            mUserId = muser.getUid();
            String uemail= muser.getEmail();
            String refine= uemail.replace(".","");
            mdbref = FirebaseDatabase.getInstance().getReference("users/"+refine);
            final DatabaseReference groupname = mdbref.child("groupname/"+ gname);
            com.getbase.floatingactionbutton.FloatingActionButton fabbut = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabchatroom);
            fabbut.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {

                    ChatMessageModel chatMessage = new ChatMessageModel(chatmessage.getText().toString(), uname  , key , gname);
                    chatmessage.setText("");
                    temp_key = groupname.push().getKey();
                    final DatabaseReference msg_root = groupname.child(temp_key);
                    msg_root.setValue(chatMessage);
                    sendMessagetoAll(gname,temp_key,chatMessage,msg_root);
                   //fillGrouplist(groupname,msg_root,gname,temp_key);
                }
            });
            displayChatMessages(gname);

        }
    }

}



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groupname, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        switch (item.getItemId()){
            case R.id.signout:
                firebaseAuth.signOut();
                loadLogin();
                return true;
            case R.id.existing_group:
                if(extras != null) {
                    if (muser == null) {
                        loadLogin();
                    } else {
                        mUserId = muser.getUid();
                        Bundle b = getIntent().getExtras();
                        gname = b.getString("groupN");
                        addnewUser(gname, muser);
                    }
                }
            default:
                return super.onOptionsItemSelected(item);

        }

    }
    public void loadLogin(){
        Intent intent = new Intent(this , LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void displayChatMessages(String g_name){
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        String uemail= muser.getEmail();
        String refine= uemail.replace(".","");
        mdbref = FirebaseDatabase.getInstance().getReference("users/"+refine);
        final DatabaseReference groupname = mdbref.child("groupname/"+g_name);
        adapter = new FirebaseListAdapter<ChatMessageModel>(this, ChatMessageModel.class,
                R.layout.messages, groupname) {
            @Override
            protected void populateView(View v, ChatMessageModel model, int position) {
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm)",
                        model.getMessageTime()));
            }
        };

        listOfMessages.setAdapter(adapter);
    }
    public void addnewUser(final String gname , final FirebaseUser muser){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        builder.setTitle("User's Email");
        final EditText group_email = new EditText(this);
        layout.addView(group_email);
        String semail = muser.getEmail();
        final String semailnodot = semail.replace(".","");
        builder.setView(layout);
        otherusers.add(semailnodot);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String email = group_email.getText().toString();
                final String email_nodot = email.replace(".","");
                mUserId = muser.getUid();
                DatabaseReference current_users_path = FirebaseDatabase.getInstance().getReference("users/"+semailnodot);
                DatabaseReference groups = current_users_path.child("groupname/"+gname);
                if(email.length() != 0){
                    final DatabaseReference rpath = FirebaseDatabase.getInstance().getReference("users/"+email_nodot);
                    final DatabaseReference rrpath = rpath.child("groupname/"+gname);
                    String groupkey = rrpath.push().getKey();

                    useringroup.add(email_nodot);
                    useringroup.add(semailnodot );
                    groups.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            rrpath.setValue(dataSnapshot.getValue());

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
    public void sendMessagetoAll(String gname, String key, ChatMessageModel chatMessageModel , DatabaseReference sender){
        String emails;
        for(int i = 0; i < useringroup.size();i++){
            emails = useringroup.get(i);
            DatabaseReference root_node = FirebaseDatabase.getInstance().getReference("users/"+emails);
            DatabaseReference groups = root_node.child("groupname/"+gname);
            final DatabaseReference key_node = groups.child(key);
            sender.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    key_node.setValue(dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    //i think one method either sendmsgall or fillgroup can perform one function..that is send message to every one in group and try to get all members in one lost soo i can have one listner send to the entire list ...God help me
    public void fillGrouplist(DatabaseReference group,DatabaseReference sender, String gname, String key){
        String emails;
        String tmp;
        for(int i= 0; i<otherusers.size(); i++){
            emails = otherusers.get(i);
            DatabaseReference root_node = FirebaseDatabase.getInstance().getReference("users/"+emails);
            DatabaseReference groups = root_node.child("groupname/"+gname);
            final DatabaseReference key_node = groups.child(key);
            sender.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    key_node.setValue(dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
