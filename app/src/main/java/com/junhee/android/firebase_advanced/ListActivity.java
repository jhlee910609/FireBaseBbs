package com.junhee.android.firebase_advanced;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.junhee.android.firebase_advanced.domain.Bbs;
import com.junhee.android.firebase_advanced.domain.Data;

import java.util.List;

import static com.junhee.android.firebase_advanced.domain.Data.list;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    CustomAdapter adapter;
    Button btnDel, btnAdd, btnFcm;

//    List<Bbs> list = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference bbsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        database = FirebaseDatabase.getInstance();
        bbsRef = database.getReference("bbs");

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnFcm = (Button) findViewById(R.id.btnFcm);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomAdapter(this);
        listView.setAdapter(adapter);

        btnAdd.setOnClickListener(this);
        btnFcm.setOnClickListener(this);
        btnDel.setOnClickListener(this);

//        loadData();
    }

//    public void postData(View view) {
//        Intent intent = new Intent(this, WriteActivity.class);
//        startActivity(intent);
//    }

    public void loadData() {
        bbsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot data) {
                list.clear();
                for (DataSnapshot item : data.getChildren()) {
                    try {
                        Bbs bbs = item.getValue(Bbs.class);
                        list.add(bbs);
                    } catch (Exception e) {
                        Log.e("Firebase", e.getMessage());
                    }
                }
                refreshData(Data.list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void refreshData(List<Bbs> list) {
        adapter.setData(list);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int count = 0;
        switch (v.getId()) {
            case R.id.btnAdd:
                intent = new Intent(this, WriteActivity.class);
                startActivity(intent);
                break;

            case R.id.btnDel:
                SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                count = adapter.getCount();

                for (int i = count - 1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        Bbs bbs = Data.list.get(i);
                        bbsRef.child(bbs.bbsKey).removeValue();
                        Data.list.remove(i);
                    }
                }
                listView.clearChoices();
                adapter.notifyDataSetChanged();
                break;

            case R.id.btnFcm:
                intent = new Intent(this, MessageActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
