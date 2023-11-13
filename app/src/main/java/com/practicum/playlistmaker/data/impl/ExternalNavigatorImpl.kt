package com.practicum.playlistmaker.data.impl

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.ExternalNavigator
import com.practicum.playlistmaker.domain.media.model.Playlist
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.sharing.model.EmailData
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity
import java.text.SimpleDateFormat
import java.util.*

class ExternalNavigatorImpl(
    private val context: Context
): ExternalNavigator {

    private val shareLink = "https://practicum.yandex.ru/android-developer/"
    private val emailText = context.getString(R.string.email_text)
    private val emailSubject = context.getString(R.string.email_subject)
    private val email = context.getString(R.string.email)
    private val termsOfUseLink = "https://yandex.ru/legal/practicum_offer/"

    private val emailData = EmailData(email = email)

    override fun shareLink() {
        context.startActivity(
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareLink)
                flags = FLAG_ACTIVITY_NEW_TASK
            })
    }



    override fun openLink() {
        val link = Uri.parse(termsOfUseLink)
        context.startActivity(
            Intent(Intent.ACTION_VIEW, link).apply{
                flags = FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    override fun openEmail() {
        context.startActivity(
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
        context.startActivity(
            Intent(context, PlayerActivity::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
            }
        )
    }

    override fun sharePlaylist(playlist: Playlist, tracks: List<Track>) {
        var text = playlist.name + "\n" + playlist.desc + "\n" + context.resources.getQuantityString(R.plurals.track_count, tracks.size, tracks.size) + "\n"
        for(i in 0 until tracks.size){
            val trackPlaytime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(tracks[i].trackTimeMillis.toInt())
            text += "${i + 1}. ${tracks[i].artistName} - ${tracks[i].trackName} (${trackPlaytime})\n"
        }
        context.startActivity(
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            flags = FLAG_ACTIVITY_NEW_TASK
        })
    }
}