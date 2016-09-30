package diy.net.menzap.activity;

/**
 * Created by viveksethia on 22.09.16.
 */

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Random;

import diy.net.menzap.R;
import diy.net.menzap.helper.DataHolder;
import diy.net.menzap.helper.MenuDBHelper;
import diy.net.menzap.model.Menu;
import diy.net.menzap.model.message.MenuMessage;
import diy.net.menzap.service.DateDialog;

public class MenuCreateActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_menu_create);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        EditText txtServedOn=(EditText)findViewById(R.id.txtServedOn);
        txtServedOn.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog=new DateDialog(view);
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                }
            }
        });



        Button createButton = (Button) findViewById(R.id.btnCreate);
        Log.d("button", createButton.toString());

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMenu();
            }
        });
    }

    public void createMenu(){
        EditText text1 = (EditText) findViewById(R.id.txtMenuName);
        String menuName = text1.getText().toString();

        EditText text2 = (EditText) findViewById(R.id.txtMenuDesc);
        String menuDesc = text2.getText().toString();

        SwitchCompat sw = (SwitchCompat) findViewById(R.id.Switch);
        int category = (sw.isChecked() == true) ? 1 : 0;

        EditText text4 = (EditText) findViewById(R.id.txtServedOn);
        String servedOn = text4.getText().toString();


        //TODO Fetch sender from user profile
        SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String emailId = pref.getString("emailId", "");
        String sender = emailId;
        int isLiked = 0;
        int isFavourite = 0;
        int likeCount = 0;

        long timestamp = System.currentTimeMillis();
        long uniqueId = RNG.nextLong();

        Menu menu = new Menu(sender, menuName, menuDesc, category,servedOn, isLiked, isFavourite, likeCount, timestamp, uniqueId);

        saveMenuAndPublish(menu);
    }

    public void saveMenuAndPublish(Menu menu){
        // Update the database
        MenuDBHelper menuDBHelper = new MenuDBHelper(this);
        if (menuDBHelper.insert(menu)) {
            Log.d("added", menuDBHelper.getAll().toString());

            MenuMessage msg = new MenuMessage("MENU", menu.getSender(), menu);
            DataHolder.getInstance().getHelper().saveAndPublish(msg.getScampiMsgObj());
        }
        setResult(1);
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

