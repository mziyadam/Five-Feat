package com.ziyad.fivefeat.main.user

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ziyad.fivefeat.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest{

    @get:Rule
    var activityRule = IntentsTestRule(HomeActivity::class.java)

    @Test
    fun verifyNavHomeClickedThenScannerCodeDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.navigation_home))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_scan))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun verifyNavOrderClickedThenTabsAndViewPagerDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.navigation_order))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.tabs))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.view_pager))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun verifyNavSettingClickedThenLogoutButtonDisplayed() {
        Espresso.onView(ViewMatchers.withId(R.id.navigation_setting))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.btn_logout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}