package com.cbu.mobil_dersi_projesi.helper

import android.app.ProgressDialog
import android.content.Context

class LoadingDialog(private val context: Context) {
    private val progressDialog = ProgressDialog(context).apply {
        setMessage("Yükleniyor")
        setCancelable(false)
    }


    fun show() = progressDialog.show()

    fun hide() = progressDialog.dismiss()
}
