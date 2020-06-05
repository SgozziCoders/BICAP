package it.unimib.bicap;

import androidx.appcompat.app.AppCompatActivity;

import it.unimib.bicap.utils.Constants;
import it.unimib.bicap.utils.FileManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EmailActivity extends AppCompatActivity {

    String email;
    EditText email_input;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        email_input = (EditText) findViewById(R.id.email_edit);
        submit = (Button) findViewById(R.id.email_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = email_input.getText().toString();
                //FileManager.writeToFile(email,getApplicationInfo().dataDir + "/email.txt");
                SharedPreferences sharedPref = getSharedPreferences(Constants.EMAIL_SHARED_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Constants.EMAIL_SHARED_PREF_KEY, email);
                editor.apply();
                Intent mIntent = new Intent(EmailActivity.this, SplashScreenActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
    }
}
