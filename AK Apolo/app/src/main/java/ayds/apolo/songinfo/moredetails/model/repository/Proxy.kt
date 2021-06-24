package ayds.apolo.songinfo.moredetails.model.repository

import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.EmptyCard
import ayds.apolo.songinfo.moredetails.model.entities.Source
import ayds.apolo3.lastfm.EmptyArticle
import ayds.apolo3.lastfm.LastFMInfoService
import ayds.apolo3.lastfm.LastFMModule
import ayds.jkhera2.nytimes.NYTArticleService
import ayds.jkhera2.nytimes.NYTModule
import ayds.jkhera2.nytimes.NYTModule.nytArticleService
import ayds.jkhera2.nytimes.entities.Article

interface Proxy{
    fun mapCard(artistName:String): Card
}

internal abstract class ProxyImpl:Proxy{
    protected val helperCardInitiator = ProxyModule.helperCardInitiator
}

internal class LastFMProxy(
    private val lastFMInfoService: LastFMInfoService = LastFMModule.lastFMInfoService
): ProxyImpl() {

    override fun mapCard(artistName:String): Card  =
        helperCardInitiator.initFullCard(callService(artistName),Source.LAST_FM)

    private fun callService(artistName:String) =
        lastFMInfoService.getCardInfo(artistName)
}

internal class NewYorkProxy(
    private val nytArticleService: NYTArticleService = NYTModule.nytArticleService
): ProxyImpl() {

    override fun mapCard(artistName: String): Card {
        return when (val callService = callService(artistName)) {
             null -> callService?.let{
                helperCardInitiator.initFullCard(
                    (callService,Source.NEW_YORK_TIMES)
                }
            else -> EmptyCard
            }
        }

    private fun callService(artistName:String) =
        nytArticleService.getArticleInfo(artistName)
}


/*internal class WikipediaProxy(
): ProxyImpl() {
    override fun mapCard(artistName:String): Card  {
    }
}*/

