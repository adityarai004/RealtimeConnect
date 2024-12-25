package com.example.realtimeconnect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeconnect.core.datastore.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataStoreHelper: DataStoreHelper): ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoggedIn.update {
                dataStoreHelper.getIsLoggedIn();
            }
            _isLoading.update { false }
        }
    }
}