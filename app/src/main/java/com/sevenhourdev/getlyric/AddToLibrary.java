package com.sevenhourdev.getlyric;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddToLibrary extends Activity {

    private int mainColor, secondaryColor, tertiaryColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_library);
        importColors();
        // Only set the tint if the device is running KitKat or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
// Holo light action bar color is #ff7c272b
            tintManager.setStatusBarTintColor(mainColor);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setNavigationBarTintColor(mainColor);
        }

        setTitle("          ");
    }

    private void importColors(){
        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (sharedpreferences.getString("prefColorScheme","1").charAt(0)) {
            case '1'://blue theme - finalized
                mainColor = Color.parseColor("#FF2b3f4c");
                secondaryColor = Color.parseColor("#fface2ed");
                tertiaryColor = Color.parseColor("#ffffffff");
                break;
            case '2'://purple - finalized
                mainColor = Color.parseColor("#ff372742");
                secondaryColor = Color.parseColor("#ff4ECDC4");
                tertiaryColor = Color.parseColor("#ffAFDBD8");
                break;
            case '3'://green - finalized
                mainColor = Color.parseColor("#FF27393B");
                secondaryColor = Color.parseColor("#ffDCEAB6");
                tertiaryColor = Color.parseColor("#ffF1F6E4");
                break;
            case '4'://orange - finalized
                mainColor = Color.parseColor("#ff282F35");
                secondaryColor = Color.parseColor("#ffFFA100");
                tertiaryColor = Color.parseColor("#ff818181");
                break;
        }
        if(sharedpreferences.getBoolean("prefNotif",true)) {
            //start checking for Music
            Intent serviceIntent = new Intent(this, BackgroundCheckMusicPlaying.class);
            serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startService(serviceIntent);
        }
        getActionBar().setBackgroundDrawable(new ColorDrawable(mainColor));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        findViewById(R.id.background).setBackgroundColor(mainColor);
        ((EditText)findViewById(R.id.edit1)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        ((EditText)findViewById(R.id.edit2)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        findViewById(R.id.linearLayout).setBackgroundColor(mainColor);
    }

    /**
     * Receives the go for searching whatever is inputted, reformat the fields to the ones properly accepted!
     * @param view the View which clicked this
     */
    public void sendInfo(View view){

        EditText text1 = (EditText) findViewById(R.id.edit1);
        EditText text2  =(EditText) findViewById(R.id.edit2);
        String song = text1.getText().toString();
        String band = text2.getText().toString();

        startSearch(song, band);
    }

    /**
     * Actually Searching for the song (new song)
     * @param title title of song
     * @param band band of the song
     */
    public void startSearch(String title, String band){
        if (title==null){
            title ="";
        }
        if (band==null){
            band ="";
        }
        final StringBuilder result = new StringBuilder(title.length());
        String[] words = title.split("\\s");
        for(int i=0,l=words.length;i<l;++i) {
            if(i>0) result.append(" ");
            result.append(Character.toUpperCase(words[i].charAt(0)))
                    .append(words[i].substring(1));

        }
        final StringBuilder result2 = new StringBuilder(band.length());
        String[] words2 = band.split("\\s");
        for(int i=0,l=words2.length;i<l;++i) {
            if(i>0) result2.append(" ");
            result2.append(Character.toUpperCase(words2[i].charAt(0)))
                    .append(words2[i].substring(1));

        }
        Song song = new Song(-1, result2.toString(), result.toString(), null);
        Intent intent = new Intent(AddToLibrary.this, DisplayMessageActivity.class);
        intent.putExtra("song",song);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_to_library, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
