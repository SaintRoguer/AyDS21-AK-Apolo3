package ayds.apolo.songinfo.moredetails.model.repository
import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.FullCard
import ayds.apolo.songinfo.moredetails.model.entities.Source
import ayds.apolo3.lastfm.Article


interface CardInitiator{
    fun initFullCard(article: Article, source:Source): Card
}

internal class CardInitiatorImpl : CardInitiator{
    override fun initFullCard(article: Article, source:Source)=
        FullCard(
            article.description,
            article.infoURL,
            source,
            article.sourceLogoURL
        )

}



