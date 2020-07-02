package com.hqq.ihalbum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hqq.album.activity.PreviewUrlActivity;
import com.hqq.album.annotation.LocalMediaType;
import com.hqq.album.common.Album;
import com.hqq.album.common.OnSelectResultCallback;
import com.hqq.album.dialog.PhotoDialog;
import com.hqq.album.entity.LocalMedia;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @Author : huangqiqiang
 * @Package : com.hqq.ihalbum
 * @FileName :   MainActivity
 * @Date : 2019/9/16 0016  下午 8:33
 * @Email :  qiqiang213@gmail.com
 * @Descrive :
 */
public class MainActivity extends AppCompatActivity {

    TextView mTvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvInfo = findViewById(R.id.tv_info);

        findViewById(R.id.button).setOnClickListener(this::previewUrl);
        findViewById(R.id.button2).setOnClickListener(this::openPhotoSelectDialog);
        findViewById(R.id.button3).setOnClickListener(this::openCamera);
        findViewById(R.id.button4).setOnClickListener(this::openAlbum);
        findViewById(R.id.button7).setOnClickListener(this::openCameraAndAlbum);
        findViewById(R.id.button8).setOnClickListener(this::openVideoAlbum);
        findViewById(R.id.button5).setOnClickListener(this::httpsTest);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<LocalMedia> list = data.getParcelableArrayListExtra("data");
            Log.e("---------------------", "onActivityResult: ");

            StringBuilder stringBuilder = new StringBuilder();

            if (list != null) {
                for (LocalMedia localMedia : list) {
                    stringBuilder.append(localMedia.getPath() + "\n");
                }
            }
            mTvInfo.setText(stringBuilder.toString());
        }

    }

    /**
     * 测试http预览
     *
     * @param view
     */
    private void httpsTest(View view) {
        handleSSLHandshake();
        String gif = "https://images.shangwenwan.com/mall/6d392bfd-6273-4992-a24d-74f4b39b19d3?imageMogr2/size-limit/54.7k!/crop/!485x485a6a8";
        PreviewUrlActivity.goPreviewUrlActivity(MainActivity.this, gif);
    }

    /**
     * 打开相册 带摄像头
     *
     * @param view
     */
    private void openCameraAndAlbum(View view) {
        Album.from(MainActivity.this)
                .choose(LocalMediaType.VALUE_TYPE_IMAGE)
                .setDisplayCamera(true)
                .forResult(0x1)
        ;
    }

    /**
     * 打开相册
     *
     * @param view
     */
    private void openAlbum(View view) {
        Album.from(MainActivity.this)
                .choose(LocalMediaType.VALUE_TYPE_IMAGE)
                .forResult(0x1)
        ;
    }

    /**
     * 打开摄像头
     *
     * @param view
     */
    private void openCamera(View view) {
        Album.from(MainActivity.this)
                .choose(LocalMediaType.VALUE_TYPE_IMAGE)
                .setStartUpCamera(true)
                .forResult(0x1)
        ;
    }

    /**
     * 通用的选择Dialog
     *
     * @param view
     */
    private void openPhotoSelectDialog(View view) {
        PhotoDialog.getPhotoSelectDialog(1, new OnSelectResultCallback() {
            @Override
            public void onSelectSuccess(List<LocalMedia> resultList) {
                if (resultList.size() > 0) {
                    Toast.makeText(MainActivity.this, resultList.get(0).getPath(), Toast.LENGTH_SHORT).show();
                }
            }
        }).show(getSupportFragmentManager());

    }

    /**
     * 视频 入口
     *
     * @param view
     */
    public void openVideoAlbum(View view) {
        Album.from(MainActivity.this)
                .choose(LocalMediaType.VALUE_TYPE_VIDEO)
                .forResult(0x1);
    }

    /**
     * 预览图片
     *
     * @param view
     */
    private void previewUrl(View view) {
        List<String> list = new ArrayList<>();
        list.add("http://img.pptjia.com/image/20180117/f4b76385a3ccdbac48893cc6418806d5.jpg");
        list.add("http://img.pptjia.com/image/20180117/f4b76385a3ccdbac48893cc6418806d5.jpg");
        list.add("http://img.pptjia.com/image/20180117/f4b76385a3ccdbac48893cc6418806d5.jpg");
        list.add("http://img.pptjia.com/image/20180117/f4b76385a3ccdbac48893cc6418806d5.jpg");
        PreviewUrlActivity.goPreviewUrlActivity(MainActivity.this, list, 0);
    }

    /**
     * https 图片加载
     */
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

}
