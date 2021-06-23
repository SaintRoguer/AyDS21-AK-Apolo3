package ayds.jkhera2.nytimes.article

import ayds.jkhera2.nytimes.entities.Article
import com.google.gson.Gson
import com.google.gson.JsonObject

interface NYTToArticleResolver {
    fun getArticleFromExternalData(serviceData: String?): Article?
}

private const val RESPONSE = "response"
private const val DOCS = "docs"
private const val ABSTRACT = "abstract"
private const val WEB_URL = "web_url"
private const val NO_RESULTS = "No Results"
private const val LOGO_URL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU"

internal class JsonToArticleResolver : NYTToArticleResolver {

    override fun getArticleFromExternalData(serviceData: String?): Article? =
        try {
            serviceData?.getFirstItem()?.let { item ->
                Article(
                    item.getAbstractFormatted(),
                    item.getWebURL(),
                    LOGO_URL,
                )
            }
        } catch (e: Exception) {
            null
        }

    private fun String?.getFirstItem(): JsonObject {
        val jsonObject = Gson().fromJson(this, JsonObject::class.java)
        return jsonObject[RESPONSE].asJsonObject
    }

    private fun JsonObject.getAbstractFormatted(): String {
        val abstractJsonElement = this[DOCS].asJsonArray[0].asJsonObject[ABSTRACT]
        abstractJsonElement?.let {
            return cleanEnterOfAbstract(it.toString())
        }?:run {return NO_RESULTS }
    }

    private fun cleanEnterOfAbstract(abstract: String): String{
        return abstract.replace("\\n", "\n")
    }

    private fun JsonObject.getWebURL(): String {
        val webUrl = this[DOCS].asJsonArray[0].asJsonObject[WEB_URL]
        return webUrl.asString
    }

}