package com.scb.mobilephone

import com.scb.mobilephone.extensions.PriceHL
import com.scb.mobilephone.extensions.PriceLH
import com.scb.mobilephone.extensions.RatingHL
import com.scb.mobilephone.models.database.entities.FavoritesEntity
import com.scb.mobilephone.models.database.entities.PhonesListEntity
import com.scb.mobilephone.presenters.SortInterface
import com.scb.mobilephone.presenters.SortList
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SortPresenterUnitTest {

    private lateinit var sortList: SortList
    private lateinit var phonesList: List<PhonesListEntity>
    private lateinit var sortedPhonesList: List<PhonesListEntity>
    private lateinit var favoritesList: List<FavoritesEntity>
    private lateinit var sortedFavoritesList: List<FavoritesEntity>

    @Mock
    private lateinit var view: SortInterface.SortToView
    private var sortType = listOf(PriceLH, PriceHL, RatingHL)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        sortList = SortList(view)
    }

    @Test
    fun `test sort phones list is true`() {
        for (i in sortType.indices) {
            sortList.sortPhonesList(sortType[i], phonesList)
            sortedPhonesList = sortList.sortedPhonesList
            Mockito.verify(view).submitPhonesList(sortedPhonesList)
        }
    }

    @Test
    fun `test sort favorites list is true`() {
        for (i in sortType.indices) {
            sortList.sortFavoritesList(sortType[i], favoritesList)
            sortedFavoritesList = sortList.sortedFavoritesList
            Mockito.verify(view).submitFavoritesList(sortedFavoritesList)
        }
    }
}
