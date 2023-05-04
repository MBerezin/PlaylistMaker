package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar  = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val imgShare = findViewById<ImageView>(R.id.img_share)
        val imgSupport = findViewById<ImageView>(R.id.img_support)
        val imgTermsOfUse = findViewById<ImageView>(R.id.img_terms_of_use)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        imgShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
            shareIntent.type = "text/plain"
            startActivity(shareIntent)
        }

        imgSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL,arrayOf(getString(R.string.email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
                startActivity(this)
            }

        }

        imgTermsOfUse.setOnClickListener {
            val link: Uri = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            val linkIntent = Intent(Intent.ACTION_VIEW, link)
            startActivity(linkIntent)
        }

    }
}