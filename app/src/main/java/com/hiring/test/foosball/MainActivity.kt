package com.hiring.test.foosball

import android.os.Bundle
import com.hiring.test.foosball.base.BaseActivity
import com.hiring.test.foosball.feature.add.results.AddResultsDialog
import com.hiring.test.foosball.feature.view.results.ViewGameResultsDialog
import com.hiring.test.foosball.feature.leaderboard.LeaderboardDialog
import com.test.foosball.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        binding.addGameResults.setOnClickListener {
            AddResultsDialog.newInstance().show(supportFragmentManager, AddResultsDialog.TAG)
        }
        binding.viewGameResults.setOnClickListener {
            ViewGameResultsDialog.newInstance().show(supportFragmentManager, ViewGameResultsDialog.TAG)
        }
        binding.viewLeaderboard.setOnClickListener {
            LeaderboardDialog.newInstance().show(supportFragmentManager, LeaderboardDialog.TAG)
        }
    }
}