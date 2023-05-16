package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var imgShare: ImageView
    private lateinit var imgSupport: ImageView
    private lateinit var imgTermsOfUse: ImageView
    private lateinit var switchTheme: com.google.android.material.switchmaterial.SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar  = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        imgShare = findViewById<ImageView>(R.id.img_share)
        imgSupport = findViewById<ImageView>(R.id.img_support)
        imgTermsOfUse = findViewById<ImageView>(R.id.img_terms_of_use)
        switchTheme = findViewById<com.google.android.material.switchmaterial.SwitchMaterial>(R.id.switch_theme)

        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        switchTheme.isChecked = sharedPrefs.getBoolean(THEME_SWITCHER_KEY, false)

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


        switchTheme.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)

            val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
            sharedPrefs.edit()
                .putBoolean(THEME_SWITCHER_KEY, checked)
                .apply()
        }

    }
}