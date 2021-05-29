package ayds.apolo.songinfo.moredetails.view

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.squareup.picasso.Picasso
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

private const val IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
private const val ARTIST_NAME = "artistName"

interface MoreDetailsView{
    val uiEventObservable : Observable<MoreDetailsUiEvent>
    val uiState : MoreDetailsUiState

    fun getArtistInfo(text: String, term: String): String
    fun updateArticle(article : Article)
}

class MoreDetailsViewActivity : AppCompatActivity() , MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private lateinit var moreDetailsModel : MoreDetailsModel

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()

    private val imageLoader : ImageLoader = UtilsModule.imageLoader
    private lateinit var apiBuilder: Retrofit
    private val helperArticleInfo: ArticleHelper = ArticleHelperImpl()

    private lateinit var moreDetailsPane: TextView
    private lateinit var buttonView: View
    private lateinit var imageView: ImageView

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

    private fun openURLActivity() {
        val openUrlAction = Intent(Intent.ACTION_VIEW)
        openUrlAction.data = Uri.parse(uiState.artistURL)
        startActivity(openUrlAction)
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

    override fun getArtistInfo(text: String, term: String): String =
        helperArticleInfo.getTextToHtml(text, term)

    override fun updateArticle(article : Article){
        updateUiState(article)
        updateArtistInfoUI()
    }

    private fun updateUiState(article : Article){
        when (article){
            is ArtistArticle -> updateArticleUiState(article)
            EmptyArticle -> updateNoResultsUiState()
        }
    }

    private fun updateArticleUiState(article : Article){
        uiState = uiState.copy(
            artistName = article.artistName,
            artistURL = article.artistURL,
            artistInfo = article.artistInfo
        )
    }

    private fun updateNoResultsUiState(){
        uiState = uiState.copy(
            artistName = "",
            artistURL = "",
            artistInfo = ""
        )
    }

    private fun updateArtistInfoUI() {
        Thread {
            loadLastFMImage()
            loadArtistInfo()
        }.start()
    }

    private fun loadLastFMImage() {
        imageLoader.loadImageIntoView(IMAGE_URL, imageView)
    }

    private fun loadArtistInfo() {
        moreDetailsPane.text = uiState.artistInfo
    }

    companion object {
        const val ARTIST_NAME_EXTRA = ARTIST_NAME
    }

}