package com.wardrobe.fragment

import android.os.Bundle

import android.view.*
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment

/**
 * Base Fragment to inherit from.
 * All common code and abstraction goes here.
 */
abstract class BaseFragment : Fragment() {

    companion object {
        private const val NO_LAYOUT = -1
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return getInflatedView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(menu != NO_LAYOUT)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (this.menu == NO_LAYOUT) return
        inflater?.inflate(this.menu, menu)
    }

    /**
     * Override this method to provide a fragment layout.
     */
    @LayoutRes
    protected open val layout = NO_LAYOUT

    /**
     * Override this value to provide a Menu.
     */
    @MenuRes
    protected open val menu = NO_LAYOUT

    /**
     * Override this value to provide a fragment tag.
     * This will be used in Fragment Transactions.
     */
    abstract val fragmentTag: String

    private fun getInflatedView(inflater: LayoutInflater): View? {
        val activityLayout = layout
        return if (activityLayout == NO_LAYOUT) null
        else inflater.inflate(layout, null, false)
    }
}
