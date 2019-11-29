package kr.ac.mjc.photo

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {

    lateinit var imageIv:ImageView
    lateinit var descriptionEt:EditText
    lateinit var uploadBtn: Button

    // 상수선언 >> 스타트엑티비티에서 호출을 받는데 어떤곳에서 받느냐를 확인하기 위해 사용함
    val SELECT_PHOTO=1001

    var fileUrl:Uri?=null

    lateinit var firestore:FirebaseFirestore
    lateinit var storage:FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addphoto)

        imageIv=findViewById(R.id.image_iv)
        descriptionEt=findViewById(R.id.desctiption_at)
        uploadBtn=findViewById(R.id.upload_btn)

        storage= FirebaseStorage.getInstance()
        firestore=FirebaseFirestore.getInstance()

        imageIv.setOnClickListener{ selectImage() }
        uploadBtn.setOnClickListener { upload() }
    }

    fun selectImage(){
        var intent = Intent(ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,SELECT_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 이미지를 가지고오는 상태인지 확인
        if(requestCode==SELECT_PHOTO && resultCode==Activity.RESULT_OK){
            imageIv.setImageURI(data?.data)
            fileUrl=data?.data
        }
    }

    fun upload(){
        if(fileUrl!=null){
            var data = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            var reference=storage.getReference().child("image").child(data)
            reference.putFile(fileUrl!!).addOnSuccessListener{task->
                task.metadata?.reference?.downloadUrl?.addOnSuccessListener {
                    it ->
                    val photo=Photo(descriptionEt.text.toString(),it.toString())
                    firestore.collection("photo").document().set(photo)
                        .addOnSuccessListener {
                            finish()
                        }
                }
            }
        }
    }

}