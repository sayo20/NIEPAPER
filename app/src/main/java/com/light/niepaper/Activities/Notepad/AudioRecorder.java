package com.light.niepaper.Activities.Notepad;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.light.niepaper.Activities.Authentication.LoginActivity;
import com.light.niepaper.R;
import com.light.niepaper.Model.NewNoteModel;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AudioRecorder extends AppCompatActivity {
    protected FirebaseAuth firebaseAuth;
    protected FirebaseStorage storage;
    protected FirebaseUser mUser;
    protected FirebaseUser muser;
    private  String extension;
    protected DatabaseReference mdbref;
    private String mUserId;
    private Button record;
    private Button stop;
    private Button play;
    int i = 0;
    EditText title;
    String mUserid;
    private MediaPlayer mPlayer = null;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private MediaRecorder mRecorder=null;
    private  String mFileName=null ;
    private ProgressDialog mProgress;
    private static final String LOG_TAG = "AudioRecordTest";
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_recorder);
        setTitle("Audio Recorder");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icarrow);
        toolbar.showOverflowMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudioRecorder.this , makeNote.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        //ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
        mProgress = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        mUser = firebaseAuth.getCurrentUser();
        mUserid = mUser.getUid();
        Intent intent =getIntent();
        Bundle extras = intent.getExtras();

        if(mUserid == null){
            loadLogin();
        }else{

            record = (Button)findViewById(R.id.recording_button);
            stop = (Button)findViewById(R.id.stop_button);
            play = (Button)findViewById(R.id.play_button);
            title = (EditText)findViewById(R.id.edit_Audio);
            Bundle bundl = getIntent().getExtras();
            String init = bundl.getString("audiotitle");
            title.setText(init);
//            if(extras != null){
//                title = (EditText)findViewById(R.id.edit_text_titlex);
//                Bundle bundlex = getIntent().getExtras();
//                String init_tit = bundlex.getString("audiotitle");
//                title.setText(init_tit);
//        }
//                i=1;
//                if(extras != null&& i !=0 ){
//                    Bundle bundle = getIntent().getExtras();
//                    String filename = bundle.getString("audiotit");
//                    title.setText(filename);
//                }




            String tit_ = title.getText().toString();

            String tit_nodot = tit_.replace(".","");

           mFileName+= tit_nodot+".3gp";





                //mFileName =title.getText().toString() + ".3gp";

                record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startRecording();
                        record.setBackgroundColor(Color.parseColor("#000080"));
                        record.setText("Recording");
                        //play.setEnabled(false);

                    }
                });

                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(play.getText().toString() == "Stop Playing..."){
                            play.setBackgroundColor(Color.parseColor("#0d2bf6"));
                            play.setText("Play");
                            stopPlaying();
                        }else{
                            stop.setBackgroundColor(Color.parseColor("#000080"));
                            record.setText("Record");
                            //record.setEnabled(false);
                            stop.setText("Done!");
                            record.setBackgroundColor(Color.parseColor("#0d2bf6"));
                            stopRecording();
                            uploadAudio();
                        }
                    }
                });

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stop.setBackgroundColor(Color.parseColor("#0d2bf6"));
                        stop.setText("Stop");
                        //record.setEnabled(false);
                        play.setBackgroundColor(Color.parseColor("#000080"));
                        play.setText("Stop Playing...");
                        startPlaying();
                    }
                });
            }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.makenote, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseAuth = FirebaseAuth.getInstance();
        switch (item.getItemId()){
            case R.id.actionlog:
                firebaseAuth.signOut();
                loadLogin();
                return true;

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
    private void startPlaying() {

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }
    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    private void uploadAudio(){
        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();
        mUserId = muser.getUid();
        String useremail = muser.getEmail();
        String uemailnodot = useremail.replace(".","");
        mdbref = FirebaseDatabase.getInstance().getReference("users/"+uemailnodot);
        final DatabaseReference note_ =mdbref.child("notes");
        mProgress.setMessage("Saving Audio...");
        mProgress.show();
        storage = FirebaseStorage.getInstance();
        final String title_= title.getText().toString();
        final String titlewithoutdot = title_.replace(".","");
        //String pathh = title_+"/"+title_+".3gp";
        String pathh = titlewithoutdot+"/"+titlewithoutdot+".3gp";
        String path = mFileName + "/"+mFileName+ ".3gp";
        StorageReference audiopath = storage.getReference(pathh);
        Uri uri = Uri.fromFile(new File(mFileName));
        final DatabaseReference note =note_.child(titlewithoutdot);
        final String notekey = note.push().getKey();

        audiopath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri url = taskSnapshot.getDownloadUrl();
                String url_ = url.toString();
                final NewNoteModel bookcontentx =new NewNoteModel(titlewithoutdot, url_);
                Map<String,Object> result = new HashMap<String, Object>();
                result.put(titlewithoutdot,"");
                note_.updateChildren(result);
                note.child(notekey).setValue(bookcontentx);
                Toast.makeText(AudioRecorder.this, "Note was successfully saved", Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }
        });
    }

}
