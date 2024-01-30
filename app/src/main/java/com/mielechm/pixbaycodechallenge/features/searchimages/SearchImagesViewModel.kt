package com.mielechm.pixbaycodechallenge.features.searchimages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mielechm.pixbaycodechallenge.data.model.ImageListItem
import com.mielechm.pixbaycodechallenge.repositories.DefaultImagesRepository
import com.mielechm.pixbaycodechallenge.utils.DEFAULT_PAGE_SIZE
import com.mielechm.pixbaycodechallenge.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private var currentPage = 1
    private val perPage = DEFAULT_PAGE_SIZE
    private var availableImages = 0

    init {
        currentPage = 1
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

}