package com.app.myarticleapp.ui


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.app.myarticleapp.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class NavigationTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigationTest() {
        val recyclerView = onView(
            allOf(
                withId(R.id.recentRecyclerview),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        val recyclerView2 = onView(
            allOf(
                withId(R.id.recentRecyclerview),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        recyclerView2.check(matches(isDisplayed()))

        val recyclerView3 = onView(
            allOf(
                withId(R.id.recentRecyclerview),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    1
                )
            )
        )
        recyclerView3.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView = onView(
            allOf(
                withId(R.id.more),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.actionBar),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())

        val recyclerView4 = onView(
            allOf(
                withId(R.id.durationRecyclerView),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    1
                )
            )
        )
        recyclerView4.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val actionMenuItemView2 = onView(
            allOf(
                withId(R.id.search),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.actionBar),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        actionMenuItemView2.perform(click())

        val searchAutoComplete = onView(
            allOf(
                withClassName(`is`("android.widget.SearchView${R.id.search}")),
                childAtPosition(
                    allOf(
                        withClassName(`is`("android.widget.LinearLayout")),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(replaceText(""), closeSoftKeyboard())

        val appCompatImageButton = onView(
            allOf(
                withContentDescription("Collapse"),
                childAtPosition(
                    allOf(
                        withId(R.id.actionBar),
                        childAtPosition(
                            withId(R.id.appBarLayout),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
