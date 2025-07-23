package fr.vitesse.android.module

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class CandidateActionComposerModule (private val context: Context) {
    fun call(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phoneNumber".toUri()
        }

        context.startActivity(intent)
    }

    fun sendSms(phoneNumber: String, message: String = "") {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = "sms:$phoneNumber".toUri()
            putExtra("sms_body", message)
        }

        context.startActivity(intent)
    }

    fun sendEmail(to: String, subject: String = "", body: String = "") {
        val uri = "mailto:$to".toUri()
        val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }

        context.startActivity(intent)
    }
}
