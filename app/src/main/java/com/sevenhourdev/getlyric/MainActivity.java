package com.sevenhourdev.getlyric;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

/**
 * Controls all the functionality of the app:
 * Including - clicking, showing databases, setting background activity. This links
 * everything together essentially
 *
 * @author David Liu
 */
public class MainActivity extends Activity  implements OnQueryTextListener {
    private ArrayList<Song>songs;
    private int mainColor, secondaryColor, tertiaryColor;
    public static final String EXTRA_MESSAGE = "com.sevenhourdev.getlyrics.MESSAGE";
    private ListView list;
    private SongAdapter songAdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
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
        findViewById(R.id.pink_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddToLibrary.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.pink_icon);
        button.setSize(FloatingActionButton.SIZE_NORMAL);
        button.setColorNormal(secondaryColor);
        button.setIcon(R.drawable.ic_content_add);
        button.setColorPressed(tertiaryColor);
        button.setStrokeVisible(true);
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
    private void showDBOnScreen(){
        WriteFile writeFile = new WriteFile(getBaseContext());
        if(!writeFile.checkImportState()) {
            getSongList(); //LOAD all songs on computer!
        }
        songs= writeFile.getAllRowsAsArrays();
        //sort through the songs!
        Collections.sort(songs, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        writeFile.close();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Background background = new Background();
        background.execute();
    }


    /**
     * Get the list of songs
     */
    private void getSongList(){
        WriteFile writeFile = new WriteFile(this);
        //retrieve song info
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                if(thisTitle.length()>1) {
                    if(thisArtist.equals("<unknown>")){
                        if((!thisTitle.contains("Hangout"))&&(!thisTitle.contains("Facebook"))&&(!thisTitle.contains("Samsung"))&&thisTitle.length()>20){
                        thisArtist = "";
                            writeFile.addRow(thisArtist, thisTitle, null);
                        }
                    }else {
                        if((!thisTitle.contains("Hangout"))&&(!thisTitle.contains("Facebook"))&&(!thisTitle.contains("Samsung"))) {
                            writeFile.addRow(thisArtist, thisTitle, null);
                        }
                    }
                }
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();
        writeFile.close();
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.wow_songs) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Menu");
            String[] menuItems = getResources().getStringArray(R.array.menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if(item.getTitle().equals("Delete")){
            Song thing = (Song) songAdt.getItem(info.position);
            WriteFile temp = new WriteFile(this);
            temp.deleteRow(thing.getID());
            temp.close();
            list.setAdapter(null);
            Background background = new Background();
            background.execute();
        }
        return true;
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        // this is your adapter that will be filtered
        if (TextUtils.isEmpty(s))
        {
            list.clearTextFilter();
        }
        else
        {
            list.setFilterText(s.toString());
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        // this is your adapter that will be filtered
        if (TextUtils.isEmpty(newText))
        {
            list.clearTextFilter();
        }
        else
        {
            list.setFilterText(newText.toString());
        }

        return true;
    }


    public class Background extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {

            findViewById(R.id.imageView).setBackgroundColor(mainColor);
            ((TextView)findViewById(R.id.textView2)).setTextColor(secondaryColor);
            list = (ListView)findViewById(R.id.wow_songs);
            findViewById(R.id.container).setBackgroundColor(mainColor);
            list.setBackgroundColor(mainColor);
            //scroll and shit

            //color setting
            findViewById(R.id.container).setBackgroundColor(mainColor);
            list.setBackgroundColor(mainColor);
            list.setDivider(null);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            showDBOnScreen();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            findViewById(R.id.imageView).setVisibility(View.GONE);
            findViewById(R.id.textView2).setVisibility(View.GONE);
            //set the scroller (with alphabetizer)
            songAdt = new SongAdapter(getBaseContext(), songs);
            list.setFastScrollEnabled(true);
            list.setAdapter(songAdt);
            registerForContextMenu(list);
            list.setTextFilterEnabled(true);
        }
    }
    /**
     * Actually Searching for the song(chosen from list)
     * @param song the song
     */
    public void startSearch(Song song){
        Intent intent = new Intent(MainActivity.this, DisplayMessageActivity.class);
        intent.putExtra("song", song
        );

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE );
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search Library");
        AutoCompleteTextView search_text = (AutoCompleteTextView) searchView.findViewById(searchView.getContext().getResources().
                getIdentifier("android:id/search_src_text", null, null));
        search_text.setTextColor(Color.WHITE);
        search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
                return true;
            case R.id.action_saveAll:
                intent = new Intent(MainActivity.this, LoadingLyrics.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//clear the rest of the activities
                startActivity(intent);
                return true;
            case R.id.action_settings:
                intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    /**
     * Adapter
     */
    public class SongAdapter extends BaseAdapter implements SectionIndexer, Filterable, View.OnCreateContextMenuListener{

        private ItemFilter mFilter = new ItemFilter();
        private ArrayList<Song>filteredData = null;
        HashMap<String, Integer> alphaIndexer;
        private ArrayList<Song> songs;
        private LayoutInflater songInf;
        private String[] sections;
        public SongAdapter(Context c, ArrayList<Song> theSongs) {
            songs = new ArrayList(theSongs);
            filteredData = new ArrayList(theSongs);
            songInf = LayoutInflater.from(c);
            alphaIndexer = new HashMap<String, Integer>();
            int size = songs.size();
            for (int x = 0; x < size; x++) {
                String s = songs.get(x).getTitle();
                // get the first letter of the store
                if(s.length()>1) {
                    String ch = s.substring(0, 1);
                    // convert to uppercase otherwise lowercase a -z will be sorted
                    // after upper A-Z
                    ch = ch.toUpperCase();
                    // put only if the key does not exist
                    if (!alphaIndexer.containsKey(ch)) {
                        alphaIndexer.put(ch, x);
                    }
                }
            }
            Set<String> sectionLetters = alphaIndexer.keySet();
            // create a list from the set to sort
            ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
            Collections.sort(sectionList);
            sections = new String[sectionList.size()];
            sectionList.toArray(sections);

        }
        public int getPositionForSection(int section) {
            return alphaIndexer.get(sections[section]);
        }


        public int getSectionForPosition(int position) {
            int sum = 0;
            for(int i = sections.length - 1; i >= 0; i--) {
                sum +=alphaIndexer.get(sections[i]);
                if(position > alphaIndexer.get(sections[i])) {
                    return i;
                }
            }

            return 0;
        }

        public Object[] getSections() {
            return sections;
        }
        @Override
        public int getCount() {
            return filteredData.size();
        }

        @Override
        public Object getItem(int arg0) {
            return filteredData.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //get title and artist views

            DataViewHolder viewHolder1 = null;
            if (convertView == null) {
                // row = inflater.inflate(customLayoutId, null);
                convertView = songInf.inflate(R.layout.song, null);
                viewHolder1 = new DataViewHolder();
                viewHolder1.songView = (TextView) convertView.findViewById(R.id.song_title);
                viewHolder1.artistView = (TextView) convertView.findViewById(R.id.song_artist);
                viewHolder1.position = position;

                convertView.setTag(viewHolder1);
            }
            else{
                viewHolder1 = (DataViewHolder) convertView.getTag();
            }
            //get song using position
            final Song currSong = filteredData.get(position);
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {//set the on click
                    // TODO Auto-generated method stub
                    try {
                        startSearch(currSong);
                    } catch (Exception e) {
                        Log.e("Add Error", e.toString());
                        e.printStackTrace();
                    }
                }
            });
            convertView.setBackgroundColor(mainColor);
            //get title and artist strings
            viewHolder1.songView.setText(currSong.getTitle());
            viewHolder1.artistView.setText(currSong.getArtist());
            //set color
            viewHolder1.songView.setTextColor(secondaryColor);
            viewHolder1.artistView.setTextColor(tertiaryColor);

            convertView.setOnCreateContextMenuListener(this);
            return convertView;
        }

        @Override
        public Filter getFilter() {
            if(mFilter==null){
                mFilter = new ItemFilter();
            }
            return mFilter;
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        }


        private class ItemFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // We implement here the filter logic
                if (constraint == null || constraint.length() == 0) {
                    // No filter implemented we return all the list
                    results.values = new ArrayList(songs);
                    results.count = songs.size();
                }
                else {
                    // We perform filtering operation
                    ArrayList<Song> nSongList = new ArrayList<Song>();

                    for (Song song : songs) {
                        if (song.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())
                                ||song.getArtist().toUpperCase().contains(constraint.toString().toUpperCase()))
                            nSongList.add(song);
                    }

                    results.values = nSongList;
                    results.count = nSongList.size();

                }
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                // Now we have to inform the adapter about the new list filtered
                if (results.count == 0)
                    notifyDataSetInvalidated();
                else {
                    filteredData = (ArrayList<Song>) results.values;
                    notifyDataSetChanged();
                }
                notifyDataSetChanged();

            }


        }
    }
    static class DataViewHolder {
        public TextView songView;
        public TextView artistView;
        public int position;
    }

}
