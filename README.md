# SubAppBarPanel

Panel that slides down the app bar, to recreate an effect similar to the quick settings panel in Android Lollipop and above.

This repo contains just the bare essential for now, but I have plans to make it into a proper library module distributed via a Maven repo.

## Usage

Basic setup:

    <android.support.design.widget.CoordinatorLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        
        <android.support.design.widget.AppBarLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
            
            <android.support.v7.widget.Toolbar
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:clickable="true"
                android:foreground="?selectableItemBackground" />
                
        </android.support.design.widget.AppBarLayout>
        
        <com.davidferrand.subappbarpanel.SubAppBarPanel
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:panel_expanded="false"
            app:panel_offset="10dp"
            app:panel_slidingQuantity="85%">
            
            <!-- Content of the sliding panel -->
            
        </com.davidferrand.subappbarpanel.SubAppBarPanel>
    
        <FrameLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="com.davidferrand.subappbarpanel.SubAppBarPanel$ScrollingViewBehavior">
            
            <!-- Main content -->
            
        </FrameLayout>
            
    </android.support.design.widget.CoordinatorLayout>

The important bits:

* The `AppBarLayout`, the `SubAppBarPanel` and main content layout all need to be children of the same `CoordinatorLayout`.
* You must set the behavior `app:layout_behavior="com.davidferrand.subappbarpanel.SubAppBarPanel$ScrollingViewBehavior"` on the main content layout.
* Expand and collapse the panel programmatically by calling `setExpanded(boolean expanded, boolean animate)`.

For a complete example, check the sample.

## Customisation

* There are three layout parameters to play with:
    * panel_expanded
    * panel_offset
    * panel_slidingQuantity

## TODOs

* Make it appear correctly in edit mode in Android Studio
* Instead of a slidingQuantity, provide a dimension for overlap when expanded
* Increase the interaction with the AppBarLayout: dragging actions, etc.

## Installation

Coming soon...

## License

Coming soon...
