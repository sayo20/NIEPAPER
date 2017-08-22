package com.light.niepaper.Model;

import android.graphics.Bitmap;

/**
 * Created by sayo on 4/28/2017.
 */

public class NewNoteModel {
    private String notetitle;
    private String notecontent;
    private String id;
    private Bitmap img;



    public NewNoteModel() {
    }

    public NewNoteModel(String notetitle , String notecontent ){
        this.notetitle = notetitle;
        this.notecontent = notecontent;

    }
    public NewNoteModel(String notetitle , Bitmap img){
        this.notetitle = notetitle;
        this.img = img;
    }
    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
    public String getNotetitle() {
        return notetitle;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public String getNotecontent() {
        return notecontent;
    }

    public void setNotecontent(String notecontent) {
        this.notecontent = notecontent;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}