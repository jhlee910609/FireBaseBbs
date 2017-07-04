package com.junhee.android.firebase_advanced;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.junhee.android.firebase_advanced.util.PermissionControl;

public class MainActivity extends AppCompatActivity implements PermissionControl.CallBack {

    // 파이어베이스 인증처리 미리 생성해둔다...
    // onCreate(); 보다 먼저 호출된다...

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d("Auth", "onAuthStateChanged:signed_out");
            }
        }
    };

    EditText editMail, editPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionControl.checkVersion(this);

        editMail = (EditText) findViewById(R.id.signInMail);
        editPw = (EditText) findViewById(R.id.signInPw);


//        btnGoList = (Button) findViewById(R.id.btnGoList);
//
//        btnGoList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goList(v);
//
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionControl.onResult(this, requestCode, grantResults);
    }

    // 여기에 코드 작성
    @Override
    public void init() {

    }

    public void signUp(View view) {

        String email = editMail.getText().toString();
        String password = editPw.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "사용자 등록을 실패했습니다..ㅜㅜㅜㅜ 또륵",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(View view) {

        String email = editMail.getText().toString();
        String password = editPw.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Auth", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("Auth", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "signin 실패",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(MainActivity.this, ListActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

    public void goMain(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }
}
