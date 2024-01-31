package com.mielechm.pixbaycodechallenge.features.imagedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mielechm.pixbaycodechallenge.data.entities.Image
import com.mielechm.pixbaycodechallenge.repositories.ImagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageDetailsViewModel @Inject constructor(private val repository: ImagesRepository) :
    ViewModel() {

    private val _imageDetails = MutableStateFlow(Image())
    val imageDetails = _imageDetails.asStateFlow()

    fun getImageDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getImageById(id).collect {
                _imageDetails.value = it
            }
        }
    }

}