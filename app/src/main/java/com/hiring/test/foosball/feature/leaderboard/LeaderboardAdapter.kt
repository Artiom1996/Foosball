package com.hiring.test.foosball.feature.leaderboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hiring.test.foosball.data.model.GameResult
import com.hiring.test.foosball.data.model.Player
import com.test.foosball.databinding.ItemLeaderboardBinding

class LeaderboardAdapter :
    RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    private val players: MutableList<Player> = mutableListOf()

    fun addPlayers(newPlayers: List<Player>) {
        players.clear()
        players.addAll(newPlayers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        return LeaderboardViewHolder(
            ItemLeaderboardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        holder.bind(players[position])
    }

    inner class LeaderboardViewHolder(
        private val binding: ItemLeaderboardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {
            binding.playerName.text = player.userName
            binding.games.text = player.games.toString()
            binding.wins.text = player.wins.toString()
        }
    }

    override fun getItemCount(): Int = players.size
}