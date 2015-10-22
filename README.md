# GetLyrics
![alt text](https://github.com/davidlky/GetLyrics/raw/master/app/src/main/res/drawable-xxhdpi/ic_launcher.png "GetLyrics")


GetLyrics is an Android application that allows retrieves lyrics for music that people would have on their phone. It is a simple app that can save all your song's lyrics offline after reading your phone's library. 

The application has a variety of features:
- Offline lyrics sync
- Retrieve all button
- Notification based on current playing music
- Adding new songs to library
- KitKat Design
- Multiple Themes
- Ad-free!

This is currenly also using [SystemBarTint](https://github.com/jgilfelt/SystemBarTint)
## Documentation
| Class | Extends | Purpose |
|-------|---------|------|
| AddToLibrary| Activity | Shows the add song to library screen. Inputed song name and artist will be used to search |
| BackgroundCheckMusicPlaying | Service | A background service that checks if music is playing (using a BroacastReceiver). Will determine the song playing and show a notification linking to the app to show the lyrics. |
| BootCompleted | BroadcastReceiver | Adds the BackgroundCheckMusicPlaying service when phone has booted up |
| DisplayMessageActivity | Activity | Displays the lyrics for the song |
| LoadingLyrics | Activity | The loading all lyrics page, downloads all of the lyrics to all songs in library on phone|
| MainActivity| Activity | Also using OnQueryTextListener. This is the main screen listing all of the songs with a FAB to add to library. You can query the list of song (as it is using a custom ListAdapter allowing categories and query filter). |
| OptionDialogPreference | DialogPreference | Runs certain scripts after certain settings in SettingsActivity has changed |
| Search | | Parsing titles of songs and analyzing search results for lyrics |
| SettingsActivity| PreferenceActivity | Settings screen that dealds with a SharedPreference |
| Song | Application | Also Parcelable. This is the Model for each song |
| WriteFile |  | Read and write to the SQLite Database used behind storing data for this app |

## TODOS
- Further Documentations
- Inline comments
- Debugging on parsing and reading music titles

## Screenshots
### Features
<img src="https://raw.githubusercontent.com/davidlky/GetLyrics/master/screens/(1).jpg" width ="300px" />
<img src="https://raw.githubusercontent.com/davidlky/GetLyrics/master/screens/(6).jpg" width ="300px" />
<img src="https://raw.githubusercontent.com/davidlky/GetLyrics/master/screens/(2).jpg" width ="300px" />
<img src="https://raw.githubusercontent.com/davidlky/GetLyrics/master/screens/(3).jpg" width ="300px" />
<img src="https://raw.githubusercontent.com/davidlky/GetLyrics/master/screens/(4).jpg" width ="300px" />


### Themes
![Themes](https://raw.githubusercontent.com/davidlky/GetLyrics/master/screens/(8).jpg "Screenshot")

## Credits
Author: David Liu

Using: [Jeff Gilfelt](https://github.com/jgilfelt)'s [SystemBarTint](https://github.com/jgilfelt/SystemBarTint)

## Copyright
Copyright 2015 David Liu
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
