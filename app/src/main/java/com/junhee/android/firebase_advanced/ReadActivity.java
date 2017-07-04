package com.junhee.android.firebase_advanced;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.junhee.android.firebase_advanced.domain.Bbs;
import com.junhee.android.firebase_advanced.domain.Data;

public class ReadActivity extends AppCompatActivity {

    ImageView img;
    TextView title, author, date, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        img = (ImageView) findViewById(R.id.read_img);
        title = (TextView) findViewById(R.id.read_title);
        author = (TextView) findViewById(R.id.read_author);
        date = (TextView) findViewById(R.id.read_date);
        content = (TextView) findViewById(R.id.read_content);

        setData();

    }

    public void setData() {

        // 목록에서 넘어온 position 값을 이용해 상세보기 데이터를 결정
        Intent intent = getIntent();
        int position = intent.getIntExtra("LIST_POSITION", -1);
        if (position > -1) {
            Bbs bbs = Data.list.get(position);
            if (bbs.fileUriString != null && !"".equals(bbs.fileUriString)) {
                Glide.with(this)
                        .load(bbs.fileUriString)
                        .into(img);
            }
            title.setText(bbs.title);
            author.setText(bbs.author);
            date.setText(bbs.date);
            content.setText(bbs.content);



        }


    }
}
