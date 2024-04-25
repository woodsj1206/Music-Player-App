package com.example.musicplayerapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerAdapter(private var trackList: ArrayList<MusicTrack>, private val context: Context) : RecyclerView.Adapter<RecyclerAdapter.MusicTrackHolder>(){

    var onItemClick : ((MusicTrack) -> Unit)? = null

    class MusicTrackHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView : ImageView = itemView.findViewById(R.id.imageView)
        val textView : TextView = itemView.findViewById(R.id.textView)
        val tvPosition : TextView = itemView.findViewById(R.id.tvPosition)
        val tvArtist : TextView = itemView.findViewById(R.id.tvArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicTrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return MusicTrackHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: MusicTrackHolder, position: Int) {
        val track = trackList[position]
        Glide.with(context).load(track.imageURL).into(holder.imageView)
        holder.textView.text = track.title
        holder.tvPosition.text = track.position.toString()
        holder.tvArtist.text = track.artist

        holder.itemView.setOnClickListener{
            onItemClick?.invoke(track)
        }
    }

    fun setFilteredList(filteredList: ArrayList<MusicTrack>){
        this.trackList = filteredList
        notifyDataSetChanged()
    }
}