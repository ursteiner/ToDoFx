package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.AvailableLanguages
import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.model.Category
import com.github.ursteiner.todofx.view.FxUtils
import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
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
    private lateinit var updateCategoryButton: Button

    @FXML
    private lateinit var deleteCategoryButton: Button

    @FXML
    private lateinit var selectLanguageComboBox: ComboBox<String>

    private var listProperty: ListProperty<String> = SimpleListProperty()
    private var categoriesObservable = FXCollections.observableArrayList<String>()
    private val categories = mutableListOf<Category>()

    lateinit var toDoFxController: ToDoFxController

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        initTranslations()
        initComboBox()

        categoriesListView.itemsProperty().bind(listProperty)
        listProperty.set(categoriesObservable)
    }

    fun initTranslations(){
        categoriesTitledPane.text = getTranslation(TranslationKeys.CATEGORIES)

        addCategoryButton.text = getTranslation(TranslationKeys.ADD_CATEGORY)
        updateCategoryButton.text = getTranslation(TranslationKeys.UPDATE)
        deleteCategoryButton.text = getTranslation(TranslationKeys.DELETE_CATEGORY)
    }

    fun initComboBox(){
        selectLanguageComboBox.items.clear()
        selectLanguageComboBox.items.add("de")
        selectLanguageComboBox.items.add("en")
        when(System.getProperty("user.language")){
            "de" -> selectLanguageComboBox.selectionModel.select(0)
            "en" -> selectLanguageComboBox.selectionModel.select(1)
        }
    }

    @FXML
    fun onAddCategoryButtonClick(){
        if(categoryTextField.text == ""){
           return
        }

        if(categories.any { it.name == categoryTextField.text }){
            FxUtils.createMessageDialog(getTranslation(TranslationKeys.DUPLICATE_CATEGORY),
                "${getTranslation(TranslationKeys.CATEGORY_IS_ALREADY_AVAILABLE)}: ${categoryTextField.text}")
            return
        }

        categoriesObservable.add(categoryTextField.text)
        getCategoryDatabase().addCategory(Category(categoryTextField.text, 0))
        initCategories()
        categoriesListView.refresh()
        categoryTextField.text = ""
    }

    @FXML
    fun onCategorySelected(){
        val selectedCategory = categoriesListView.selectionModel.selectedItem
        if(selectedCategory == null){
            return
        }

        categoryTextField.text = selectedCategory
    }

    @FXML
    fun onUpdateCategoryButtonClick(){
        val selectedCategory = categoriesListView.selectionModel.selectedItem
        if(categoryTextField.text == "" || selectedCategory == null){
            FxUtils.createMessageDialog(getTranslation(TranslationKeys.CATEGORIES),
                getTranslation(TranslationKeys.PLEASE_FIRST_SELECT_A_CATEGORY))
            return
        }

        categories.filter { it.name == selectedCategory }.forEach { category ->
            category.name = categoryTextField.text
            getCategoryDatabase().updateCategory(category)
        }

        categoryTextField.text = ""
        initCategories()
    }

    @FXML
    fun onDeleteCategoryButtonClick(){
        val selectedCategory = categoriesListView.selectionModel.selectedItem
        if(selectedCategory == null){
            FxUtils.createMessageDialog(getTranslation(TranslationKeys.CATEGORIES),
                getTranslation(TranslationKeys.PLEASE_FIRST_SELECT_A_CATEGORY))
            return
        }

        try {
            categories.filter { it.name == selectedCategory }.forEach { getCategoryDatabase().deleteCategory(it.id) }
            initCategories()
        }catch (ex: Exception){
            logger.error("Could not delete category ${ex.message}")
            FxUtils.createMessageDialog(getTranslation(TranslationKeys.COULD_NOT_DELETE_CATEGORY),
                "${getTranslation(TranslationKeys.CATEGORY_MIGHT_STILL_BE_IN_USE)}\n\n${ex.message}",
                Alert.AlertType.WARNING)
        }
    }

    @FXML
    fun onClearSelectedCategoryButtonClick(){
        categoryTextField.text = ""
        categoriesListView.selectionModel.select(null)
    }

    fun initCategories(){
        categoriesObservable.clear()

        categories.clear()
        categories.addAll(getCategoryDatabase().getCategories())
        categories.forEach {
            categoriesObservable.add(it.name)
        }
    }

    @FXML
    fun onLanguageSelectedComboBox(){
        val selectedLanguage = selectLanguageComboBox.selectionModel.selectedItem

        when(selectedLanguage){
            "de" -> setLanguage(AvailableLanguages.GERMAN)
            "en" -> setLanguage(AvailableLanguages.ENGLISH)
        }

        initTranslations()
        //Change the language of the parent controller taking care of the tab names
        toDoFxController.initialize(null, null)
    }
}