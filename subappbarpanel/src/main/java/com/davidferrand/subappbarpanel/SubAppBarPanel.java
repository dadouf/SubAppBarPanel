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

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Panel that slides down the app bar, to recreate an effect similar to the quick settings panel in
 * Android Lollipop and above.
 */
@CoordinatorLayout.DefaultBehavior(SubAppBarPanel.Behavior.class)
public class SubAppBarPanel extends FrameLayout {
    private final static boolean DEFAULT_EXPANDED = false;
    private final static int DEFAULT_OFFSET_COLLAPSED = 0;
    private final static int DEFAULT_OFFSET_EXPANDED = 0;
    /**
     * Specifies how much the panel is apparent below the app bar in collapsed position.
     * TODO provide getter and setter
     */
    private final int offsetCollapsed;
    /**
     * Specifies how much the panel stays covered by the app bar in expanded position.
     * TODO provide getter and setter
     */
    private final int offsetExpanded;
    private boolean expandedOrExpanding;
    /**
     * Corresponds to the translationY value for the "collapsed" appearance.
     * This is set in accordance to the position of the {@link AppBarLayout}.
     */
    private float targetTranslationYCollapsed;
    /**
     * Corresponds to the translationY value for the "expanded" appearance.
     * This is set in accordance to the position of the {@link AppBarLayout}.
     */
    private float targetTranslationYExpanded;

    private OnPanelMovementListener listener;

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
            offsetCollapsed = a.getDimensionPixelOffset(R.styleable.SubAppBarPanel_panel_offsetCollapsed, DEFAULT_OFFSET_COLLAPSED);
            offsetExpanded = a.getDimensionPixelOffset(R.styleable.SubAppBarPanel_panel_offsetExpanded, DEFAULT_OFFSET_EXPANDED);
        } finally {
            a.recycle();
        }
    }

    private void initPanelOffsets(AppBarLayout appBarLayout) {
        targetTranslationYCollapsed = appBarLayout.getY() // In normal conditions: 0.0
                + appBarLayout.getHeight() // In normal conditions: ?actionBarSize
                - getHeight()
                + offsetCollapsed;

        targetTranslationYExpanded = appBarLayout.getY() // In normal conditions: 0.0
                + appBarLayout.getHeight() // In normal conditions: ?actionBarSize
                - offsetExpanded;

//        Log.v("initPanelOffsets", "appBarLayout.getY() = " + appBarLayout.getY()
//                + "\nappBarLayout.getHeight() = " + appBarLayout.getHeight()
//                + "\ngetHeight() = " + getHeight()
//                + "\noffsetCollapsed = " + offsetCollapsed
//                + "\noffsetExpanded = " + offsetExpanded
//                + "\ntargetTranslationYCollapsed=" + targetTranslationYCollapsed
//                + "\ntargetTranslationYExpanded=" + targetTranslationYExpanded);
    }

    /**
     * Set the state of the panel.
     *
     * @param shouldExpand If true, expand the panel. If false, collapse it.
     * @param animate      If true, there's a smooth transition to the new state.
     *                     If false, the new state is instantly set.
     */
    public void setExpanded(final boolean shouldExpand, boolean animate) {
        float targetTranslation;
        if (shouldExpand) {
            targetTranslation = targetTranslationYExpanded;
        } else {
            targetTranslation = targetTranslationYCollapsed;
        }

        if (animate) {
            animate().translationY(targetTranslation).setListener(new Animator.AnimatorListener() {
                boolean hasBeenCanceled = false;

                @Override
                public void onAnimationStart(Animator animator) {
                    if (listener != null) {
                        listener.onPanelMovementStarted(shouldExpand);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (!hasBeenCanceled && listener != null) {
                        listener.onPanelMovementEnded(shouldExpand);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    hasBeenCanceled = true;
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        } else {
            setTranslationY(targetTranslation);
            post(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.onPanelMovementStarted(shouldExpand);
                        listener.onPanelMovementEnded(shouldExpand);
                    }
                }
            });
        }

        expandedOrExpanding = shouldExpand;
    }

    /**
     * Retrieve the state of the panel.
     *
     * @return True if the panel is expanded or currently expanding.
     */
    public boolean isExpanded() {
        return expandedOrExpanding;
    }

    /**
     * Like {@link #setExpanded(boolean, boolean)} with animate = true.
     *
     * @param shouldExpand If true, expand the panel. If false, collapse it.
     */
    public void setExpanded(boolean shouldExpand) {
        setExpanded(shouldExpand, true);
    }

    /**
     * Like {@link #toggle(boolean)} with animate = true.
     */
    public void toggle() {
        toggle(true);
    }

    /**
     * Toggle the state of the panel.
     *
     * @param animate If true, the panel will be smoothly expanded or collapsed.
     *                If false, the new state is instantly set.
     */
    public void toggle(boolean animate) {
        setExpanded(!expandedOrExpanding, animate);
    }

    /**
     * Define the listener for expanding/collapsing/expanded/collapsed events.
     *
     * @param listener A listener for panel movements.
     */
    public void setOnPanelMovementListener(OnPanelMovementListener listener) {
        this.listener = listener;
    }

    // TODO cross-version
//    public SubAppBarPanel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    /**
     * Listener for expanding/collapsing/expanded/collapsed events.
     */
    public interface OnPanelMovementListener {
        /**
         * Called when the panel has started to expand or collapse.
         * For consistency, this is called even when the panel is not animated: in that case
         * {@link #onPanelMovementEnded(boolean)} will be called right after.
         *
         * @param expanding True if the movement started is an expanding movement,
         *                  false if it's a collapsing movement.
         */
        void onPanelMovementStarted(boolean expanding);

        /**
         * Called when the panel has reached a resting state (expanded or collapsed).
         *
         * @param expanded True if the panel is expanded, false if it's collapsed.
         */
        void onPanelMovementEnded(boolean expanded);
    }

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
            child.initPanelOffsets((AppBarLayout) dependency);

            // Init the position of the panel. TODO not do it here, buggy if app bar moves
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
