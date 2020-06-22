package it.unimib.bicap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import it.unimib.bicap.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setVersionText();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setVersionText(){
        TextView mVersionAboutTextView = (TextView) findViewById(R.id.versionAboutTextView);
        try {
            PackageInfo mPackageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String mVersion = mPackageInfo.versionName;
            mVersionAboutTextView.setText(mVersionAboutTextView.getText() + " " + mVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
