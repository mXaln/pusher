package org.bibletranslationtools.jvm.controls.skins

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
import org.bibletranslationtools.jvm.ui.filedataitem.MAX_CHAPTER_LENGTH
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
        filter.chapterProperty.bind(chapter.textProperty())
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
        filter.selectedLanguageProperty.bind(languagesList.selectionModel.selectedItemProperty())
        filter.selectedResourceTypeProperty.bind(resourceTypesList.selectionModel.selectedItemProperty())
        filter.selectedBookProperty.bind(booksList.selectionModel.selectedItemProperty())
        filter.selectedMediaExtensionProperty.bind(mediaExtensionsList.selectionModel.selectedItemProperty())
        filter.selectedMediaQualityProperty.bind(mediaQualitiesList.selectionModel.selectedItemProperty())
        filter.selectedGroupingProperty.bind(groupingsList.selectionModel.selectedItemProperty())
    }

    private fun loadFXML() {
        val loader = FXMLLoader(javaClass.getResource("/skins/FileDataFilter.fxml"))
        loader.setController(this)
        val root: Node = loader.load()
        children.add(root)
    }
}
