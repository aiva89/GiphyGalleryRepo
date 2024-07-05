package com.example.giphygallery.ui

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.giphygallery.databinding.ActivityGifBinding
import com.giphy.sdk.core.GPHCore
import com.giphy.sdk.core.models.enums.RenditionType
import com.giphy.sdk.ui.views.GPHMediaView

class GifActivity: AppCompatActivity() {

    private lateinit var mediaId: String
    private lateinit var gifView: GPHMediaView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityGifBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        gifView = binding.gifView

        mediaId = intent.getStringExtra("media_id").toString()

        getGifById(mediaId)
    }

    private fun getGifById (id: String) {
        GPHCore.gifById(id) { result, e ->
            result?.let {
                gifView.setMedia(result.data, RenditionType.original)
            } ?: showErrorDialog(e?.message.toString())
        }
    }

    private fun showErrorDialog (error: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("$error\n\n Please try again.")
        builder.setNeutralButton("Back") { dialog, _ ->
            dialog.dismiss()
            finish()
        }
        builder.setPositiveButton("Retry") { dialog, _ ->
            getGifById(mediaId)
            dialog.dismiss()
        }
        builder.show()
    }
}