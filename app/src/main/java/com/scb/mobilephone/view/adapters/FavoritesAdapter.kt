package com.scb.mobilephone.view.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scb.mobilephone.R
import com.scb.mobilephone.extensions.RemoveFavorite
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.presenters.interfaces.FavoriteInterface
import kotlinx.android.synthetic.main.favorite_list.view.*
import java.sql.Array
import java.util.*
import kotlin.collections.ArrayList

class FavoritesAdapter(val context: Context, private val listener: FavoriteListener) :
    RecyclerView.Adapter<FavoritesAdapter.CustomHolder>(),
    CustomItemTouchHelperListener {

    private lateinit var favoritePresenter: FavoriteInterface.FavoritePresenter
    var mData: ArrayList<FavoritesEntity> = arrayListOf()

    fun setData(list: ArrayList<FavoritesEntity>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(mData, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onItemDismiss(position: Int) {
        listener.removeFromFavorite(mData, position)
        mData.removeAt(position)
        listener.getFavoritesList()
        notifyItemRemoved(position)
        showToast(RemoveFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomHolder {
        return CustomHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.favorite_list,
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
        holder.phoneName.text = item.name
        holder.phonePrice.text = item.price.toString()
        holder.phoneRating.text = "Rating : " + item.rating

        Glide.with(context).load(item.thumbImageURL).into(holder.phoneImage)

        holder.cardView.setOnClickListener { listener.gotoDetailPage(item) }
    }

    inner class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.cardViewFav
        val phoneImage: ImageView = view.phoneImageFav
        val phoneName: TextView = view.phoneNameFav
        val phonePrice: TextView = view.phonePriceFav
        val phoneRating: TextView = view.phoneRatingFav
    }

    interface FavoriteListener {

        fun gotoDetailPage(item: FavoritesEntity)

        fun removeFromFavorite(item: ArrayList<FavoritesEntity>, position: Int)

        fun getFavoritesList()
    }
}

class CustomItemTouchHelperCallback(private var listener: CustomItemTouchHelperListener) :
    ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {

        val dragFlags = 0
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        viewHolder.let {
            listener.onItemDismiss(viewHolder.adapterPosition)
        }
    }
}

interface CustomItemTouchHelperListener {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun onItemDismiss(position: Int)
}