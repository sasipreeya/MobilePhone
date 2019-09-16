package com.scb.mobilephone.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scb.mobilephone.R
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity
import kotlinx.android.synthetic.main.phone_list.view.*

class ListAdapter(val context: Context, private val listener: ListListener) :
    RecyclerView.Adapter<CustomHolder>() {

    private var mData: List<PhonesListEntity> = arrayListOf()

    fun setData(list: List<PhonesListEntity>) {
        mData = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {
        return CustomHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.phone_list,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return mData.count()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomHolder, position: Int) {
        val item = mData[position]
        val favoriteItem = FavoritesEntity(
            item.id,
            item.description,
            item.brand,
            item.name,
            item.price,
            item.rating,
            item.thumbImageURL
        )
        holder.phoneName.text = item.name
        holder.phoneDetail.text = item.description
        holder.phonePrice.text = "Price : $" + item.price
        holder.phoneRating.text = "Rating : " + item.rating

        Glide.with(context).load(item.thumbImageURL).into(holder.phoneImage)

        holder.cardView.setOnClickListener { listener.gotoDetailPage(item) }

        holder.favBtn.text = null
        holder.favBtn.textOn = null
        holder.favBtn.textOff = null

        holder.favBtn.isChecked = listener.checkedHeart(favoriteItem)

        holder.favBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                listener.addToFavorites(item)
            } else {
                listener.removeFromFavorites(item)
            }
        }
    }

    interface ListListener {

        fun gotoDetailPage(item: PhonesListEntity)

        fun checkedHeart(item: FavoritesEntity): Boolean

        fun addToFavorites(item: PhonesListEntity)

        fun removeFromFavorites(item: PhonesListEntity)
    }
}

class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {
    val cardView: CardView = view.cardView
    val phoneImage: ImageView = view.phoneImage
    val phoneName: TextView = view.phoneName
    val phoneDetail: TextView = view.phoneDetail
    val phonePrice: TextView = view.phonePrice
    val phoneRating: TextView = view.phoneRating
    val favBtn: ToggleButton = view.favoriteBtn
}