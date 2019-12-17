package com.cammace.aurora.viewmodel

import android.widget.TextView
import androidx.test.rule.ActivityTestRule
import com.cammace.aurora.R
import com.cammace.aurora.coordinates
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MainActivityViewModelTest {
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule<coordinates>(coordinates::class.java)
    private var mActivity: coordinates? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        mActivity = mActivityTestRule.getActivity()
    }

    @Test
    fun testLaunch() {
        val view = mActivity?.findViewById<TextView>(R.id.textView_mainActivity_temp)
        assertNull(view)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        mActivity = null
    }
}

