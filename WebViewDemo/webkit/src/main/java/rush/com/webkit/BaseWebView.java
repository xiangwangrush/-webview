package rush.com.webkit;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 抽取webView的常用操作 封装
 */

public class BaseWebView extends WebView{
    public BaseWebView(Context context) {
        super(context);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
