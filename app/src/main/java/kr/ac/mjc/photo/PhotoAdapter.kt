package kr.ac.mjc.photo


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// PhotoAdapter에 빨간줄이 뜨면 알트+엔터로 포함해야하는 오버라이드를 추가
class PhotoAdapter(var context: Context, var photoList:ArrayList<Photo>) :RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    var onClickListener:OnClickListener?=null

    // 그려야할 아이템이 어떤 레이아웃으로 구성해야하는가
    // RecyclerView는 Adapter에게 ViewHolder 객체를 받는다
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.item_photo,parent,false)
        return ViewHolder(itemView)
    }

    // 내가 그려야할 아이템의 갯수
    // 메서드는 전체 리스트 항목의 갯수를 반환
    override fun getItemCount(): Int {
        return photoList.size
    }

    // 한개 아이템의 어떤 아이템으로 구성할지
    // 호출하면서 전에 Adapter에게 받았던 ViewHolder 객체와 리스트에서 해당 ViewHolder의 위치를 인자로 전달
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var photo = photoList[position]
        holder.bind(photo)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imageIv:ImageView=itemView.findViewById(R.id.image_iv)

        fun bind(photo:Photo){
            // 파이어베이스에 있는 downloadUrl 지정
            Glide.with(imageIv).load(photo.downloadUrl).into(imageIv)
            imageIv.setOnClickListener {
                if(onClickListener!=null){
                    onClickListener?.onClick(photo)
                }
            }

        }
    }

    //
    interface OnClickListener{
        fun onClick(photo: Photo)
    }
}
