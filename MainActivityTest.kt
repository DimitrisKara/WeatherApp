package com.cammace.aurora.ui


import android.widget.TextView
import androidx.test.rule.ActivityTestRule
import com.cammace.aurora.R
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @Rule @JvmField
    var mActivityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java)
    private var mActivity: MainActivity? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        mActivity = mActivityTestRule.getActivity()
    }
    @Test
    fun testLaunch() {
        val view = mActivity?.findViewById<TextView>(R.id.textView_mainActivity_temp)
        assertNotNull(view)
    }
    @After
    @Throws(Exception::class)
    fun tearDown() {
        mActivity = null
    }
}