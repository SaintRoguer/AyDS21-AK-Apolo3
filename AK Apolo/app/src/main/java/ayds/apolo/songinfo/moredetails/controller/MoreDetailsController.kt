package ayds.apolo.songinfo.moredetails.controller

import ayds.apolo.songinfo.moredetails.model.MoreDetailsModel
import ayds.apolo.songinfo.moredetails.view.MoreDetailsUiEvent
import ayds.apolo.songinfo.moredetails.view.MoreDetailsView
import ayds.observer.Observer

interface MoreDetailsController{
     fun setMoreDetailsView(moreDetailsView : MoreDetailsView)
}

internal class MoreDetailsControllerImpl(
    private val moreDetailsModel : MoreDetailsModel
) : MoreDetailsController {

    private lateinit var moreDetailsView : MoreDetailsView

    override fun setMoreDetailsView(moreDetailsView: MoreDetailsView){
        this.moreDetailsView = moreDetailsView
       // moreDetailsView.uiEventObservable.subscribe(observer)
    }
    /**Habra que hacer este observer teniendo el de spotify,
     * de ultima le preguntamos a emma, en las 2 clases que tenemos, no lo veo.
     **/
/*  private val observer: Observer<HomeUiEvent> =
        Observer { value ->
            when (value) {
                HomeUiEvent.Search -> searchSong()
                HomeUiEvent.MoreDetails -> moreDetails()
                is HomeUiEvent.OpenSongUrl -> openSongUrl()
            }
        }*/


}