package diy.net.menzap.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import diy.net.menzap.service.AppLibService;
import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

/**
 * Created by swathissunder on 21/09/16.
 */

public class AppLibHelper {

    public AppLibHelper(Context context) {
        this.context = context;
    }

    private Context context;
    private AppLibService appLibService;
    private ServiceConnection serviceConnection;

    public void initService() {
        this.appLibService = new AppLibService();
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
                if ( !( iBinder instanceof AppLibService.AppLibBinder ) ) {
                    Log.e( "Logs ::::", "Wrong type of binder in onServiceConnected()" );
                    return;
                }

                AppLibService.AppLibBinder binder =
                        ( AppLibService.AppLibBinder ) iBinder;
                AppLibHelper.this.appLibService = binder.getService();
            }

            @Override
            public void onServiceDisconnected( ComponentName componentName ) {
                Log.d("on service disconnected", "here");
            }
        };
    }

    private void doBindService() {
        this.serviceConnection = this.getServiceConnection();
        Intent intent = new Intent(this.context, AppLibService.class);
        this.context.bindService(intent,
                this.serviceConnection, Context.BIND_AUTO_CREATE );
    }

    private void doUnbindService() {
        this.context.unbindService(this.serviceConnection);
    }

    public void saveAndPublish(SCAMPIMessage message) {
        // Send the message
        if (this.appLibService != null) {
            boolean published = this.appLibService.publish(message);
            Log.d("CUSTOM INFO: PUBLISHED", Boolean.toString(published));
        } else {
            Log.d("CUSTOM INFO: REVIEWS", "Couldn't send message, no AppLib instance.");
        }
    }
}
