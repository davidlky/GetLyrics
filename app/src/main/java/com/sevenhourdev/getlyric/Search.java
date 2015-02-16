package com.sevenhourdev.getlyric;

import android.os.StrictMode;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * Lyrics Searcher
 *
 * @author David Liu
 *
 */
public class Search {

    /**
     * Retrieve Source File from a web site
     * @param string
     * @return
     * @throws Exception
     * @throws IOException
     */
    public String search(String string, int choice) throws Exception, IOException{
        boolean start = false;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);//use the earliest version of HTTP

        DefaultHttpClient client = new DefaultHttpClient(params);
        HttpGet request = new HttpGet(string);
        HttpResponse response = client.execute(request);
        InputStream in = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder str = new StringBuilder();
        String line = null;
        switch(choice){
            case 1://from metrolyrics
                while((line = reader.readLine()) != null)
                {
                    if(!start&&line.contains("lyrics-body-text")&&line.contains("")){
                        start = true;
                    }
                    if(start){
                        str.append(line);
                    }
                    if(start&&line.contains("</div>")){
                        start = false;
                    }
                }
                break;
            case 2://from azlyrics
                while((line = reader.readLine()) != null)
                {
                    if(line.contains("start of lyrics")){
                        start = true;
                    }
                    if(start){
                        str.append(line);
                    }
                    if(start&&line.contains("end of lyrics")){
                        start = false;
                    }
                }
                break;
            case 3://from songlyrics
                while((line = reader.readLine()) != null)
                {
                    if(line.contains("songLyricsDiv-outer")){
                        start = true;
                    }
                    if(start){
                        str.append(line);
                    }
                    if(start&&line.contains("</div>")){
                        start = false;
                    }
                }
                break;
        }
        in.close();
        return str.toString();
    }
}
