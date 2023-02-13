package com.hiring.test.foosball.feature.add.results

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.hiring.test.foosball.MainActivityViewModel
import com.hiring.test.foosball.base.BaseBottomSheetDialogFragment
import com.hiring.test.foosball.data.model.GameResult
import com.hiring.test.foosball.data.model.UserGameData
import com.test.foosball.databinding.DialogAddResultsBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber

@AndroidEntryPoint
class AddResultsDialog : BaseBottomSheetDialogFragment() {

    override val binding by lazy { DialogAddResultsBinding.inflate(layoutInflater) }

    private val viewModel by activityViewModels<MainActivityViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.close.setOnClickListener {
            dismissWithAnimation()
        }
        binding.saveResults.setOnClickListener {
            if (checkIsValidInfo()) {
                viewModel.addGameResults(
                    GameResult(
                        firstUserGameData = UserGameData(
                            userName = binding.firstPlayerName.text.toString(),
                            userScore = binding.firstPlayerScore.text.toString().toInt()
                        ),
                        secondUserGameData = UserGameData(
                            userName = binding.secondPlayerName.text.toString(),
                            userScore = binding.secondPlayerScore.text.toString().toInt()
                        )
                    )
                ).observeOn(AndroidSchedulers.mainThread()).subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        Timber.d("onSubscribe: $d")
                    }

                    override fun onComplete() {
                        Timber.d("onComplete: results added")
                        Toast.makeText(context, "Results saved!", Toast.LENGTH_SHORT).show()
                        dismissWithAnimation()
                    }

                    override fun onError(e: Throwable) {
                        Timber.e("onError: $e")
                        Toast.makeText(
                            context,
                            "Error happened while saving results",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                Toast.makeText(context, "Please fill all the required fields", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    private fun checkIsValidInfo(): Boolean {
        return binding.firstPlayerName.text.isNotEmpty() && binding.secondPlayerName.text.isNotEmpty()
                && binding.firstPlayerScore.text.isNotEmpty() && binding.secondPlayerScore.text.isNotEmpty()
    }

    companion object {
        const val TAG = "AddResultsDialog"

        fun newInstance() = AddResultsDialog()
    }
}