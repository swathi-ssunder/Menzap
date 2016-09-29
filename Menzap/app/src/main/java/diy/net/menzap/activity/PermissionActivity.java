package diy.net.menzap.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import diy.net.menzap.R;
import diy.net.menzap.helper.DataHolder;
import diy.net.menzap.helper.TrackingDBHelper;

public class PermissionActivity extends AppCompatActivity {

    private TrackingDBHelper trackingDBHelper;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.trackingDBHelper = new TrackingDBHelper(this);
        SQLiteDatabase db = this.trackingDBHelper.getReadableDatabase();
        this.trackingDBHelper.onCreate(db);

        this.getPermissions();
    }

    private void getPermissions() {

        int hasAccessLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasAccessLocationPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        DataHolder.getInstance().getEddystoneHelper().addUrlCallback();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    DataHolder.getInstance().getEddystoneHelper().addUrlCallback();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "ACCESS_LOCATION Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        finish();
    }
}
