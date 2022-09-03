package com.app.myarticleapp.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.app.myarticleapp.R
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class ScreenTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun screenTest() {
        val imageView = onView(
            allOf(
                withId(R.id.shapeableImageView),
                withParent(withParent(withId(R.id.articleRecyclerview))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val button = onView(
            allOf(
                withId(R.id.search),
                withParent(withParent(withId(R.id.actionBar))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val button2 = onView(
            allOf(
                withId(R.id.more),
                withParent(withParent(withId(R.id.actionBar))),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))

        val button3 = onView(
            allOf(
                withId(R.id.more),
                withParent(withParent(withId(R.id.actionBar))),
                isDisplayed()
            )
        )
        button3.check(matches(isDisplayed()))
    }
}
