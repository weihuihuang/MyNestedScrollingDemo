package com.example.mynestedscrollingdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mynestedscrollingdemo.R;

public class NestedScrollViewDispatchEvent extends LinearLayout {

    private View headView;
    private int headViewHeight;
    private boolean isHeadViewVisible;
    private View scrollView;

    public NestedScrollViewDispatchEvent(Context context) {
        this(context, null);
    }

    public NestedScrollViewDispatchEvent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    private int mLastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int y = ( int)event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int diffY = mLastY - y;
                if(Math.abs(diffY) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    if (diffY > 0) { //上滑
                        return isHeadViewVisible || getScrollY() == 0;
                    }
                    if (diffY < 0) {
                        if(scrollView != null){
                            if(isHeadViewVisible && getScrollY() != 0 && getScrollY() < headViewHeight){
                                return true;
                            }else {
                                if(scrollView instanceof RecyclerView){
                                    RecyclerView recyclerView = (RecyclerView)scrollView;
                                    if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
                                        LinearLayoutManager manager = (LinearLayoutManager)recyclerView.getLayoutManager();
                                        int position = manager.findFirstCompletelyVisibleItemPosition();
                                        return position == 0;
                                    }
                                }else {
                                    return scrollView.getScrollY() == 0;
                                }
                            }
                        }
                    }
                }
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int y = ( int)event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int diffY = mLastY - y;
                if(Math.abs(diffY) >= ViewConfiguration.get(getContext()).getScaledTouchSlop()){
                    int scrollY = getScrollY();
                    int canScrollOffset;
                    if(diffY < 0){
                        canScrollOffset = - scrollY;
                        if(diffY < canScrollOffset){
                            diffY = canScrollOffset;
                        }
                    }
                    if(diffY > 0){
                         canScrollOffset = headViewHeight - scrollY;
                        if(diffY > canScrollOffset){
                            diffY = canScrollOffset;
                        }
                    }
                    scrollBy(0, diffY);
                }
                mLastY = y;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        isHeadViewVisible = getScrollY() < headViewHeight;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(headView != null){
            headViewHeight = headView.getMeasuredHeight();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for(int i = 0; i < getChildCount(); i++){
            View childView = getChildAt(i);
            if(childView.getLayoutParams() instanceof MyLayoutParams){
                if(((MyLayoutParams)childView.getLayoutParams()).isHeadView){
                    headView = childView;
                }
                if(((MyLayoutParams)childView.getLayoutParams()).isScrollView){
                    scrollView = childView;
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentMeasureHeight = getMeasuredHeight();
        if(headView != null && scrollView != null){
            ViewGroup.LayoutParams params = scrollView.getLayoutParams();
            params.height = parentMeasureHeight + headView.getMeasuredHeight();
            scrollView.setLayoutParams(params);
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), parentMeasureHeight);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MyLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof MyLayoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return new MyLayoutParams(lp);
    }

    public static class MyLayoutParams extends LinearLayout.LayoutParams{

        private boolean isHeadView;
        private boolean isScrollView;

        public MyLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.NestedScrollViewDispatchEvent);
            isHeadView = typedArray.getBoolean(R.styleable.NestedScrollViewDispatchEvent_is_head_view, false);
            isScrollView = typedArray.getBoolean(R.styleable.NestedScrollViewDispatchEvent_is_scroll_view, false);
            typedArray.recycle();
        }

        public MyLayoutParams(int width, int height) {
            super(width, height);
        }

        public MyLayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }
    }
}
