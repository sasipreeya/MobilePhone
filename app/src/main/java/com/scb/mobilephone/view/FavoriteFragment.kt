package com.scb.mobilephone.view


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.scb.mobilephone.R
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity
import com.scb.mobilephone.presenters.FavoritePresenter
import com.scb.mobilephone.presenters.SortInterface
import com.scb.mobilephone.presenters.SortList
import com.scb.mobilephone.presenters.interfaces.FavoriteInterface
import com.scb.mobilephone.view.adapters.CustomItemTouchHelperCallback
import com.scb.mobilephone.view.adapters.FavoritesAdapter
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import kotlinx.android.synthetic.main.fragment_list.*

class FavoriteFragment : BaseSortFragment(), FavoriteInterface.FavoriteView,
    SortInterface.SortToView {

    private lateinit var sortPresenter: SortInterface.SortPresenter

    private lateinit var favoritePresenter: FavoriteInterface.FavoritePresenter
    private lateinit var mAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sortPresenter = SortList(this)
        favoritePresenter = FavoritePresenter(this, sortPresenter)
        favoritePresenter.setupTreadManager()
        favoritePresenter.setupDatabase(context!!)

        mAdapter = FavoritesAdapter(context!!, object : FavoritesAdapter.FavoriteListener {
            override fun getFavoritesList() {
                favoritePresenter.getFavoritesList(context!!)
            }

            override fun removeFromFavorite(item: ArrayList<FavoritesEntity>, position: Int) {
                favoritePresenter.removeFavoriteItem(item[position].id)
            }

            override fun gotoDetailPage(item: FavoritesEntity) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("name", item.name)
                intent.putExtra("brand", item.brand)
                intent.putExtra("detail", item.description)
                intent.putExtra("price", item.price)
                intent.putExtra("rating", item.rating)
                startActivity(intent)
            }
        })

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
        mAdapter.mData = favoritesSortedList
        mAdapter.setData(favoritesSortedList)
        mAdapter.notifyDataSetChanged()

        swipeRefresh.setOnRefreshListener {
            updateFragment()
        }
    }

    override fun updateFragment() {
        favoritePresenter.getFavoritesList(context!!)
        mAdapter.notifyDataSetChanged()
    }

    override fun submitPhonesList(phonesList: List<PhonesListEntity>) {

    }

    override fun submitFavoritesList(favoriteList: List<FavoritesEntity>) {
        mAdapter.setData(ArrayList(favoriteList))
        mAdapter.notifyDataSetChanged()
    }

    override fun getSortType(sortType: String) {
        favoritePresenter.getSortType(sortType)
        mAdapter.notifyDataSetChanged()
    }
}
