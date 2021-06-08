package ayds.apolo.songinfo.moredetails.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import ayds.apolo.songinfo.R
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModel
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModelModule
import ayds.apolo.songinfo.moredetails.model.entities.Article
import ayds.apolo.songinfo.moredetails.model.entities.ArtistArticle
import ayds.apolo.songinfo.moredetails.model.entities.EmptyArticle
import ayds.apolo.songinfo.moredetails.view.MoreDetailsUiState.Companion.IMAGE_URL
import ayds.apolo.songinfo.utils.navigation.openExternalUrl
import ayds.apolo.songinfo.utils.UtilsModule
import ayds.observer.Observable
import ayds.observer.Subject

private const val ARTIST_NAME = "artistName"
private const val STORE_LETTER = "*\n\n"

interface MoreDetailsView {
    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiState: MoreDetailsUiState

    fun updateArticle(article: Article)
    fun openArticleURLActivity()
    fun updateUrl(url: String)
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private lateinit var moreDetailsModel: MoreDetailsModel
    private val helperArticleInfo = MoreDetailsViewModule.helperArticleInfo

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiState: MoreDetailsUiState = MoreDetailsUiState()

    private lateinit var moreDetailsPane: TextView
    private lateinit var moreDetailsButton: Button
    private lateinit var imageView: ImageView

    override fun updateUrl(url: String) {
        uiState = uiState.copy(articleURL = url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
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

    private fun initModule() {
        MoreDetailsViewModule.init(this)
        moreDetailsModel = MoreDetailsModelModule.getMoreDetailsModel()
    }

    private fun initProperties() {
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
        }
    }

    override fun openArticleURLActivity() {
        openExternalUrl(uiState.articleURL)
    }

    private fun initObservers() {
        moreDetailsModel.articleObservable()
            .subscribe { value ->
                updateArticle(value)
            }
    }

    private fun notifyFullArticleAction() {
        onActionSubject.notify(MoreDetailsUiEvent.ViewFullArticle)
    }

    override fun updateArticle(article: Article) {
        updateUiState(article)
        updateArtistInfoUI()
    }

    private fun updateUiState(article: Article) {
        when (article) {
            is ArtistArticle -> updateArticleUiState(article)
            EmptyArticle -> updateNoResultsUiState()
        }
    }

    private fun updateArticleUiState(article: Article) {
        when (article.isLocallyStoraged) {
            true -> updateStoredArticleUiState(article)
            else -> updateNewArticleUiState(article)
        }
    }

    private fun updateStoredArticleUiState(article: Article) {
        uiState = uiState.copy(
            articleURL = article.articleURL,
            artistInfo = STORE_LETTER.plus(article.articleInfo)
        )
    }

    private fun updateNewArticleUiState(article: Article) {
        uiState = uiState.copy(
            articleURL = article.articleURL,
            artistInfo = article.articleInfo,
        )
    }

    private fun updateNoResultsUiState() {
        uiState = uiState.copy(
            articleURL = "Artículo no encontrado!",
            artistInfo = "Información no encontrada!"
        )
    }

    private fun updateArtistInfoUI() {
        runOnUiThread {
            loadLastFMImage()
            loadArtistInfo()
        }
    }

    private fun loadLastFMImage() {
        UtilsModule.imageLoader.loadImageIntoView(IMAGE_URL, imageView)
    }

    private fun loadArtistInfo() {
        moreDetailsPane.text = Html.fromHtml(
            helperArticleInfo.getTextToHtml(uiState.artistInfo, uiState.artistName)
        )
    }

    companion object {
        const val ARTIST_NAME_EXTRA = ARTIST_NAME
    }
}