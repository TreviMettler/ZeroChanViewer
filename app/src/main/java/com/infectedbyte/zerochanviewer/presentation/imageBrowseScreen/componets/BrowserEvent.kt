package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets

sealed class BrowserEvent {
    data object Refresh: BrowserEvent()
    data class SearchQueryChange(val query: String): BrowserEvent()
    data class Search(val query: String): BrowserEvent()
}