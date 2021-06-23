package ayds.jkhera2.nytimes.article

import ayds.jkhera2.nytimes.entities.Article
import ayds.jkhera2.nytimes.NYTArticleService
import retrofit2.Response

internal class NYTArticleServiceImpl(
    private val nytArticleAPI: NYTArticleAPI,
    private val nytToArticleResolver: NYTToArticleResolver,
) : NYTArticleService {

    override fun getArticleInfo(nameOfArtist: String): Article? {
        val callResponse = getArticleFromService(nameOfArtist)
        return nytToArticleResolver.getArticleFromExternalData(callResponse.body())
    }

    private fun getArticleFromService(query: String): Response<String> {
        return nytArticleAPI.getArticleInfo(query).execute()
    }

}