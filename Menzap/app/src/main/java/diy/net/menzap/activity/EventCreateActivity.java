package diy.net.menzap.activity;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Random;

import diy.net.menzap.R;
import diy.net.menzap.helper.EventDBHelper;
import diy.net.menzap.model.Event;
import diy.net.menzap.service.DateDialog;
//import diy.net.menzap.service.TimeDialog;

public class EventCreateActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private static final Random RNG = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toolbar toolbar;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        EditText txtFrom=(EditText)findViewById(R.id.txtFrom);
        txtFrom.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });

        EditText txtTo=(EditText)findViewById(R.id.txtTo);
        txtTo.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });

        /*EditText txtTime = (EditText)findViewById(R.id.txtFromTime);
        txtTime.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    TimeDialog dialog= new TimeDialog();
                    dialog.show(getSupportFragmentManager(), "TimeDialog");
                }
            }
        });*/

        Button createButton = (Button) findViewById(R.id.btnCreate);
        Log.d("button", createButton.toString());

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });
    }

    public void createEvent(){
        EditText text1 = (EditText) findViewById(R.id.txtEventName);
        String eventName = text1.getText().toString();

        EditText text2 = (EditText) findViewById(R.id.txtEventDesc);
        String eventDesc = text2.getText().toString();

        EditText text3 = (EditText) findViewById(R.id.txtEventLocation);
        String location = text3.getText().toString();

        EditText text4 = (EditText) findViewById(R.id.txtFrom);
        String fromTime = text4.getText().toString();

        EditText text5 = (EditText) findViewById(R.id.txtTo);
        String toTime = text5.getText().toString();

        Event event = new Event(eventName, eventDesc, location,fromTime, toTime);

        saveEventAndPublish(event);
    }

    public void saveEventAndPublish(Event event){
        // Update the database
        EventDBHelper dbHelper = new EventDBHelper(this);
        dbHelper.insert(event);

        Log.d("added", dbHelper.getAll().toString());
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        //AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

