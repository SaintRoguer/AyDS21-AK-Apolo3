package ayds.apolo.songinfo.moredetails.view

import ayds.apolo.songinfo.moredetails.controller.MoreDetailsControllerModule
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModelModule

object MoreDetailsViewModule {
    val helperArticleInfo: ArticleFormatter = ArticleFormatterImpl()

    fun init(moreDetailsView: MoreDetailsView) {
        MoreDetailsModelModule.initMoreDetailsModel(moreDetailsView)
        MoreDetailsControllerModule.viewStart(moreDetailsView)
    }
}