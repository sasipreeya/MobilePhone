package com.scb.mobilephone.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scb.mobilephone.R
import com.scb.mobilephone.extensions.AddFavorite
import com.scb.mobilephone.extensions.RemoveFavorite
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity
import com.scb.mobilephone.presenters.ListPresenter
import com.scb.mobilephone.presenters.SortInterface
import com.scb.mobilephone.presenters.SortList
import com.scb.mobilephone.presenters.interfaces.ListInterface
import com.scb.mobilephone.view.adapters.ListAdapter
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*


class ListFragment : BaseSortFragment(), ListInterface.ListView, SortInterface.SortToView {

    private lateinit var sortPresenter: SortInterface.SortPresenter
    private lateinit var phonesList: List<PhonesListEntity>

    lateinit var listPresenter: ListInterface.ListPresenter
    lateinit var mAdapter: ListAdapter
    lateinit var favoritesList: List<FavoritesEntity>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortPresenter = SortList(this)
        listPresenter = ListPresenter(this, sortPresenter)
        listPresenter.setupTreadManager()
        listPresenter.setupDatabase(context!!)

        mAdapter = ListAdapter(context!!, object : ListAdapter.ListListener {
            override fun checkedHeart(item: FavoritesEntity): Boolean {
                return favoritesList.contains(item)
            }

            override fun addToFavorites(item: PhonesListEntity) {
                listPresenter.addFavoriteItem(item)
                showToast(AddFavorite)
            }

            override fun removeFromFavorites(item: PhonesListEntity) {
                listPresenter.removeFavoriteItem(item.id)
                showToast(RemoveFavorite)
            }

            override fun gotoDetailPage(item: PhonesListEntity) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("name", item.name)
                intent.putExtra("brand", item.brand)
                intent.putExtra("detail", item.description)
                intent.putExtra("price", item.price)
                intent.putExtra("rating", item.rating)
                startActivity(intent)
            }
        })

        view.listRecyclerView.let {
            it.adapter = mAdapter
            it.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager?
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

    override fun showPhonesList(phonesSortedList: List<PhonesListEntity>?) {
        phonesList = phonesSortedList!!
        mAdapter.setData(phonesList)
        mAdapter.notifyDataSetChanged()

        swipeRefresh.setOnRefreshListener {
            refresh()
        }
    }

    override fun updateFragment() {
        refresh()
    }

    private fun refresh() {
        val task = Runnable {
            phonesList = listPresenter.getPhones()
            favoritesList = listPresenter.getFavorites()
        }
        listPresenter.postTask(task)
        mAdapter.setData(phonesList)
        mAdapter.notifyDataSetChanged()
        showPhonesList(phonesList)
        hideLoading()
    }

    override fun submitPhonesList(phonesList: List<PhonesListEntity>) {
        this.phonesList = phonesList
        mAdapter.setData(phonesList)
        mAdapter.notifyDataSetChanged()
    }

    override fun submitFavoritesList(favoriteList: List<FavoritesEntity>) {

    }

    override fun getSortType(sortType: String) {
        listPresenter.getSortType(sortType)
        mAdapter.notifyDataSetChanged()
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
