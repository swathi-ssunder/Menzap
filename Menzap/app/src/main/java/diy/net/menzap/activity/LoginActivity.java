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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import diy.net.menzap.R;
import diy.net.menzap.helper.DataHolder;
import diy.net.menzap.helper.UserDBHelper;
import diy.net.menzap.model.User;
import diy.net.menzap.model.message.UserMessage;

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

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void createUser(){
        EditText text1 = (EditText) findViewById(R.id.txtEmailId);
        String emailId = text1.getText().toString();

        EditText text2 = (EditText) findViewById(R.id.txtName);
        String name = text2.getText().toString();

        if (!isValidEmail(emailId)) {
            text1.setError("Invalid Email");
            return;
        }

        long timestamp = System.currentTimeMillis();
        long uniqueId = RNG.nextLong();
        long isFriend = -1; // since self
        String sender = emailId;

        User user = new User(sender, emailId, name, isFriend, timestamp, uniqueId);

        UserDBHelper userDbHelper = new UserDBHelper(this);
        if( userDbHelper.insert(user)) {
            Log.d("added", userDbHelper.getAll().toString());

            user.setIsFriend(0);
            UserMessage registerMessage = new UserMessage("REGISTER", emailId, user);
            DataHolder.getInstance().getHelper().saveAndPublish(registerMessage.getScampiMsgObj());

            SharedPreferences pref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

            // We need an editor object to make changes
            SharedPreferences.Editor editor = pref.edit();

            // Set/Store data
            editor.putString("emailId", emailId);

            // Commit the changes
            editor.commit();

            Intent intent = new Intent(this, TabsActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
