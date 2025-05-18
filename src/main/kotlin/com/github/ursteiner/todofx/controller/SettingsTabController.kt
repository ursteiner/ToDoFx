package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.model.Category
import com.github.ursteiner.todofx.view.FxUtils
import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.control.TitledPane
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*


class SettingsTabController: CommonController() {

    private val logger = LoggerFactory.getLogger(SettingsTabController::class.java)

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
    private var categoriesObservable = FXCollections.observableArrayList<String>()
    private val categories = mutableListOf<Category>()

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        categoriesTitledPane.text = getTranslation(TranslationKeys.CATEGORIES)

        addCategoryButton.text = getTranslation(TranslationKeys.ADD_CATEGORY)
        deleteCategoryButton.text = getTranslation(TranslationKeys.DELETE_CATEGORY)

        categoriesListView.itemsProperty().bind(listProperty)
        listProperty.set(categoriesObservable)
    }

    @FXML
    fun onAddCategoryButtonClick(){
        if(categoryTextField.text != ""){
            categoriesObservable.add(categoryTextField.text)
            getCategoryDatabase().addCategory(Category(categoryTextField.text, 0))
            initCategories()
            categoriesListView.refresh()
            categoryTextField.text = ""
        }
    }

    @FXML
    fun onDeleteCategoryButtonClick(){
        val selectedCategory = categoriesListView.selectionModel.selectedItem
        if(selectedCategory == null){
            FxUtils.createMessageDialog(getTranslation(TranslationKeys.CATEGORIES), getTranslation(TranslationKeys.PLEASE_FIRST_SELECT_A_CATEGORY))
            return
        }

        try {
            categories.filter { it.name == selectedCategory }.forEach { getCategoryDatabase().deleteCategory(it.id) }
            initCategories()
        }catch (ex: Exception){
            logger.error("Could not delete category ${ex.message}")
            FxUtils.createMessageDialog("Could not delete category", "Category might still be in use:\n\n${ex.message}", Alert.AlertType.WARNING)
        }
    }

    fun initCategories(){
        categoriesObservable.clear()

        categories.clear()
        categories.addAll(getCategoryDatabase().getCategories())
        categories.forEach {
            categoriesObservable.add(it.name)
        }
    }
}