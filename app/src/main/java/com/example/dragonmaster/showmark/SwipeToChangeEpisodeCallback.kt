package com.example.dragonmaster.showmark

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MotionEvent
import android.view.View

enum class ButtonState {
    GONE,
    RIGHT_VISIBLE,
    LEFT_VISIBLE
}

abstract class SwipeToChangeEpisodeCallback(val context: Context) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val plusIcon = ContextCompat.getDrawable(context, R.drawable.ic_add)
    private val minusIcon = ContextCompat.getDrawable(context, R.drawable.ic_remove)
    private val intrinsicWidth = plusIcon!!.intrinsicWidth
    private val intrinsicHeight = plusIcon!!.intrinsicHeight
    private val background = ColorDrawable()
    private val backgroundColor = Color.parseColor("#f44336")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    private var swipeBack: Boolean = false
    private var buttonShowedState: ButtonState = ButtonState.GONE
    private val buttonWidth: Float = 300f

    override fun onMove(p0: RecyclerView, p1: RecyclerView.ViewHolder, p2: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top

        if (dX < 0) {
            background.color = ContextCompat.getColor(context, R.color.colorAccent)
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            background.draw(c)

            val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val iconMargin = (itemHeight - intrinsicHeight) / 2
            val iconLeft = itemView.right - iconMargin - intrinsicWidth
            val iconRight = itemView.right - iconMargin
            val iconBottom = iconTop + intrinsicHeight

            plusIcon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            plusIcon.draw(c)
        } else if (dX > 0) {
            background.color = ContextCompat.getColor(context, R.color.colorAccent)
            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
            background.draw(c)

            val iconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val iconMargin = (itemHeight - intrinsicHeight) / 2
            val iconLeft = itemView.left + iconMargin
            val iconRight = itemView.left + iconMargin + intrinsicWidth
            val iconBottom = iconTop + intrinsicHeight

            minusIcon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            minusIcon.draw(c)
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if (swipeBack) {
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    private fun setTouchListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        recyclerView.setOnTouchListener(View.OnTouchListener { v, event ->
            swipeBack = ((event.action == MotionEvent.ACTION_CANCEL) || (event.action == MotionEvent.ACTION_UP))
            if (swipeBack) {
                if (dX < -buttonWidth) {
                    buttonShowedState = ButtonState.RIGHT_VISIBLE
                } else if (dX > buttonWidth) {
                    buttonShowedState = ButtonState.LEFT_VISIBLE
                }
                if (buttonShowedState != ButtonState.GONE) {
                    if (buttonShowedState == ButtonState.LEFT_VISIBLE) {
                        minusEpisode(viewHolder)
                    }
                    if (buttonShowedState == ButtonState.RIGHT_VISIBLE) {
                        plusEpisode(viewHolder)
                    }
                }
            }
            return@OnTouchListener false
        })
    }

    abstract fun minusEpisode(viewHolder: RecyclerView.ViewHolder)

    abstract fun plusEpisode(viewHolder: RecyclerView.ViewHolder)

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}