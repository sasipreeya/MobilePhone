package com.scb.mobilephone


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.scb.mobilephone.models.PhoneBean
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.phone_list.view.*

class FavoriteFragment : Fragment() {

    private var favoriteItem: ArrayList<PhoneBean> = ArrayList<PhoneBean>()
    lateinit var mAdapter: FavoriteFragment.CustomAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val _view = inflater.inflate(R.layout.fragment_favorite, container, false)

        mAdapter = CustomAdapter(context!!)
        _view.recyclerView.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(activity)
        }

        return _view
    }


    inner class CustomAdapter(val context: Context) : RecyclerView.Adapter<CustomHolder>() {
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
            return favoriteItem.count()
        }

        override fun onBindViewHolder(holder: CustomHolder, position: Int) {
            val item = favoriteItem[position]
            holder.phoneName.text = item.name
            holder.phonePrice.text = "Price : $" + item.price
            holder.phoneRating.text = "Rating : " + item.rating

            Glide.with(context).load(item.thumbImageURL).apply(RequestOptions.circleCropTransform())
                    .into(holder.phoneImage)
        }


    }

    inner class CustomHolder(view: View): RecyclerView.ViewHolder(view) {
        val phoneImage: ImageView = view.phoneImage
        val phoneName: TextView = view.phoneName
        val phonePrice: TextView = view.phonePrice
        val phoneRating: TextView = view.phoneRating
    }

}
