package com.xattacker.android.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.xattacker.android.rx.MenuItemPack
import com.xattacker.android.rx.RxContextMenu

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rxContextMenu = RxContextMenu(this)
        rxContextMenu.request(
            MenuItemPack(0, 1, "aaaa"),
            MenuItemPack(0, 3, "nnnnnn"),
            MenuItemPack(0, 2, "cccccc"))
            .subscribe { item ->
                android.util.Log.i("aaa", "selected item: " + item.title)
            }

        rxContextMenu.registerForContextMenu(findViewById<View>(R.id.button_1))
    }

    fun onButtonClick(view: View)
    {
        // you could make menu show on click event
        view.showContextMenu()
    }
}
