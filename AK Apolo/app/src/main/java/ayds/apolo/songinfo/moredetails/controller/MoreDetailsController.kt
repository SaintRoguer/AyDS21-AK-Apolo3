package ayds.apolo.songinfo.moredetails.controller

import ayds.apolo.songinfo.moredetails.model.MoreDetailsModelImpl
import ayds.apolo.songinfo.moredetails.view.MoreDetailsView


interface MoreDetailsController{
     fun setMoreDetailsView(moreDetailsView : MoreDetailsView)
}

internal class MoreDetailsControllerImpl(
    private val moreDetailsModelImpl : MoreDetailsModelImpl
) : MoreDetailsController {

    private lateinit var moreDetailsView : MoreDetailsView

    override fun setMoreDetailsView(moreDetailsView: MoreDetailsView){
        this.moreDetailsView = moreDetailsView
       // moreDetailsView.uiEventObservable.subscribe(observer)
    }
/*
    private val observer: Observer<HomeUiEvent> =
        Observer { value ->
            when (value) {
                HomeUiEvent.Search -> searchSong()
                HomeUiEvent.MoreDetails -> moreDetails()
                is HomeUiEvent.OpenSongUrl -> openSongUrl()
            }
        }*/


}