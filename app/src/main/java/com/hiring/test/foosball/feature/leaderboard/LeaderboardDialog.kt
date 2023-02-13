package com.hiring.test.foosball.feature.leaderboard

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.hiring.test.foosball.MainActivityViewModel
import com.hiring.test.foosball.base.BaseBottomSheetDialogFragment
import com.hiring.test.foosball.data.model.GameResult
import com.test.foosball.databinding.DialogLeaderboardBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

@AndroidEntryPoint
class LeaderboardDialog : BaseBottomSheetDialogFragment() {

    override val binding by lazy { DialogLeaderboardBinding.inflate(layoutInflater) }

    private val viewModel by activityViewModels<MainActivityViewModel>()

    private var leaderboardAdapter: LeaderboardAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.close.setOnClickListener {
            dismissWithAnimation()
        }
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rv.layoutManager = linearLayoutManager
        leaderboardAdapter = LeaderboardAdapter()
        binding.rv.adapter = leaderboardAdapter

        binding.switchView.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.requestTopPlayers(MainActivityViewModel.SortType.WINS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { Timber.e(it) }
                    .doOnSuccess {
                        if (it.isNullOrEmpty()) {
                            Toast.makeText(context, "No items yet", Toast.LENGTH_SHORT).show()
                        } else {
                            binding.leaderboardInfo.isVisible = true
                            leaderboardAdapter?.addPlayers(it)
                        }
                    }
                    .subscribe()
            } else {
                viewModel.requestTopPlayers(MainActivityViewModel.SortType.NUMBER_OF_GAMES)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError { Timber.e(it) }
                    .doOnSuccess {
                        if (it.isNullOrEmpty()) {
                            Toast.makeText(context, "No items yet", Toast.LENGTH_SHORT).show()
                        } else {
                            binding.leaderboardInfo.isVisible = true
                            leaderboardAdapter?.addPlayers(it)
                        }
                    }
                    .subscribe()
            }
        }

        binding.switchView.isChecked = true
    }

    companion object {
        const val TAG = "LeaderboardDialog"

        fun newInstance() = LeaderboardDialog()
    }
}