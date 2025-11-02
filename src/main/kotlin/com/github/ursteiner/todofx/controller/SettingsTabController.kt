package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.AppSettings
import com.github.ursteiner.todofx.constants.AvailableLanguages
import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.model.Category
import com.github.ursteiner.todofx.view.FxUtils
import javafx.application.HostServices
import javafx.application.Platform
import javafx.beans.property.ListProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import org.slf4j.LoggerFactory
import java.net.URL
import java.util.*


class SettingsTabController : CommonController() {

    private val logger = LoggerFactory.getLogger(SettingsTabController::class.java)
    lateinit var hostServices: HostServices

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

    override fun initTranslations() {
        categoriesTitledPane.text = getTranslation(TranslationKeys.CATEGORIES)

        addCategoryButton.text = getTranslation(TranslationKeys.ADD_CATEGORY)
        updateCategoryButton.text = getTranslation(TranslationKeys.UPDATE)
        deleteCategoryButton.text = getTranslation(TranslationKeys.DELETE_CATEGORY)
    }

    fun initComboBox() {
        selectLanguageComboBox.items.clear()
        selectLanguageComboBox.items.add(AvailableLanguages.GERMAN.abbreviation)
        selectLanguageComboBox.items.add(AvailableLanguages.ENGLISH.abbreviation)
        when (getSettingsDatabase().getSetting(AppSettings.LANGUAGE) ?: System.getProperty("user.language")) {
            AvailableLanguages.GERMAN.abbreviation -> selectLanguageComboBox.selectionModel.select(0)
            AvailableLanguages.ENGLISH.abbreviation -> selectLanguageComboBox.selectionModel.select(1)
        }
    }

    @FXML
    fun onAddCategoryButtonClick() {
        if (categoryTextField.text.isBlank()) {
            return
        }

        if (categories.any { it.name == categoryTextField.text }) {
            FxUtils.createMessageDialog(
                getTranslation(TranslationKeys.DUPLICATE_CATEGORY),
                "${getTranslation(TranslationKeys.CATEGORY_IS_ALREADY_AVAILABLE)}: ${categoryTextField.text}"
            )
            return
        }

        categoriesObservable.add(categoryTextField.text)
        getCategoryDatabase().addCategory(Category(categoryTextField.text, 0))
        initCategories()
        categoriesListView.refresh()
        categoryTextField.text = ""
    }

    @FXML
    fun onCategorySelected() {
        val selectedCategory = categoriesListView.selectionModel.selectedItem ?: return
        categoryTextField.text = selectedCategory
    }

    @FXML
    fun onUpdateCategoryButtonClick() {
        val selectedCategory = categoriesListView.selectionModel.selectedItem
        if (categoryTextField.text == "" || selectedCategory == null) {
            FxUtils.createMessageDialog(
                getTranslation(TranslationKeys.CATEGORIES),
                getTranslation(TranslationKeys.PLEASE_FIRST_SELECT_A_CATEGORY)
            )
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
    fun onDeleteCategoryButtonClick() {
        val selectedCategory = categoriesListView.selectionModel.selectedItem
        if (selectedCategory == null) {
            FxUtils.createMessageDialog(
                getTranslation(TranslationKeys.CATEGORIES),
                getTranslation(TranslationKeys.PLEASE_FIRST_SELECT_A_CATEGORY)
            )
            return
        }

        try {
            categories.filter { it.name == selectedCategory }.forEach { getCategoryDatabase().deleteCategory(it.id) }
            initCategories()
        } catch (ex: Exception) {
            logger.error("Could not delete category ${ex.message}")
            FxUtils.createMessageDialog(
                getTranslation(TranslationKeys.COULD_NOT_DELETE_CATEGORY),
                "${getTranslation(TranslationKeys.CATEGORY_MIGHT_STILL_BE_IN_USE)}\n\n${ex.message}",
                Alert.AlertType.WARNING
            )
        }
    }

    @FXML
    fun onClearSelectedCategoryButtonClick() {
        categoryTextField.text = ""
        categoriesListView.selectionModel.select(null)
    }

    fun initCategories() {
        categories.clear()
        categories.addAll(getCategoryDatabase().getCategories())
        categoriesObservable.setAll(categories.map { it.name })
    }

    @FXML
    fun onLanguageSelectedComboBox() {
        val selectedLanguage = selectLanguageComboBox.selectionModel.selectedItem

        when (selectedLanguage) {
            AvailableLanguages.GERMAN.abbreviation -> setLanguage(AvailableLanguages.GERMAN)
            AvailableLanguages.ENGLISH.abbreviation -> setLanguage(AvailableLanguages.ENGLISH)
        }
        getSettingsDatabase().updateSetting(AppSettings.LANGUAGE, selectedLanguage)
        initTranslations()
        //Change the language of the parent controller taking care of the tab names
        toDoFxController.initialize(null, null)
    }

    @FXML
    fun openLink() {
        Platform.runLater({
            hostServices.showDocument("https://github.com/ursteiner/ToDoFx")
        })
    }
}