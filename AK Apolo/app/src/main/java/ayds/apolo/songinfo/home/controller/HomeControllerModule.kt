package ayds.apolo.songinfo.home.controller

import ayds.apolo.songinfo.home.model.HomeModelModule
import ayds.apolo.songinfo.home.view.HomeView

object HomeControllerModule {

    fun onViewStarted(homeView: HomeView) {
        HomeControllerImpl(HomeModelModule.getHomeModel()).apply {
            setHomeView(homeView)
        }
    }
}