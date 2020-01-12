package com.rush.webview.weiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rush.webview.R;

/**
 * Created by 86151 on 2020/1/3.
 */

public class BigTabView extends View {
    public BigTabView(Context context) {
        super(context);
    }

    public BigTabView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BigTabView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.bigTag);

        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
