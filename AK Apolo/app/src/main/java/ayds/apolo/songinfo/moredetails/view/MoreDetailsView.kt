package ayds.apolo.songinfo.moredetails.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.*
import ayds.apolo.songinfo.R
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModel
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModelModule
import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.EmptyCard
import ayds.apolo.songinfo.moredetails.model.entities.FullCard
import ayds.apolo.songinfo.moredetails.model.entities.Source
import ayds.apolo.songinfo.utils.navigation.openExternalUrl
import ayds.apolo.songinfo.utils.UtilsModule
import ayds.observer.Observable
import ayds.observer.Subject

private const val ARTIST_NAME = "artistName"
private const val STORE_LETTER = "*\n\n"

interface MoreDetailsView {
    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiStateService: MoreDetailsUiState

    fun updateCard(card: Card)
    fun openCardURLActivity()
    fun updateUrl(url: String)
    fun changeCard(sourceLabel: Source)
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private lateinit var moreDetailsModel: MoreDetailsModel
    private val helperCardInfo = MoreDetailsViewModule.helperCardInfo
    private lateinit var listOfCards: List<Card>

    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiStateService: MoreDetailsUiState = MoreDetailsUiState()

    private lateinit var moreDetailsPane: TextView
    private lateinit var moreDetailsButton: Button
    private lateinit var imageView: ImageView
    private lateinit var spinnerSource: Spinner
    private lateinit var sourceInfoPane: TextView

    override fun updateUrl(url: String) {
        uiStateService = uiStateService.copy(cardURL = url)
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
        sourceInfoPane = findViewById(R.id.sourceLabel)
        spinnerSource = findViewById(R.id.spinner)
    }

    private fun initArtistName() {
        uiStateService =
            uiStateService.copy(artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString())
    }

    private fun initSpinner(list: List<Card>) {
        listOfCards = list
        val spinnerNames: MutableList<String> = mutableListOf()

        for (card in listOfCards) {
            if (card is FullCard)

                spinnerNames.add(card.source.name)
        }
        Log.e("CARTA","Size: "+listOfCards.size)
        if (spinnerNames.isNotEmpty())
            runOnUiThread {
                spinnerSource.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerNames)
            }
    }

    private fun initListeners() {
        initSpinnerListener()
        initURLButtonListener()
    }

    private fun initSpinnerListener() {
        spinnerSource.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateCard(listOfCards[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                updateCard(listOfCards.first())
            }
        }
    }

    private fun initURLButtonListener() {
        moreDetailsButton.setOnClickListener {
            notifyFullCardAction()
        }
    }

    override fun openCardURLActivity() {
        openExternalUrl(uiStateService.cardURL)
    }

    private fun initObservers() {
        moreDetailsModel.cardObservable()
            .subscribe { value ->
                initSpinner(value)
            }
    }

    private fun notifyFullCardAction() {
        onActionSubject.notify(MoreDetailsUiEvent.ViewFullCard)
    }

    override fun updateCard(card: Card) {
        updateUiState(card)
        updateArtistInfoUI()
    }

    private fun updateUiState(card: Card) {
        when (card) {
            is EmptyCard -> updateNoResultsUiState()
            else -> updateCardUiState(card)
        }
    }

    private fun updateCardUiState(card: Card) {
        when (card.isLocallyStoraged) {
            true -> updateStoredCardUiState(card)
            else -> updateNewCardUiState(card)
        }
    }

    override fun changeCard(sourceLabel: Source) {
        listOfCards.forEach {
            if (it.source == sourceLabel)
                updateCard(it)
        }
    }

    private fun updateStoredCardUiState(card: Card) {
        uiStateService = uiStateService.copy(
            cardURL = card.infoURL,
            cardInfo = STORE_LETTER.plus(card.description),
            sourceLogoURL = card.sourceLogoURL,
            sourceLabel = card.source,
        )
    }

    private fun updateNewCardUiState(card: Card) {
        uiStateService = uiStateService.copy(
            cardURL = card.infoURL,
            cardInfo = card.description,
            sourceLogoURL = card.sourceLogoURL,
            sourceLabel = card.source
        )
    }

    private fun updateNoResultsUiState() {
        uiStateService = uiStateService.copy(
            cardURL = "",
            cardInfo = "Data not found!"
        )
    }

    private fun updateArtistInfoUI() {
        runOnUiThread {
            loadLastImage()
            loadArtistInfo()
            loadSourceInfo()
        }
    }

    private fun loadLastImage() {
        UtilsModule.imageLoader.loadImageIntoView(uiStateService.sourceLogoURL, imageView)
    }

    private fun loadArtistInfo() {
        moreDetailsPane.text = Html.fromHtml(
            helperCardInfo.getTextToHtml(uiStateService.cardInfo, uiStateService.artistName)
        )
    }

    private fun loadSourceInfo() {
        sourceInfoPane.text = "FROM: " + uiStateService.sourceLabel.toString()
    }

    companion object {
        const val ARTIST_NAME_EXTRA = ARTIST_NAME
    }
}