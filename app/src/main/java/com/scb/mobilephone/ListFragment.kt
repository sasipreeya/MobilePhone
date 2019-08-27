package com.scb.mobilephone


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.network.ApiInterface
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.phone_list.*
import kotlinx.android.synthetic.main.phone_list.view.*
import kotlinx.android.synthetic.main.phone_list.view.favoriteBtn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.core.content.ContextCompat.startActivity
import android.R.id.message
import android.view.*


class ListFragment : Fragment() {

    private var mDataArray: ArrayList<PhoneBean> = ArrayList<PhoneBean>()
    private lateinit var mAdapter: ListFragment.CustomAdapter
    var favClick: Int = 0

    private var favoriteItem: ArrayList<PhoneBean> = ArrayList<PhoneBean>()

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

        feedData()

        _view.swipeRefresh.setOnRefreshListener {
            feedData()
        }

        return _view
    }

    fun feedData() {

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
            val item = mDataArray[position]
            holder.phoneName.text = item.name
            holder.phoneDetail.text = item.description
            holder.phonePrice.text = "Price : $" + item.price
            holder.phoneRating.text = "Rating : " + item.rating

            Glide.with(context).load(item.thumbImageURL).apply(RequestOptions.circleCropTransform())
                    .into(holder.phoneImage)

            holder.phoneImage.setOnClickListener {
                val intent = Intent(activity, DetailActivity::class.java)
                intent.putExtra("image", item.thumbImageURL)
                intent.putExtra("name", item.name)
                intent.putExtra("brand", item.brand)
                intent.putExtra("detail", item.description)
                intent.putExtra("id", item.id)
                startActivity(intent)
            }

            holder.favBtn?.setOnClickListener {
                holder.favBtn.setImageResource(R.drawable.heart)
                favoriteItem.add(mDataArray[position])
                favClick = 1
            }

            if (favClick == 1) {
                holder.favBtn?.setOnClickListener {
                    holder.favBtn.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                    favoriteItem.remove(item)
                    favClick = 0
                }
            }
        }


    }

    inner class CustomHolder(view: View): RecyclerView.ViewHolder(view) {
        val phoneImage: ImageView = view.phoneImage
        val phoneName: TextView = view.phoneName
        val phoneDetail: TextView = view.phoneDetail
        val phonePrice: TextView = view.phonePrice
        val phoneRating: TextView = view.phoneRating
        val favBtn: ImageButton? = view.favoriteBtn
    }


}
