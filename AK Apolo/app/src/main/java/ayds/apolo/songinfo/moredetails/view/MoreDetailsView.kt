package ayds.apolo.songinfo.moredetails.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.text.HtmlCompat
import ayds.apolo.songinfo.R
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModel
import ayds.apolo.songinfo.moredetails.model.MoreDetailsModelModule
import ayds.apolo.songinfo.moredetails.model.entities.Card
import ayds.apolo.songinfo.moredetails.model.entities.NoResultsCard
import ayds.apolo.songinfo.utils.navigation.openExternalUrl
import ayds.apolo.songinfo.utils.UtilsModule
import ayds.observer.Observable
import ayds.observer.Subject

private const val ARTIST_NAME = "artistName"

interface MoreDetailsView {
    val uiEventObservable: Observable<MoreDetailsUiEvent>
    val uiStateService: MoreDetailsUiState

    fun updateCard(cards: List<Card>)
    fun openCardURLActivity()
}

class MoreDetailsViewActivity : AppCompatActivity(), MoreDetailsView {

    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private lateinit var moreDetailsModel: MoreDetailsModel
    private val helperCardInfo = MoreDetailsViewModule.helperCardInfo


    override val uiEventObservable: Observable<MoreDetailsUiEvent> = onActionSubject
    override var uiStateService: MoreDetailsUiState = MoreDetailsUiState()

    private var listOfCards = uiStateService.cards

    private lateinit var moreDetailsPane: TextView
    private lateinit var openURLButton: Button
    private lateinit var imageView: ImageView
    private lateinit var spinnerSource: Spinner
    private lateinit var sourceInfoPane: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_info)

        initModule()
        initProperties()
        initArtistName()
        initObservers()
        initListeners()
        notifyCreated()

    }

    private fun initListeners() {
        initURLButtonListener()
        initSpinnerListener()
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
        openURLButton = findViewById(R.id.openUrlButton)
        imageView = findViewById(R.id.imageView)
        sourceInfoPane = findViewById(R.id.sourceLabel)
        spinnerSource = findViewById(R.id.spinner)
    }

    private fun initArtistName() {
        uiStateService =
            uiStateService.copy(artistName = intent.getStringExtra(ARTIST_NAME_EXTRA).toString())
    }

    private fun enableActions(enable: Boolean) {
        runOnUiThread {
            openURLButton.isEnabled = enable
            spinnerSource.isEnabled = enable
        }
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
                uiStateService = uiStateService.copy(indexSpinner = position)
                updateCard(listOfCards)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                updateCard(listOfCards)
            }
        }
    }

    private fun initURLButtonListener() {
        openURLButton.setOnClickListener {
            notifyFullCardAction()
        }
    }

    override fun openCardURLActivity() {
        val card = uiStateService.getCurrentCard()
        openExternalUrl(card.infoURL)
    }

    private fun initObservers() {
        moreDetailsModel.cardObservable()
            .subscribe { value ->
                run {
                    uiStateService = uiStateService.copy(cards = value)
                    initSpinner()
                }
            }
    }

    private fun initSpinner() {
        listOfCards = uiStateService.cards
        val spinnerNames: MutableList<String> = mutableListOf()
        spinnerNames.addAll(listOfCards.map { it.source.service })
        if (spinnerNames.isEmpty()) {
            addNoResultsCard(spinnerNames)
            setDisabledAction()
            updateMoreDetailsState()
        }

        runOnUiThread {
            spinnerSource.adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerNames)
        }
    }

    private fun addNoResultsCard(spinnerNames: MutableList<String>) {
        listOfCards = mutableListOf(NoResultsCard)
        spinnerNames.add(NoResultsCard.source.service)
    }

    private fun setDisabledAction() {
        uiStateService = uiStateService.copy(actionsEnabled = false)
    }

    private fun updateMoreDetailsState() {
        enableActions(uiStateService.actionsEnabled)
    }

    private fun notifyFullCardAction() {
        onActionSubject.notify(MoreDetailsUiEvent.ViewFullCard)
    }

    override fun updateCard(cards: List<Card>) {
        updateUiState(cards)
        updateArtistInfoUI()
    }

    private fun updateUiState(cards: List<Card>) {
        when {
            cards.isEmpty() -> updateNoResultsUiState()
            else -> updateResultsUiState(cards)
        }
    }

    private fun updateResultsUiState(cards: List<Card>) {
        uiStateService = uiStateService.copy(cards = cards)
    }

    private fun updateNoResultsUiState() {
        uiStateService = uiStateService.copy(cards = listOf())
    }

    private fun updateArtistInfoUI() {
        runOnUiThread {
            loadServiceImage()
            loadArtistInfo()
            loadSourceInfo()
        }
    }

    private fun loadServiceImage() {
        UtilsModule.imageLoader.loadImageIntoView(
            uiStateService.getCurrentCard().sourceLogoURL,
            imageView
        )
    }

    private fun loadArtistInfo() {
        moreDetailsPane.text = HtmlCompat.fromHtml(
            helperCardInfo.getTextToHtml(
                uiStateService.getCurrentCard().description,
                uiStateService.artistName
            ), HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    private fun loadSourceInfo() {
        val source = "FROM: " + uiStateService.getCurrentCard().source.service
        sourceInfoPane.text = source
    }

    companion object {
        const val ARTIST_NAME_EXTRA = ARTIST_NAME
    }
}