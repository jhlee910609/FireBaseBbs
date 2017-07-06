# 1. FireBase의 기본 개념

### 1.1 Firebase란

![](https://1.bp.blogspot.com/-YIfQT6q8ZM4/Vzyq5z1B8HI/AAAAAAAAAAc/UmWSSMLKtKgtH7CACElUp12zXkrPK5UoACLcB/s1600/image00.png)



> web server와 DB 역할을 동시에 하며 손쉽게 구축할 수 있다. 손쉬운 구축 과정으로 인해 프로토타입 혹은 간편한 app 등에 많이 이용되고 있다. 



### 1.2. Firebase의 데이터 구조

##### 1.2.1. Firebase 데이터베이스의 데이터 기본 구조

- 모든 Firebase 실시간 데이터베이스 데이터는 JSON 개체로 저장된다. 실제 Firebase의 `realtime DB`에 저장되는 모습을 보면 명확히 알 수 있다.  **SQL 데이터베이스와 달리 테이블이나 레코드가 없으며(NoSql), JSON 트리에 추가된 데이터는 연결된 키를 갖는 기존 JSON 구조의 노드가 된다.**
- 사용자 ID 또는 의미 있는 이름을 키로 직접 지정할 수도 있고, [`push()`](https://firebase.google.com/docs/reference/js/firebase.database.Reference?hl=ko#push)를 사용하여 자동 지정 또한 가능하다.

> 파이어베이스 데이터 구조 예시 = JSON의 데이터 구조와 똑같다.

```javascript
{
  message1 : "Hello world",
  message2 : {
    field1 : "hi, there"
    filed2 : {
      column1 :  123,
      column2 : "Good to see you"
    }
  }
}
```

- Firebase는 Data table이 존재하는 SQL DB들과 달리 sorting 해야 하는 column 마다 테이블을 따로 만들어 줘야하는 번거로움이 있다. 따라서 주로 태그 형식(인스타그램 해쉬태그처럼)으로 태그들을 따로 임의로 지정한 노드에 모아 관리한다.


- 이미 DB 설계 시, JSON 데이터 구조를 기준으로 머리 속으로 파이어베이스 데이터를 설계해야 한다.

  >  Therefore, in practice, it's best to keep your data structure as flat as possible.
  >
  >  [출처 : Firebase Develper's Documents]

  - 속도와 유지/보수 차원에서 이점이 있기 때문에 DB를 구조화할 때, Depth를 줄이라는 이야기다. 

  ​

  ##### 1.2.2. Firebase 데이터베이스의 데이터 기본 구조 살펴보기

![](https://i.stack.imgur.com/UnZnf.png)

- 파이어베이스에서 main key는 key와 value 역할을 자신이 알아서 다한다. 위 예시에서는 `restaurantA`는 `key`이자` value`이다.
  - NoSql과 sql의 DB 구조 방식의 큰 차이를 알 수 있는 부분이다. 
- 이미 존재하는 key에 새로운 value들을 저장할 때 기존 key에 해당하는 value를 `update` 해버린다. 이는 DB 상에 중복 key가 있을 수 없고, key는 고유의 값임을 의미한다. (Primary Key)




암호화는 단방향 암호화 (복호화 X)

- CRUD 이외의 작업들은 클라이언 단에서 처리해주는 것이 좋다. 
- 서버의 부하를 줄일 수 있기 때문에~


# 2. Firebase로 게시판 만들기

### 2.1. Firebase란

> web server와 DB 역할을 동시에 하며 손쉽게 구축할 수 있다. 손쉬운 구축 과정으로 인해 프로토타입 혹은 간편한 app 등에 많이 이용되고 있다.

### 2.2. 내 어플리케이션에 Firebase 연동하기

- 안드로이드 스튜디오 상단 메뉴 탭의 `[Tools] - [Firebase]` 를 통해 쉽게 Firebase를 내 app과 연동시킬 수 있다.
- 그 후, 오른편 Assistance의 도움을 받아 Firebase를 사용하면 된다.

![](https://ws4.sinaimg.cn/large/006tKfTcgy1fh9hb6awe0j31kw0zk7qi.jpg)



### 2.3. Bbs 구현하기

##### 2.3.1. `ListActivity`에서 `ReadActivity`로 Activity 전환하기

>  `convertView`에 `onClick();` 을 구현한다.

```java
[CustomAdapter.java]

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<Bbs> list;
    public Bbs bbs;
    LayoutInflater inflater;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.bbs_item, null);
        }

        // ... 생략
		
      	// convertView 자체에 onClick(); 을 구현했다.
      	// convertView를 tab 했을 경우, 해당 convertView의 position 값이 Bundle에 담겨 ReadActivity.class에 			  intent로 함께 전달된다.
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ReadActivity.class);
                intent.putExtra("LIST_POSITION", position);
                v.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    //... 이하 메소드 생략
}
```

>  ListActivity -> ReadActivity로 Activity 이동

```java
[ReadActivity.java]

public class ReadActivity extends AppCompatActivity {

    ImageView img;
    TextView title, author, date, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
		
        // ... findViewById(); 생략

        setData();

    }

    public void setData() {

        // 목록에서 넘어온 position 값을 이용해 상세보기 데이터를 결정한다.
        Intent intent = getIntent();
      	// bundle에서 position 값 꺼낼 때, 없을 경우 default 값으로 -1을 넣는다.
        int position = intent.getIntExtra("LIST_POSITION", -1);
       	// position 값이 있을 경우에 나머지 작업 수행한다.
      	// 아래와 같이 예외처리 하지 않을 경우, nullPointerException이 떨어지게 된다.
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
```

##### 2.3.2. Firebase Storage와 사진 파일 업로드 구현하기

> Firebase Storage는 이미지, 동영상 등 텍스트 파일에 비해 용량이 큰 미디어 파일들을 주로 담아두기 위해 사용된다.

- [Tools] - [Firebase] 메뉴를 통해 가장 먼저 `Firebase Storage`를 추가한다.

```java
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
```

```java
  public void afterUploadFile(Uri imageUri) {

        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        String author = editAuthor.getText().toString();

        Bbs bbs = new Bbs(title, content, author);

        if (imageUri != null) {
            bbs.fileUriString = imageUri.toString();
        }

        String bbsKey = bbsRef.push().getKey();
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
```







