package it.unimib.bicap;

import androidx.appcompat.app.AppCompatActivity;
import it.unimib.bicap.utils.FileManager;

import android.content.Intent;
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
                FileManager.writeToFile(email,getApplicationInfo().dataDir + "/email.txt");
                Intent mIntent = new Intent(EmailActivity.this, TabbedActivity.class);
                startActivity(mIntent);
                finish();
            }
        });
    }
}
