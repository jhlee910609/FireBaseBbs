package com.junhee.android.firebase_advanced;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.junhee.android.firebase_advanced.domain.Bbs;

import java.io.File;

public class WriteActivity extends AppCompatActivity {

    EditText editTitle, editContent, editAuthor;
    TextView date, textImg;
    Button btnPost, btnDel;
    String bbsKey;

    public static final String BBS_KEY = "bbsKey";
    Bbs bbs;

    FirebaseDatabase database;
    DatabaseReference bbsRef;
    // storage ref
    private StorageReference mStorageRef;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        editAuthor = (EditText) findViewById(R.id.edit_author);
        editContent = (EditText) findViewById(R.id.edit_content);
        editTitle = (EditText) findViewById(R.id.edit_title);
        textImg = (TextView) findViewById(R.id.detail_imgName);

//        btnDel = (Button) findViewById(R.id.btnDel);

        date = (TextView) findViewById(R.id.detail_date);
        database = FirebaseDatabase.getInstance();
        bbsRef = database.getReference("bbs");
        bbsKey = bbsRef.push().getKey();
        // storage ref.
        mStorageRef = FirebaseStorage.getInstance().getReference("images");

        dialog = new ProgressDialog(this);
        dialog.setTitle("Upload Data");
        dialog.setMessage("uploading....");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // ============= TODO 디테일 정보 넘겨받기 ==============
//        Intent intent = getIntent();
//        bbs = (Bbs) intent.getSerializableExtra(BBS_KEY);
//
//        if (bbs != null) {
//            editTitle.setText(bbs.title);
//            editAuthor.setText(bbs.author);
//            editContent.setText(bbs.content);
//            date.setText(bbs.date);
//        }
    }

    public void DeleteData(View view) {
        try {
            bbsRef.child(bbs.bbsKey).removeValue();
            finish();
        } catch (Exception e) {
            Log.e("Detail", "DeleteDate(); error ==== " + e.getMessage());
        }

    }

    public void postData(View view) {
        Log.e("Deatil", "============= postData():");
        dialog.show();
        // 경로 자체를 db에 저장해 나중에 경로를 통해 꺼내 쓸 수 있게 한다..
        String imagePath = textImg.getText().toString();
        // 이미지가 있으면 이미지 경로를 받아 저장해야하기 떄문에
        //
        if (imagePath != null && !"".equals(imagePath)) {
            uploadFile(imagePath);
        } else {
            afterUploadFile(null);
        }
    }

    public void uploadFile(String filePath) {
        // 스마트폰에 있는 파일 경로
        File file = new File(filePath);
        Uri uri = Uri.fromFile(file);

        // 파이어베이스에 있는 파일 경로
        String fileName = file.getName(); // + 시간값 or UUID 추가해서 만듬
        //                                      "dir/fileName.jpg"
        // 데이터베이스의 key = value
        StorageReference fileRef = mStorageRef.child(fileName);
        fileRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    //                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // 파이어베이스 스토리지에 방금 업로드한 파일의 경로
                        @SuppressWarnings("VisibleForTests")
                        Uri uploadedUri = taskSnapshot.getDownloadUrl();
                        afterUploadFile(uploadedUri);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Log.e("FBStorage", "Upload File : " + exception.getMessage());
                    }
                });
    }

    public void afterUploadFile(Uri imageUri) {

        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        String author = editAuthor.getText().toString();

        Bbs bbs = new Bbs(title, content, author);

        if (imageUri != null) {
            bbs.fileUriString = imageUri.toString();
        }

        String bbsKey = bbsRef.push().getKey();
//        bbsKey = bbsRef.push().getKey();
        bbsRef.child(bbsKey).setValue(bbs);
        dialog.dismiss();
        finish();
    }

    // 1. btnGallery onClick 시, 자동 링크
    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // 가. 이미지 선택창 호출
        startActivityForResult(Intent.createChooser(intent, "앱을 선택하세요"), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 나. 이미지 선택창에서 선택된 이미지의 경로 추출
                case 100:
                    Uri imageUri = data.getData();
                    String filePath = getPathFromUri(this, imageUri);
                    Log.d("DEtail", "filePath ======" + filePath);
                    textImg.setText(filePath);
                    break;
            }
        }
    }

    // Uri에서 실제 경로 꺼내는 함수
    private String getPathFromUri(Context context, Uri uri) {

        String realPath = "";
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToNext()) {
            realPath = cursor.getString(cursor.getColumnIndex("_data"));
        }
        cursor.close();
        return realPath;
    }
}
