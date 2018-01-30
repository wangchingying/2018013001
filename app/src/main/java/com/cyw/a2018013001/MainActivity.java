package com.cyw.a2018013001;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 123;
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //順序1, 先宣告LocationManager
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    //順序1.5, 執行LocationListener介面
    //順序2, 設定按鈕時LocationManager開始定位, 已經被寫到startLoc()
    //順序3, 將敏感權限設定寫在if內, 參考綠豆湯
    public void click1(View v) {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // 無權限，向使用者請求, 會跳出權限視窗
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{ACCESS_FINE_LOCATION,
                            ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION
            );
            Log.d("here", "P return");

            return;
        } else {
            //重要:上面的if是下面這行燈泡紅字時選擇第一個add permission check出現的,然後會自己出現一個 MyListener的class的檔案
            Log.d("here", "P else startLoc ");
            startLoc();

        }
    }
    //順序4, 當使用者允許或拒絕權限
    // 使用者在任何權限請求對話中可按下「允許」或「拒絕」按鈕，當按下按鈕時會自動執行onRequestPermissionsResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //取得權限，進行檔案存取
                Log.d("here", "onRequestP");
                startLoc();
            } else {

            }
        }
    }
    //定位的method
    private void startLoc() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new MyListener());
    }
    //順序1.5, 執行LocationListener介面, 當位置改變時,會執行下列複寫方法
    class MyListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.d("LOC", "Change!!"+ location.getLatitude() + "," + location.getLongitude());

            //台北101的位置
            Location loc101 = new Location("LOC");
            loc101.setLatitude(25.0336);
            loc101.setLongitude(121.5646);

            //台北101與目前位置的距離, 超過100公里就不準, 因為地球是圓的,這個算法是直線距離
            float dist = location.distanceTo(loc101);
            Log.d("LOC", "Dist:" + dist);

            //這段把地址抓回來, 有IOException的原因是因為只有android手機可以去抓,不然所有導航機都去抓google也太笨了
            Geocoder geocoder = new Geocoder(MainActivity.this);
            try {
                List<Address> mylist = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                Address addr = mylist.get(0);
                Log.d("LOC", addr.getAddressLine(0));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}



