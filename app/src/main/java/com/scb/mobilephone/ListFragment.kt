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
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.network.ApiInterface
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.phone_list.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class ListFragment : Fragment() {

    private var mDataArray: ArrayList<PhoneBean> = ArrayList<PhoneBean>()
    private var mDataArraySorted: ArrayList<PhoneBean> = ArrayList<PhoneBean>()
    private lateinit var mAdapter: ListFragment.CustomAdapter

    private var favoriteItem: ArrayList<PhoneBean> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val _view = inflater.inflate(R.layout.fragment_list, container, false)

        mAdapter = CustomAdapter(context!!)
        _view.recyclerView.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(activity)
        }

        feedData("data")

        _view.swipeRefresh.setOnRefreshListener {
            feedData("data")
        }

        return _view
    }

    fun getFavItem() {
        LocalBroadcastManager.getInstance(context!!).registerReceiver(
            object : BroadcastReceiver(){
                override fun onReceive(context: Context, intent: Intent) {
                    favoriteItem.clear()
                    favoriteItem.addAll(intent.getParcelableArrayListExtra("RECEIVED_REMOVE_MESSAGE"))
                    mAdapter.notifyDataSetChanged()
                }
            },
            IntentFilter("RECEIVED_REMOVE_FAV")
        )
    }

    fun feedData(sort: String) {

        val _call = ApiInterface.getClient().getPhones()
        // check url api
        Log.d("scb_network", _call.request().url().toString())

        _call.enqueue(object : Callback<List<PhoneBean>> {
            override fun onFailure(call: Call<List<PhoneBean>>, t: Throwable) {
                Log.d("scb_network", t.message.toString())
                // close refreshing
                view?.swipeRefresh?.isRefreshing = false
            }

            override fun onResponse(call: Call<List<PhoneBean>>, response: Response<List<PhoneBean>>) {
                Log.d("scb_network", response.body().toString())
                if (response.isSuccessful) {
                    mDataArray.clear()
                    mDataArray.addAll((response.body()!!))
                    when (sort) {
                        "Price low to high" -> {
                            mDataArraySorted.clear()
                            mDataArraySorted.addAll(mDataArray.sortedBy { it.price })
                            Log.d("scb_network", mDataArraySorted.toString())
                        }
                        "Price high to low" -> {
                            mDataArraySorted.clear()
                            mDataArraySorted.addAll(mDataArray.sortedByDescending { it.price })
                            Log.d("scb_network", mDataArraySorted.toString())
                        }
                        "Rating 5-1" -> {
                            mDataArraySorted.clear()
                            mDataArraySorted.addAll(mDataArray.sortedByDescending{ it.rating })
                            Log.d("scb_network", mDataArraySorted.toString())
                        }
                        else -> {
                            mDataArraySorted.clear()
                            mDataArraySorted.addAll(mDataArray)
                        }
                    }
                    mAdapter.notifyDataSetChanged()

                    Handler().postDelayed({
                        view?.swipeRefresh?.isRefreshing = false
                    }, 3000)

                }
            }

        })

    }

    inner class CustomAdapter(val context: Context) : RecyclerView.Adapter<CustomHolder>() {
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
            return mDataArray.count()
        }

        override fun onBindViewHolder(holder: CustomHolder, position: Int) {
            val item = mDataArraySorted[position]
            holder.phoneName.text = item.name
            holder.phoneDetail.text = item.description
            holder.phonePrice.text = "Price : $" + item.price
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

            holder.favBtn.text = null
            holder.favBtn.textOn = null
            holder.favBtn.textOff = null

            getFavItem()
            holder.favBtn.isChecked = item in favoriteItem

            holder.favBtn.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    favoriteItem.add(item)
                    Log.d("favItem", favoriteItem.toString())
                    sendBroadcastMessage(favoriteItem)
                } else {
                    favoriteItem.remove(item)
                    Log.d("favItem", favoriteItem.toString())
                    sendBroadcastMessage(favoriteItem)
                }
            }
        }
    }

    private fun sendBroadcastMessage(content: ArrayList<PhoneBean>) {
        // set key
        Intent("RECEIVED_NEW_MESSAGE").let {
            // set data key
            it.putExtra("RECEIVED_MESSAGE", content)
            LocalBroadcastManager.getInstance(context!!).sendBroadcast(it)
        }
    }

    inner class CustomHolder(view: View): RecyclerView.ViewHolder(view) {
        val cardview: CardView = view.cardView
        val phoneImage: ImageView = view.phoneImage
        val phoneName: TextView = view.phoneName
        val phoneDetail: TextView = view.phoneDetail
        val phonePrice: TextView = view.phonePrice
        val phoneRating: TextView = view.phoneRating
        val favBtn: ToggleButton = view.favoriteBtn
    }


}
