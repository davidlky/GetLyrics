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
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * Displays the Lyrics onto the screen
 *
 * @author David Liu
 *
 */
public class DisplayMessageActivity extends Activity {
    //keeps all the details

    private int mainColor, secondaryColor, tertiaryColor;
    private TextView textView;
    protected String[]  deets;
    protected    ProgressBar spinner;
    protected Song song;
    protected LoadLyrics loadLyrics = new LoadLyrics();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        //load all intent
            setTitle("     ");
        importColors();
        // Set the Navi Bar
        // Only set the tint if the device is running KitKat or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // Holo light action bar color is #ff7c272b
            tintManager.setStatusBarTintColor(mainColor);
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setNavigationBarTintColor(mainColor);
        }
        Intent intent = getIntent();
        deets = intent.getStringArrayExtra (MainActivity.EXTRA_MESSAGE);
        Bundle data = getIntent().getExtras();
        song = data.getParcelable("song");
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

        findViewById(R.id.container2).setBackgroundColor(mainColor);
        findViewById(R.id.linearLayout2).setBackgroundColor(mainColor);
        getActionBar().setBackgroundDrawable(new ColorDrawable(mainColor));
        TextView textView = (TextView)findViewById(R.id.lyrics_text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,Integer.parseInt(sharedpreferences.getString("prefFontSize","12")));
        ((TextView)findViewById(R.id.lyrics_title)).setTextSize(TypedValue.COMPLEX_UNIT_PX,6+Integer.parseInt(sharedpreferences.getString("prefFontSize","12")));
    }


    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //set the lyrics on screen
        findViewById(R.id.linearLayout2).setVisibility(View.GONE);
        findViewById(R.id.editText).setVisibility(View.GONE);
        findViewById(R.id.save_button).setVisibility(View.GONE);
        findViewById(R.id.cancel_button).setVisibility(View.GONE);
        textView = (TextView) findViewById(R.id.lyrics_text);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
        loadLyrics.execute();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }




    /**
     * Edit the lyrics locally
     */
    public void edit(){
        if(((TextView)findViewById(R.id.lyrics_text)).getText().toString()!=null) {
            ((EditText) findViewById(R.id.editText)).setTextSize(TypedValue.COMPLEX_UNIT_PX,textView.getTextSize());
            ((EditText) findViewById(R.id.editText)).setTextColor(tertiaryColor);
            ((EditText) findViewById(R.id.editText)).setText(((TextView) findViewById(R.id.lyrics_text)).getText().toString());
        }
        findViewById(R.id.lyrics_text).setVisibility(View.GONE);
        findViewById(R.id.editText).setVisibility(View.VISIBLE);
        findViewById(R.id.save_button).setVisibility(View.VISIBLE);
        findViewById(R.id.cancel_button).setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_edit:
                edit();
                return true;
            case R.id.action_settings:
                Intent intent;
                intent = new Intent(DisplayMessageActivity.this,SettingsActivity.class);
                startActivity(intent);
                finish();
                return true;
            case android.R.id.home:
                loadLyrics.cancel(true);
                finish();
                return true;
            case R.id.action_delete:
                WriteFile  writeFile = new WriteFile(this);
                writeFile.deleteRow(song.getID());
                writeFile.close();
                finish();
                return true;
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

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            loadLyrics.cancel(true);
            finish();
        }
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            TextView textView = (TextView)findViewById(R.id.lyrics_text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() + 2);
            editor.putString("prefFontSize", String.valueOf(Math.round(textView.getTextSize())));
            editor.commit();
            ((TextView)findViewById(R.id.lyrics_title)).setTextSize(TypedValue.COMPLEX_UNIT_PX, 6 + Integer.parseInt(sharedpreferences.getString("prefFontSize", "12")));
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            TextView textView = (TextView)findViewById(R.id.lyrics_text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textView.getTextSize() - 2);
            editor.putString("prefFontSize", String.valueOf(Math.round(textView.getTextSize())));
            editor.commit();
            ((TextView)findViewById(R.id.lyrics_title)).setTextSize(TypedValue.COMPLEX_UNIT_PX, 6 + Integer.parseInt(sharedpreferences.getString("prefFontSize", "12")));
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            loadLyrics.cancel(true);
            finish();
        }
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
        }
        return true;
    }

    private class LoadLyrics extends AsyncTask<Void, Void, Void> {
        String lyrics;
        @Override
        protected void onPreExecute() {
            //textView.setText("Hello !!!");
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            if(song==null) {
                song = new Song(Long.parseLong(deets[3]), deets[0], deets[1], deets[2]);
                //see if we need to update
            }
            WriteFile temp = new WriteFile(getApplicationContext());
            Song song1 = temp.getRowAsArrayReal(song.getTitle(), song.getArtist());
            if(song1.getArtist()!=null&&song1.getTitle()!=null) {
                lyrics = song1.getLyrics();
                song.setLyrics(lyrics);
            }
            lyrics = song.getLyrics();
            if(song.getID()!=-1) {
                temp.updateRow(song.getID(), song.getArtist(), song.getTitle(), lyrics);
            }else{
                temp.addRow(song.getArtist(), song.getTitle(), lyrics);
            }
            temp.close();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            findViewById(R.id.container2).setBackgroundColor(mainColor);
            spinner.setVisibility(View.GONE);
            ((TextView)findViewById(R.id.lyrics_title)).setTextColor(secondaryColor);
            ((TextView)findViewById(R.id.lyrics_title)).setText(song.getTitle() + "\n" + song.getArtist());
            textView.setTextColor(tertiaryColor);
            textView.setText(lyrics);
            if(lyrics.equals("No Result Found")){
                textView.setText(lyrics+"\nTry Searching Manually Below");
                findViewById(R.id.linearLayout2).setVisibility(View.VISIBLE);
            }
            super.onPostExecute(aVoid);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
        }
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
        long id = song.getID();
        song = new Song(id, result2.toString(), result.toString(), null);
        loadLyrics = new LoadLyrics();
        loadLyrics.execute();
        findViewById(R.id.linearLayout2).setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void finish() {
        loadLyrics.cancel(true);
        song = null;
        Intent intent = new Intent(DisplayMessageActivity.this, MainActivity.class);
        startActivity(intent);
        super.finish();
    }

    public void save(View view){
        WriteFile temp = new WriteFile(getApplicationContext());
        temp.updateRow(song.getID(), song.getArtist(), song.getTitle(), ((EditText)findViewById(R.id.editText)).getText().toString());
        ((TextView) findViewById(R.id.lyrics_text)).setText(((EditText) findViewById(R.id.editText)).getText().toString());
        cancel(view);

        temp.close();
    }
    public void cancel(View view){

        findViewById(R.id.lyrics_text).setVisibility(View.VISIBLE);
        findViewById(R.id.editText).setVisibility(View.GONE);
        findViewById(R.id.save_button).setVisibility(View.GONE);
        findViewById(R.id.cancel_button).setVisibility(View.GONE);
    }

}
