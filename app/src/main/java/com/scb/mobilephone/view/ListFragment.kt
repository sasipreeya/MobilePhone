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
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scb.mobilephone.R
import com.scb.mobilephone.models.PhoneBean
import com.scb.mobilephone.presenters.ListInterface
import com.scb.mobilephone.presenters.ListPresenter
import com.scb.mobilephone.presenters.ListPresenter.Companion.favoriteItem
import com.scb.mobilephone.presenters.ListPresenter.Companion.mDataArray
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.phone_list.view.*


class ListFragment : Fragment(), ListInterface.ListView {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var listPresenter: ListInterface.ListPresenter
        @SuppressLint("StaticFieldLeak")
        lateinit var mAdapter: CustomAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val _view = inflater.inflate(R.layout.fragment_list, container, false)

        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAdapter = CustomAdapter(context!!)
        view.recyclerView.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(activity)
        }

        progressBar.visibility = View.VISIBLE

        listPresenter = ListPresenter(this)
        listPresenter.setupTreadManager()
        listPresenter.setupDatabase(context!!)
        listPresenter.feedPhonesList()

        super.onViewCreated(view, savedInstanceState)
    }

    override fun showLoading() {
        swipeRefresh.isRefreshing = true
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        swipeRefresh.isRefreshing = false
    }

    override fun showPhonesList(phonesSortedList: ArrayList<PhoneBean>) {
        CustomAdapter(context!!).recieveData(phonesSortedList)
        mAdapter.notifyDataSetChanged()

        swipeRefresh.setOnRefreshListener {
            listPresenter.getPhonesList()
        }
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

        fun recieveData(data: ArrayList<PhoneBean>) {
            mDataArray = data
        }

        override fun getItemCount(): Int {
            return mDataArray.count()
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CustomHolder, position: Int) {
            val item = mDataArray[position]
            holder.phoneName.text = item.name
            holder.phoneDetail.text = item.description
            holder.phonePrice.text = "Price : $" + item.price
            holder.phoneRating.text = "Rating : " + item.rating

            Glide.with(context).load(item.thumbImageURL).into(holder.phoneImage)

            holder.cardview.setOnClickListener {
                val intent = Intent(activity, DetailActivity::class.java)
                listPresenter.openDetailPage(
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

            holder.favBtn.text = null
            holder.favBtn.textOn = null
            holder.favBtn.textOff = null

            listPresenter.getFavoriteItems(context)
            holder.favBtn.isChecked = item in favoriteItem

            holder.favBtn.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    favoriteItem.add(item)
                    listPresenter.sendFavoriteItems(context, favoriteItem)
                } else {
                    favoriteItem.remove(item)
                    listPresenter.sendFavoriteItems(context, favoriteItem)
                }
            }
        }
    }

    inner class CustomHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardview: CardView = view.cardView
        val phoneImage: ImageView = view.phoneImage
        val phoneName: TextView = view.phoneName
        val phoneDetail: TextView = view.phoneDetail
        val phonePrice: TextView = view.phonePrice
        val phoneRating: TextView = view.phoneRating
        val favBtn: ToggleButton = view.favoriteBtn
    }
}
