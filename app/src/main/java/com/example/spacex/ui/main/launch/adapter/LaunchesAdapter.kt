package com.example.spacex.ui.main.launch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.spacex.R
import com.example.spacex.ui.main.launch.view.LaunchListInteraction
import com.example.spacex.ui.main.launch.view.LaunchModel
import java.text.SimpleDateFormat
import java.util.*

class LaunchesAdapter(private val callback: LaunchListInteraction) : RecyclerView.Adapter<LaunchesAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<LaunchModel>() {
        override fun areItemsTheSame(oldItem: LaunchModel, newItem: LaunchModel): Boolean {
            return oldItem.launch.id == newItem.launch.id
        }

        override fun areContentsTheSame(oldItem: LaunchModel, newItem: LaunchModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    inner class ViewHolder(itemView: View, private val callback: LaunchListInteraction) : RecyclerView.ViewHolder(itemView) {

        private val previewImageView: ImageView = itemView.findViewById(R.id.previewImageView)
        private val favoriteImageView: ImageView = itemView.findViewById(R.id.favoriteImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.rocketNameTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        init {
            itemView.setOnClickListener { callback.onLaunchSelected(adapterPosition) }
            favoriteImageView.setOnClickListener{ callback.onLaunchFavoriteSelected(adapterPosition) }
        }

        fun bind(launchModel: LaunchModel){

            Glide
                .with(itemView.context)
                .load(launchModel.launch.links.patch.small)
                .centerCrop()
                .into(previewImageView)

            favoriteImageView.setImageResource(if (launchModel.isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off)
            nameTextView.text = launchModel.launch.name
            dateTextView.text = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(launchModel.launchDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_launch, parent, false), callback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(differ.currentList[position])

    override fun getItemCount(): Int = differ.currentList.count()

}