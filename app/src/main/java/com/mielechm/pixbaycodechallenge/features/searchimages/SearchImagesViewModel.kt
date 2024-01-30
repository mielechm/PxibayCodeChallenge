package com.mielechm.pixbaycodechallenge.features.searchimages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mielechm.pixbaycodechallenge.data.entities.Image
import com.mielechm.pixbaycodechallenge.data.model.ImageListItem
import com.mielechm.pixbaycodechallenge.repositories.DefaultImagesRepository
import com.mielechm.pixbaycodechallenge.utils.DEFAULT_PAGE_SIZE
import com.mielechm.pixbaycodechallenge.utils.Resource
import com.mielechm.pixbaycodechallenge.utils.toImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchImagesViewModel @Inject constructor(private val repository: DefaultImagesRepository) :
    ViewModel() {

    private val _images = MutableStateFlow<List<ImageListItem>>(emptyList())
    val images = _images.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loadError = MutableStateFlow("")
    val loadError = _loadError.asStateFlow()

    private val _endReached = MutableStateFlow(false)
    val endReached = _endReached.asStateFlow()

    private val _cachedImages = MutableStateFlow<List<ImageListItem>>(emptyList())
    val cachedImages = _cachedImages.asStateFlow()

    private val _cachedImage = MutableStateFlow<Image>(Image())
    val cachedImage = _cachedImage.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchResults = MutableStateFlow<List<ImageListItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private var currentPage = 1
    private val perPage = DEFAULT_PAGE_SIZE
    private var availableImages = 0

    init {
        currentPage = 1
        getAllImagesFromDb()
        searchImagesPaginated()
    }

    fun searchImagesPaginated(query: String = "fruits") {
            viewModelScope.launch {
                _isLoading.value = true
                when(val result = repository.searchImagesPaginated(query, currentPage, perPage)) {
                    is Resource.Error -> {
                        _isLoading.value = false
                        _loadError.value = result.message.toString()
                    }
                    is Resource.Success -> {
                        availableImages = result.data!!.totalHits
                        val imageItems = result.data.hits.map {
                            ImageListItem(
                                id = it.id,
                                previewUrl = it.previewURL,
                                user = it.user,
                                tags = it.tags,
                                largeImageUrl = it.largeImageURL,
                                likes = it.likes,
                                downloads = it.downloads,
                                comments = it.comments
                            )
                        }
                        imageItems.forEach {
                            insertImageToDb(it)
                        }
                        if (_images.value.size < availableImages) {
                            currentPage++
                        } else {
                            _endReached.value = true
                        }

                        _isLoading.value = false
                        _loadError.value = ""
                        _images.value += imageItems
                    }
                }
            }
    }

    fun insertImageToDb(imageListItem: ImageListItem) {
        viewModelScope.launch {
            val imageToAdd = imageListItem.toImage()
            repository.insertImage(imageToAdd)
        }
    }

    fun getAllImagesFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            repository.getAllImages().collect {
                _cachedImages.value = it.map {image ->
                    ImageListItem(
                        id = image.id,
                        previewUrl = image.previewUrl,
                        user = image.user,
                        tags = image.tags,
                        largeImageUrl = image.largeImageUrl,
                        likes = image.likes,
                        downloads = image.downloads,
                        comments = image.comments
                    )
                }
            }
        }
    }

}