package com.victorloveday.healthio.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.victorloveday.healthio.R
import com.victorloveday.healthio.database.models.Run
import com.victorloveday.healthio.databinding.ItemRunBinding
import com.victorloveday.healthio.ui.fragments.TrackingFragment
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    inner class RunViewHolder(val binding: ItemRunBinding) : RecyclerView.ViewHolder(binding.root)

    val diffCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(list: List<Run>) = differ.submitList(list)

    private lateinit var binding: ItemRunBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            ItemRunBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(run.img).into(binding.mapSnapshot)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.date
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            binding.date.text = dateFormat.format(calendar.time)

            val avgSpeed = "${run.averageSpeed}km/h"
            binding.averageSpeed.text = avgSpeed

            val distanceInKm = "${run.distanceCovered / 1000f}km"
            binding.totalDistance.text = distanceInKm

            binding.totalTime.text = TrackingFragment().getFormattedStopWatchTime (run.duration)

            val caloriesBurned = "${run.burntCalories}kcal"
            binding.burntCalories.text = caloriesBurned
        }
    }
}

