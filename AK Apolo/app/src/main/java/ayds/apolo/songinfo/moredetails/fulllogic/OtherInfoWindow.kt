package ayds.apolo.songinfo.moredetails.fulllogic

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ayds.apolo.songinfo.R
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.*

class OtherInfoWindow : AppCompatActivity() {
    private lateinit var moreDetailsPane: TextView
    private lateinit var dataBase: DataBase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)
        moreDetailsPane = findViewById(R.id.moreDetailsPane)
        getInfoMoreDetailsPane(intent.getStringExtra("artistName"))
    }

    private fun getInfoMoreDetailsPane(artistName: String?) {
        dataBase = DataBase(this)
        getArtistInfo(artistName)
    }

    private fun adaptJInterfaceToHTTP() =
        Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(LastFMAPI::class.java)

    private fun bioContentToHTML(bioContent: JsonElement?, artistName: String): String {
        var moreDetailsDescription: String
        when (bioContent) {
            null -> {
                moreDetailsDescription = "No Results"
            }
            else -> {
                moreDetailsDescription = bioContent.asString.replace("\\n", "\n")
                moreDetailsDescription = textToHtml(moreDetailsDescription, artistName)
                dataBase.saveArtist(artistName, moreDetailsDescription)
            }
        }
        return moreDetailsDescription
    }

    private fun setURLButtonListener(url: JsonElement) {
        val urlString = url.asString
        findViewById<View>(R.id.openUrlButton).setOnClickListener {
            val openUrlAction = Intent(Intent.ACTION_VIEW)
            openUrlAction.data = Uri.parse(urlString)
            startActivity(openUrlAction)
        }
    }

    private fun getResponseFromService(lastFMAPI: LastFMAPI, artistName: String): Response<String> {
        lateinit var callResponse: Response<String>
        try {
            callResponse = lastFMAPI.getArtistInfo(artistName).execute()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return callResponse
    }

    private fun parseFromJson(callResponse: Response<String>): List<JsonElement> {
        val infoFromJsonBioContentAndUrl = mutableListOf<JsonElement>()
        val gson = Gson()
        val jObj = gson.fromJson(callResponse.body(), JsonObject::class.java)
        val artist = jObj["artist"].asJsonObject
        val bio = artist["bio"].asJsonObject
        val bioContent = bio["content"]
        val artistUrl = artist["url"]
        infoFromJsonBioContentAndUrl.add(bioContent)
        infoFromJsonBioContentAndUrl.add(artistUrl)
        return infoFromJsonBioContentAndUrl
    }

    private fun getArtistInfo(artistName: String?) {
        val lastFMAPI = adaptJInterfaceToHTTP()
        Thread {
            var moreDetailsDescription = dataBase.getInfo(artistName!!)
            if (moreDetailsDescription != null)// exists in db
                moreDetailsDescription = "[*]$moreDetailsDescription"
            else { // get from service
                val callResponse = getResponseFromService(lastFMAPI, artistName)
                val bioContentAndUrl = parseFromJson(callResponse)
                val bioContent = bioContentAndUrl[0]
                val url = bioContentAndUrl[1]
                moreDetailsDescription = bioContentToHTML(bioContent, artistName)
                setURLButtonListener(url)
            }
            val apiImageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
            runOnUiThread {
                Picasso.get().load(apiImageUrl)
                    .into(findViewById<View>(R.id.imageView) as ImageView)
                moreDetailsPane.text = Html.fromHtml(moreDetailsDescription)
            }
        }.start()
    }


    private fun textToHtml(text: String, term: String?): String {
        val builder = StringBuilder()
        builder.append("<html><div width=400>")
        builder.append("<font face=\"arial\">")
        val textFormatted = formatText(term, text)
        builder.append(textFormatted)
        builder.append("</font></div></html>")
        return builder.toString()
    }


    private fun formatText(term: String?, text: String): String {
        val textWithBold = text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)" + term!!.toRegex(),
                "<b>" + term.toUpperCase(Locale.getDefault()) + "</b>"
            )
        return textWithBold
    }

    companion object {
        const val ARTIST_NAME_EXTRA = "artistName"
    }
}