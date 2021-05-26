package ayds.apolo.songinfo.moredetails.model.repository.local.lastFM

import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.moredetails.model.entities.ArticleArtist

interface ArtistLocalStorage {

    //ver como busca para corregir los parametros
    //fun updateArtistTerm(query: String, songId: String)

    fun saveArtist(artist: String, info: String)

    fun getInfo(artist: String): String?

    fun getArticleByTerm(artistName: String): ArticleArtist
}