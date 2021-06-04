package ayds.apolo.songinfo.moredetails.view

sealed class MoreDetailsUiEvent {
    object ViewFullArticle : MoreDetailsUiEvent()
    object OnCreated : MoreDetailsUiEvent()
}