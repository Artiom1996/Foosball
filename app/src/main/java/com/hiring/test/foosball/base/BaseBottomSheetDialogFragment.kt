package com.hiring.test.foosball.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.foosball.R
import timber.log.Timber

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    abstract val binding: ViewBinding

    private var baseBehavior: BottomSheetBehavior<FrameLayout>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = binding.root

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = object : BottomSheetDialog(requireContext(), R.style.Theme_BottomSheetDialog) {
            override fun onBackPressed() {
                this@BaseBottomSheetDialogFragment.onBackPressed()
            }
        }

        dialog.window?.setBackgroundDrawableResource(R.color.shark_95)
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val bottomSheet =
                d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            bottomSheet?.requestLayout()

            BottomSheetBehavior.from(bottomSheet!!).let { bottomSheetBehavior ->
                baseBehavior = bottomSheetBehavior
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
                bottomSheetBehavior.isDraggable = false
            }
        }
        return dialog
    }

    fun dismissWithAnimation() {
        if (baseBehavior != null) {
            baseBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            dismissAllowingStateLoss()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    open fun init() {
        Timber.d("default initUI")
    }

    open fun onBackPressed() {
        dismissAllowingStateLoss()
    }
}