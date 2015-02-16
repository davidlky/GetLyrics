package com.sevenhourdev.getlyric;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Specifically made to collect all lyrics from online
 */
public class LoadingLyrics extends Activity {

    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private TextView textView;
    private int mainColor,secondaryColor,tertiaryColor;

    private DownloadWebPageTask mTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_lyrics);
importColors();

        // Only set the tint if the device is running KitKat or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
// Holo light action bar color is #ff7c272b
            tintManager.setStatusBarTintColor(mainColor);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setNavigationBarTintColor(mainColor);
        }
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
        getActionBar().setBackgroundDrawable(new ColorDrawable(mainColor));
    }
    /**
     * The code to start downloading
     */
    private void openSettings(){
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mProgress.setIndeterminate(true);
        downloadPage();
        //Intent intent = new Intent(LoadingLyrics.this,MainActivity.class);

        //startActivity(intent);
    }
    private void downloadPage() {
        if (mTask != null
                && mTask.getStatus() != DownloadWebPageTask.Status.FINISHED) {
            Intent intent = new Intent(LoadingLyrics.this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        WriteFile writeFile = new WriteFile(this);
        // execute(String[]) you can put array of links to web pages, or array of Integer[]
        // if first param is Integer[] etc.
        Song[] songs = new Song[writeFile.getAllRowsAsArrays().size()];
        writeFile.getAllRowsAsArrays().toArray(songs);
        mProgress.setMax(songs.length);
        mTask = (DownloadWebPageTask) new DownloadWebPageTask()
                .execute(songs);
    }

    // AsyncTask <TypeOfVarArgParams , ProgressValue , ResultValue> .
    private class DownloadWebPageTask extends AsyncTask<Song, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            //textView.setText("Hello !!!");
            mProgress = (ProgressBar) findViewById(R.id.progressBar);
            textView = (TextView) findViewById(R.id.textView2);
            mProgress.setIndeterminate(false);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgress.setProgress(mProgressStatus);
            textView.setText("Syncing "+mProgressStatus +" of "+ values[0]);

        }

        @Override
        protected Integer doInBackground(Song[] songs) {
            int count = 0;
            final WriteFile temp = new WriteFile(getBaseContext());
            for(Song song:songs){
                count++;
                temp.updateRow(song.getID(), song.getArtist(), song.getTitle(), song.getLyrics());
                final int length = songs.length;
                mProgressStatus = count;
                publishProgress(length);
                if (isCancelled()) break;
            }
                return count;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Intent intent = new Intent(LoadingLyrics.this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }@Override
     public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return true;
    }
    @Override
    /**
     * Get downloading
     */
    protected void onPostCreate(Bundle savedInstanceState){

        super.onPostCreate(savedInstanceState);
        openSettings();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.loading_lyrics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }if(id ==R.id.abs__home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        mTask.cancel(true);
        super.finish();
    }
}
