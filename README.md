# ProjectAndroidSampler

Inspired by the BOSS RC-505 Loop Recorder, OP-1 Sampler and Audio Workstation, Spectroid Audio Visualization app and SunVox DAW

![app](/dmg.png)

## App Purpose
Rudamentary audio playback, sampling of audio files, asynchronous playback of "pad" objects in order to have an easy-to-trigger customizable soundboard. Other features originally planned.

## How to use
After opening the app, press "Choose Audio File" and select an audio file to load into the app. When loaded in, Playback and Rewind controls handle where the playback head is relative to the timeline. Pressing SetPos keeps track of a position to set a pad for. SeekToPos will return playback to the last set audio position.

After enough positions have been set, the user can press the Music Pads button at the bottom right of the app to move to the Pads activity.

![app](/bvr.png)

When in this activity, the user must press each pad to initialize each audio device, and then any pad can be played freely to move to the audio position of that designated pad. The user can return to the last activity to continue setting audio positions.

## Known Issues

- Clicking Back in the choose file intent crashes program
- Clicking ChooseAudioFile expects the choosing operation to succeed
- MusicPads have to be played at least once to play from right time
- Rapid pressing pads causes playback issues, mainly has to do with asynchronisity
- Switching back and forth between the two activities repeatedly causes issues with how the second activity is loaded in. Closing the activity would help fix issues.


## TODO

| *Goal* | *Task* |
| ------ | ------ |
| 1 | exception handling when failing intent |
| 2 | better way to handle pads/instant playback |
| 3 | fix and improve traversal between activities |
| 4 | create method of adjusting playback positions; make seek more understandable |