package com.example.musicplayerapp

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject

class SharedViewModel : ViewModel() {
    // Store music tracks
    private val _musicTracks = MutableLiveData<Map<String, JSONObject>>()
    val musicTracks: LiveData<Map<String, JSONObject>> get() = _musicTracks

    // Id of the music track selected from the search
    private val _selectedMusicTrack = MutableLiveData<String>()
    val selectedMusicTrack: LiveData<String> get() = _selectedMusicTrack

    // Id of the music track currently displayed in the UI
    private val _currentMusicTrack = MutableLiveData<String>()
    val currentMusicTrack: LiveData<String> get() = _currentMusicTrack

    private val _mediaPlayer = MutableLiveData<MediaPlayer>()
    val mediaPlayer: LiveData<MediaPlayer> get() = _mediaPlayer

    fun setMusicTracks(musicTracks: Map<String, JSONObject>) {
        _musicTracks.value = musicTracks
    }

    fun setMediaPlayer(mediaPlayer: MediaPlayer){
        _mediaPlayer.value = mediaPlayer
    }

    fun setCurrentMusicTrack(currentMusicTrack: String) {
        _currentMusicTrack.value = currentMusicTrack
    }

    fun setSelectedMusicTrack(selectedMusicTrack: String) {
        _selectedMusicTrack.value = selectedMusicTrack
    }
}