package com.junhee.android.firebase_advanced.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * Created by JunHee on 2017. 7. 4..
 */

public class Bbs implements Serializable {

    public String bbsKey;
    public String id;
    public String title;
    public String author;
    public String date;
    public String content;
    public String fileUriString;
    public long count;

    public Bbs() {

    }

    public Bbs(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.date = setDate();
    }

    private String setDate() {

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(currentTime);

    }
}
