package com.oilutt.tournament_manager.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Túlio on 17/09/2017.
 */

public class WrapContentViewPager extends ViewPager {

    private int mCurrentPagePosition = 0;

    public WrapContentViewPager(Context context) {
        super(context);
        initPageChangeListener();
    }

    public WrapContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPageChangeListener();
    }

    private void initPageChangeListener() {
        addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                requestLayout();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;
            if (wrapHeight) {
                View child = getChildAt(mCurrentPagePosition);
                if (child != null) {
                    child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int h = child.getMeasuredHeight();

                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
