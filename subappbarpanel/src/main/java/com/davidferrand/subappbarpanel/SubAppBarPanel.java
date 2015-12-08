/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 David Ferrand
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.davidferrand.subappbarpanel;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

@CoordinatorLayout.DefaultBehavior(SubAppBarPanel.Behavior.class)
public class SubAppBarPanel extends FrameLayout {
    private final static boolean DEFAULT_EXPANDED = false;
    private final static int DEFAULT_OFFSET = 20;
    private final static float DEFAULT_SLIDING_QUANTITY = 1f;
    private final int offset; // TODO provide getters and setters for offset
    private final float slidingQuantity; // TODO provide getters and setters for slidingQuantity
    private boolean expandedOrExpanding;
    /**
     * Corresponds to the translationY value for the "collapsed" appearance.
     * This is set in accordance to the position of the {@link AppBarLayout}.
     */
    private float baseTranslationY;

    public SubAppBarPanel(Context context) {
        this(context, null);
    }

    public SubAppBarPanel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubAppBarPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SubAppBarPanel, defStyleAttr, 0);

        try {
            expandedOrExpanding = a.getBoolean(R.styleable.SubAppBarPanel_panel_expanded, DEFAULT_EXPANDED);
            offset = a.getDimensionPixelOffset(R.styleable.SubAppBarPanel_panel_offset, DEFAULT_OFFSET);
            slidingQuantity = a.getFraction(R.styleable.SubAppBarPanel_panel_slidingQuantity, 1, 1, DEFAULT_SLIDING_QUANTITY);
        } finally {
            a.recycle();
        }
    }

    private int getOffset() {
        return offset;
    }

    public void setExpanded(final boolean shouldExpand, boolean animate) {
        float translationOffset;
        if (shouldExpand) {
            translationOffset = slidingQuantity * getHeight();
        } else {
            translationOffset = 0;
        }

        if (animate) {
            animate().translationY(baseTranslationY + translationOffset);
        } else {
            setTranslationY(baseTranslationY + translationOffset);
        }

        expandedOrExpanding = shouldExpand;
    }

    public boolean isExpanded() {
        return expandedOrExpanding;
    }

    public void setExpanded(boolean expanded) {
        setExpanded(expanded, true);
    }

    public void toggle() {
        toggle(true);
    }

    public void toggle(boolean animate) {
        setExpanded(!expandedOrExpanding, animate);
    }

    private void setBaseTranslationY(float baseTranslationY) {
        this.baseTranslationY = baseTranslationY;
    }

    // TODO cross-version
//    public SubAppBarPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    /**
     * Behavior implemented by the {@link SubAppBarPanel}: it will stay below the
     * {@link AppBarLayout} and expand below.
     */
    public static class Behavior extends CoordinatorLayout.Behavior<SubAppBarPanel> {
        public Behavior() {
        }

        public Behavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, SubAppBarPanel child, View dependency) {
            return dependency instanceof AppBarLayout;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, SubAppBarPanel child, View dependency) {
            // Set the base for all successive calculations
            child.setBaseTranslationY(
                    dependency.getY() // In normal conditions: 0.0
                            + dependency.getHeight() // In normal conditions: ?actionBarSize
                            + child.getOffset()
                            - child.getHeight());

            // Init the position of the panel
            child.setExpanded(child.isExpanded(), false);

            return true;
        }
    }

    /**
     * The view that will be pushed down by the panel should implement this behavior.
     */
    public static class ScrollingViewBehavior extends CoordinatorLayout.Behavior<View> {
        public ScrollingViewBehavior() {
        }

        public ScrollingViewBehavior(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
            return dependency instanceof SubAppBarPanel;
        }

        @Override
        public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
            // TODO performance could be improved

            float newChildTranslationY = dependency.getTranslationY() + dependency.getHeight();
            child.setTranslationY(newChildTranslationY);

            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            params.height = (int) (parent.getHeight() - newChildTranslationY);
            child.setLayoutParams(params);

            return true;
        }
    }
}
