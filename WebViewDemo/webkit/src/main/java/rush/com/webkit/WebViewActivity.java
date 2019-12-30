package rush.com.webkit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import rush.com.base.PluginInit;
import rush.com.base.activity.BaseActivity;
import rush.com.base.bridge.JsBridgeInterface;
import rush.com.base.cmd.CommandDispatcher;
import rush.com.base.cmd.CommandManager;
import rush.com.base.plugin.ToastPlugin;

/**
 * webview 的页面
 * 在另一进程remoteWeb中
 */

public class WebViewActivity extends BaseActivity {
    WebView webView;
    private LinearLayout llContainer;
    private JsBridgeInterface bridgeInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        llContainer = findViewById(R.id.ll_container);
        initWebView();
        CommandDispatcher.getInstance().initAIDLConnect(getApplicationContext());
        webView.loadUrl("file:///android_asset/test.html");
    }

    private void initWebView() {
        //创建一个LayoutParams宽高设定为全屏
        LinearLayout.LayoutParams layoutParams = new LinearLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        //创建WebView
        webView = new WebView(WebViewActivity.this);
        //设置WebView的宽高
        webView.setLayoutParams(layoutParams);
        //把webView添加到容器中
        llContainer.addView(webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        if (bridgeInterface == null) {
            bridgeInterface = new JsBridgeInterface(this);
            bridgeInterface.setJavaScriptCommand(new JsBridgeInterface.JavaScriptCommand() {
                @Override
                public void exec(Context context, String cmd, String params) {
                    CommandDispatcher.getInstance().exec(cmd, params, webView);
                }
            });
        }
        webView.addJavascriptInterface(bridgeInterface, "NativeBridge");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(
                    WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if ("debug".equals(BuildConfig.BUILD_TYPE)) {
            webView.setWebContentsDebuggingEnabled(true);
        } else {
            webView.setWebContentsDebuggingEnabled(false);
        }
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("https://") || url.startsWith("http://") ||
                        url.startsWith("file://")) {
                    return false;
                } else {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri mUri = Uri.parse(url);
                        intent.setData(mUri);
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return true;
                    }
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view,url,message,result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                                      String defaultValue, JsPromptResult result) {
                if (defaultValue.startsWith("gap")) {
                    result.confirm("");
                } else {
                }
                return true;
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                return true;
            }

            //<3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            }

            //>3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            }

            //>4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType,
                                        String capture) {
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });
    }
}
