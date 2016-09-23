package diy.net.menzap.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import diy.net.menzap.R;
import diy.net.menzap.helper.UserDBHelper;

public class LoginActivity extends AppCompatActivity {

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

        UserDBHelper userDbHelper = new UserDBHelper(this);
        userDbHelper.insert(emailId, 0);

        Intent intent = new Intent(this, TabsActivity.class);
        startActivity(intent);
    }

}
