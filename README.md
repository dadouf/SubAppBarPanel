# SubAppBarPanel

**A panel that slides down from below the app bar**, to recreate an effect similar to the quick settings panel in Android Lollipop and above.

![](https://github.com/dadouf/SubAppBarPanel/blob/master/art/demo.gif)

## Usage

Basic setup:

```xml
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
        app:panel_offsetCollapsed="0dp"
        app:panel_offsetCollapsed="0dp">

        <!-- Content of the sliding panel -->

    </com.davidferrand.subappbarpanel.SubAppBarPanel>

    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="com.davidferrand.subappbarpanel.SubAppBarPanel$ScrollingViewBehavior">

        <!-- Main content -->

    </FrameLayout>

</android.support.design.widget.CoordinatorLayout>
```

The important bits:

* The `AppBarLayout`, the `SubAppBarPanel` and main content layout all need to be children of the same `CoordinatorLayout`.
* You must set the behavior `app:layout_behavior="com.davidferrand.subappbarpanel.SubAppBarPanel$ScrollingViewBehavior"` on the main content layout.
* Expand and collapse the panel programmatically by calling `setExpanded(boolean expanded, boolean animate)` or `toggle(boolean animate)`.

For a complete example, check the sample.

## Advanced setup

* There are three layout parameters to play with:
    * `panel_expanded` (default false)
    * `panel_offsetCollapsed` (default 0dp): specifies how much the panel is apparent below the app bar in collapsed position
    * `panel_offsetExpanded` (default 0dp): specifies how much the panel stays covered by the app bar in expanded position

* You can set a listener with `setOnPanelMovementListener()` to get callbacks:
    * `onPanelMovementStarted(boolean expanding)` when the panel has started to expand or collapse
    * `onPanelMovementEnded(boolean expanded)` when the panel has reached a resting state (expanded or collapsed)

## TODOs

* Make it appear correctly in edit mode in Android Studio.
* Increase the interaction with the `AppBarLayout`: dragging actions, etc.
* It should be possible to improve the performance of the `SubAppBarPanel$ScrollingViewBehavior`.

## Installation

This project relies on [JitPack.io](https://jitpack.io/).

To add the library to your project:

1. Add the JitPack repository to your build file:
    ```gradle
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
    ```

2. Add the dependency
    ```gradle
    dependencies {
        compile 'com.github.dadouf:SubAppBarPanel:v0.3.0'
    }
    ```

## License

The MIT License (MIT). Copyright (c) 2015 David Ferrand.
See [LICENSE.txt](https://github.com/dadouf/SubAppBarPanel/blob/master/LICENSE.txt).
