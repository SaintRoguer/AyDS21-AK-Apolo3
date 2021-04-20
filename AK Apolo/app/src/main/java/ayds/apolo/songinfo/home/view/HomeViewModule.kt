package ayds.apolo.songinfo.home.view

import ayds.apolo.songinfo.home.controller.HomeControllerModule
import ayds.apolo.songinfo.home.model.HomeModelModule

object HomeViewModule {

    val songDescriptionHelper: SongDescriptionHelper = SongDescriptionHelperImpl()

    fun init(homeView: HomeView) {
        HomeModelModule.initHomeModel(homeView)
        HomeControllerModule.onViewStarted(homeView)
    }
}