package com.example.dragonmaster.showmark

class ShowItem {

    var id: Int = 0
    var showName: String? = null
    var allEpisodes: Int = 0
    var currentEpisode: Int = 0

    constructor(id: Int, showName: String, currentEpisode: Int, allEpisodes: Int) {
        this.id = id
        this.showName = showName
        this.currentEpisode = currentEpisode
        this.allEpisodes = allEpisodes
    }

    constructor(showName: String, allEpisodes: Int) {
        this.showName = showName
        this.allEpisodes = allEpisodes
    }
}