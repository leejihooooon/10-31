package kr.ac.mjc.photo

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class PhotoActivity : AppCompatActivity() {

    lateinit var imageIv:ImageView
    lateinit var descriptionTv:TextView

    lateinit var firestore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        imageIv = findViewById(R.id.image_iv)
        descriptionTv = findViewById(R.id.desctiption_tv)

        firestore = FirebaseFirestore.getInstance()

        var id = intent.getStringExtra("id")
        firestore.collection("photo").document(id).get().addOnCompleteListener {
            task ->
                if(task.isSuccessful){
                    var downloadUrl = task.result?.get("downloadUrl")
                    var description = task.result?.get("description")
                    Glide.with(imageIv).load(downloadUrl).into(imageIv)
                    descriptionTv.text = description.toString()
                }
        }
    }
}