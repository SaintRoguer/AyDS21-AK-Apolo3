package ayds.apolo.songinfo.moredetails.model.repository.local.broker

import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.EmptyCard
import ayds.apolo.songinfo.moredetails.model.entities.FullCard
import ayds.apolo.songinfo.moredetails.model.entities.Source
import ayds.apolo3.lastfm.Article
import ayds.apolo3.lastfm.ArtistArticle
import ayds.apolo3.lastfm.LastFMInfoService

internal class LastFMProxy(
    private val lastFMInfoService: LastFMInfoService
) : Proxy {

    override fun getCard(artistName: String): Card =
        initFullCard(callService(artistName))

    private fun callService(artistName: String) =
        lastFMInfoService.getCardInfo(artistName)

    private fun initFullCard(article: Article?) =
        when (article) {
            is ArtistArticle -> FullCard(
                article.description,
                article.infoURL,
                Source.LAST_FM,
                article.sourceLogoURL
            )
            else ->EmptyCard
        }
}