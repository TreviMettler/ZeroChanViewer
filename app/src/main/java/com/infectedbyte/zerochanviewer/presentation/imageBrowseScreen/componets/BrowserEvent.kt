package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets

sealed class BrowserEvent {
    data object Refresh: BrowserEvent()
    data class SearchQueryChange(val query: String): BrowserEvent()
    data class Search(val query: String): BrowserEvent()
    data class SortRecently(val sortType: String): BrowserEvent()
    data class SortDimensions(val sortType: String): BrowserEvent()
    data class SortColor(val sortType: String): BrowserEvent()
}