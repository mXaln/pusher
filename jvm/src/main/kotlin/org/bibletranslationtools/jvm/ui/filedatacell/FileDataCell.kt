package org.bibletranslationtools.jvm.ui.filedatacell

import com.jfoenix.controls.JFXComboBox
import com.jfoenix.controls.JFXTextField
import javafx.event.EventTarget
import javafx.scene.control.ListCell
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.bibletranslationtools.assets.AppResources
import org.bibletranslationtools.common.data.Grouping
import org.bibletranslationtools.common.data.MediaExtension
import org.bibletranslationtools.common.data.MediaQuality
import org.bibletranslationtools.common.data.ResourceType
import org.bibletranslationtools.jvm.controls.filedatafilter.FileDataFilter
import org.bibletranslationtools.jvm.controls.filedatafilter.MAX_CHAPTER_LENGTH
import org.bibletranslationtools.jvm.ui.FileDataItem
import org.bibletranslationtools.jvm.ui.main.MainViewModel
import tornadofx.*

class FileDataCell(
    private val fileDataItem: FileDataItem,
    private val filter: FileDataFilter
) : VBox() {

    private val mainViewModel = find<MainViewModel>()

    private val initLanguage = fileDataItem.language
    private val initResourceType = fileDataItem.resourceType
    private val initBook = fileDataItem.book
    private val initChapter = fileDataItem.chapter
    private val initGrouping = fileDataItem.grouping

    init {
        importStylesheet(AppResources.load("/css/file-data-cell.css"))
        addClass("file-data-cell")

        hbox {
            label(fileDataItem.file.name)
            addClass("file-data-cell__title")
        }
        hbox {
            addClass("file-data-cell__options")
            spacing = 10.0
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["language"])
                add(
                    JFXComboBox<String>(mainViewModel.languages).apply {
                        addClass("file-data-cell__dropdown")

                        selectionModel.select(fileDataItem.language)
                        bindSelected(fileDataItem.languageProperty)
                        filter.selectedLanguageProperty.onChange {
                            if (fileDataItem.language == null) {
                                selectionModel.select(it)
                            }
                        }

                        isDisable = !initLanguage.isNullOrEmpty()
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["resourceType"])
                add(
                    JFXComboBox<ResourceType>(mainViewModel.resourceTypes).apply {
                        addClass("file-data-cell__dropdown")

                        selectionModel.select(fileDataItem.resourceType)
                        bindSelected(fileDataItem.resourceTypeProperty)
                        filter.selectedResourceTypeProperty.onChange {
                            if (fileDataItem.resourceType == null) {
                                selectionModel.select(it)
                            }
                        }

                        isDisable = initResourceType != null
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["book"])
                add(
                    JFXComboBox<String>(mainViewModel.books).apply {
                        addClass("file-data-cell__dropdown")

                        selectionModel.select(fileDataItem.book)
                        bindSelected(fileDataItem.bookProperty)
                        filter.selectedBookProperty.onChange {
                            if (fileDataItem.book == null) {
                                selectionModel.select(it)
                            }
                        }

                        isDisable = !initBook.isNullOrEmpty()
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["chapter"])
                add(
                    JFXTextField().apply {
                        addClass("file-data-cell__chapter")

                        text = fileDataItem.chapter?.toString() ?: ""
                        fileDataItem.chapterProperty.bind(textProperty())
                        filter.chapterProperty.onChange {
                            if (initChapter.isNullOrEmpty()) {
                                text = it
                            }
                        }

                        filterInput {
                            it.controlNewText.isInt() && it.controlNewText.length <= MAX_CHAPTER_LENGTH
                        }

                        isDisable = !initChapter.isNullOrEmpty()
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["mediaExtension"])
                add(
                    JFXComboBox<MediaExtension>(mainViewModel.mediaExtensions).apply {
                        addClass("file-data-cell__dropdown")
                        enableWhen(fileDataItem.mediaExtensionAvailable)

                        selectionModel.select(fileDataItem.mediaExtension)
                        bindSelected(fileDataItem.mediaExtensionProperty)
                        filter.selectedMediaExtensionProperty.onChange {
                            if (fileDataItem.mediaExtensionAvailable.value) {
                                selectionModel.select(it)
                            }
                        }
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["mediaQuality"])
                add(
                    JFXComboBox<MediaQuality>(mainViewModel.mediaQualities).apply {
                        addClass("file-data-cell__dropdown")
                        enableWhen(fileDataItem.mediaQualityAvailable)

                        selectionModel.select(fileDataItem.mediaQuality)
                        bindSelected(fileDataItem.mediaQualityProperty)
                        filter.selectedMediaQualityProperty.onChange {
                            if (fileDataItem.mediaQualityAvailable.value && fileDataItem.mediaQuality == null) {
                                selectionModel.select(it)
                            }
                        }
                    }
                )
            }
            vbox {
                hgrow = Priority.ALWAYS
                label(FX.messages["grouping"])
                add(
                    JFXComboBox<Grouping>(mainViewModel.groupings).apply {
                        addClass("file-data-cell__dropdown")

                        selectionModel.select(fileDataItem.grouping)
                        bindSelected(fileDataItem.groupingProperty)
                        filter.selectedGroupingProperty.onChange {
                            val notRestricted = !mainViewModel.restrictedGroupings(fileDataItem.file).contains(it)
                            if (notRestricted && fileDataItem.grouping == null) {
                                selectionModel.select(it)
                            }
                        }

                        isDisable = initGrouping != null

                        setCellFactory {
                            object : ListCell<Grouping>() {
                                override fun updateItem(item: Grouping?, empty: Boolean) {
                                    super.updateItem(item, empty)
                                    text = item?.toString() ?: ""
                                    if (mainViewModel.restrictedGroupings(fileDataItem.file).contains(item)) {
                                        isDisable = true
                                        opacity = 0.5
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

fun EventTarget.filedatacell(
    data: FileDataItem,
    filter: FileDataFilter,
    op: FileDataCell.() -> Unit = {}
): FileDataCell {
    val fileDataItem = FileDataCell(data, filter)
    return opcr(this, fileDataItem, op)
}
