package com.sevenhourdev.getlyric;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity{

    private int mainColor;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//// Holo light action bar color is #ff7c272b
//            actionBarColor = Color.parseColor("#FF2b3f4c");
//            tintManager.setStatusBarTintColor(actionBarColor);
//            tintManager.setNavigationBarTintEnabled(true);
//            tintManager.setNavigationBarTintColor(actionBarColor);
//        }
        importColors();
    }

    private void importColors(){

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (sharedpreferences.getString("prefColorScheme","1").charAt(0)) {
            case '1'://blue theme - finalized
                mainColor = Color.parseColor("#FF2b3f4c");
                break;
            case '2'://purple - finalized
                mainColor = Color.parseColor("#ff372742");
                break;
            case '3'://green - finalized
                mainColor = Color.parseColor("#FF27393B");
                break;
            case '4'://orange - finalized
                mainColor = Color.parseColor("#ff282F35");

                break;
        }
        getActionBar().setBackgroundDrawable(new ColorDrawable(mainColor));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    /**
     * Volume button changes the song
     size of font
     */
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }




}
