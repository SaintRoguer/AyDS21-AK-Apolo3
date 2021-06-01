package ayds.apolo.songinfo.moredetails.model.repository.external.lastfm.info

import android.util.Log
import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.lang.Exception

interface LastFMToInfoResolver {
    fun getInfoFromExternalData(serviceData: String?): ArtistArticle?
}
const val DATA_ARTIST = "artist"
const val DATA_CONTENT = "content"
const val ARTIST_NAME = "name"
const val DATA_BIO = "bio"
const val DATA_URL = "url"


internal class JsonToInfoResolver : LastFMToInfoResolver{

    override fun getInfoFromExternalData(serviceData: String?): ArtistArticle? =
      try{
          serviceData?.getJson()?.getArtistInfo()?.let { item->
              ArtistArticle(
                  item.getArtistName(), item.getArticleDescription(),
                  item.getArticleUrl()
              )
          }
      } catch (e: Exception){
          null
      }

    private fun String?.getJson():JsonObject{
        val gson = Gson()
        return gson.fromJson(this, JsonObject::class.java)
    }

    private fun JsonObject.getArtistInfo(): JsonObject {
        return this[DATA_ARTIST].asJsonObject
    }

    private fun JsonObject.getArtistName(): String {
        return this[ARTIST_NAME].asString
    }

    private fun JsonObject.getArticleDescription(): String {
        val articleDescription= this[DATA_BIO].asJsonObject
        Log.e("Mensaje de TAG", "QUIERO VER LO QUE HAY DENTRO: ${articleDescription["content"]}")
        return articleDescription[DATA_CONTENT].asString
    }

    private fun JsonObject.getArticleUrl(): String {
        return this[DATA_URL].asString
    }
}