package com.example.giphygallery.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.giphygallery.databinding.ActivityGalleryBinding
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.MediaType
import com.giphy.sdk.core.models.enums.RatingType
import com.giphy.sdk.ui.Giphy
import com.giphy.sdk.ui.GiphyLoadingProvider
import com.giphy.sdk.ui.pagination.GPHContent
import com.giphy.sdk.ui.views.GPHGridCallback
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

private const val SEARCH_DELAY_MS = 1000L
private const val GIPHY_KEY = "xAIdMy9bQZAS1GH7RoKj4QR99RB65JoV"

class GalleryActivity: AppCompatActivity() {

    @OptIn(FlowPreview::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Giphy.configure(this, GIPHY_KEY)

        val binding = ActivityGalleryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val textChangesFlow = MutableStateFlow("")

        binding.searchText.doAfterTextChanged { text ->
            textChangesFlow.value = text.toString()
        }

        lifecycleScope.launch {
            textChangesFlow
                .debounce(SEARCH_DELAY_MS)
                .collectLatest { text ->
                    binding.grid.content = GPHContent.searchQuery(text, MediaType.gif, RatingType.pg13)
                }
        }

        binding.grid.setGiphyLoadingProvider(loadingProvider)
        binding.grid.callback = object: GPHGridCallback {
            override fun contentDidUpdate(resultCount: Int) {}

            override fun didSelectMedia(media: Media) {
                val intent = Intent(this@GalleryActivity, GifActivity::class.java)
                intent.putExtra("media_id", media.id)
                startActivity(intent);
            }
        }
    }

    private val loadingProvider = object : GiphyLoadingProvider {
        override fun getLoadingDrawable(position: Int): Drawable {
            return LoadingDrawable()
        }
    }
}