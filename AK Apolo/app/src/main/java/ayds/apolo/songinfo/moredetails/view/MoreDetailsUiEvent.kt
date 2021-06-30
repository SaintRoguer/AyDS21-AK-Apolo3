package ayds.apolo.songinfo.moredetails.view

sealed class MoreDetailsUiEvent {
    object ViewFullCard : MoreDetailsUiEvent()
    object OnCreated : MoreDetailsUiEvent()
    object ShowSelectedCard : MoreDetailsUiEvent()
}