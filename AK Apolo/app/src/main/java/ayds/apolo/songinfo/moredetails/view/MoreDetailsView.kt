package ayds.apolo.songinfo.moredetails.view

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ayds.apolo.songinfo.R
import ayds.apolo.songinfo.moredetails.*
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModel
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModelModule
import ayds.apolo.songinfo.moredetails.model.entities.Article
import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle
import ayds.apolo.songinfo.moredetails.model.entities.EmptyArticle
import ayds.apolo.songinfo.utils.UtilsModule
import ayds.apolo.songinfo.utils.view.ImageLoader
import ayds.observer.Observable
import ayds.observer.Subject
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

private const val IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"

interface MoreDetailsView{
    val uiEventObservable : Observable<MoreDetailsUiEvent>
    val uiState : MoreDetailsUiState

    fun navigate()
    fun open()
}

class MoreDetailsViewActivity : AppCompatActivity() , MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private lateinit var moreDetailsModel : MoreDetailsModel

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()

    private val imageLoader : ImageLoader = UtilsModule.imageLoader
    private lateinit var apiBuilder: Retrofit

    private lateinit var moreDetailsPane: TextView
    private lateinit var buttonView: View
    private lateinit var imageView: ImageView


    override fun navigate() {
        TODO("Not yet implemented")
    }

    override fun open(){
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initProperties()
        initListeners()
        initObservers()
        initApiBuilder()
    }


    private fun initModule(){
        MoreDetailsViewModule.init(this)
        moreDetailsModel = MoreDetailsModelModule.getMoreDetailsModel()
    }

    private fun initProperties(){
        buttonView = findViewById(R.id.openUrlButton)
        imageView = findViewById(R.id.imageView)
        moreDetailsPane = findViewById(R.id.moreDetailsPane)
    }


    private fun initListeners() {
        initURLButtonListener()
    }

    private fun initURLButtonListener() =
        buttonView.setOnClickListener {
            openURLActivity()
        }

    private fun initApiBuilder() {
        apiBuilder = Retrofit.Builder()
            .baseUrl(LASTFM_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private fun initObservers(){
        moreDetailsModel.articleObservable()
            .subscribe{
                value -> updateArticle(value)
            }
    }

    private fun updateArticle(article : Article){
        updateUiState(article)
    }

    private fun updateUiState(article : Article){
        when (article){
            is ArtistArticle -> updateArticleUiState(article)
            EmptyArticle -> updateNoResultsUiState()
        }
    }

    private fun updateArticleUiState(article : Article){
    /*    uiState = uiState.copy(
            artistName = article.artistName,
            artistURL = article.artistURL
        )*/
    }

    private fun updateNoResultsUiState(){
     /*   uiState = uiState.copy(
            artistName = "",
            artistURL = "",
        )*/
    }

    private fun initArtistThread() {
        Thread {
            updateArtistInfo()
            updateArtistInfoUI()
        }.start()
    }

    //Los updatese los puse aca porque gun dice el grafico van aca, pero
    //habria los get se le hacen al model
    private fun updateArtistInfo() {
        artistInfo = getArtistFromDatabase()
        if(artistInfo != null)
            addStorePrefix()
        else
        {
            artistInfo = getArtistInfoFromLastFM()
            saveArtistInDatabase()
        }
    }

    private fun updateArtistInfoUI() {
        runOnUiThread {
            loadArtistImage()
            loadArtistText()
        }
    }

    private fun openURLActivity() {
        val openUrlAction = Intent(Intent.ACTION_VIEW)
       // urlString creo que corresponde a model, pero
        //una nueva activity creo es view, por ende
        //lo dejo comentado
        // openUrlAction.data = Uri.parse(urlString)
        startActivity(openUrlAction)
    }

    private fun initArtistInfo() {
        artistName = (intent.getStringExtra(ARTIST_NAME)).toString()
        artistName?.let {
            initArtistThread()
        }
    }

    private fun addStorePrefix(): String = STORE_LETTER.plus(artistInfo)

    private fun getArtistFromDatabase(): String? {
        return dataBase.getInfo(artistName)
    }

    private fun getArtistInfoFromLastFM(): String {
        setContentAndURL()
        assignArtistContent = bioContentToHTML()
        return assignArtistContent
    }

    private fun setContentAndURL() {
        parseFromJson(getResponseFromService(artistName))
    }

    private fun saveArtistInDatabase() {
        dataBase.saveArtist(artistName, assignArtistContent)
    }

    private fun bioContentToHTML(): String =
        textToHtml(jsonContent.asString.replace("\\n", "\n"), artistName)

    private fun getResponseFromService(artistName: String): Response<String> =
        lastFMAPI.getArtistInfo(artistName).execute()


    private fun parseFromJson(callResponse: Response<String>) {
        val artistJson = getArtistJson(callResponse)
        jsonContent = artistJson[DATA_BIO].asJsonObject[DATA_CONTENT]
        urlString = artistJson[DATA_URL].asString
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
}