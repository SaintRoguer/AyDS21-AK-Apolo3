package ayds.jkhera2.nytimes.article

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface NYTArticleAPI {
    @GET("articlesearch.json?api-key=fFnIAXXz8s8aJ4dB8CVOJl0Um2P96Zyx")
    fun getArticleInfo(@Query("q") artist: String?): Call<String>
}

