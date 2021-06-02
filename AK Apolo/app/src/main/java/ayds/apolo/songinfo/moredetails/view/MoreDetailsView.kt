package ayds.apolo.songinfo.moredetails.view

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import ayds.apolo.songinfo.R
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModel
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModelModule
import ayds.apolo.songinfo.moredetails.model.entities.Article
import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle
import ayds.apolo.songinfo.moredetails.model.entities.EmptyArticle
import ayds.apolo.songinfo.utils.UtilsModule
import ayds.apolo.songinfo.utils.view.ImageLoader
import ayds.observer.Observable
import ayds.observer.Subject

private const val IMAGE_URL =
    "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d4/Lastfm_logo.svg/320px-Lastfm_logo.svg.png"
private const val LASTFM_URL = "https://ws.audioscrobbler.com/2.0/"
private const val ARTIST_NAME = "artistName"
const val STORE_LETTER = "*"

interface MoreDetailsView{
    val uiEventObservable : Observable<MoreDetailsUiEvent>
    val uiState : MoreDetailsUiState

    fun updateArticle(article : Article)
    fun openURLActivity()
    fun updateUrl(url: String)
}

class MoreDetailsViewActivity() : AppCompatActivity() , MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private lateinit var moreDetailsModel : MoreDetailsModel

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()

    private val imageLoader : ImageLoader = UtilsModule.imageLoader


    private val helperArticleInfo: ArticleHelper = MoreDetailsViewModule.helperArticleInfo
    private lateinit var moreDetailsPane: TextView
    private lateinit var moreDetailsButton: Button
    private lateinit var imageView: ImageView


    override fun updateUrl(url: String) {
        uiState = uiState.copy(articleURL = url)
    }

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initProperties()
        initArtistName()
        initListeners()
        initObservers()
        notifyCreated()
    }

    private fun notifyCreated() {
        onActionSubject.notify(MoreDetailsUiEvent.OnCreated)
    }

    private fun initModule(){
        MoreDetailsViewModule.init(this)
        moreDetailsModel = MoreDetailsModelModule.getMoreDetailsModel()
    }

    private fun initProperties(){
        moreDetailsPane = findViewById(R.id.moreDetailsPane)
        moreDetailsButton = findViewById(R.id.openUrlButton)
        imageView = findViewById(R.id.imageView)
    }

    private fun initArtistName() {
        uiState = uiState.copy(artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString())
    }

    private fun initListeners() {
        initURLButtonListener()
    }

    private fun initURLButtonListener() {
        moreDetailsButton.setOnClickListener {
            notifyFullArticleAction()
            openURLActivity()
        }
    }

    override fun openURLActivity() {
        val openUrlAction = Intent(Intent.ACTION_VIEW)
        openUrlAction.data = Uri.parse(uiState.articleURL)
        startActivity(openUrlAction)
    }

    private fun initObservers(){
        moreDetailsModel.articleObservable()
            .subscribe{
                value -> updateArticle(value)
            }
    }

    private fun notifyFullArticleAction() {
        onActionSubject.notify(MoreDetailsUiEvent.ViewFullArticle)
    }

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
        when(article.isLocallyStoraged){
            true -> addStorePrefix(article)
            else -> withoutPrefix(article)
        }
    }

    private fun addStorePrefix(article : Article){
        uiState = uiState.copy(
            artistName = article.artistName,
            articleURL = article.artistURL,
            artistInfo = STORE_LETTER.plus(article.artistInfo)
        )
    }

    private fun withoutPrefix(article : Article) {
        uiState = uiState.copy(
            artistName = article.artistName,
            articleURL = article.artistURL,
            artistInfo = article.artistInfo,
        )
    }

    private fun updateNoResultsUiState(){
        uiState = uiState.copy(
            artistName = "",
            articleURL = "",
            artistInfo = ""
        )
    }

    private fun updateArtistInfoUI() {
        runOnUiThread {
            loadLastFMImage()
            loadArtistInfo()
        }
    }

    private fun loadLastFMImage() {
        imageLoader.loadImageIntoView(IMAGE_URL, imageView)
    }

    private fun loadArtistInfo() {
        moreDetailsPane.text = Html.fromHtml(helperArticleInfo.
                                            getTextToHtml(uiState.artistInfo,uiState.artistName))
    }

    companion object {
        const val ARTIST_NAME_EXTRA = ARTIST_NAME
    }

}