package com.mielechm.pixbaycodechallenge.features.searchimages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mielechm.pixbaycodechallenge.data.model.ImageListItem
import com.mielechm.pixbaycodechallenge.repositories.DefaultImagesRepository
import com.mielechm.pixbaycodechallenge.utils.DEFAULT_PAGE_SIZE
import com.mielechm.pixbaycodechallenge.utils.Resource
import com.mielechm.pixbaycodechallenge.utils.toImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchImagesViewModel @Inject constructor(private val repository: DefaultImagesRepository) :
    ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loadError = MutableStateFlow("")
    val loadError = _loadError.asStateFlow()

    private val _endReached = MutableStateFlow(false)
    val endReached = _endReached.asStateFlow()

    private val _cachedImages = MutableStateFlow<List<ImageListItem>>(emptyList())
    val cachedImages = _cachedImages.asStateFlow()

    private val _isSearching = MutableStateFlow(false)

    private val _searchResults = MutableStateFlow<List<ImageListItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    val images = searchText.debounce(500).onEach { _isSearching.update { true } }
        .combine(_cachedImages) { text, cached ->
            if (text.isBlank()) {
                cached
            } else {
                searchImagesPaginated(text)
                cached.filter {
                    it.tags.contains(text, ignoreCase = true) || it.user.contains(
                        text,
                        ignoreCase = true
                    )
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _cachedImages.value)

    private var currentPage = 1
    private val perPage = DEFAULT_PAGE_SIZE
    private var availableImages = 0

    init {
        _searchText.value = "fruits"
        currentPage = 1
        getAllImagesFromDb()
        searchImagesPaginated()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun searchImagesPaginated(query: String = "fruits") {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = repository.searchImagesPaginated(query, currentPage, perPage)) {
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
                    if (_searchResults.value.size < availableImages) {
                        currentPage++
                    } else {
                        _endReached.value = true
                    }

                    _isLoading.value = false
                    _loadError.value = ""
                    _searchResults.value += imageItems
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
                _cachedImages.value = it.map { image ->
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
                _isLoading.value = false
            }
        }
    }

}