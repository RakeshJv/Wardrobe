package com.wardrobe.adapter

import androidx.viewpager.widget.ViewPager


abstract class PageChangedListener : ViewPager.OnPageChangeListener {

    // NO-OP
    override fun onPageScrollStateChanged(state: Int) {}

    // NO-OP
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

}
