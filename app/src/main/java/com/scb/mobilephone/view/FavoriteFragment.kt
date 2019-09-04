package com.scb.mobilephone.view


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scb.mobilephone.R
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.presenters.FavoriteInterface
import com.scb.mobilephone.presenters.FavoritePresenter
import com.scb.mobilephone.presenters.FavoritePresenter.Companion.favoritesSortList
import com.scb.mobilephone.presenters.ListPresenter.Companion.favoriteItem
import kotlinx.android.synthetic.main.favorite_list.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.util.*

class FavoriteFragment : Fragment(), FavoriteInterface.FavoriteView {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var favoritePresenter: FavoriteInterface.FavoritePresenter
        @SuppressLint("StaticFieldLeak")
        lateinit var mAdapter: CustomAdapter
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        mAdapter = CustomAdapter(context!!, favoritesSortList)

        val _view = inflater.inflate(R.layout.fragment_favorite, container, false)

        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        favoritePresenter = FavoritePresenter(this)
        favoritePresenter.getFavoritesList(context!!)

        mAdapter = CustomAdapter(context!!, favoritesSortList)
        view.recyclerView.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(activity)

            val callback = CustomItemTouchHelperCallback(mAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(view.recyclerView)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefresh.isRefreshing = false
    }

    override fun showFavoritesList(phonesSortedList: ArrayList<PhoneBean>) {
        mAdapter.notifyDataSetChanged()

        swipeRefresh.setOnRefreshListener {
            favoritePresenter.getFavoritesList(context!!)
            mAdapter.notifyDataSetChanged()
        }
    }

    inner class CustomAdapter(val context: Context, private val androidList: ArrayList<PhoneBean>) : RecyclerView.Adapter<CustomAdapter.CustomHolder>(),
        CustomItemTouchHelperListener {
        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            Collections.swap(androidList, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onItemDismiss(position: Int) {
            androidList.removeAt(position)
            favoriteItem.removeAt(position)
            Log.d("favItem", favoriteItem.toString())
            // favoritePresenter.sendFavoriteItems(context, favoriteItem)
            ListFragment.mAdapter.notifyDataSetChanged()
            notifyItemRemoved(position)
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
            return favoritesSortList.count()
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CustomHolder, position: Int) {
            val item = favoritesSortList[position]
            holder.phoneName.text = item.name
            holder.phonePrice.text = item.price.toString()
            holder.phoneRating.text = "Rating : " + item.rating

            Glide.with(context).load(item.thumbImageURL).into(holder.phoneImage)

            holder.cardview.setOnClickListener {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra("image", item.thumbImageURL)
                intent.putExtra("name", item.name)
                intent.putExtra("brand", item.brand)
                intent.putExtra("detail", item.description)
                intent.putExtra("id", item.id)
                intent.putExtra("rating", item.rating)
                intent.putExtra("price", item.price)
                startActivity(intent)
            }
        }

        inner class CustomHolder(view: View): RecyclerView.ViewHolder(view) {
            val cardview: CardView = view.cardViewFav
            val phoneImage: ImageView = view.phoneImageFav
            val phoneName: TextView = view.phoneNameFav
            val phonePrice: TextView = view.phonePriceFav
            val phoneRating: TextView = view.phoneRatingFav
        }
    }

    inner class CustomItemTouchHelperCallback(private var listener: CustomItemTouchHelperListener) : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {

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
        fun onItemMove(fromPosition: Int, toPosition: Int) : Boolean

        fun onItemDismiss(position: Int)
    }

}
