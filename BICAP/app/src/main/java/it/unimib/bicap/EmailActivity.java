package it.unimib.bicap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import it.unimib.bicap.databinding.ActivityEmailBinding;
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

    private ActivityEmailBinding binding;
    private AlertDialog.Builder mAlertDialog;

    private String email;
    EditText email_input;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmailBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        mAlertDialog = new AlertDialog.Builder(this);
        binding.emailSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.emailEdit.getText().toString().length() != 0){
                    email = binding.emailEdit.getText().toString();
                    SaveOnSharePref();
                    ReloadSpashScreen();
                }else{
                    mAlertDialog
                            .setTitle(R.string.dialog_error)
                            .setMessage(R.string.dialog_email)
                            .setPositiveButton(R.string.dialog_roger, null)
                            .show();
                }
            }
        });
    }

    private void SaveOnSharePref() {
        SharedPreferences sharedPref = getSharedPreferences(Constants.EMAIL_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.EMAIL_SHARED_PREF_KEY, email);
        editor.apply();
    }

    private void ReloadSpashScreen(){
        Intent mIntent = new Intent(EmailActivity.this, SplashScreenActivity.class);
        startActivity(mIntent);
        finish();
    }

}
