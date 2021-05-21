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
import java.util.*

const val ARTIST_NAME = "artistName"
const val BASE_URL = "https://ws.audioscrobbler.com/2.0/"
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
    private lateinit var imageView : ImageView
    private var resultArtistFromDatabase: String? = null
    private lateinit var lastFMAPI:LastFMAPI
    private lateinit var artistName : String
    private lateinit var jsonContent : JsonElement
    private lateinit var urlString : String

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
        initImageView()
        initLastFMAPI()
        initListeners()
    }

    private fun initApiBuilder() {
        apiBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun initArtistInfo() {
        artistName = (intent.getStringExtra(ARTIST_NAME)).toString()
        artistName?.let {
            initArtistThread()
        }
    }

    private fun initButtonView() {
        buttonView = findViewById(R.id.openUrlButton)
    }

    private fun initImageView(){
        imageView = findViewById(R.id.imageView)
    }

    private fun initLastFMAPI(){
       lastFMAPI = apiBuilder.create(LastFMAPI::class.java)
    }

    private fun initListeners(){
        initURLButtonListener()
    }

    private fun initArtistThread() {
        Thread {
            checkArtistInDatabase()
            runUiThread()
        }.start()
    }

    private fun checkArtistInDatabase() {
        resultArtistFromDatabase = getArtistFromDatabase()
        resultArtistFromDatabase =
            when (resultArtistFromDatabase) {
                null -> writeArtistInDatabase()
                else -> plusStoreLetter()
            }
    }

    private fun plusStoreLetter() : String = STORE_LETTER.plus(resultArtistFromDatabase)

    private fun runUiThread(){
        runOnUiThread {
            initApiImage(resultArtistFromDatabase)
        }
    }

    private fun initApiImage(resultArtistFromDatabase: String?) {
        Picasso.get().load(IMAGE_URL).into(imageView)
        moreDetailsPane.text = Html.fromHtml(resultArtistFromDatabase)
    }

    private fun getArtistFromDatabase(): String? {
        return dataBase.getInfo(artistName)
    }

    private fun writeArtistInDatabase(): String {
        getResponse()
        val assignArtistContent = bioContentToHTML()
        saveArtistInDatabase(assignArtistContent)
        return assignArtistContent
    }

    private fun getResponse(){
        parseFromJson(getResponseFromService(artistName))
    }

    private fun saveArtistInDatabase(assignArtistContent: String) {
        dataBase.saveArtist(artistName, assignArtistContent)
    }

    private fun bioContentToHTML(): String =
        when (jsonContent) {
            else -> textToHtml(jsonContent.asString.replace("\\n", "\n"), artistName)
        }


    private fun initURLButtonListener() =
        buttonView.setOnClickListener {
            openURLActivity()
        }

    private fun openURLActivity() {
        val openUrlAction = Intent(Intent.ACTION_VIEW)
        openUrlAction.data = Uri.parse(urlString)
        startActivity(openUrlAction)
    }

    private fun getResponseFromService(artistName: String): Response<String> =
        lastFMAPI.getArtistInfo(artistName).execute()


    private fun parseFromJson(callResponse: Response<String>){
        val artistJson = getArtistJson(callResponse)
        jsonContent =artistJson[DATA_BIO].asJsonObject[DATA_CONTENT]
        urlString = artistJson[DATA_URL].toString()
    }

    private fun getArtistJson(callResponse: Response<String>): JsonObject {
        val gson = Gson()
        val jObj = gson.fromJson(callResponse.body(), JsonObject::class.java)
        return jObj[DATA_ARTIST].asJsonObject
    }

    private fun textToHtml(text: String, term: String): String {
        builder.append(START_HTML)
        builder.append(FONT_HTML)
        val textFormatted = formatText(term, text)
        builder.append(textFormatted)
        builder.append(END_HTML)
        return builder.toString()
    }

    private fun formatText(term: String, text: String): String {
        return text
            .replace("'", " ")
            .replace("\n", "<br>")
            .replace(
                "(?i)" + term.toRegex(),
                "<b>" + term.toUpperCase(Locale.getDefault()) + "</b>"
            )
    }

    companion object {
        const val ARTIST_NAME_EXTRA = ARTIST_NAME
    }
}