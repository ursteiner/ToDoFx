package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import java.net.URL
import java.util.*


class SettingsController: CommonController() {

    @FXML
    private lateinit var categoriesListView: ListView<String>

    @FXML
    private lateinit var categoryTextField: TextField

    @FXML
    private lateinit var addCategoryButton: Button

    @FXML
    private lateinit var deleteCategoryButton: Button

    var listProperty: ListProperty<String> = SimpleListProperty()
    private lateinit var categories: ObservableList<String>

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        addCategoryButton.text = getTranslation(TranslationKeys.ADD_CATEGORY)
        deleteCategoryButton.text = getTranslation(TranslationKeys.DELETE_CATEGORY)

        //TODO replace static category List with entries from database
        categories = FXCollections.observableArrayList()
        categories.add("Privat")
        categories.add("Firma")

        categoriesListView.itemsProperty().bind(listProperty)
        listProperty.set(categories)
    }

    @FXML
    fun onAddCategoryButtonClick(){
        if(categoryTextField.text != ""){
            categories.add(categoryTextField.text)
            categoriesListView.refresh()

            categoryTextField.text = ""
        }
    }

    @FXML
    fun onDeleteCategoryButtonClick(){
        if(categoriesListView.selectionModel.selectedItem == null){
            return
        }

        categories.remove(categoriesListView.selectionModel.selectedItem)
    }
}