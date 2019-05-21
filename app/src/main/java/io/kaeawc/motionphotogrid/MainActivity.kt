package io.kaeawc.motionphotogrid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.TOP
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.START
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.END
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        photo_1?.setOnClickListener { toggleZoom(it) }
        photo_2?.setOnClickListener { toggleZoom(it) }
        photo_3?.setOnClickListener { toggleZoom(it) }
        photo_4?.setOnClickListener { toggleZoom(it) }
        photo_5?.setOnClickListener { toggleZoom(it) }
        photo_6?.setOnClickListener { toggleZoom(it) }
        photo_7?.setOnClickListener { toggleZoom(it) }
        photo_8?.setOnClickListener { toggleZoom(it) }
        photo_9?.setOnClickListener { toggleZoom(it) }
    }

    private fun toggleZoom(view: View) {
        if (motion_scene?.currentState == R.id.zoomed_in) {
            backToGrid()
        } else {
            prepareToZoom(view)
            motion_scene?.transitionToEnd()
        }
    }

    private fun backToGrid() {
        motion_scene?.transitionToStart()
        motion_scene?.rebuildScene()
    }

    private fun prepareToZoom(view: View) {

        val cs = motion_scene?.getConstraintSet(R.id.zoomed_in) ?: return

        setOf(R.id.photo_1,
                R.id.photo_2,
                R.id.photo_3,
                R.id.photo_4,
                R.id.photo_5,
                R.id.photo_6,
                R.id.photo_7,
                R.id.photo_8,
                R.id.photo_9).forEach { resetZoomedView(cs, it) }

        cs.constrainWidth(view.id, boundaries.width)
        cs.constrainHeight(view.id, boundaries.height)

        cs.connect(view.id, TOP, R.id.boundaries, TOP, 0)
        cs.connect(view.id, START, R.id.boundaries, START, 0)
        cs.connect(view.id, END, R.id.boundaries, END, 0)
        cs.connect(view.id, BOTTOM, R.id.boundaries, BOTTOM, 0)

        cs.setElevation(view.id, 1f)

        motion_scene?.updateState(R.id.zoomed_in, cs)
    }

    private fun resetZoomedView(cs: ConstraintSet, viewId: Int?) {

        if (viewId == null) return // No need to do anything

        val screenWidth = resources.displayMetrics.widthPixels
        val gridItemSize = screenWidth / 3

        cs.constrainWidth(viewId, gridItemSize)
        cs.constrainHeight(viewId, gridItemSize)

        when (viewId) {
            R.id.photo_1,
            R.id.photo_2,
            R.id.photo_3 -> {
                cs.connect(viewId, TOP, R.id.boundaries, TOP, 0)
                cs.connect(viewId, BOTTOM, R.id.horizontal_middle, TOP, 0)
            }
            R.id.photo_4,
            R.id.photo_5,
            R.id.photo_6 -> {
                cs.connect(viewId, TOP, R.id.horizontal_middle, TOP, 0)
                cs.connect(viewId, BOTTOM, R.id.horizontal_middle, BOTTOM, 0)
            }
            R.id.photo_7,
            R.id.photo_8,
            R.id.photo_9 -> {
                cs.connect(viewId, TOP, R.id.horizontal_middle, BOTTOM, 0)
                cs.connect(viewId, BOTTOM, R.id.boundaries, BOTTOM, 0)
            }
            else -> return
        }

        when (viewId) {
            R.id.photo_1,
            R.id.photo_4,
            R.id.photo_7 -> {
                cs.connect(viewId, START, R.id.boundaries, START, 0)
                cs.connect(viewId, END, R.id.vertical_middle, START, 0)
            }
            R.id.photo_2,
            R.id.photo_5,
            R.id.photo_8 -> {
                cs.connect(viewId, START, R.id.vertical_middle, START, 0)
                cs.connect(viewId, END, R.id.vertical_middle, END, 0)
            }
            R.id.photo_3,
            R.id.photo_6,
            R.id.photo_9 -> {
                cs.connect(viewId, START, R.id.vertical_middle, END, 0)
                cs.connect(viewId, END, R.id.boundaries, END, 0)
            }
            else -> return
        }

        cs.setElevation(viewId, 0f)
    }
}
