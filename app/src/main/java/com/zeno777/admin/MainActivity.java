package com.zeno777.admin;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.view.Gravity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.view.MotionEvent;

public class MainActivity extends Activity {

    private static final String ADMIN_URL =
            "https://zeno777.up.railway.app/admin";

    private WebView webView;

    private float startY;
    private boolean refreshing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showSplash();

        new Handler().postDelayed(() -> openAdmin(), 1800);
    }

    private void showSplash() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.BLACK);

        ImageView logo = new ImageView(this);
        logo.setImageResource(R.drawable.zeno_logo);
        logo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        int size = (int) (220 *
                getResources().getDisplayMetrics().density);

        layout.addView(
                logo,
                new LinearLayout.LayoutParams(size, size)
        );

        setContentView(layout);
    }

    private void openAdmin() {

        webView = new WebView(this);
        webView.setBackgroundColor(Color.WHITE);
        webView.setWebViewClient(new WebViewClient());

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);

        webView.loadUrl(ADMIN_URL);

        webView.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    startY = event.getY();
                    break;

                case MotionEvent.ACTION_UP:

                    float endY = event.getY();
                    float distance = endY - startY;

                    if (distance > 180 &&
                            webView.getScrollY() == 0 &&
                            !refreshing) {

                        refreshing = true;

                        Toast.makeText(
                                MainActivity.this,
                                "Refreshing...",
                                Toast.LENGTH_SHORT
                        ).show();

                        webView.reload();

                        new Handler().postDelayed(
                                () -> refreshing = false,
                                1500
                        );
                    }

                    break;
            }

            return false;
        });

        setContentView(webView);
    }

    @Override
    public void onBackPressed() {

        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
