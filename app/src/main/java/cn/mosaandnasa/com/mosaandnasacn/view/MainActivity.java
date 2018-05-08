package cn.mosaandnasa.com.mosaandnasacn.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.net.HttpURLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import cn.mosaandnasa.com.mosaandnasacn.R;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    String URL = "";
    LinearLayout ll;
    LottieAnimationView animationView;

    private String URL_DOMAIN_cn = "http://m.mosaandnasa.com.cn/Admin/Index/?Token="; //大陸
    private String URL_DOMAIN = "http://m.mosaandnasa.com/Admin/Index/?Token="; //台灣
    private boolean isTaiwan = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{
            String url = URLEncoder.encode(getIntent().getStringExtra("URL"), "UTF-8");
            URL = new StringBuffer().append(isTaiwan?URL_DOMAIN : URL_DOMAIN_cn).append(url).toString();
        }catch (Exception e){

        }



        //
        ll = (LinearLayout)this.findViewById(R.id.ll);
        ll.setVisibility(View.VISIBLE);
        animationView = (LottieAnimationView) findViewById(R.id.animView);
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("like_button.json");
        animationView.loop(true);
        animationView.playAnimation();
        //
        setWebView();
    }

    void setWebView() {
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.setDownloadListener(new MyDownloadListener());
        webView.setVisibility(View.INVISIBLE);
        webView.loadUrl(URL);

        //取得WebSettings物件
        WebSettings webSettings = webView.getSettings();
        //啟用JS(重要)
        webSettings.setJavaScriptEnabled(true);
        //啟用Storage(重要)
        webSettings.setDomStorageEnabled(true);
        //將圖片調整到適合螢幕大小
        webSettings.setUseWideViewPort(true);
        //縮放置螢幕大小
        webSettings.setLoadWithOverviewMode(true);
        //支持縮放(預設為true)
        webSettings.setSupportZoom(true);
        //設置內置的縮放控件。若為false，則該WebView不可縮放
        webSettings.setBuiltInZoomControls(true);
        //隱藏原生的縮放控件
        webSettings.setDisplayZoomControls(false);
        //關閉WebView緩存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //是否可以開啟文件
        webSettings.setAllowFileAccess(true);
        //圖片是否自動加載
        webSettings.setLoadsImagesAutomatically(true);
        //設定編碼格式
        webSettings.setDefaultTextEncodingName("utf-8");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
//                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//                dialog.setTitle("提醒");
//                dialog.setMessage("是否登出？");
//                dialog.setIcon(R.mipmap.ic_launcher);
//                dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                        dialogInterface.dismiss();
//
//                    }
//                });
//                dialog.setNegativeButton("返回", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//
//                dialog.show();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            animationView.cancelAnimation();
            animationView.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> valueCallback) {
            Log.d("MyWebChromeClient : ", "onShowFileChooser: ");
        }

        // For Android  >= 3.0
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            Log.d("MyWebChromeClient : ", "onShowFileChooser: ");
        }

        //For Android  >= 4.1
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            Log.d("MyWebChromeClient : ", "onShowFileChooser: ");
        }

        // For Android >= 5.0
        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         WebChromeClient.FileChooserParams fileChooserParams) {
            Log.d("MyWebChromeClient : ", "onShowFileChooser: ");
            return true;
        }
    }

    class MyDownloadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            String[] urlStringArray = url.split("\\.");
            switch (urlStringArray[urlStringArray.length - 1]) {
                case "pdf":
                case "docx":
                case "doc":
                case "ppt":
                case "pptx":
                case "xls":
                case "xlsx":
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    break;
                case "rar":
                case "zip":
                    Toast.makeText(MainActivity.this, "行动装置上无法使用此类型档案", Toast.LENGTH_SHORT).show();
                    break;
                default:

                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }


}
