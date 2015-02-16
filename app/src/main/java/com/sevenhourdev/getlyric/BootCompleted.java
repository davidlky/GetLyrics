package com.sevenhourdev.getlyric;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;

// here is the OnRevieve methode which will be called when boot completed

/**
 * Checks when phone is started up
 * @author David Liu
 */
public class BootCompleted extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        //we double check here for only boot complete event
        //after boot is completed
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED))
        {
            //here we start the service
            Intent serviceIntent = new Intent(context, BackgroundCheckMusicPlaying.class);
            serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(serviceIntent);
        }
    }
}
