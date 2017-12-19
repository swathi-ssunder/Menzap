import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import fi.tkk.netlab.dtn.scampi.applib.AppLib;
import fi.tkk.netlab.dtn.scampi.applib.AppLibLifecycleListener;
import fi.tkk.netlab.dtn.scampi.applib.MessageReceivedCallback;
import fi.tkk.netlab.dtn.scampi.applib.PublishDoneCallback;
import fi.tkk.netlab.dtn.scampi.applib.SCAMPIMessage;

public class ImageService  {
  static private final AppLib APP_LIB = AppLib.builder().build();
  public static final String MSG_PIC_FIELD = "PIC";
  public static final String MSG_LOCATION_FIELD = "LOCATION";
  public static final String MSG_TYPE_FIELD = "TYPE";
  public static final String MSG_SENDER_FIELD = "SENDER";
  public static final String MSG_TIMESTAMP_FIELD = "TIMESTAMP";
  public static final String MSG_UNIQUE_ID_FIELD = "UNIQUE_ID";
  public static final long MSG_LIFETIME
          = 60 * 60 * 24 * 30;
  private static final Random RNG = new Random();


  public static void main( String[] args )
          throws InterruptedException, IOException {
    // Setup
    APP_LIB.start();
    APP_LIB.connect();


    File folder = new File("/home/pi/Desktop/Menzap/Images");
    File[] listOfFiles = folder.listFiles();

    for (File file : listOfFiles) {
      if (file.isFile()) {
        File imageFile = new File(file.getAbsolutePath());

        long timestamp = System.currentTimeMillis();
        long uniqueId = RNG.nextLong();
        SCAMPIMessage msg = SCAMPIMessage.builder()
                .lifetime(MSG_LIFETIME, TimeUnit.MINUTES)
                .build();

        // Content items
        msg.putString( MSG_TYPE_FIELD, "IMAGE" );
        msg.putString( MSG_SENDER_FIELD, "RASPBERRYPI");
        msg.putString( MSG_LOCATION_FIELD, "MENSA");
        msg.putBinary( MSG_PIC_FIELD, imageFile );
        msg.putInteger( MSG_TIMESTAMP_FIELD, timestamp );
        msg.putInteger( MSG_UNIQUE_ID_FIELD, uniqueId );

        // Publish a message
        try {
          APP_LIB.publish( msg, "MenzapTags",
                  // Add a callback to set the routed status
                  new PublishDoneCallback() {
                    @Override
                    public void publishDone( AppLib appLib,
                                             SCAMPIMessage scampiMessage ) {
                      System.out.println(scampiMessage);
                    }
                  } );
        } catch ( InterruptedException e ) {
          // Swallow the exception since it can only occur when we interrupt
          // the applib ourselves.
        }
      }
    }
  }

}
