package com.scb.mobilephone.view


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scb.mobilephone.R
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.presenters.FavoritePresenter
import com.scb.mobilephone.presenters.interfaces.FavoriteInterface
import kotlinx.android.synthetic.main.favorite_list.view.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.*
import kotlin.collections.ArrayList

class FavoriteFragment : BaseSortFragment(), FavoriteInterface.FavoriteView {

    lateinit var favoritePresenter: FavoriteInterface.FavoritePresenter
    lateinit var mAdapter: CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoritePresenter = FavoritePresenter(this)
        favoritePresenter.setupTreadManager()
        favoritePresenter.setupDatabase(context!!)

        mAdapter = CustomAdapter(context!!)
        view.recyclerView.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(activity)

            val callback = CustomItemTouchHelperCallback(mAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(view.recyclerView)
        }

        favoritePresenter.getFavoritesList(context!!)
    }

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun showFavoritesList(favoritesSortedList: ArrayList<FavoritesEntity>) {
        mAdapter.setData(favoritesSortedList)

        swipeRefresh.setOnRefreshListener {
            updateFragment()
        }
    }

    override fun updateFragment() {
        favoritePresenter.getFavoritesList(context!!)
        mAdapter.notifyDataSetChanged()
    }

    inner class CustomAdapter(val context: Context) :
        RecyclerView.Adapter<CustomAdapter.CustomHolder>(),
        CustomItemTouchHelperListener {

        private val mData: ArrayList<FavoritesEntity> = arrayListOf()

        fun setData(list: ArrayList<FavoritesEntity>) {
            mData.clear()
            mData.addAll(list)
            mAdapter.notifyDataSetChanged()
        }

        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            Collections.swap(mData, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onItemDismiss(position: Int) {
            favoritePresenter.removeFavoriteItem(mData[position].id)
            favoritePresenter.getFavoritesList(context)
            mData.removeAt(position)
            notifyItemRemoved(position)
            mAdapter.notifyDataSetChanged()
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

            holder.cardView.setOnClickListener {
                val intent = Intent(activity, DetailActivity::class.java)
                favoritePresenter.openDetailPage(
                    intent,
                    item.thumbImageURL,
                    item.name,
                    item.brand,
                    item.description,
                    item.id,
                    item.rating,
                    item.price
                )
                startActivity(intent)
            }
        }

        inner class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {
            val cardView: CardView = view.cardViewFav
            val phoneImage: ImageView = view.phoneImageFav
            val phoneName: TextView = view.phoneNameFav
            val phonePrice: TextView = view.phonePriceFav
            val phoneRating: TextView = view.phoneRatingFav
        }
    }

    inner class CustomItemTouchHelperCallback(private var listener: CustomItemTouchHelperListener) :
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
}
