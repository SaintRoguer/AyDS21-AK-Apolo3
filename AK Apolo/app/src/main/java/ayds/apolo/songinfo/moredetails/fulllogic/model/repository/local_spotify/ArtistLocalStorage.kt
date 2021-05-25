package ayds.apolo.songinfo.moredetails.fulllogic.model.repository.local_spotify

import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.moredetails.fulllogic.model.entities.SpotifyArtist

interface ArtistLocalStorage {

    //ver como busca para corregir los parametros
    //fun updateArtistTerm(query: String, songId: String)

    fun insertArtist(query: String, artistName: SpotifyArtist)

    fun getArtistByTerm(term: String): SpotifyArtist?

    fun getArtistByName(artistName: String): SpotifyArtist?
}