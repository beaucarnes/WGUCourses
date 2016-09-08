package net.beauvine.wgucourses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final int SETTINGS_INFO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intentPreferences = new Intent(getApplicationContext(),
                    SettingsActivity.class);


            startActivityForResult(intentPreferences, SETTINGS_INFO);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goTerms(View view) {
        Intent getTermScreenIntent = new Intent(this, TermsActivity.class);

        startActivity(getTermScreenIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SETTINGS_INFO){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String colorStr = sharedPreferences.getString("pref_color", "#3F51B5");

            ActionBar actionBar = getSupportActionBar();
            actionBar.setHomeButtonEnabled(true);
            ColorDrawable colorDrawable = new ColorDrawable(
                    Color.parseColor(colorStr));
            actionBar.setBackgroundDrawable(colorDrawable);

        }
    }
}
