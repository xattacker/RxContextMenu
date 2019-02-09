package com.xattacker.android.rx

import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.MenuItem
import android.view.View
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function

class RxContextMenu
{
    @FunctionalInterface
    interface Lazy<V>
    {
        fun get(): V
    }

    @VisibleForTesting internal var fragment: Lazy<RxContextMenuFragment>

    companion object
    {
        private val TAG = RxContextMenu::class.java.getSimpleName()
        private val TRIGGER = Any()
    }

    constructor(activity: FragmentActivity)
    {
        fragment = getLazySingleton(activity.supportFragmentManager)
    }

    constructor(fragment: Fragment)
    {
        this.fragment = getLazySingleton(fragment.childFragmentManager)
    }

    fun registerForContextMenu(view: View)
    {
        this.fragment.get().registerForContextMenu(view)
    }

    fun unregisterForContextMenu(view: View)
    {
        this.fragment.get().unregisterForContextMenu(view)
    }

    fun request(vararg items: MenuItemPack): Observable<MenuItem>
    {
        return Observable.just(TRIGGER).compose(ensure(*items))
    }

    fun <T> ensure(vararg items: MenuItemPack): ObservableTransformer<T, MenuItem>
    {
        return ObservableTransformer {
            o ->
            request(o, *items)
                // Transform Observable<MenuItem> to ObservableSource<MenuItem>
                .buffer(1).flatMap(Function<List<MenuItem>, ObservableSource<MenuItem>> {
                     item: List<MenuItem> ->
                     Observable.just(item[0])
                })
        }
    }

    private fun request(trigger: Observable<*>, vararg items: MenuItemPack): Observable<MenuItem>
    {
        if (items.size == 0)
        {
            throw IllegalArgumentException("RxContextMenu.request/requestEach requires at least one input menuItem")
        }

        return requestImplementation(*items)
    }

    private fun requestImplementation(vararg items: MenuItemPack): Observable<MenuItem>
    {
        for (i in items)
        {
            fragment.get().addMenuItem(i)
        }

        return Observable.concat(Observable.fromArray(fragment.get().publishSubject))
    }

    private fun getLazySingleton(fragmentManager: FragmentManager): Lazy<RxContextMenuFragment>
    {
        return object : Lazy<RxContextMenuFragment>
        {
            private var fragment: RxContextMenuFragment? = null

            @Synchronized
            override fun get(): RxContextMenuFragment
            {
                if (fragment == null)
                {
                    fragment = getFragment(fragmentManager)
                }

                return fragment!!
            }
        }
    }

    private fun getFragment(fragmentManager: FragmentManager): RxContextMenuFragment
    {
        var fragment = findFragment(fragmentManager)
        if (fragment == null)
        {
            fragment = RxContextMenuFragment()
            fragmentManager.beginTransaction().add(fragment, TAG).commitNow()
        }

        return fragment
    }

    private fun findFragment(fragmentManager: FragmentManager): RxContextMenuFragment?
    {
        return fragmentManager.findFragmentByTag(TAG) as RxContextMenuFragment?
    }
}