package com.sevenhourdev.getlyric;


import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.regex.Pattern;

public class Song extends Application implements Parcelable {
    private long id;
    private String title, ttitle;
    private String artist,tartist;
    private String lyrics;

    /**
     * Make a new Song
     *
     * @param songID ID of the song
     * @param songArtist Artist of the Song
     * @param songTitle Title of the Song
     * @param songLyrics Lyrics of the Song
     */
    public Song(long songID, String songArtist, String songTitle, String songLyrics) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        lyrics = songLyrics;
    }

    /**
     *
     * @return the ID of the song
     */
    public long getID(){return id;}

    /**
     *
     * @return the Title of the song
     */
    public String getTitle(){return title;}

    /**
     *
     * @return the Artist of the song
     */
    public String getArtist(){return artist;}

    public void setLyrics(String lyric){
        lyrics = lyric;
    }

    /**
     * Searches the lyrics
     * @return Lyrics of the song
     */
    public String getLyrics() {
        if((artist==null || artist.equals(""))&&title.length()>1) {
            artist = title.substring(0, title.indexOf("-"));
            title = title.substring(title.indexOf("-"));
            actuallyGetLyrics();
            if (lyrics.equals("No Result Found")) {
                String temp = artist;
                artist = title;
                title = temp;
            }
        }
        actuallyGetLyrics();
        return lyrics;
    }

    private void actuallyGetLyrics(){
        if (lyrics==null||lyrics.length()<20||lyrics.equals("No Result Found")) {//if nothing found
            Search search = new Search();
            try {
                //search metro lyrics
                modifyInput4();
                lyrics = search.search("http://www.metrolyrics.com/" + ttitle+"-lyrics-"+tartist + ".html", 1);
                lyrics = modify2(lyrics);

                if(lyrics ==null||lyrics.length()<20||lyrics.equals("No Result Found")){//if nothing found
                    lyrics = search.search("http://www.songlyrics.com/" + tartist + "/" + ttitle + "-lyrics/", 3);
                    lyrics = modify3(lyrics);
                    if(lyrics ==null||lyrics.length()<20||lyrics.equals("No Result Found")) {//if nothing found
                        lyrics = "No Result Found";
                    }
                }


            } catch (Exception e) {//if an issue
                Log.e("aasdf", e.getMessage());
                e.printStackTrace();
                if (lyrics==null||(!Pattern.matches("[a-zA-Z]+", lyrics))||lyrics.equals("No Result Found")) {//if nothing found
                    lyrics ="No Result Found";
                }
            }
        }
    }

    /**
     * Searches Metro Lyrics
     */
    private void modifyInput4(){
        tartist=artist.toLowerCase();
        ttitle=title.toLowerCase();
        //Disable Searching of "feat"
        if(tartist.contains("(")){
            int beginning = tartist.indexOf("(");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("(")){
            int beginning = ttitle.indexOf("(");
            ttitle = ttitle.substring(0, beginning);
        }

        //Disable Searching of "["
        while(tartist.contains("[")){
            int beginning = tartist.indexOf("[");
            tartist = tartist.substring(0, beginning);
        }while(ttitle.contains("[")){
            int beginning = ttitle.indexOf("[");
            ttitle = ttitle.substring(0, beginning);
        }

        //Disable Searching of "ft"
        if(tartist.contains(" (ft")){
            int beginning = tartist.indexOf(" (ft");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains(" (ft")){
            int beginning = ttitle.indexOf(" (ft");
            ttitle = ttitle.substring(0, beginning);
        }
        if(tartist.contains("(feat")){
            int beginning = tartist.indexOf("(feat");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("(feat")){
            int beginning = ttitle.indexOf("(feat");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "ft"
        if(tartist.contains("(ft")){
            int beginning = tartist.indexOf("(ft");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("(ft")){
            int beginning = ttitle.indexOf("(ft");
            ttitle = ttitle.substring(0, beginning);
        }
        if(tartist.contains(" feat")){
            int beginning = tartist.indexOf(" feat");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains(" feat")){
            int beginning = ttitle.indexOf(" feat");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "ft"
        if(tartist.contains(" ft")){
            int beginning = tartist.indexOf(" ft");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains(" ft")){
            int beginning = ttitle.indexOf(" ft");
            ttitle = ttitle.substring(0, beginning);
        }
        if(tartist.contains("feat")){
            int beginning = tartist.indexOf("feat");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("feat")){
            int beginning = ttitle.indexOf("feat");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "ft"
        if(tartist.contains("ft")){
            int beginning = tartist.indexOf("ft");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("ft")){
            int beginning = ttitle.indexOf("ft");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "&"
        if(tartist.contains("&")){
            int beginning = tartist.indexOf("&");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("&")){
            int beginning = ttitle.indexOf("&");
            ttitle = ttitle.substring(0, beginning)+ttitle.substring(beginning+2, ttitle.length());
        }
        //Disable Searching of "featuring"
        if(tartist.contains("featuring")){
            int beginning = tartist.indexOf("featuring");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("featuring")){
            int beginning = ttitle.indexOf("featuring");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable searching tartists starting with "a"
        if(ttitle.substring(0,2).equals("a ")){
            ttitle = ttitle.substring(2);
        }
        //Disable searching tartists starting with "the"
        if(ttitle.length()>4) {
            if (ttitle.substring(0, 4).equals("the ")) {
                ttitle = ttitle.substring(4);
            }
        }

        tartist = tartist.replaceAll("[^a-zA-Z0-9 ]", "-");
        ttitle = ttitle.replaceAll("[^a-zA-Z0-9 ]", "-");
        tartist = tartist.replaceAll(" ", "-");
        ttitle = ttitle.replaceAll(" ", "-");
        ttitle = ttitle.replaceAll("--", "-");
        while(ttitle.charAt(ttitle.length()-1) =='-'){
            ttitle = ttitle.substring(0,ttitle.length()-1);
        }
        while(tartist.charAt(tartist.length()-1) =='-'){
            tartist = tartist.substring(0,tartist.length()-1);
        }
        while(ttitle.charAt(0) =='-'){
            ttitle = ttitle.substring(1,ttitle.length());
        }
        while(tartist.charAt(0) =='-'){
            tartist = tartist.substring(1,tartist.length());
        }
    }

    /**
     * Searches Metro Lyrics
     */
    private void modifyInput2(){
        tartist=artist.toLowerCase();
        ttitle=title.toLowerCase();
        //Disable Searching of "["
        while(tartist.contains("[")){
            int beginning = tartist.indexOf("[");
            tartist = tartist.substring(0, beginning);
        }while(ttitle.contains("[")){
            int beginning = ttitle.indexOf("[");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "feat"
        if(tartist.contains(" (feat")){
            int beginning = tartist.indexOf(" (feat");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains(" (feat")){
            int beginning = ttitle.indexOf(" (feat");
            ttitle = ttitle.substring(0, beginning);
        }


        //Disable Searching of "ft"
        if(tartist.contains(" (ft")){
            int beginning = tartist.indexOf(" (ft");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains(" (ft")){
            int beginning = ttitle.indexOf(" (ft");
            ttitle = ttitle.substring(0, beginning);
        }
        if(tartist.contains("(feat")){
            int beginning = tartist.indexOf("(feat");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("(feat")){
            int beginning = ttitle.indexOf("(feat");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "ft"
        if(tartist.contains("(ft")){
            int beginning = tartist.indexOf("(ft");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("(ft")){
            int beginning = ttitle.indexOf("(ft");
            ttitle = ttitle.substring(0, beginning);
        }
        if(tartist.contains(" feat")){
            int beginning = tartist.indexOf(" feat");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains(" feat")){
            int beginning = ttitle.indexOf(" feat");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "ft"
        if(tartist.contains(" ft")){
            int beginning = tartist.indexOf(" ft");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains(" ft")){
            int beginning = ttitle.indexOf(" ft");
            ttitle = ttitle.substring(0, beginning);
        }
        if(tartist.contains("feat")){
            int beginning = tartist.indexOf("feat");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("feat")){
            int beginning = ttitle.indexOf("feat");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "ft"
        if(tartist.contains("ft")){
            int beginning = tartist.indexOf("ft");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("ft")){
            int beginning = ttitle.indexOf("ft");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "&"
        if(tartist.contains("&")){
            int beginning = tartist.indexOf("&");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("&")){
            int beginning = ttitle.indexOf("&");
            ttitle = ttitle.substring(0, beginning)+ttitle.substring(beginning+2, ttitle.length());
        }
        //Disable Searching of "featuring"
        if(tartist.contains("featuring")){
            int beginning = tartist.indexOf("featuring");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("featuring")){
            int beginning = ttitle.indexOf("featuring");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable searching tartists starting with "a"
        if(ttitle.substring(0,2).equals("a ")){
            ttitle = ttitle.substring(2);
        }

        tartist = tartist.replaceAll("[^a-zA-Z0-9 ]", "-");
        ttitle = ttitle.replaceAll("[^a-zA-Z0-9 ]", "-");
        tartist = tartist.replaceAll(" ", "-");
        ttitle = ttitle.replaceAll(" ", "-");
        ttitle = ttitle.replaceAll("--", "-");
        while(ttitle.charAt(ttitle.length()-1) =='-'){
            ttitle = ttitle.substring(0,ttitle.length()-1);
        }
        while(tartist.charAt(tartist.length()-1) =='-'){
            tartist = tartist.substring(0,tartist.length()-1);
        }
    }


    /**
     * Searches AZLyrics
     */
    private void modifyInput(){
        tartist=artist.toLowerCase();
        ttitle=title.toLowerCase();
        //Disable Searching of "feat"
        if(tartist.contains("feat ")){
            int beginning = tartist.indexOf("feat");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("feat ")){
            int beginning = ttitle.indexOf("feat");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "feat."
        if(tartist.contains("feat.")){
            int beginning = tartist.indexOf("feat.");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("feat.")){
            int beginning = ttitle.indexOf("feat.");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "ft"
        if(tartist.contains("ft")){
            int beginning = tartist.indexOf("ft");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("ft")){
            int beginning = ttitle.indexOf("ft");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable Searching of "&"
        if(tartist.contains("&")){
            int beginning = tartist.indexOf("&");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("&")){
            int beginning = ttitle.indexOf("&");
            ttitle = ttitle.substring(0, beginning)+ttitle.substring(beginning+2, ttitle.length());
        }
        //Disable Searching of "featuring"
        if(tartist.contains("featuring")){
            int beginning = tartist.indexOf("featuring");
            tartist = tartist.substring(0, beginning);
        }if(ttitle.contains("featuring")){
            int beginning = ttitle.indexOf("featuring");
            ttitle = ttitle.substring(0, beginning);
        }
        //Disable searching tartists starting with "a"
        if(ttitle.substring(0,2).equals("a ")){
            ttitle = ttitle.substring(2);
        }
        //Disable searching tartists starting with "the"
        if(ttitle.length()>4) {
            if (ttitle.substring(0, 4).equals("the ")) {
                ttitle = ttitle.substring(4);
            }
        }
        tartist = tartist.replaceAll("[^a-zA-Z0-9]", "");
        ttitle = ttitle.replaceAll("[^a-zA-Z0-9]", "");
    }

    /**
     * Send back the newly modified string
     * AZLyrics
     *
     * @param input the oringinal Lyrics
     * @return the formatted Lyrics
     */
    public String modify(String input){
        if(input.length()>47) {
            input = input.substring(24);
            String answer = input.replace("<br />", "\n");
            answer = answer.replace("<br/>", "\n");
            answer = answer.replace("<br>", "\n");
            answer = answer.replace("<i>", "");
            answer = answer.replace("</i>", "");

            answer = answer.replace("</div>", "");
            answer = answer.substring(0, answer.length() - 22);
            return answer;
        }else{
            return "No Result Found";
        }
    }/**
     * Send back the newly modified string
     * Metro Lyrics
     * @param input the oringinal Lyrics
     * @return the formatted Lyrics
     */
    public String modify2(String input){
        if(input!=null) {
            if (input.length() > 49) {
                input = input.substring(49);
                String answer = input.replace("<br/>", "\n");
                answer = answer.replace("<br/>", "\n");
                answer = answer.replace("<br>", "\n");
                input = answer.replace("<i>", "");
                answer = input.replace("</i>", "");
                input = answer.replace("</p>", "\n");
                answer = input.replace("</div>", "");

                input = answer.replace("<p class='verse'>", "\n");

                return input;
            } else {
                return "No Result Found";
            }
        }else{
            return "No Result Found";
        }
    }

    /**
     * Send back the newly modified string
     * SongLyrics
     * @param input the oringinal Lyrics
     * @return the formatted Lyrics
     */
    public String modify3(String input){
        if(input!=null) {
            if (lyrics.length()>20) {
                String answer = input.replace("\t", "");
                input = answer.replace("<br />", "\n");
                answer = input.replace("<i>", "");
                answer = answer.replace("</i>", "");
                input = answer.replace("</p>", "\n");
                answer = input.replace("</div>", "");
                while(answer.contains("<")) {
                    int beginning = answer.indexOf("<");
                    int ending = answer.indexOf(">");
                    answer = answer.substring(0, beginning) + answer.substring(ending+1, answer.length());
                }
                return answer;
            } else {
                return "No Result Found";
            }
        }else{
            return "No Result Found";
        }
    }

    public Song (Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        this.id = Long.parseLong(data[0]);
        this.artist = data[1];
        this.title = data[2];
        this.lyrics = data[3];
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeStringArray(new String[] {String.valueOf(this.id),
                this.artist,
                this.title, this.lyrics});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
