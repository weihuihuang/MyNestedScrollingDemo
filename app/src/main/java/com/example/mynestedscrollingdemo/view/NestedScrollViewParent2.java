package com.example.mynestedscrollingdemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import com.example.mynestedscrollingdemo.R;

public class NestedScrollViewParent2 extends LinearLayout implements NestedScrollingParent2 {

    private View topView;
    private View scrollView;
    private int topViewHeight;

    private NestedScrollingParentHelper scrollingParentHelper = new NestedScrollingParentHelper(this);


    public NestedScrollViewParent2(Context context) {
        this(context, null);
    }

    public NestedScrollViewParent2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        scrollingParentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        scrollingParentHelper.onStopNestedScroll(target, type);
    }

    //即将消耗的距离
    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if(dy > 0 && getScrollY() < topViewHeight){
            if(dy >topViewHeight){
                dy = topViewHeight;
            }
            consumed[1] = dy;
            scrollBy(0, dy);
        }
    }

    //嵌套滑动
    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //当子控件处理完后，剩下的距离，父控件处理
        //仅处理向下滑动
        if (dyUnconsumed < 0) {
            scrollBy(0, dyUnconsumed);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentMeasureHeight = getMeasuredHeight();
        if(topView != null && scrollView != null){
            ViewGroup.LayoutParams params = scrollView.getLayoutParams();
            params.height = scrollView.getMeasuredHeight() + topView.getMeasuredHeight();
            scrollView.setLayoutParams(params);
            parentMeasureHeight += topView.getMeasuredHeight();
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), parentMeasureHeight);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > topViewHeight) {
            y = topViewHeight;
        }
        super.scrollTo(x, y);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(topView != null){
            topViewHeight = topView.getMeasuredHeight();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topView = findViewById(R.id.top_view);
        scrollView = findViewById(R.id.recycler_view);
    }
}
