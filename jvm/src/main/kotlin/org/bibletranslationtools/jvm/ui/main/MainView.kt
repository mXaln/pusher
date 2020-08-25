package org.bibletranslationtools.jvm.ui.main

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXSnackbar
import com.jfoenix.controls.JFXSnackbarLayout
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
import org.bibletranslationtools.jvm.ui.filedatacell.filedatacell
import tornadofx.*

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
            addClass("main__drop-files")

            visibleWhen(viewModel.fileDataListProperty.emptyProperty())

            label(messages["dropFiles"])

            onDragOver = onDragOverHandler()
            onDragDropped = onDragDroppedHandler()
        }

        vbox {
            alignment = Pos.CENTER

            hiddenWhen(viewModel.fileDataListProperty.emptyProperty())

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
            }

            add(filter)

            datagrid(viewModel.fileDataList) {
                vgrow = Priority.ALWAYS
                addClass("main__file-list")

                cellWidthProperty.bind(this@datagrid.widthProperty().minus(35.0))
                maxCellsInRow = 1

                cellCache { item ->
                    filedatacell(item, filter)
                }

                onDragOver = onDragOverHandler()
                onDragDropped = onDragDroppedHandler()
            }

            hbox {
                addClass("main__footer")
                add(
                    JFXButton(messages["upload"]).apply {
                        addClass("btn", "btn--primary", "main__upload_btn")
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
}
