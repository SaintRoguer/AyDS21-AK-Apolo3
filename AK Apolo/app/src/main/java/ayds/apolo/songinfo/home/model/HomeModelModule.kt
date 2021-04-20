package ayds.apolo.songinfo.home.model

import android.content.Context
import ayds.apolo.songinfo.home.model.repository.SongRepository
import ayds.apolo.songinfo.home.model.repository.SongRepositoryImpl
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyModule
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.apolo.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.CursorToSpotifySongMapperImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.SpotifyLocalStorageImpl
import ayds.apolo.songinfo.home.view.HomeView

object HomeModelModule {

    private lateinit var homeModel: HomeModel

    fun getHomeModel(): HomeModel = homeModel

    fun initHomeModel(homeView: HomeView) {
        val spotifyLocalStorage: SpotifyLocalStorage = SpotifyLocalStorageImpl(
          homeView as Context, CursorToSpotifySongMapperImpl()
        )
        val spotifyTrackService: SpotifyTrackService = SpotifyModule.spotifyTrackService

        val repository: SongRepository =
            SongRepositoryImpl(spotifyLocalStorage, spotifyTrackService)

        homeModel = HomeModelImpl(repository)
    }
}