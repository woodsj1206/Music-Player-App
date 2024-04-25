package com.example.musicplayerapp

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.musicplayerapp.databinding.FragmentMusicInformationBinding

class MusicInformationFragment : Fragment() {
    private val sharedViewModel : SharedViewModel by activityViewModels()

    private lateinit var binding : FragmentMusicInformationBinding

    private lateinit var mediaPlayer : MediaPlayer
    private lateinit var seekBar : SeekBar

    private  lateinit var  runnable : Runnable
    private lateinit var  handler : Handler

    //Visit https://rapidapi.com/deezerdevs/api/deezer-1/ to get an API key.
    private val apiKey = "REPLACE_WITH_YOUR_API_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMusicInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seekBar = binding.sbMusicDuration

        handler = Handler(Looper.getMainLooper())

        // Initialize MediaPlayer instance
        mediaPlayer = MediaPlayer()

        setButtonIcon()
        initializeSeekbar()

        sharedViewModel.selectedMusicTrack.observe(viewLifecycleOwner) { selectedMusicTrack ->
            // Change music track
            if(sharedViewModel.currentMusicTrack.value != selectedMusicTrack){
                // Set music track information based on the selected item
                populateDisplay(selectedMusicTrack)

                val mediaURL = sharedViewModel.musicTracks.value?.get(selectedMusicTrack)?.getString("preview")?.toUri()
                mediaPlayer.apply {
                    // Reset MediaPlayer to its uninitialized state
                    reset()

                    // Set new data source
                    if (mediaURL != null) { setDataSource(requireContext(), mediaURL) }

                    // Prepare the MediaPlayer asynchronously
                    prepare()

                    // Start playback
                    start()
                }
                setButtonIcon()
                initializeSeekbar()

                // Update the current music track
                sharedViewModel.setCurrentMusicTrack(selectedMusicTrack)
            }
            else{
                populateDisplay(selectedMusicTrack)
            }
        }

        binding.btnController.setOnClickListener {
            if(mediaPlayer.isPlaying){
                // Pause audio playback
                mediaPlayer.pause()
            }
            else{
                // Start audio playback
                mediaPlayer.start()
            }
            setButtonIcon()
        }
    }

    private fun setButtonIcon(){
        if(mediaPlayer.isPlaying){
            binding.btnController.text = "Pause"

            // Get the drawable from the button's compound drawables
            val drawable = ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_media_pause)

            // Apply a color filter to change the tint
            drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(requireContext(), R.color.app_theme_color), PorterDuff.Mode.SRC_IN)

            // Set the compound drawable with the updated tint to the button
            binding.btnController.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
        else{
            binding.btnController.text = "Play"

            // Get the drawable from the button's compound drawables
            val drawable = ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_media_play)

            // Apply a color filter to change the tint
            drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(requireContext(), R.color.app_theme_color), PorterDuff.Mode.SRC_IN)

            // Set the compound drawable with the updated tint to the button
            binding.btnController.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)

        }
    }

    private fun populateDisplay(selectedMusicTrack: String){
        // Retrieve the corresponding JSON object from music tracks map
        val musicTracks = sharedViewModel.musicTracks.value
        val trackInfo = musicTracks?.get(selectedMusicTrack)

        binding.tvSongName.text = trackInfo?.getString("title")
        binding.tvAlbumName.text = trackInfo?.getJSONObject("album")?.getString("title")
        binding.tvArtist.text = trackInfo?.getJSONObject("artist")?.getString("name")

        binding.ivAlbumCover.setImageResource(R.drawable.music_player_app_placeholder)
        Glide.with(requireContext()).load(trackInfo?.getJSONObject("album")?.getString("cover_big")).into(binding.ivAlbumCover)
    }

    private fun initializeSeekbar(){
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser) mediaPlayer.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        seekBar.max = mediaPlayer.duration

        runnable = Runnable{
            seekBar.progress = mediaPlayer.currentPosition
            handler.postDelayed(runnable, 1000)

            val playedTime = mediaPlayer.currentPosition/1000

            if(playedTime < 10){
                binding.tvCurrentTime.text = "0:0$playedTime"
            }
            else{
                binding.tvCurrentTime.text = "0:$playedTime"
            }
        }
        handler.postDelayed(runnable, 1000)

    }
}