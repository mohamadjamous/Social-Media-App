package com.example.socialmediaapp.utils

sealed class LoadState {
    data object LOADING : LoadState()
    data object SUCCESS : LoadState()
    data object ERROR : LoadState()
}
