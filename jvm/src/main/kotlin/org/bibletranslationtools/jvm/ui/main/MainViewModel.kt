package org.bibletranslationtools.jvm.ui.main

import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javafx.beans.property.SimpleListProperty
import org.bibletranslationtools.common.data.Grouping
import org.bibletranslationtools.common.data.MediaExtension
import org.bibletranslationtools.common.data.MediaQuality
import org.bibletranslationtools.common.data.ResourceType
import org.bibletranslationtools.common.usecases.ParseFileName
import org.bibletranslationtools.common.usecases.ValidateFile
import org.bibletranslationtools.jvm.io.BooksReader
import org.bibletranslationtools.jvm.io.LanguagesReader
import org.bibletranslationtools.jvm.ui.FileDataItem
import org.bibletranslationtools.mappers.FileDataMapper
import tornadofx.*
import java.io.File
import java.text.MessageFormat

class MainViewModel : ViewModel() {

    val fileDataList = observableListOf<FileDataItem>()
    val fileDataListProperty = SimpleListProperty<FileDataItem>(fileDataList)

    val languages = observableListOf<String>()
    val resourceTypes = ResourceType.values().toList().toObservable()
    val books = observableListOf<String>()
    val mediaExtensions = MediaExtension.values().toList().toObservable()
    val mediaQualities = MediaQuality.values().toList().toObservable()
    val groupings = Grouping.values().toList().toObservable()

    val snackBarObservable: PublishSubject<String> = PublishSubject.create()

    init {
        loadLanguages()
        loadBooks()
    }

    fun onDropFiles(files: List<File>) {
        files.forEach {
            if (it.isDirectory) {
                importFolder(it)
            } else {
                importFile(it)
            }
        }
    }

    private fun importFile(file: File) {
        if (file.isDirectory) return
        ValidateFile(file).validate()
            .subscribeOn(Schedulers.io())
            .observeOnFx()
            .doOnError {
                val notImportedText = MessageFormat.format(messages["notImported"], file.name)
                snackBarObservable.onNext("$notImportedText ${it.message ?: ""}")
            }
            .toSingleDefault(true)
            .onErrorReturnItem(false)
            .subscribe { success ->
                if (success) {
                    parseFileName(file)
                }
            }
    }

    private fun importFolder(folder: File) {
        if (!folder.isDirectory) return

        folder.walk().forEach {
            importFile(it)
        }
    }

    private fun parseFileName(file: File) {
        ParseFileName(file).parse()
            .subscribeOn(Schedulers.io())
            .observeOnFx()
            .subscribe { fileData, error ->
                when {
                    fileData != null -> {
                        val fileDataItem = FileDataMapper().fromEntity(fileData)
                        if (!fileDataList.contains(fileDataItem)) {
                            fileDataList.add(fileDataItem)
                        }
                    }
                    error != null -> {
                        val notImportedText = MessageFormat.format(messages["notImported"], file.name)
                        snackBarObservable.onNext("$notImportedText ${error.message ?: ""}")
                    }
                }
            }
    }

    private fun loadLanguages() {
        LanguagesReader().read()
            .subscribeOn(Schedulers.io())
            .observeOnFx()
            .subscribe { _languages ->
                languages.addAll(_languages)
            }
    }

    private fun loadBooks() {
        BooksReader().read()
            .subscribeOn(Schedulers.io())
            .observeOnFx()
            .subscribe { _books ->
                books.addAll(_books)
            }
    }
}
