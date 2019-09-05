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
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.presenters.ListPresenter
import com.scb.mobilephone.presenters.interfaces.ListInterface
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.phone_list.view.*


class ListFragment : Fragment(), ListInterface.ListView {

    lateinit var listPresenter: ListInterface.ListPresenter
    lateinit var mAdapter: CustomAdapter

    lateinit var phonesList: ArrayList<PhoneBean>
    lateinit var favoritesList: List<FavoritesEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listPresenter = ListPresenter(this)
        listPresenter.setupTreadManager()
        listPresenter.setupDatabase(context!!)

        mAdapter = CustomAdapter(context!!)
        view.recyclerView.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(activity)
        }

        progressBar.visibility = View.VISIBLE
        listPresenter.feedPhonesList(context!!)

        val task = Runnable {
            phonesList = listPresenter.getPhones()
            favoritesList = listPresenter.getFavorites()
        }
        listPresenter.postTask(task)
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
            val task = Runnable {
                phonesList = listPresenter.getPhones()
                favoritesList = listPresenter.getFavorites()
            }
            listPresenter.postTask(task)
            mAdapter.notifyDataSetChanged()
            hideLoading()
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
            phonesList = data
        }

        override fun getItemCount(): Int {
            return phonesList.count()
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: CustomHolder, position: Int) {
            val item = phonesList[position]
            val favoriteItem = FavoritesEntity(item.brand, item.description, item.id, item.name, item.price, item.rating, item.thumbImageURL)
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

            holder.favBtn.isChecked = favoritesList.contains(favoriteItem)

            holder.favBtn.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    listPresenter.addFavoriteItem(item)
                } else {
                    listPresenter.removeFavoriteItem(item.id)
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
