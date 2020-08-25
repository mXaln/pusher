package org.bibletranslationtools.jvm.controls.skins

import javafx.beans.property.Property
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.SkinBase
import javafx.scene.control.TextField
import org.bibletranslationtools.common.data.Grouping
import org.bibletranslationtools.common.data.MediaExtension
import org.bibletranslationtools.common.data.MediaQuality
import org.bibletranslationtools.common.data.ResourceType
import org.bibletranslationtools.jvm.controls.filedatafilter.FileDataFilter
import org.bibletranslationtools.jvm.controls.filedatafilter.MAX_CHAPTER_LENGTH
import tornadofx.*

class FileDataFilterSkin(private val filter: FileDataFilter) : SkinBase<FileDataFilter>(filter) {

    @FXML
    private lateinit var languageLabel: Label

    @FXML
    private lateinit var languagesList: ComboBox<String>

    @FXML
    private lateinit var resourceTypeLabel: Label

    @FXML
    private lateinit var resourceTypesList: ComboBox<ResourceType>

    @FXML
    private lateinit var bookLabel: Label

    @FXML
    private lateinit var booksList: ComboBox<String>

    @FXML
    private lateinit var chapterLabel: Label

    @FXML
    private lateinit var chapter: TextField

    @FXML
    private lateinit var mediaExtensionLabel: Label

    @FXML
    private lateinit var mediaExtensionsList: ComboBox<MediaExtension>

    @FXML
    private lateinit var mediaQualityLabel: Label

    @FXML
    private lateinit var mediaQualitiesList: ComboBox<MediaQuality>

    @FXML
    private lateinit var groupingLabel: Label

    @FXML
    private lateinit var groupingsList: ComboBox<Grouping>

    private var isSelectionChanging = false

    init {
        loadFXML()
        initialize()
    }

    private fun initialize() {
        initializeControls()
        initializeActions()
    }

    private fun initializeControls() {
        languageLabel.textProperty().bind(filter.languageLabelProperty)
        languagesList.itemsProperty().bind(filter.languagesProperty)

        resourceTypeLabel.textProperty().bind(filter.resourceTypeLabelProperty)
        resourceTypesList.itemsProperty().bind(filter.resourceTypesProperty)

        bookLabel.textProperty().bind(filter.bookLabelProperty)
        booksList.itemsProperty().bind(filter.booksProperty)

        chapterLabel.textProperty().bind(filter.chapterLabelProperty)
        chapter.filterInput {
            it.controlNewText.isInt() && it.controlNewText.length <= MAX_CHAPTER_LENGTH
        }

        mediaExtensionLabel.textProperty().bind(filter.mediaExtensionLabelProperty)
        mediaExtensionsList.itemsProperty().bind(filter.mediaExtensionsProperty)

        mediaQualityLabel.textProperty().bind(filter.mediaQualityLabelProperty)
        mediaQualitiesList.itemsProperty().bind(filter.mediaQualitiesProperty)

        groupingLabel.textProperty().bind(filter.groupingLabelProperty)
        groupingsList.itemsProperty().bind(filter.groupingsProperty)
    }

    private fun initializeActions() {
        languagesList.selectionModel.selectedItemProperty().addListener { _, old, new ->
            itemSelectionAction(filter.selectedLanguageProperty, languagesList, old, new)
        }

        resourceTypesList.selectionModel.selectedItemProperty().addListener { _, old, new ->
            itemSelectionAction(filter.selectedResourceTypeProperty, resourceTypesList, old, new)
        }

        booksList.selectionModel.selectedItemProperty().addListener { _, old, new ->
            itemSelectionAction(filter.selectedBookProperty, booksList, old, new)
        }

        chapter.setOnAction {
            filter.onConfirmActionProperty.value.handle(ActionEvent())
            filter.callbackObserver?.subscribe { answer ->
                if (answer) {
                    filter.chapterProperty.value = chapter.text
                } else {
                    chapter.text = null
                }
            }
        }

        mediaExtensionsList.selectionModel.selectedItemProperty().addListener { _, old, new ->
            itemSelectionAction(filter.selectedMediaExtensionProperty, mediaExtensionsList, old, new)
        }

        mediaQualitiesList.selectionModel.selectedItemProperty().addListener { _, old, new ->
            itemSelectionAction(filter.selectedMediaQualityProperty, mediaQualitiesList, old, new)
        }

        groupingsList.selectionModel.selectedItemProperty().addListener { _, old, new ->
            itemSelectionAction(filter.selectedGroupingProperty, groupingsList, old, new)
        }
    }

    private fun <T> itemSelectionAction(property: Property<T>, box: ComboBox<T>, old: T, new: T) {
        if (!isSelectionChanging) {
            isSelectionChanging = true
            filter.onConfirmActionProperty.value.handle(ActionEvent())
            filter.callbackObserver?.subscribe { answer ->
                if (answer) {
                    property.value = new
                    isSelectionChanging = false
                } else {
                    runLater {
                        box.selectionModel.select(old)
                        isSelectionChanging = false
                    }
                }
            }
        }
    }

    private fun loadFXML() {
        val loader = FXMLLoader(javaClass.getResource("/skins/FileDataFilter.fxml"))
        loader.setController(this)
        val root: Node = loader.load()
        children.add(root)
    }
}
