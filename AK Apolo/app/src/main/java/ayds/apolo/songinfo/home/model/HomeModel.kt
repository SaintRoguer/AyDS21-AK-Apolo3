package ayds.apolo.songinfo.home.model

import android.util.Log
import ayds.apolo.songinfo.home.model.entities.Song
import ayds.apolo.songinfo.home.model.repository.SongRepository
import ayds.observer.Observable
import ayds.observer.Subject

interface HomeModel {

    fun searchSong(term: String)

    fun songObservable(): Observable<Song>

    fun getSongById(id: String): Song
}

internal class HomeModelImpl(private val repository: SongRepository) : HomeModel {

    private val songSubject = Subject<Song>()

    override fun searchSong(term: String) {
        repository.getSongByTerm(term).let {
            Log.e("Spotify Song", "model notify $it")
            songSubject.notify(it)
        }
    }

    override fun songObservable(): Observable<Song> = songSubject

    override fun getSongById(id: String): Song = repository.getSongById(id)
}