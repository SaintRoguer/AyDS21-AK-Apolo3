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

const val ARTIST_NAME = "artistName"
const val BASE_URL = "https://ws.audioscrobbler.com/2.0/"
const val NO_RESULTS = "No Results"
const val DATA_ARTIST = "artist"
const val DATA_BIO = "bio"
const val DATA_URL = "url"
const val DATA_CONTENT = "content"
const val STORE_LETTER = "*"
const val IMAGE_URL="https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
const val START_HTML="<html><div width=400>"
const val FONT_HTML="<font face=\"arial\">"
const val END_HTML="</font></div></html>"

class OtherInfoWindowActivity : AppCompatActivity() {
    private lateinit var moreDetailsPane: TextView
    private lateinit var dataBase: ArtistTablesCreate
    private val builder = StringBuilder()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initDatabase()

        moreDetailsPane = findViewById(R.id.moreDetailsPane)

        getArtistInfo(intent.getStringExtra(ARTIST_NAME))
    }

    private fun initDatabase() {
        dataBase = ArtistTablesCreate(this)
    }

    private fun bioContentToHTML(bioContent: JsonElement?, artistName: String): String {
        return when (bioContent) {
            null -> {
                NO_RESULTS
            }
            else -> {
                textToHtml(bioContent.asString.replace("\\n", "\n"), artistName)
            }
        }
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
        val artist = jObj[DATA_ARTIST].asJsonObject
        val bio = artist[DATA_BIO].asJsonObject
        val bioContent = bio[DATA_CONTENT]
        val artistUrl = artist[DATA_URL]
        infoFromJsonBioContentAndUrl.add(bioContent)
        infoFromJsonBioContentAndUrl.add(artistUrl)
        return infoFromJsonBioContentAndUrl
    }

    private fun getArtistInfo(artistName: String?) {
        val lastFMAPI = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(LastFMAPI::class.java)

        Thread {
            var moreDetailsDescription = dataBase.getInfo(artistName!!)
            if (moreDetailsDescription != null)// exists in db
                moreDetailsDescription = STORE_LETTER.plus(moreDetailsDescription)
            else { // get from service
                val callResponse = getResponseFromService(lastFMAPI, ARTIST_NAME)
                val bioContentAndUrl = parseFromJson(callResponse)
                val bioContent = bioContentAndUrl[0]
                val url = bioContentAndUrl[1]
                moreDetailsDescription = bioContentToHTML(bioContent, ARTIST_NAME)
                dataBase.saveArtist(artistName, moreDetailsDescription)
                setURLButtonListener(url)
            }
            val apiImageUrl = IMAGE_URL
                runOnUiThread {
                Picasso.get().load(apiImageUrl)
                    .into(findViewById<View>(R.id.imageView) as ImageView)
                moreDetailsPane.text = Html.fromHtml(moreDetailsDescription)
            }
        }.start()
    }


    private fun textToHtml(text: String, term: String?): String {
        builder.append(START_HTML)
        builder.append(FONT_HTML)
        val textFormatted = formatText(term, text)
        builder.append(textFormatted)
        builder.append(END_HTML)
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
        const val ARTIST_NAME_EXTRA = ARTIST_NAME
    }
}