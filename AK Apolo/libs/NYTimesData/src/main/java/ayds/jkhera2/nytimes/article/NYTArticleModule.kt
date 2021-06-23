package ayds.jkhera2.nytimes.article

import ayds.jkhera2.nytimes.NYTArticleService
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

internal object NYTArticleModule {

    private const val NYT_URL = "https://api.nytimes.com/svc/search/v2/"
    private val nytAPIRetrofit = Retrofit.Builder()
        .baseUrl(NYT_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val nytArticleAPI = nytAPIRetrofit.create(NYTArticleAPI::class.java)

    private val nytToArticleResolver: NYTToArticleResolver = JsonToArticleResolver()

    val nytArticleService: NYTArticleService = NYTArticleServiceImpl(
        nytArticleAPI,
        nytToArticleResolver
    )
}