package com.demo.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;           //唯一id
    private String mTitle;      //标题
    private Date mDate;         //日期
    private boolean mSolved;    //是否已经解决

    public Crime(){
        this(UUID.randomUUID());
    }

    public Crime(UUID id){
        mId=id;
        mDate=new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
