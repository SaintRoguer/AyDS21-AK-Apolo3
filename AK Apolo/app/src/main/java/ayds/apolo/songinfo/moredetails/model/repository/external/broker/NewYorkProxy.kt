package ayds.apolo.songinfo.moredetails.model.repository.external.broker

import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.EmptyCard
import ayds.apolo.songinfo.moredetails.model.entities.FullCard
import ayds.apolo.songinfo.moredetails.model.entities.Source
import ayds.jkhera2.nytimes.NYTArticleService
import ayds.jkhera2.nytimes.entities.Article
import java.lang.Exception

internal class NewYorkProxy(
    private val nytArticleService: NYTArticleService
) : Proxy {

    override fun getCard(artistName: String): Card =
        try {
            initFullCard(callService(artistName))
        } catch (e: Exception) {
            EmptyCard
        }

    private fun callService(artistName: String) =
        nytArticleService.getArticleInfo(artistName)

    private fun initFullCard(article: Article?) =
        when (article) {
            null -> EmptyCard
            else -> FullCard(
                article.description,
                article.infoUrl,
                Source.NEW_YORK_TIMES,
                article.sourceLogoUrl
            )
        }
}