package diy.net.menzap.helper;

import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import de.tum.in.cm.android.eddystonelib.EddystoneService;
import de.tum.in.cm.android.eddystonelib.frames.EddystoneUrlFrame;
import diy.net.menzap.activity.PermissionActivity;
import diy.net.menzap.model.Tracking;
import diy.net.menzap.model.message.TrackingMessage;

import static fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage.RNG;

/**
 * Created by swathissunder on 24/09/16.
 */

public class EddystoneHelper {

    public EddystoneHelper(Context context) {
        this.context = context;
    }

    private Context context;
    private EddystoneService eddystoneService;
    private ServiceConnection serviceConnection;

    public void initService() {
        this.eddystoneService = new EddystoneService();
        this.doBindService();
    }

    public void destroy() {
        this.doUnbindService();
    }
    //==========================================================================//
    // Service Handling
    //------------------------------------------------------------------------//
    // The new post activity must bind to the applib service in order to
    // publish the new message.
    //==========================================================================//
    private ServiceConnection getServiceConnection() {
        return new ServiceConnection() {
            @Override
            public void onServiceConnected( ComponentName componentName,
                                            IBinder iBinder ) {
                if ( !( iBinder instanceof EddystoneService.EddystoneServiceBinder ) ) {
                    Log.e( "Logs ::::", "Wrong type of binder in onServiceConnected()" );
                    return;
                }

                EddystoneService.EddystoneServiceBinder binder =
                        ( EddystoneService.EddystoneServiceBinder ) iBinder;
                EddystoneHelper.this.eddystoneService = binder.getService();
            }

            @Override
            public void onServiceDisconnected( ComponentName componentName ) {
                Log.d("on service disconnected", "here");
            }
        };
    }

    private void doBindService() {
        this.serviceConnection = this.getServiceConnection();
        Intent intent = new Intent(this.context, EddystoneService.class);
        this.context.startService(intent);
        this.context.bindService(intent,
                this.serviceConnection, Context.BIND_AUTO_CREATE );
    }

    private void doUnbindService() {
        this.context.unbindService(this.serviceConnection);
    }

    public void addUrlCallback() {

        EddystoneService.UrlCallback callback = new EddystoneService.UrlCallback() {
            @Override
            public void gotUrl(EddystoneUrlFrame urlFrame, ScanResult scanResult) {
                Log.i("GOT URL => FRAME => ", urlFrame.toString());
                Toast.makeText(EddystoneHelper.this.context, urlFrame.toString(), Toast.LENGTH_SHORT)
                        .show();

                //Fetching sender details from preferences
                SharedPreferences pref = EddystoneHelper.this.context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                String sender = pref.getString("emailId", "");
                String userName = pref.getString("userName", "");

                long timestamp = System.currentTimeMillis();
                long uniqueId = RNG.nextLong();

                Tracking tracking = new Tracking(sender, userName, urlFrame.url.toString(), timestamp, uniqueId);

                // Insert the tracking details into the database table
                TrackingDBHelper trackingDBHelper = new TrackingDBHelper(EddystoneHelper.this.context);
                /*long timestamp1 = timestamp- (24*60*60*1000);
                long timestamp2 = timestamp- (2*24*60*60*1000);
                long timestamp3 = timestamp- (3*24*60*60*1000);
                long timestamp4 = timestamp- (4*24*60*60*1000);

                int i;
                for( i= 0; i< 5; i++){
                    String track_sender = sender+i;
                    userName = userName+i;
                    tracking = new Tracking(track_sender, userName,"https://p11.cm.in.tum.de" , timestamp, uniqueId);
                    trackingDBHelper.insert(tracking);
                    tracking = new Tracking(track_sender, userName,"https://p12.cm.in.tum.de" , timestamp-(i*10000), uniqueId);
                    trackingDBHelper.insert(tracking);


                    tracking = new Tracking(track_sender, userName,"https://p11.cm.in.tum.de" , timestamp1, uniqueId);
                    trackingDBHelper.insert(tracking);
                    tracking = new Tracking(track_sender, userName,"https://p12.cm.in.tum.de" , timestamp1 -(i*10000), uniqueId);
                    trackingDBHelper.insert(tracking);
                    tracking = new Tracking(track_sender, userName,"https://p13.cm.in.tum.de" , timestamp1 -(i*20000), uniqueId);
                    trackingDBHelper.insert(tracking);

                    tracking = new Tracking(track_sender, userName,"https://p11.cm.in.tum.de" , timestamp2, uniqueId);
                    trackingDBHelper.insert(tracking);

                    tracking = new Tracking(track_sender, userName,"https://p11.cm.in.tum.de" , timestamp3, uniqueId);
                    trackingDBHelper.insert(tracking);
                    tracking = new Tracking(track_sender, userName,"https://p12.cm.in.tum.de" , timestamp3 -(i*10000), uniqueId);
                    trackingDBHelper.insert(tracking);
                    tracking = new Tracking(track_sender, userName,"https://p13.cm.in.tum.de" , timestamp3 -(i*20000), uniqueId);
                    trackingDBHelper.insert(tracking);

                    tracking = new Tracking(track_sender, userName,"https://p11.cm.in.tum.de" , timestamp4, uniqueId);
                    trackingDBHelper.insert(tracking);
                    tracking = new Tracking(track_sender, userName,"https://p13.cm.in.tum.de" , timestamp4 -(i*20000), uniqueId);
                    trackingDBHelper.insert(tracking);
                }*/



                if (trackingDBHelper.insert(tracking)) {
                    Log.d("added", trackingDBHelper.getAll().toString());

                    TrackingMessage msg = new TrackingMessage(tracking.getSender(), tracking);
                    DataHolder.getInstance().getHelper().saveAndPublish(msg.getScampiMsgObj());
                }
            }
        };

        if (this.eddystoneService != null) {
            this.eddystoneService.addUrlCallback(callback);
        }
    }

    public void startScan() {

        /*If the scan is already in progress, return*/
        if (this.eddystoneService.getState().toString().equals("SCANNING")) {
            return;
        }

        Intent intent = new Intent(this.context, PermissionActivity.class);
        this.context.startActivity(intent);

        // Start the scan
        if (this.eddystoneService != null) {
            try{
                this.eddystoneService.startScan();
            } catch(IOException e) {
                Log.d("CUSTOM INFO:", e.toString());
            }
            Log.d("CUSTOM INFO:", "SCAN started");
        } else {
            Log.d("CUSTOM INFO:", "Couldn't start scan, no Eddystone instance.");
        }
    }

    public void stopScan() {
        /*If no scan is curently in progress, return*/
        if (this.eddystoneService.getState().toString().equals("IDLE")) {
            return;
        }
        // Stop the scan
        if (this.eddystoneService != null) {
            this.eddystoneService.stopScan();
            Log.d("CUSTOM INFO:", "SCAN stopped");
        } else {
            Log.d("CUSTOM INFO:", "Couldn't stop scan, no Eddystone instance.");
        }
    }
}
