package diy.net.menzap.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

import diy.net.menzap.R;
import diy.net.menzap.helper.UserDBHelper;
import diy.net.menzap.model.User;

public class LoginActivity extends AppCompatActivity {
    private static final Random RNG = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button registerButton = (Button) findViewById(R.id.btnRegister);
        Log.d("button", registerButton.toString());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }

    public void createUser(){
        EditText text1 = (EditText) findViewById(R.id.txtEmailId);
        String emailId = text1.getText().toString();

        long timestamp = System.currentTimeMillis();
        long uniqueId = RNG.nextLong();
        int isFriend = 0; // since self
        String sender = emailId;

        User user = new User(sender, emailId, isFriend, timestamp, uniqueId);


        UserDBHelper userDbHelper = new UserDBHelper(this);
        userDbHelper.insert(user);

        SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // We need an editor object to make changes
        SharedPreferences.Editor edit = pref.edit();

        // Set/Store data
        edit.putString("emailId", emailId);

        // Commit the changes
        edit.commit();

        Intent intent = new Intent(this, TabsActivity.class);
        startActivity(intent);
    }

}
