package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.model.Category
import com.github.ursteiner.todofx.view.FxUtils
import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.control.TitledPane
import java.net.URL
import java.util.*


class SettingsController: CommonController() {

    @FXML
    private lateinit var categoriesTitledPane: TitledPane

    @FXML
    private lateinit var categoriesListView: ListView<String>

    @FXML
    private lateinit var categoryTextField: TextField

    @FXML
    private lateinit var addCategoryButton: Button

    @FXML
    private lateinit var deleteCategoryButton: Button

    private var listProperty: ListProperty<String> = SimpleListProperty()
    private lateinit var categoriesObservable: ObservableList<String>
    private val categories = mutableListOf<Category>()

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        categoriesTitledPane.text = getTranslation(TranslationKeys.CATEGORIES)

        addCategoryButton.text = getTranslation(TranslationKeys.ADD_CATEGORY)
        deleteCategoryButton.text = getTranslation(TranslationKeys.DELETE_CATEGORY)

        initCategories()

        categoriesListView.itemsProperty().bind(listProperty)
        listProperty.set(categoriesObservable)
    }

    @FXML
    fun onAddCategoryButtonClick(){
        if(categoryTextField.text != ""){
            categoriesObservable.add(categoryTextField.text)
            getDatabase().addCategory(Category(categoryTextField.text, 0))
            initCategories()
            categoriesListView.refresh()
            categoryTextField.text = ""
        }
    }

    @FXML
    fun onDeleteCategoryButtonClick(){
        val selectedCategory = categoriesListView.selectionModel.selectedItem
        if(categoriesListView.selectionModel.selectedItem == null){
            FxUtils.createMessageDialog(getTranslation(TranslationKeys.CATEGORIES), getTranslation(TranslationKeys.PLEASE_FIRST_SELECT_A_CATEGORY))
            return
        }

        try {
            categories.filter { it.name == selectedCategory }.forEach { getDatabase().deleteCategory(it.id) }
            initCategories()
        }catch (ex: Exception){
            //TODO missing translation
            FxUtils.createMessageDialog("Could not delete category", "Category might still be in use:\n\n${ex.message}", Alert.AlertType.WARNING)
        }
    }

    fun initCategories(){
        categories.clear()
        categories.addAll(getDatabase().getCategories())
        categoriesObservable = FXCollections.observableArrayList()
        categories.forEach {
            categoriesObservable.add(it.name)
        }
    }
}