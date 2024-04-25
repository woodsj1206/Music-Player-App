package com.example.musicplayerapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.musicplayerapp.databinding.FragmentMusicSearchBinding
import org.json.JSONException
import org.json.JSONObject

class MusicSearchFragment : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var binding : FragmentMusicSearchBinding

    private lateinit var recyclerView : RecyclerView
    private lateinit var trackList : ArrayList<MusicTrack>
    private lateinit var searchView: SearchView

    private lateinit var recyclerAdapter: RecyclerAdapter

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
        binding = FragmentMusicSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if music tracks are already available
        val musicTracks = sharedViewModel.musicTracks.value

        if (musicTracks != null) {
            // Populate search view with existing data
            populateSearchView(musicTracks)
        } else {
            // Fetch music tracks and populate search view when done
            getTopMusicTracks { musicTracksMap ->
                // Store music tracks in ViewModel
                sharedViewModel.setMusicTracks(musicTracksMap)

                // Populate search view
                populateSearchView(musicTracksMap)
            }
        }

        searchView = binding.svMusicSearch

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterList(newText)
                return true
            }
        })

    }

    private fun filterList(text: String) {
        val filteredList = ArrayList<MusicTrack>()

        for(track in trackList){
            if(track.title.lowercase().contains(text.lowercase())){
                filteredList.add(track)
            }
        }

        recyclerAdapter.setFilteredList(filteredList)
    }

    private fun getTopMusicTracks(callback: (Map<String, JSONObject>) -> Unit){
        val trackMap = mutableMapOf<String, JSONObject>()

        val queue = Volley.newRequestQueue(requireContext())
        val url = "https://deezerdevs-deezer.p.rapidapi.com/playlist/3155776842"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val tracksArray = jsonObject.getJSONObject("tracks").getJSONArray("data")

                    // Now you can iterate over the tracksArray to access each track object
                    for (i in 0 until tracksArray.length()) {
                        val trackObject = tracksArray.getJSONObject(i)
                        trackMap[trackObject.getString("id")] = trackObject
                    }

                    // Call the callback with the populated trackMap
                    callback(trackMap)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { _ ->
                Log.e("MusicPlayerApp", "FAILED TO GET A RESPONSE!")
            }) {
            // Include custom headers
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["X-RapidAPI-Key"] = apiKey
                headers["X-RapidAPI-Host"] = "deezerdevs-deezer.p.rapidapi.com"
                return headers
            }
        }

        queue.add(stringRequest)
    }

    private fun populateSearchView(musicTracks: Map<String, JSONObject>) {
        recyclerView = binding.rvTrackList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        trackList = ArrayList()

        var i = 1
        for (track in musicTracks) {
            trackList.add(MusicTrack(i, track.value.getString("id"), track.value.getString("title"), track.value.getJSONObject("artist").getString("name"), track.value.getJSONObject("album").getString("cover_big")))
            i++
        }

        recyclerAdapter = RecyclerAdapter(trackList, requireContext())

        // Set onItemClick listener in the adapter
        recyclerAdapter.onItemClick = { selectedTrack ->
            sharedViewModel.setSelectedMusicTrack(selectedTrack.id)
        }

        recyclerView.adapter = recyclerAdapter
    }
}