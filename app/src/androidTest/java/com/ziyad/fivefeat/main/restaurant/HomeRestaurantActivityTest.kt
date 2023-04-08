package com.ziyad.fivefeat.main.restaurant

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.ziyad.fivefeat.R

@RunWith(AndroidJUnit4::class)
class HomeRestaurantActivityTest{

    @get:Rule
    var activityRule = IntentsTestRule(HomeRestaurantActivity::class.java)

    @Test
    fun verifyNavOrderClickedThenTabsAndViewPagerDisplayed() {
        onView(withId(R.id.navigation_order_restaurant)).perform(click())
        onView(withId(R.id.tabs))
            .check(matches(isDisplayed()))
        onView(withId(R.id.view_pager))
            .check(matches(isDisplayed()))
    }

    @Test
    fun verifyNavMenuClickedThenRvMenuAndAddButtonDisplayed() {
        onView(withId(R.id.navigation_menu_restaurant)).perform(click())
        onView(withId(R.id.btn_add))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_menu))
            .check(matches(isDisplayed()))
    }

    @Test
    fun verifyNavSettingClickedThenQRAndLogoutButtonDisplayed() {
        onView(withId(R.id.navigation_setting_restaurant)).perform(click())
        onView(withId(R.id.btn_logout))
            .check(matches(isDisplayed()))
        onView(withId(R.id.iv_barcode))
            .check(matches(isDisplayed()))
    }
}