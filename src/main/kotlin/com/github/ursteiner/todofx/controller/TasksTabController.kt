package com.github.ursteiner.todofx.controller

import com.github.ursteiner.todofx.constants.TranslationKeys
import com.github.ursteiner.todofx.model.Category
import com.github.ursteiner.todofx.model.FXTask
import com.github.ursteiner.todofx.view.FxUtils
import com.github.ursteiner.todofx.viewModel.ViewModelProvider
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class TasksTabController : CommonController() {
    @FXML
    private lateinit var taskNameInput: TextArea

    @FXML
    private lateinit var taskUpdateArea: TextArea

    @FXML
    private lateinit var tableView: TableView<FXTask>

    @FXML
    private lateinit var hideDoneTasksCheckBox: CheckBox

    @FXML
    private lateinit var updateCategoryComboBox: ComboBox<Category>

    @FXML
    private lateinit var updateTaskButton: Button

    @FXML
    private lateinit var newCategoryComboBox: ComboBox<Category>

    @FXML
    private lateinit var addTaskButton: Button

    @FXML
    private lateinit var resolveTaskButton: Button

    @FXML
    private lateinit var deleteTaskButton: Button

    @FXML
    private lateinit var searchButton: Button

    @FXML
    private lateinit var searchTextField: TextField

    @FXML
    private lateinit var idColumn: TableColumn<String, String>

    @FXML
    private lateinit var descriptionColumn: TableColumn<String, String>

    @FXML
    private lateinit var dateColumn: TableColumn<String, String>

    @FXML
    private lateinit var categoryColumn: TableColumn<String, String>

    @FXML
    private lateinit var doneColumn: TableColumn<String, String>

    @FXML
    private lateinit var resolvedDateColumn: TableColumn<String, String>

    @FXML
    private lateinit var editTaskPane: TitledPane

    @FXML
    private lateinit var newTaskPane: TitledPane

    @FXML
    private lateinit var numberOfTasks: Label

    private val dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    private val categoryViewModel = ViewModelProvider.categoryViewModel
    private val taskViewModel = ViewModelProvider.taskViewModel
    private val classificationViewModel = ViewModelProvider.classificationViewModel

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        tableView.items = taskViewModel.tasks
        setUpdateTaskVisibility(false)

        initTranslations()
        initDropDowns()
        updateAmountOfTasksLabel()
    }

    fun initDropDowns(){
        categoryViewModel.loadCategories()

        initCategoryComboBox(newCategoryComboBox, categoryViewModel.categories)
        newCategoryComboBox.selectionModel.selectFirst()

        initCategoryComboBox(updateCategoryComboBox, categoryViewModel.categories)
    }

    private fun initCategoryComboBox(comboBox: ComboBox<Category>, categories: List<Category>) {
        comboBox.items.clear()
        comboBox.items.add(Category("", -1))
        comboBox.items.addAll(categories)
    }

    @Suppress("unused")
    @FXML
    private fun onAddTaskButtonClick() {
        if (taskNameInput.text.isEmpty()) {
            showDialogMessage(
                getTranslation(TranslationKeys.TASK),
                getTranslation(TranslationKeys.FIRST_FILL_IN_A_DESCRIPTION)
            )
            return
        }

        val selectedCategory = newCategoryComboBox.selectionModel.selectedItem

        val newTask = FXTask(taskNameInput.text, LocalDateTime.now().format(dateTimeFormat), -1, false, "", selectedCategory.name)
        taskViewModel.addTask(newTask, selectedCategory.id)
        getTasksBasedOnFilters()
        taskNameInput.text = ""
        newCategoryComboBox.selectionModel.selectFirst()
    }

    @Suppress("unused")
    @FXML
    private fun onSearchButtonClick(actionEvent: ActionEvent) {
        if (searchTextField.text.isEmpty()) {
            FxUtils.createMessageDialog(
                getTranslation(TranslationKeys.MISSING_SEARCH_TERM),
                getTranslation(TranslationKeys.PLEASE_FILL_IN_SEARCH_TERM)
            )
            return
        }

        taskViewModel.searchTasks(searchTextField.text)
        hideDoneTasksCheckBox.isSelected = false
        updateAmountOfTasksLabel()
    }

    @Suppress("unused")
    @FXML
    private fun onClearSearchButton() {
        searchTextField.text = ""
        hideDoneTasksCheckBox.isSelected = true
        getTasksBasedOnFilters()
    }

    @Suppress("unused")
    @FXML
    private fun onDeleteTaskButtonClick() {
        val selectedTask = tableView.selectionModel.selectedItem
        if (selectedTask == null) {
            showDialogMessageFirstSelectATask()
            return
        }

        val dialogResult = FxUtils.createMessageDialog(
            getTranslation(TranslationKeys.CONFIRM_DELETING_TASK),
            """
                #${getTranslation(TranslationKeys.DO_YOU_WANT_TO_DELETE_THE_TASK)}
                #'${selectedTask.getNameProperty()}'?
                """.trimMargin("#"),
            Alert.AlertType.CONFIRMATION
        )

        if (dialogResult.isPresent && dialogResult.get() == ButtonType.OK) {
            taskViewModel.deleteTask(selectedTask)

            getTasksBasedOnFilters()
            setUpdateTaskVisibility(false)
        }
    }

    @Suppress("unused")
    @FXML
    private fun onCompletedTaskButtonClick() {
        val selectedTask = tableView.selectionModel.selectedItem
        if (selectedTask == null) {
            showDialogMessageFirstSelectATask()
            return
        }

        when (selectedTask.getResolvedDateProperty()) {
            "" -> selectedTask.setResolvedDate(LocalDateTime.now().format(dateTimeFormat))
            else -> selectedTask.setResolvedDate("")
        }

        selectedTask.setIsDoneProperty(!selectedTask.getIsDoneProperty())
        taskViewModel.updateTask(selectedTask)

        if (hideDoneTasksCheckBox.isSelected) {
            setUpdateTaskVisibility(false)
        }

        getTasksBasedOnFilters()
    }

    @Suppress("unused")
    @FXML
    private fun onUpdateTaskButtonClick() {
        val selectedTask = tableView.selectionModel.selectedItem
        val selectedCategory = updateCategoryComboBox.selectionModel.selectedItem
        if (selectedTask == null) {
            showDialogMessageFirstSelectATask()
            return
        }

        selectedTask.setNameProperty(taskUpdateArea.text)
        selectedTask.setCategoryProperty(selectedCategory.name)

        taskViewModel.updateTask(selectedTask, selectedCategory.id)
        tableView.refresh()
        setUpdateTaskVisibility(false)
    }

    private fun setUpdateTaskVisibility(isVisible: Boolean) {
        taskUpdateArea.isVisible = isVisible
        updateTaskButton.isVisible = isVisible
        editTaskPane.isVisible = isVisible
        updateCategoryComboBox.isVisible = isVisible
    }

    @Suppress("unused")
    @FXML
    private fun onTaskSelected() {
        val selectedTask = tableView.selectionModel.selectedItem
        selectedTask ?: return

        taskUpdateArea.text = selectedTask.getNameProperty()

        when (selectedTask.getCategoryProperty()) {
            "" -> updateCategoryComboBox.selectionModel.selectFirst()
            else -> updateCategoryComboBox.items.forEach {
                if (it.name == selectedTask.getCategoryProperty()) {
                    updateCategoryComboBox.selectionModel.select(it)
                }
            }
        }

        setUpdateTaskVisibility(true)
    }

    @FXML
    private fun getTasksBasedOnFilters() {

        if (hideDoneTasksCheckBox.isSelected) {
            taskViewModel.loadOpenTasks()
            setUpdateTaskVisibility(false)
        } else {
            taskViewModel.loadTasks()
        }

        updateAmountOfTasksLabel()
    }

    @Suppress("unused")
    @FXML
    private fun onPredictCategory(){
        if (taskNameInput.text.isEmpty()) {
            showDialogMessage(
                getTranslation(TranslationKeys.TASK),
                getTranslation(TranslationKeys.FIRST_FILL_IN_A_DESCRIPTION)
            )
            return
        }

        val predictedCategoryName = classificationViewModel.predictCategory(taskNameInput.text) ?: ""
        val predicatedCategory = newCategoryComboBox.items.firstOrNull { it.name == predictedCategoryName }
        newCategoryComboBox.selectionModel.select(predicatedCategory)
    }

    fun updateAmountOfTasksLabel() {
        numberOfTasks.text = taskViewModel.getTasksVisibleOfTotalText(getTranslation(TranslationKeys.TASKS))
    }

    fun showDialogMessageFirstSelectATask() {
        showDialogMessage(
            getTranslation(TranslationKeys.TASK),
            getTranslation(TranslationKeys.FIRST_SELECT_TASK_IN_TABLE)
        )
    }

    fun showDialogMessage(title: String, content: String) {
        FxUtils.createMessageDialog(title, content)
    }

    override fun initTranslations() {
        hideDoneTasksCheckBox.text = getTranslation(TranslationKeys.HIDE_DONE_TASKS)

        editTaskPane.text = getTranslation(TranslationKeys.EDIT_TASK_DESCRIPTION)
        newTaskPane.text = getTranslation(TranslationKeys.NEW_TASK_DESCRIPTION)

        addTaskButton.text = getTranslation(TranslationKeys.ADD_TASK)
        resolveTaskButton.text = getTranslation(TranslationKeys.RESOLVE_TASK)
        deleteTaskButton.text = getTranslation(TranslationKeys.DELETE_TASK)
        updateTaskButton.text = getTranslation(TranslationKeys.UPDATE)
        searchButton.text = getTranslation(TranslationKeys.SEARCH)

        idColumn.text = getTranslation(TranslationKeys.ID)
        descriptionColumn.text = getTranslation(TranslationKeys.DESCRIPTION)
        dateColumn.text = getTranslation(TranslationKeys.DATE)
        categoryColumn.text = getTranslation(TranslationKeys.CATEGORY)
        doneColumn.text = getTranslation(TranslationKeys.DONE)
        resolvedDateColumn.text = getTranslation(TranslationKeys.COMPLETION_DATE)
    }
}