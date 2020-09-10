package org.bibletranslationtools.jvm.ui.main

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXSnackbar
import com.jfoenix.controls.JFXSnackbarLayout
import javafx.beans.binding.BooleanExpression
import javafx.beans.property.Property
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.input.DragEvent
import javafx.scene.input.TransferMode
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.util.Duration
import org.bibletranslationtools.assets.AppResources
import org.bibletranslationtools.jvm.controls.filedatafilter.filedatafilter
import org.bibletranslationtools.jvm.ui.FileDataItem
import org.bibletranslationtools.jvm.ui.filedatacell.filedatacell
import tornadofx.*
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

class MainView : View() {
    private val viewModel: MainViewModel by inject()

    private val filter = filedatafilter()

    init {
        title = messages["appName"]
        importStylesheet(AppResources.load("/css/main.css"))
    }

    override val root = stackpane {
        addClass("main")

        createSnackBar(this)

        vbox {
            alignment = Pos.CENTER

            addClass("main__progress")
            progressindicator()
            label(messages["processing"])

            visibleWhen(viewModel.isProcessing)
        }

        vbox {
            addClass("main__drop-files")

            visibleWhen {
                viewModel.fileDataListProperty.emptyProperty()
                    .and(viewModel.isProcessing.not())
            }

            label(messages["dropFiles"])

            onDragOver = onDragOverHandler()
            onDragDropped = onDragDroppedHandler()
        }

        vbox {
            alignment = Pos.CENTER

            hiddenWhen {
                viewModel.fileDataListProperty.emptyProperty()
                    .or(viewModel.isProcessing)
            }

            filter.apply {
                maxWidthProperty().bind(primaryStage.widthProperty())

                languageLabelProperty.set(messages["language"])
                languagesProperty.set(viewModel.languages)

                resourceTypeLabelProperty.set(messages["resourceType"])
                resourceTypesProperty.set(viewModel.resourceTypes)

                bookLabelProperty.set(messages["book"])
                booksProperty.set(viewModel.books)

                chapterLabelProperty.set(messages["chapter"])

                mediaExtensionLabelProperty.set(messages["mediaExtension"])
                mediaExtensionsProperty.set(viewModel.mediaExtensions)

                mediaQualityLabelProperty.set(messages["mediaQuality"])
                mediaQualitiesProperty.set(viewModel.mediaQualities)

                groupingLabelProperty.set(messages["grouping"])
                groupingsProperty.set(viewModel.groupings)

                onConfirmAction {
                    showConfirmDialog { answer ->
                        onConfirmCallback(answer)
                    }
                }

                setFilterChangeListeners()
            }

            add(filter)

            datagrid(viewModel.fileDataList) {
                vgrow = Priority.ALWAYS
                addClass("main__file-list")

                cellWidthProperty.bind(this@datagrid.widthProperty().minus(35.0))
                maxCellsInRow = 1

                cellCache { item ->
                    filedatacell(item)
                }

                onDragOver = onDragOverHandler()
                onDragDropped = onDragDroppedHandler()
            }

            hbox {
                addClass("main__footer")
                add(
                    JFXButton(messages["upload"]).apply {
                        addClass("btn", "btn--primary", "main__upload_btn")

                        setOnAction {
                            viewModel.upload()
                        }
                    }
                )
            }
        }
    }

    private fun onDragOverHandler(): EventHandler<DragEvent> {
        return EventHandler {
            if (it.gestureSource != this && it.dragboard.hasFiles()) {
                it.acceptTransferModes(TransferMode.COPY)
            }
            it.consume()
        }
    }

    private fun onDragDroppedHandler(): EventHandler<DragEvent> {
        return EventHandler {
            var success = false
            if (it.dragboard.hasFiles()) {
                viewModel.onDropFiles(it.dragboard.files)
                success = true
            }
            it.isDropCompleted = success
            it.consume()
        }
    }

    private fun createSnackBar(pane: Pane) {
        val snackBar = JFXSnackbar(pane)
        viewModel.snackBarObservable.subscribe { message ->
            snackBar.enqueue(
                JFXSnackbar.SnackbarEvent(
                    JFXSnackbarLayout(
                        message,
                        messages["ok"],
                        EventHandler { snackBar.close() }
                    ),
                    Duration.INDEFINITE,
                    null
                )
            )
        }
    }

    private fun showConfirmDialog(op: (answer: Boolean) -> Unit) {
        Alert(Alert.AlertType.CONFIRMATION).apply {
            title = messages["confirmSelection"]
            headerText = null
            contentText = messages["confirmSelectionQuestion"]

            val yesButton = ButtonType(messages["yes"])
            val noButton = ButtonType(messages["no"])

            buttonTypes.setAll(
                yesButton,
                noButton
            )

            val result = showAndWait()

            when (result.get()) {
                yesButton -> op.invoke(true)
                noButton -> op.invoke(false)
            }
        }
    }

    private fun setFilterChangeListeners() {
        setPropertyListener(
            filter.selectedLanguageProperty,
            FileDataItem::initLanguage,
            FileDataItem::language
        )

        setPropertyListener(
            filter.selectedResourceTypeProperty,
            FileDataItem::initResourceType,
            FileDataItem::resourceType
        )

        setPropertyListener(
            filter.selectedBookProperty,
            FileDataItem::initBook,
            FileDataItem::book
        )

        setPropertyListener(
            filter.chapterProperty,
            FileDataItem::initChapter,
            FileDataItem::chapter
        )

        setPropertyListener(
            filter.selectedMediaExtensionProperty,
            FileDataItem::initMediaExtension,
            FileDataItem::mediaExtension,
            FileDataItem::mediaExtensionAvailable
        )

        setPropertyListener(
            filter.selectedMediaQualityProperty,
            FileDataItem::initMediaQuality,
            FileDataItem::mediaQuality,
            FileDataItem::mediaQualityAvailable
        )

        setPropertyListener(
            filter.selectedGroupingProperty,
            FileDataItem::initGrouping,
            FileDataItem::grouping
        )
    }

    private fun <T> setPropertyListener(
        property: Property<T>,
        initProp: KProperty1<FileDataItem, T?>,
        targetProp: KMutableProperty1<FileDataItem, T?>,
        availableProp: KProperty1<FileDataItem, BooleanExpression>? = null
    ) {
        property.onChange {
            it?.let { prop ->
                viewModel.fileDataList.forEach { fileDataItem ->
                    val initValue = initProp.get(fileDataItem)
                    val available = availableProp?.get(fileDataItem)?.value ?: true
                    if (available && initValue == null) {
                        targetProp.set(fileDataItem, prop)
                    }
                }
            }
        }
    }
}
