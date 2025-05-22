package com.example.artinstituteapp.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.artinstituteapp.R
import com.example.artinstituteapp.model.Artwork
//import com.example.artinstituteapp.util.ImageLoadingExt.loadImage
import com.example.artinstituteapp.util.loadImage
import com.example.artinstituteapp.util.orEmpty

/**
 * Adapter for displaying artwork items in a RecyclerView.
 */
class ArtworkAdapter(
    private val onItemClick: (Artwork) -> Unit
) : ListAdapter<Artwork, ArtworkAdapter.ArtworkViewHolder>(ArtworkDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtworkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artwork, parent, false)
        return ArtworkViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ArtworkViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /**
     * ViewHolder for artwork items.
     */
    class ArtworkViewHolder(
        itemView: View,
        private val onItemClick: (Artwork) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val imageView: ImageView = itemView.findViewById(R.id.artworkImage)
        private val titleText: TextView = itemView.findViewById(R.id.artworkTitle)
        private val artistText: TextView = itemView.findViewById(R.id.artworkArtist)
        private val dateText: TextView = itemView.findViewById(R.id.artworkDate)

        fun bind(artwork: Artwork) {
            // Load artwork image with placeholder and error handling
            imageView.loadImage(artwork.getThumbnailUrl()) {}

            // Set text fields
            titleText.text = artwork.title
            artistText.text = artwork.artistDisplay
            dateText.text = artwork.dateDisplay.orEmpty()

            // Set click listener
            itemView.setOnClickListener { onItemClick(artwork) }
        }
    }

    /**
     * DiffUtil callback for efficient RecyclerView updates.
     */
    private class ArtworkDiffCallback : DiffUtil.ItemCallback<Artwork>() {
        override fun areItemsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Artwork, newItem: Artwork): Boolean {
            return oldItem == newItem
        }
    }
}
