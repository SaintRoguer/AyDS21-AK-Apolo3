package ayds.apolo.songinfo.home.view

import ayds.apolo.songinfo.home.controller.HomeControllerModule
import ayds.apolo.songinfo.home.model.HomeModelModule

object HomeViewModule {
    private val SongToReleaseDateFactory : SongToReleaseDateFactory = SongToReleaseDateFactoryImpl()
    val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl(SongToReleaseDateFactory)

    fun init(homeView: HomeView) {
        HomeModelModule.initHomeModel(homeView)
        HomeControllerModule.onViewStarted(homeView)
    }
}