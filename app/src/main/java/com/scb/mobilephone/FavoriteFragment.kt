package com.scb.mobilephone


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scb.mobilephone.models.PhoneBean
import kotlinx.android.synthetic.main.favorite_list.view.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class FavoriteFragment : Fragment() {

    private var favoriteItem: ArrayList<PhoneBean> = ArrayList<PhoneBean>()
    private var favoriteItemSorted: ArrayList<PhoneBean> = ArrayList<PhoneBean>()
    lateinit var mAdapter: CustomAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val _view = inflater.inflate(R.layout.fragment_favorite, container, false)

        mAdapter = CustomAdapter(context!!, favoriteItemSorted)
        _view.recyclerView.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(activity)

            val callback = CustomItemTouchHelperCallback(mAdapter)
            val itemTouchHelper = ItemTouchHelper(callback)
            itemTouchHelper.attachToRecyclerView(_view.recyclerView)
        }

        feedFavData("data")

        _view.swipeRefresh.setOnRefreshListener {
            feedFavData("data")
        }

        return _view
    }

    fun feedFavData(sort: String) {

        LocalBroadcastManager.getInstance(context!!).registerReceiver(
            object : BroadcastReceiver(){
                override fun onReceive(context: Context, intent: Intent) {
                    favoriteItem.clear()
                    favoriteItem.addAll(intent.getParcelableArrayListExtra("RECEIVED_MESSAGE"))
                    favoriteItemSorted.clear()
                    favoriteItemSorted.addAll(favoriteItem)
                    mAdapter.notifyDataSetChanged()

                    Handler().postDelayed({
                        view?.swipeRefresh?.isRefreshing = false
                    }, 3000)
                }
            },
            IntentFilter("RECEIVED_NEW_MESSAGE")
        )

        when (sort) {
            "Price low to high" -> {
                favoriteItemSorted.clear()
                favoriteItemSorted.addAll(favoriteItem.sortedBy { it.price })
            }
            "Price high to low" -> {
                favoriteItemSorted.clear()
                favoriteItemSorted.addAll(favoriteItem.sortedByDescending { it.price })
            }
            "Rating 5-1" -> {
                favoriteItemSorted.clear()
                favoriteItemSorted.addAll(favoriteItem.sortedByDescending{ it.rating })
            }
            else -> {
                favoriteItemSorted.clear()
                favoriteItemSorted.addAll(favoriteItem)
            }
        }
        mAdapter.notifyDataSetChanged()

        Handler().postDelayed({
            view?.swipeRefresh?.isRefreshing = false
        }, 3000)

    }

    inner class CustomAdapter(val context: Context, private val androidList: ArrayList<PhoneBean>) : RecyclerView.Adapter<CustomAdapter.CustomHolder>(), CustomItemTouchHelperListener {
        override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
            Collections.swap(androidList, fromPosition, toPosition)
            notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onItemDismiss(position: Int) {
            androidList?.removeAt(position)
            favoriteItem.removeAt(position)
            Log.d("favItem", favoriteItem.toString())
            sendBroadcastMessage(favoriteItem)
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
            return favoriteItemSorted.count()
        }

        override fun onBindViewHolder(holder: CustomHolder, position: Int) {
            val item = favoriteItemSorted[position]
            holder.phoneName.text = item.name
            holder.phonePrice.text = item.price.toString()
            holder.phoneRating.text = "Rating : " + item.rating

            Glide.with(context).load(item.thumbImageURL).into(holder.phoneImage)
        }

        inner class CustomHolder(view: View): RecyclerView.ViewHolder(view) {
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
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            viewHolder?.let {
                listener.onItemDismiss(viewHolder.adapterPosition)
            }
        }



    }

    interface CustomItemTouchHelperListener {
        fun onItemMove(fromPosition: Int, toPosition: Int) : Boolean

        fun onItemDismiss(position: Int)
    }

    private fun sendBroadcastMessage(content: ArrayList<PhoneBean>) {
        // set key
        Intent("RECEIVED_REMOVE_FAV").let {
            // set data key
            it.putExtra("RECEIVED_REMOVE_MESSAGE", content)
            LocalBroadcastManager.getInstance(context!!).sendBroadcast(it)
        }
    }

}
