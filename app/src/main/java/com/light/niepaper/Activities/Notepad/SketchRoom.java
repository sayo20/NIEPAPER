package com.light.niepaper.Activities.Notepad;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.light.niepaper.Activities.Authentication.LoginActivity;
import com.light.niepaper.Model.NewNoteModel;
import com.light.niepaper.R;
import com.light.niepaper.Customviews.DrawingClassView;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SketchRoom extends AppCompatActivity {
    private Toolbar mToolbar_bottom;
    private FloatingActionButton mfab;
    private String mUserId;
    private String url;
    FirebaseStorage store;
   // private float smallBrush, mediumBrush, largeBrush;
    private DrawingClassView drawingClass;
    private ImageButton currPaint, eraseBtn;
    protected FirebaseAuth firebaseAuth;
    private ProgressDialog mProgress;
    protected FirebaseUser muser;
    protected EditText sketch_title;
    protected DatabaseReference mdbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Sketch Pad");
        toolbar.setNavigationIcon(R.drawable.icarrow);
        toolbar.showOverflowMenu();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SketchRoom.this , makeNote.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        mProgress = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();
        Intent intent =getIntent();
        Bundle extras = intent.getExtras();
        if(muser == null){
            loadLogin();
        }else {
            store = FirebaseStorage.getInstance();
            drawingClass = (DrawingClassView) findViewById(R.id.drawing_class);
            mToolbar_bottom = (Toolbar) findViewById(R.id.toolbar_bottom);
            mToolbar_bottom.inflateMenu(R.menu.draw_menu);
            mToolbar_bottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    handleDrawingIconTouched(item.getItemId());
                    return false;
                }
            });
            if (extras != null) {
                Bundle bundle = getIntent().getExtras();
                String note = bundle.getString("sketchtit");
                int index = note.length() - 3;
                StringBuilder note_withdot = new StringBuilder(note);
                note_withdot.insert(index,".");
                final EditText sketcht = (EditText)findViewById(R.id.edit_text_titles);
                sketcht.setText(note_withdot);
                final String imgurl ="gs://niepaper.appspot.com";
                final StorageReference photoRef = store.getReferenceFromUrl(imgurl).child(note+"/"+note+".png");
                final long ONE_MEGABYTE = 1024 * 1024;
                photoRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        drawingClass.redraw(bitmap);
                    }
                });
            }
        }

    }
    private void handleDrawingIconTouched(int itemId) {

        switch (itemId) {
            case R.id.action_delete:
                deleteDialog();
                break;
//            case R.id.action_undo:
//                drawingClass.onClickUndo();
//                break;
//            case R.id.action_redo:
//                drawingClass.onClickRedo();
//                break;
            case R.id.action_brush:
                drawingClass = (DrawingClassView) findViewById(R.id.drawing_class);
                drawingClass.setErase(false);
                //if(this.View.getId()==R.id.action_brush){
                final Dialog brushDialog = new Dialog(this);
                brushDialog.setTitle("Brush size:");
                brushDialog.setContentView(R.layout.brush_chooser);
                ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
                smallBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawingClass.setBrushSize(getResources().getInteger(R.integer.small_size));
                        drawingClass.setLastBrushSize(getResources().getInteger(R.integer.small_size));
                        brushDialog.dismiss();
                    }
                });
                ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
                mediumBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawingClass.setBrushSize(getResources().getInteger(R.integer.medium_size));
                        drawingClass.setLastBrushSize(getResources().getInteger(R.integer.medium_size));
                        brushDialog.dismiss();
                    }
                });

                ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
                largeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawingClass.setBrushSize(getResources().getInteger(R.integer.large_size));
                        drawingClass.setLastBrushSize(getResources().getInteger(R.integer.large_size));
                        brushDialog.dismiss();
                    }
                });
                brushDialog.show();
                //}
                //}
                break;
            case R.id.action_color:
                colorMenu();
                break;
            case R.id.action_erase:
                final Dialog brushDialogx = new Dialog(this);
                brushDialogx.setTitle("Eraser size:");
                brushDialogx.setContentView(R.layout.brush_chooser);
                ImageButton smallBtnx = (ImageButton)brushDialogx.findViewById(R.id.small_brush);
                smallBtnx.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        drawingClass.setErase(true);
                        drawingClass.setBrushSize(getResources().getInteger(R.integer.small_size));
                        //drawingClass.setErase(false);
                        brushDialogx.dismiss();
                    }
                });
                ImageButton mediumBtnx = (ImageButton)brushDialogx.findViewById(R.id.medium_brush);
                mediumBtnx.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        drawingClass.setErase(true);
                        drawingClass.setBrushSize(getResources().getInteger(R.integer.medium_size));
                        //drawingClass.setErase(false);
                        brushDialogx.dismiss();
                    }
                });
                ImageButton largeBtnx = (ImageButton)brushDialogx.findViewById(R.id.large_brush);
                largeBtnx.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        drawingClass.setErase(true);
                        drawingClass.setBrushSize(getResources().getInteger(R.integer.large_size));
                       // drawingClass.setErase(false);
                        brushDialogx.dismiss();
                    }
                });
                brushDialogx.show();
                break;
            case R.id.action_save:
                save();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.makenote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (item.getItemId()){
            case R.id.savetext:
                save();
                break;
            case R.id.actionlog:
                loadLogin();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void deleteDialog(){
        drawingClass = (DrawingClassView)findViewById(R.id.drawing_class);
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this);
        deleteDialog.setTitle(getString(R.string.deleteDrawing));
        deleteDialog.setMessage(getString(R.string.deleteConfirmation));
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawingClass.eraseAll();
                dialog.dismiss();
            }
        });
        deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        deleteDialog.show();
    }
    public void loadLogin(){
        Intent intent = new Intent(this , LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void  colorMenu(){
        Dialog d = new Dialog(SketchRoom.this);
        d.setTitle("Change Color");
        d.setContentView(R.layout.colorpicker);
        LinearLayout paintLayout = (LinearLayout)d.findViewById(R.id.paintcolors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paintpressed));

        d.show();
    }
    public void paintClicked(View view){
        drawingClass = (DrawingClassView)findViewById(R.id.drawing_class);
        if(view!=currPaint){
            drawingClass.setErase(false);
            drawingClass.setBrushSize(drawingClass.getLastBrushSize());
            ImageButton imgView = (ImageButton)view;
            String color = imgView.getTag().toString();
            drawingClass.setColorx(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paintpressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
           currPaint=imgView;
        }
    }

    public void save(){
        mProgress.setMessage("Saving Sketch...");
        mProgress.show();
        firebaseAuth = FirebaseAuth.getInstance();
        muser = firebaseAuth.getCurrentUser();
        mUserId = muser.getUid();
        String useremail = muser.getEmail();
        String uemailnodot = useremail.replace(".","");
        mdbref = FirebaseDatabase.getInstance().getReference("users/"+uemailnodot);
        final DatabaseReference note_ =mdbref.child("notes");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        drawingClass = (DrawingClassView)findViewById(R.id.drawing_class);
        drawingClass.setDrawingCacheEnabled(true);
        drawingClass.buildDrawingCache();
        Bitmap bitmap = drawingClass.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        drawingClass.setDrawingCacheEnabled(false);
        byte[] data = baos.toByteArray();
        final EditText sketcht = (EditText)findViewById(R.id.edit_text_titles);
        final String sketchpng = sketcht.getText().toString();
        final String sketchpng_nodot = sketchpng.replace(".","");
        final DatabaseReference note =note_.child(sketchpng_nodot);
        final String notekey = note.push().getKey();
        String path = sketchpng_nodot+"/"+sketchpng_nodot+".png";
        StorageReference sref = storage.getReference(path);

        StorageMetadata stmeta = new StorageMetadata.Builder()
                .setCustomMetadata("text" , sketchpng_nodot)
                .build();
        UploadTask uploadTask = sref.putBytes(data,stmeta);
        uploadTask.addOnSuccessListener(SketchRoom.this , new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                Uri url = taskSnapshot.getDownloadUrl();
                String url_ = url.toString();
                final NewNoteModel bookcontentx =new NewNoteModel(sketchpng_nodot, url_);
                Map<String,Object>result = new HashMap<String, Object>();
                result.put(sketchpng_nodot,"");
                note_.updateChildren(result);
                note.child(notekey).setValue(bookcontentx);
                sketcht.setText("");
                drawingClass.eraseAll();
                Toast.makeText(SketchRoom.this, "Sketch was successfully saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SketchRoom.this , makeNote.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                mProgress.dismiss();
             }
        });



    }

}
