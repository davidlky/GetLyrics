package com.sevenhourdev.getlyric;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;

import java.io.File;

/**
 * The OptionDialogPreference will display a dialog, and will persist the
 * <code>true</code> when pressing the positive button and <code>false</code>
 * otherwise. It will persist to the android:key specified in xml-preference.
 */
public class OptionDialogPreference extends DialogPreference {

    public OptionDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            if (getTitle().equals("Reset Lyrics")){
                WriteFile writeFile = new WriteFile(getContext());
                System.exit(0);
            }else{
                clearApplicationData();
                System.exit(0);
            }
        }
        super.onDialogClosed(positiveResult);
        persistBoolean(positiveResult);
    }

    public void clearApplicationData() {
        File cache = getContext().getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/com.sevenhourdev.getlyrics/" + s + " DELETED *******************");
                }
            }
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

}