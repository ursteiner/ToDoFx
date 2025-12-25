package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.AppSettings
import com.github.ursteiner.todofx.constants.AvailableLanguages
import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.database.CategoryDatabaseServiceImpl
import com.github.ursteiner.todofx.database.NaiveBayesModelServiceImpl
import com.github.ursteiner.todofx.database.SettingsDatabaseServiceImpl
import com.github.ursteiner.todofx.database.TaskDatabaseServiceImpl
import com.github.ursteiner.todofx.model.Category
import com.github.ursteiner.todofx.service.NaiveBayesClassification
import com.github.ursteiner.todofx.view.FxUtils
import com.github.ursteiner.todofx.viewModel.CategoryViewModel
import com.github.ursteiner.todofx.viewModel.ClassificationModelView
import com.github.ursteiner.todofx.viewModel.SettingsViewModel
import com.github.ursteiner.todofx.viewModel.TaskViewModel
import javafx.application.HostServices
import javafx.application.Platform
import javafx.beans.property.SimpleListProperty
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
    private lateinit var lastModelUpdate: Button

    @FXML
    private lateinit var selectLanguageComboBox: ComboBox<String>

    lateinit var toDoFxController: ToDoFxController
    private var categoryViewModel: CategoryViewModel = CategoryViewModel(CategoryDatabaseServiceImpl.getInstance())
    private var settingsViewModel: SettingsViewModel = SettingsViewModel(SettingsDatabaseServiceImpl.getInstance())
    private var taskViewModel: TaskViewModel = TaskViewModel(TaskDatabaseServiceImpl.getInstance())
    private var classificationViewModel: ClassificationModelView = ClassificationModelView(NaiveBayesModelServiceImpl.getInstance())

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        initTranslations()
        settingsViewModel.loadSettings()
        initLanguageComboBox()

        categoriesListView.itemsProperty().bind(SimpleListProperty(categoryViewModel.categoryNames))
        categoryViewModel.loadCategories()
    }

    override fun initTranslations() {
        categoriesTitledPane.text = getTranslation(TranslationKeys.CATEGORIES)

        addCategoryButton.text = getTranslation(TranslationKeys.ADD_CATEGORY)
        updateCategoryButton.text = getTranslation(TranslationKeys.UPDATE)
        deleteCategoryButton.text = getTranslation(TranslationKeys.DELETE_CATEGORY)

        updateModelLabel()
    }

    fun initLanguageComboBox() {
        selectLanguageComboBox.items.setAll(
            AvailableLanguages.GERMAN.abbreviation,
            AvailableLanguages.ENGLISH.abbreviation
        )
        when (settingsViewModel.getSetting(AppSettings.LANGUAGE) ?: System.getProperty("user.language")) {
            AvailableLanguages.GERMAN.abbreviation -> selectLanguageComboBox.selectionModel.select(0)
            AvailableLanguages.ENGLISH.abbreviation -> selectLanguageComboBox.selectionModel.select(1)
        }
    }

    @FXML
    fun onAddCategoryButtonClick() {
        if (categoryTextField.text.isBlank()) {
            return
        }

        if (categoryViewModel.categories.any { it.name == categoryTextField.text }) {
            FxUtils.createMessageDialog(
                getTranslation(TranslationKeys.DUPLICATE_CATEGORY),
                "${getTranslation(TranslationKeys.CATEGORY_IS_ALREADY_AVAILABLE)}: ${categoryTextField.text}"
            )
            return
        }

        categoryViewModel.addCategory(Category(categoryTextField.text, 0))
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

        categoryViewModel.categories.filter { it.name == selectedCategory }.forEach { category ->
            category.name = categoryTextField.text
            categoryViewModel.updateCategory(category)
        }

        categoryTextField.text = ""
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
            categoryViewModel.categories.filter { it.name == selectedCategory }.forEach { categoryViewModel.deleteCategory(it) }
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

    @FXML
    fun onLanguageSelectedComboBox() {
        val selectedLanguage = selectLanguageComboBox.selectionModel.selectedItem

        when (selectedLanguage) {
            AvailableLanguages.GERMAN.abbreviation -> setLanguage(AvailableLanguages.GERMAN)
            AvailableLanguages.ENGLISH.abbreviation -> setLanguage(AvailableLanguages.ENGLISH)
        }
        settingsViewModel.updateSetting(AppSettings.LANGUAGE, selectedLanguage)
        initTranslations()
        //Change the language of the parent controller taking care of the tab names
        toDoFxController.initialize(null, null)
    }

    @FXML
    fun onRecreateNaiveBayesModel() {
        val tasks = taskViewModel.getTaskList()
        val taskClassification = NaiveBayesClassification()

        tasks.forEach {
            taskClassification.train(it.name, it.category ?: "")
        }

        classificationViewModel.updateClassificationModel(taskClassification.exportModel())
        updateModelLabel()
    }

    @FXML
    fun openLink() {
        Platform.runLater {
            hostServices.showDocument("https://github.com/ursteiner/ToDoFx")
        }
    }

    private fun updateModelLabel(){
        lastModelUpdate.text = getTranslation(TranslationKeys.GENERATE_NEW_CLASSIFICATION_MODEL) +
                " (" + classificationViewModel.getModelDate() + ")"
    }
}