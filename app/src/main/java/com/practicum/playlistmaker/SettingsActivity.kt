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
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.course_link))
            shareIntent.type = getString(R.string.share_type)
            startActivity(shareIntent)
        }

        imgSupport.setOnClickListener {
            val mailIntent = Intent(Intent.ACTION_SENDTO)
            mailIntent.data = Uri.parse("mailto:")
            mailIntent.putExtra(Intent.EXTRA_EMAIL,arrayOf(getString(R.string.email)))
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
            mailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            startActivity(mailIntent)
        }

        imgTermsOfUse.setOnClickListener {
            val link: Uri = Uri.parse(getString(R.string.terms_of_use_link))
            val linkIntent = Intent(Intent.ACTION_VIEW, link)
            startActivity(linkIntent)
        }

    }
}