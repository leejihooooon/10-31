package kr.ac.mjc.photo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

// MainActivity 오류일 때 알트+엔터로 onClick를 override하기
class MainActivity : AppCompatActivity(), PhotoAdapter.OnClickListener {

    // 몇 번째 이미지가 클릭 되었는지 확인하기 위함
    override fun onClick(photo: Photo) {
        var intent = Intent(this,PhotoActivity::class.java)
        intent.putExtra("id",photo.id)
        startActivity(intent)
    }

    lateinit var addBtn : Button
    lateinit var photoRv: RecyclerView

    lateinit var photoList:ArrayList<Photo>

    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addBtn = findViewById(R.id.add_btn)
        photoRv = findViewById(R.id.photo_rv)

        // Array 초기화
        photoList = ArrayList<Photo>()

        firestore = FirebaseFirestore.getInstance()

        // Array 생성
        var photoAdapter = PhotoAdapter(this,photoList)
        photoAdapter.onClickListener = this
        photoRv.adapter = photoAdapter
        // 아이템들을 어떤 레이아웃을 가지고 구성할지 지정
        photoRv.layoutManager = GridLayoutManager(this,3)

        addBtn.setOnClickListener{
            var intent= Intent(this,AddPhotoActivity::class.java)
            startActivity(intent)
        }

        // firestore에 있는 collection의 값들을 가지고 와서 dc 변수에 지정해주고 그 리스트를 포토에 넣어줌
        firestore.collection("photo").addSnapshotListener{
            querySnapshot, firebaseFirestoreException ->
            if(querySnapshot!=null){
                for(dc in querySnapshot.documentChanges){
                    var photo:Photo = dc.document.toObject(Photo::class.java)
                    photo.id=dc.document.id
                    photoList.add(photo)
                    photoAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}
