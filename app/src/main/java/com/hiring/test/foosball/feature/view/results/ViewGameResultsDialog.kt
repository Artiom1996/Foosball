package com.hiring.test.foosball.feature.view.results

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hiring.test.foosball.MainActivityViewModel
import com.hiring.test.foosball.base.BaseBottomSheetDialogFragment
import com.test.foosball.databinding.DialogViewGameResultsBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

@AndroidEntryPoint
class ViewGameResultsDialog : BaseBottomSheetDialogFragment() {

    override val binding by lazy { DialogViewGameResultsBinding.inflate(layoutInflater) }

    private val viewModel by activityViewModels<MainActivityViewModel>()

    private var viewGameResultsAdapter: ViewGameResultsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.close.setOnClickListener {
            dismissWithAnimation()
        }
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rv.layoutManager = linearLayoutManager
        viewGameResultsAdapter = ViewGameResultsAdapter()
        viewGameResultsAdapter?.removeClickListener =
            object : ViewGameResultsAdapter.RemoveClickListener {
                override fun onRemoveClick(position: Int) {
                    viewModel.removeGameResults(position)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                            Timber.e(it)
                        }
                        .doOnComplete {
                            viewGameResultsAdapter?.removeItem(position)
                            if (viewGameResultsAdapter?.itemCount == 0) {
                                binding.removeDetailsInfo.isVisible = false
                            }
                        }
                        .subscribe()
                }
            }
        binding.rv.adapter = viewGameResultsAdapter

        viewModel.requestGameResults().observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                Timber.e(it)
            }.doOnSuccess {
                if (it.isNullOrEmpty()) {
                    Toast.makeText(context, "No game results yet", Toast.LENGTH_SHORT).show()
                } else {
                    if (it.size > 0) {
                        binding.removeDetailsInfo.isVisible = true
                    }
                    viewGameResultsAdapter?.addGameResults(it)

                }
            }.subscribe()
    }

    companion object {
        const val TAG = "ViewGameResultsDialog"

        fun newInstance() = ViewGameResultsDialog()
    }

}