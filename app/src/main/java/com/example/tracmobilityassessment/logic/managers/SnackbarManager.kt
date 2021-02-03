package com.example.tracmobilityassessment.logic.managers

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.tracmobilityassessment.R
import com.example.tracmobilityassessment.logic.enums.SnackBarLength
import com.google.android.material.snackbar.Snackbar

class SnackbarManager(private val context: Context, private val view: View) {

    fun showSnackBar(message: String, length: SnackBarLength) {
        val snackbar: Snackbar
        if (length == SnackBarLength.INDEFINITE) {
            snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction(context.getString(R.string.snackbar_actiontext)) { snackbar.dismiss() }
            snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.app_primary_color))
            snackbar.duration = 60000
        } else snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }
}