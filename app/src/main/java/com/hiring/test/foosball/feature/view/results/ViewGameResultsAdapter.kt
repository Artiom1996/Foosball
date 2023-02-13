package com.hiring.test.foosball.feature.view.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hiring.test.foosball.data.model.GameResult
import com.test.foosball.databinding.ItemRemoveResultBinding

class ViewGameResultsAdapter : RecyclerView.Adapter<ViewGameResultsAdapter.ViewGameResultsViewHolder>() {

    private val results: MutableList<GameResult> = mutableListOf()

    var removeClickListener: RemoveClickListener? = null

    fun addGameResults(gameResults: MutableList<GameResult>) {
        results.clear()
        results.addAll(gameResults)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        results.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewGameResultsViewHolder {
        return ViewGameResultsViewHolder(
            ItemRemoveResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewGameResultsViewHolder, position: Int) {
        holder.bind(results[position], object : RemoveClickListener {
            override fun onRemoveClick(position: Int) {
                removeClickListener?.onRemoveClick(position)
            }
        })
    }

    inner class ViewGameResultsViewHolder(
        private val binding: ItemRemoveResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gameResult: GameResult, removeClickListener: RemoveClickListener) {
            binding.firstPlayerName.text = gameResult.firstUserGameData.userName
            binding.firstPlayerScore.text = gameResult.firstUserGameData.userScore.toString()
            binding.secondPlayerName.text = gameResult.secondUserGameData.userName
            binding.secondPlayerScore.text = gameResult.secondUserGameData.userScore.toString()
            binding.remove.setOnClickListener {
                removeClickListener.onRemoveClick(absoluteAdapterPosition)
            }
        }
    }

    override fun getItemCount(): Int = results.size

    interface RemoveClickListener {
        fun onRemoveClick(position: Int)
    }

}