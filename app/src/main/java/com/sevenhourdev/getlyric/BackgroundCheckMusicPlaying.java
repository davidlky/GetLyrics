package com.sevenhourdev.getlyric;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Checks if Music is playing in the Background
 *
 * Created by David Liu on 05/06/2014.
 */
public class BackgroundCheckMusicPlaying extends Service {
    public static final String EXTRA_MESSAGE = "com.sevenhourdev.getlyrics.MESSAGE";
    private NotificationCompat.Builder mBuilder;
    private boolean previosulydisplayed = false;
    private String TAG = "adfasfsd";

    private NotificationManager mNotificationManager;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    String prev_artist = "";
    String pev_album = "";
    String prev_track = "";
    @Override
    public void onCreate() {
        super .onCreate();
        //Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");
        //start when the state is changed
        IntentFilter iF = new IntentFilter();
        iF.addAction("com.android.music.metachanged");
        iF.addAction("com.android.music.playstatechanged");

        AudioManager manager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);

        previosulydisplayed = manager.isMusicActive();
        registerReceiver(mReceiver, iF);
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            String cmd = intent.getStringExtra("command");
            Log.d("onReceive ", action + " / " + cmd);
            String artist = intent.getStringExtra("artist");
            String album = intent.getStringExtra("album");
            String track = intent.getStringExtra("track");

            Log.d("Music",artist+":"+album+":"+track);
            if(artist!=null&&album!=null&&track!=null) {
                //make a intent and song
                intent = new Intent(context, DisplayMessageActivity.class);
                WriteFile temp = new WriteFile(context);
                Song song = temp.getRowAsArray(track,artist);
                //if new song
                if(song.getID()==-1) {
                    song = new Song(-1, artist, track, null);
                    intent.putExtra("song", song
                    );
                }else{
                    intent.putExtra("song", song
                    );
                }
                temp.close();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // build notification
                // the addAction re-use the same intent to keep the example short
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                mBuilder =
                        new NotificationCompat.Builder(context)//set all the icons and etc.
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentTitle(track.toUpperCase())
                                .setContentText(artist.toUpperCase())
                                .setAutoCancel(false);
                mBuilder.setContentIntent(pIntent);
                mNotificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                //see if notification should be removed

                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if(sharedpreferences.getBoolean("prefNotif", true)) {
                    if (action.contains("playstatechanged")) {
                            if (!previosulydisplayed) {
                                mNotificationManager.notify(1, mBuilder.build());
                                previosulydisplayed = true;
                            } else {
                                mNotificationManager.cancel(1);
                                previosulydisplayed = false;
                            }
                    } else{
                        if((!(prev_artist.equals(artist)))&&(!(prev_track.equals(track)))&&(!(pev_album.equals(album)))) {
                            mNotificationManager.notify(1, mBuilder.build());
                            previosulydisplayed = true;
                            prev_artist = artist;
                            prev_track = track;
                            pev_album = album;
                        }
                    }
                }
            }
        }
    };


}
