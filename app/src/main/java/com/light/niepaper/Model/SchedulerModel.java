package com.light.niepaper.Model;

import java.util.Date;

/**
 * Created by sayo on 7/3/2017.
 */

public class SchedulerModel {
    private String todo;
    private String checkstate;
    private long schedTime;
    private String title;

    public SchedulerModel(String todo, String checkstate,String title) {
        this.todo = todo;
        this.checkstate = checkstate;
        this.title = title;
        schedTime = new Date().getTime();
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getSchedTime() {
        return schedTime;
    }

    public void setSchedTime(long schedTime) {
        this.schedTime = schedTime;
    }


    public SchedulerModel(String checkstate) {

        this.checkstate = checkstate;

    }
    public SchedulerModel(){}

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getCheckstate() {
        return checkstate;
    }

    public void setCheckstate(String checkstate) {
        this.checkstate = checkstate;
    }
}
