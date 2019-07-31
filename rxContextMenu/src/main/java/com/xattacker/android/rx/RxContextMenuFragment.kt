package com.xattacker.android.rx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import io.reactivex.subjects.PublishSubject
import java.util.*

internal class RxContextMenuFragment : androidx.fragment.app.Fragment()
{
    private val menuItems = ArrayList<MenuItemPack>()

    var publishSubject: PublishSubject<MenuItem>? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?)
    {
        super.onCreateContextMenu(menu, v, menuInfo)

        for ((i, item) in menuItems.withIndex())
        {
            menu?.add(item.groupId, item.itemId, i, item.title)
        }
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean
    {
        item?.let {
            publishSubject?.onNext(it)
           // publishSubject?.onComplete()
        }
//
//        val ft = fragmentManager?.beginTransaction()
//        ft?.hide(this)
//        ft?.commitAllowingStateLoss()

        return true//super.onContextItemSelected(item)
    }

    override fun onDestroy()
    {
        super.onDestroy()

        publishSubject?.onComplete()
    }

    internal fun addMenuItem(item: MenuItemPack)
    {
        menuItems.add(item)
        publishSubject = PublishSubject.create()
    }
}