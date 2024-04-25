package com.example.musicplayerapp

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

    fun setMusicTracks(musicTracks: Map<String, JSONObject>) {
        _musicTracks.value = musicTracks
    }

    fun setCurrentMusicTrack(currentMusicTrack: String) {
        _currentMusicTrack.value = currentMusicTrack
    }

    fun setSelectedMusicTrack(selectedMusicTrack: String) {
        _selectedMusicTrack.value = selectedMusicTrack
    }
}