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
const val IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
const val START_HTML = "<html><div width=400>"
const val FONT_HTML = "<font face=\"arial\">"
const val END_HTML = "</font></div></html>"

class OtherInfoWindowActivity : AppCompatActivity() {
    private lateinit var moreDetailsPane: TextView
    private lateinit var dataBase: ArtistTablesCreate
    private val builder = StringBuilder()
    private lateinit var apiBuilder: Retrofit
    private lateinit var buttonView: View
    private var resultArtistFromDatabase: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initDatabase()
        initMoreDetailsPane()
        initProperties()
    }

    private fun initDatabase() {
        dataBase = ArtistTablesCreate(this)
    }

    private fun initMoreDetailsPane() {
        moreDetailsPane = findViewById(R.id.moreDetailsPane)
    }

    private fun initProperties() {
        initApiBuilder()
        initArtistInfo()
        initButtonView()
    }

    private fun initApiBuilder() {
        apiBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun initArtistInfo() {
        getArtistInfo(intent.getStringExtra(ARTIST_NAME))
    }

    private fun initButtonView() {
        buttonView = findViewById<View>(R.id.openUrlButton)
    }

    private fun getArtistInfo(artistName: String?) {
        val lastFMAPI = apiBuilder.create(LastFMAPI::class.java)
        initArtistThread(artistName, lastFMAPI)
    }

    private fun initArtistThread(artistName: String?, lastFMAPI: LastFMAPI) {
        Thread {
            checkArtistInDatabase(artistName, lastFMAPI)
        }.start()
    }

    private fun checkArtistInDatabase(
        artistName: String?,
        lastFMAPI: LastFMAPI
    ) {
        resultArtistFromDatabase = getArtistFromDatabase(artistName)
        resultArtistFromDatabase = if (resultArtistFromDatabase != null)
            STORE_LETTER.plus(resultArtistFromDatabase)
        else {
            writeArtistInDatabase(lastFMAPI, artistName!!)
        }
        val apiImageUrl = IMAGE_URL
        println(apiImageUrl)
        runOnUiThread {
            initApiImage(apiImageUrl, resultArtistFromDatabase)
        }
    }

    private fun initApiImage(apiImageUrl: String, resultArtistFromDatabase: String?) {
        Picasso.get().load(apiImageUrl)
            .into(findViewById<View>(R.id.imageView) as ImageView)
        moreDetailsPane.text = Html.fromHtml(resultArtistFromDatabase)
    }

    private fun getArtistFromDatabase(artistName: String?): String? {
        return dataBase.getInfo(artistName!!)
    }

    private fun writeArtistInDatabase(lastFMAPI: LastFMAPI, artistName: String): String {
        val contentAndUrl = initContentAndUrl(lastFMAPI, artistName)
        var assignArtistContent = bioContentToHTML(contentAndUrl[0], artistName)
        initURLButtonListener(contentAndUrl[1])
        saveArtistInDatabase(artistName, assignArtistContent)
        return assignArtistContent
    }

    private fun initContentAndUrl(lastFMAPI: LastFMAPI, artistName: String): List<JsonElement> {
        val callResponse = getResponseFromService(lastFMAPI, artistName)
        return parseFromJson(callResponse)
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

    private fun initURLButtonListener(URL: JsonElement) {
        setURLButtonListener(URL)
    }

    private fun saveArtistInDatabase(artistName: String, assignArtistContent: String) {
        dataBase.saveArtist(artistName!!, assignArtistContent)
    }

    private fun setURLButtonListener(url: JsonElement) {
        buttonView.setOnClickListener {
            val openUrlAction = Intent(Intent.ACTION_VIEW)
            openUrlAction.data = Uri.parse(url.asString)
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

        val artist = getArtistJson(callResponse)

        infoFromJsonBioContentAndUrl.add(artist[DATA_BIO].asJsonObject[DATA_CONTENT])
        infoFromJsonBioContentAndUrl.add(artist[DATA_URL])

        return infoFromJsonBioContentAndUrl
    }

    private fun getArtistJson(callResponse: Response<String>): JsonObject {
        val gson = Gson()
        val jObj = gson.fromJson(callResponse.body(), JsonObject::class.java)
        return jObj[DATA_ARTIST].asJsonObject
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