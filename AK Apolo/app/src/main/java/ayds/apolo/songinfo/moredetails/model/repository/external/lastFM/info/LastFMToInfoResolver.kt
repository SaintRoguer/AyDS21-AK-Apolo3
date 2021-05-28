package ayds.apolo.songinfo.moredetails.model.repository.external.lastFM.info

import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.lang.Exception

interface LastFMToInfoResolver {
    fun getInfoFromExternalData(serviceData: String?): ArtistArticle?
}
const val DATA_ARTIST = "artist"
const val ARTIST_NAME = "artistName"
const val DATA_BIO = "bio"
const val DATA_URL = "url"

internal class JsonToInfoResolver : LastFMToInfoResolver{

    override fun getInfoFromExternalData(serviceData: String?): ArtistArticle? =
      try{
          serviceData?.getFirstItem()?.let { item->
              ArtistArticle(
                  item.getArtistName(), item.getArtistInfo(),
                  item.getArtistUrl(), false

              )
          }
      } catch (e: Exception){
          null
      }

      private fun String?.getFirstItem(): JsonObject {
          val jobj = Gson().fromJson(this,JsonObject::class.java)
          return jobj[DATA_ARTIST].asJsonObject

      }

    private fun JsonObject.getArtistName() = this[ARTIST_NAME].toString()
    //info es bio en el model ArticleArtist?
    private fun JsonObject.getArtistInfo() = this[DATA_BIO].toString()

    private fun JsonObject.getArtistUrl() = this[DATA_URL].toString()




}