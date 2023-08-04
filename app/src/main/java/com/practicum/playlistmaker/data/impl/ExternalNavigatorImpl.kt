package com.practicum.playlistmaker.data.impl

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.ExternalNavigator
import com.practicum.playlistmaker.domain.sharing.model.EmailData
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity

class ExternalNavigatorImpl(
    private val application: Application
): ExternalNavigator {

    private val shareLink = "https://practicum.yandex.ru/android-developer/"
    private val emailText = application.getString(R.string.email_text)
    private val emailSubject = application.getString(R.string.email_subject)
    private val email = application.getString(R.string.email)
    private val termsOfUseLink = "https://yandex.ru/legal/practicum_offer/"

    private val emailData = EmailData(email = email)

    override fun shareLink() {
        application.startActivity(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareLink)
                flags = FLAG_ACTIVITY_NEW_TASK
            })
    }

    override fun openLink() {
        val link = Uri.parse(termsOfUseLink)
        application.startActivity(
            Intent(Intent.ACTION_VIEW, link).apply{
                flags = FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    override fun openEmail() {
        application.startActivity(
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(emailData.mailto)
                putExtra(Intent.EXTRA_EMAIL, emailData.email)
                putExtra(Intent.EXTRA_SUBJECT, emailSubject)
                putExtra(Intent.EXTRA_TEXT, emailText)
                flags = FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    override fun openPlayer() {
        application.startActivity(
            Intent(application, PlayerActivity::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
            }
        )
    }


}