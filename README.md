# FireBase

> 파이어베이스 데이터 구조 예시

```javascript
{
  message : "Hello world",
  message2 : {
    field1 : "hi, there"
    filed2 : {
      column1 :  123,
      column2 : "Good to see you"
    }
  }
}
```



```javascript
{
// 배열로 처리해서 index를 보고 작업한다. 
  tableName : {
  			  1 : {article 1},
       		  2 : {article 2},
       		  3 : {article 3}
  	}
    tableName_title : {
      	title1 : {article1},
        title2 : {article2},
        title3 : {article3}
    } 
}
```

- sorting 해야 하는 카데고리 마다 테이블을 따로 만들어 줘야하는 번거로움이 있다.
- 따라서 주로 태그 형식(인스타그램 해쉬태그처럼)으로 태그들을 따로 모아 관리한다.
- JSON을 기준으로 머리 속으로 파이어베이스 데이터를 설계해야 한다.
- **Therefore, in practice, it's best to keep your data structure as flat as possible.**
  - 데이터베이스를 구조화할 때, depth를 줄이라는 이야기다. 
  - 속도와 유지/보수 차원에서 이점이 있기 때문.
  - [flat data와 그렇지 않은 데이터를 비교해보시오~ ](https://firebase.google.com/docs/database/android/structure-data)

```javascript
{
  key : value,
  key : {
   		 key(article no) : value,
    	 key(article no) : value
  		}
}
```



- 파이어베이스에서 main key는 key와 value 역할을 자신이 알아서 다한다.
  - Nosql과 sql의 차이라고 생각한다.
- id에 해당되는 것이 key다.
- id 중복 입력 불가 > id에 해당하는 value를 업데이트 해버림….
- id를 pk라고 이해하면 정확한 것은 아니니.. 그냥 key라고 이해해두자 



DataSnapshot 

> 파이어베이스에서 데이터를 넘겨주는 구조를 DataSnapShot 형식으로 준다.

- reference를 뭘로 하냐에 따라 DataSnapshot에 찍히는 것이 다르다.

```
// 요청한 시점의 데이터를 사진(snapshot)처럼 만들어 넘겨줌


```







암호화는 단방향 암호화 (복호화 X)

- CRUD 이외의 작업들은 클라이언 단에서 처리해주는 것이 좋다. 
- 서버의 부하를 줄일 수 있기 때문에~





속성에는 전체값을 넣어놓는다.





> token 특정 기기의 특정 앱의 일종의 id.



