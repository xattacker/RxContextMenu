# RxContextMenu
an Android RX ContextMenu Component

make Android ContextMenu using more easily

### Setup:

minSdkVersion: 15 [ ![Download](https://api.bintray.com/packages/xattacker/maven/RxContextMenu/images/download.svg?version=1.0.2) ](https://bintray.com/xattacker/maven/RxContextMenu/1.0.2/link)

``` 
allprojects {
    repositories {
        ...
        jcenter()
    }
}

dependencies {
    implementation 'com.xattacker.android:RxContextMenu:1.0.2'
}
``` 

### How to use:
``` 
	// register menu and subscribe click event
        val rxContextMenu = RxContextMenu(this)
        rxContextMenu.request(
            MenuItemPack(0, 1, "aaaa"),
            MenuItemPack(0, 3, "nnnnnn"),
            MenuItemPack(0, 2, "cccccc"))
            .subscribe {
		item -> // menu item selected event triggered
                android.util.Log.i("aaa", "selected item: " + item.title)
            }

	val view = findViewById<View>(R.id.button_1)
        rxContextMenu.registerForContextMenu(view)
	
	// show menu
	view.showContextMenu()
``` 
