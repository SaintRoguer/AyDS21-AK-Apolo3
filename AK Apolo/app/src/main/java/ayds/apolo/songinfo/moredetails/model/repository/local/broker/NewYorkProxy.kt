package ayds.apolo.songinfo.moredetails.model.repository.local.broker

import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.EmptyCard
import ayds.apolo.songinfo.moredetails.model.entities.FullCard
import ayds.apolo.songinfo.moredetails.model.entities.Source
import ayds.jkhera2.nytimes.NYTArticleService
import ayds.jkhera2.nytimes.entities.Article


internal class NewYorkProxy(
    private val nytArticleService: NYTArticleService
) : Proxy {

    override fun getCard(artistName: String, cards: MutableList<Card>): MutableList<Card> =
        cards.apply {
            add(initFullCard(callService(artistName)))
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