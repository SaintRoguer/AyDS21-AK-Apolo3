package ayds.apolo.songinfo.moredetails.model.repository.local.lastFM

import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.moredetails.model.entities.ArticleArtist

interface ArtistLocalStorage {

    //ver como busca para corregir los parametros
    //fun updateArtistTerm(query: String, songId: String)

    fun saveArtist(artist: String, info: String)

    fun getInfo(artist: String): String?

    fun getArticleByArtistName(artistName: String): ArticleArtist

    //No se si es info lo que guardamos o sea que tipo es el artistArticle de ArticleRepository
    fun updateArtist(artistName: String,info: String )
    //Esta tampoco se
    fun insertArtist(artistName: String,info: String)



}