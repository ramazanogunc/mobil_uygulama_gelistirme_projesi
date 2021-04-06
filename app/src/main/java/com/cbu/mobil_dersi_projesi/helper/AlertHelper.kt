package com.cbu.mobil_dersi_projesi.helper

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cbu.mobil_dersi_projesi.model.Mekan


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Fragment.toast(message: String) = requireContext().toast(message)

fun Context.alertConfirm(message: String, confirm: () -> Unit) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Emin misiniz")
    builder.setMessage(message)
    builder.setPositiveButton(
        "Evet"
    ) { dialog, _ ->
        confirm()
        dialog.dismiss()
    }

    builder.setNegativeButton("İptal") { dialog, _ -> dialog.dismiss() }

    val alert: AlertDialog = builder.create()
    alert.show()
}

fun mekanInfoDialog(context: Context, mekan: Mekan) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Mekan Adı: " + mekan.name)
    builder.setMessage("Açıklama: " + mekan.description + "\n\nkonum bilgileri" + mekan.latitude + mekan.longitude)

    builder.setNegativeButton("Kapat") { dialog, _ -> dialog.dismiss() }

    val alert: AlertDialog = builder.create()
    alert.show()
}