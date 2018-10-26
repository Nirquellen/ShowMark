package com.example.dragonmaster.showmark

class ShowItem {

    companion object Factory {
        fun create(): ShowItem = ShowItem()
    }

    var showName: String? = null
    var allEpisodes: Int = 0
    var currentEpisode: Int = 0

}