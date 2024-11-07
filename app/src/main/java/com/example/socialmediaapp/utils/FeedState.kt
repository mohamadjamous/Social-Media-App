package com.example.socialmediaapp.utils

sealed class FeedState {
    data object Loading : FeedState()
    data object Published : FeedState()
    data object DoneProcessing : FeedState()
    data class Error(val message: String) : FeedState()
}