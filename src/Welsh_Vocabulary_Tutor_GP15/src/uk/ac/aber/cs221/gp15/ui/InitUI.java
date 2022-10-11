package uk.ac.aber.cs221.gp15.ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uk.ac.aber.cs221.gp15.BaseApplication;
import uk.ac.aber.cs221.gp15.dictionary.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * This class receives the JavaFX launch() from the BaseApplication class and sets up the content of all of the tabs that provide system functionality
 * User interface elements for the test screen are contained in the TestUI class.
 *
 * @author Ieuan Boucher
 * @author Shane Waters
 * @author Jakub Tomanik
 * @author Kaare Boyum
 * <p>
 * version 1.0 Initial proof of concept on TableViews. Manually added a few words from the provided JSON file
 * version 1.1 Added TabPane and Tabs to display the content of the Dictionary and Practice List screens
 * version 1.2 Reformatted code. Separated UI elements for the tabs into separate methods (e.g. "dictionaryTabElements").
 * version 1.3 Added Jakub's proof of concept initial version of the multiple choice test user interface
 * version 1.4 Added CSS styling to dictionary and practice list screen to better match the user interface specification
 * version 3 Integrated front end UI codebase with Shane's backend JSON Parser class, allowing the dictionary screen to display all data from the JSON file
 * version 3.1 Adpated code from https://stackoverflow.com/a/49066796 to create TableButtonCell to allow adding buttons to the dictionary and practice list TableViews to move words beteween them
 * version 3.2 Implemented scaling of UI elements in Dictionary & Practice list screens
 * version 3.3 Implemented searching and sorting of both tables
 * version 4.0 Finalised code functionality, ensured functionality meets acceptance tests
 * @version 1.0 (release) added JavaDoc, refactored code to meet 0.9 standards
 * @see TestUI
 */
public class InitUI extends Application {

   //Allows communication to the backend
   private final BaseApplication app = new BaseApplication();
   //Reference to the initial application window.
   private Stage stage;
   //Screen resolution
   private final Rectangle2D screen = Screen.getPrimary().getBounds();
   //Current prefix being searched and displayed in the dictionaryTable.
   private String prefix;
   //Dictionary dictionaryTable (needs renaming)
   private final TableView<Word> dictionaryTable = new TableView<>();
   private final TableView<Word> practiceListTable = new TableView<>();

   //Maximum number of characters allowed for a new word
   private static final short maxWordLength = 50;

   private boolean dictionarySortByEnglish = true;
   private boolean practiceSortByEnglish = true;

   /**
    * Method to start the JavaFX application.
    * Initialises the main dictionary and the practice list, practice list screen, main dictionary screen and the tests screen
    *
    * @param primaryStage the main application window
    */
   @Override
   public void start(Stage primaryStage) {
      //Starts the backend
      app.start();
      stage = primaryStage;
      try {
         primaryStage.getIcons().add(new Image("file:icon.png"));
      } catch (Exception e) {
         showErrorMessage("Could not load icon.");
      }

      //Min Resolution
      stage.setMinWidth(640);
      stage.setMinHeight(480);

      //Saving on close
      stage.setOnCloseRequest(event -> app.getParser().serializeJson(app.getDictionary().getEnglishWords()));

      // Define buttons here for access by multiple methods
      Button btnExit = new Button("Exit");
      // Make Exit button bigger by using larger font for label
      btnExit.setStyle("-fx-font-size: 15pt;");

      // Setup tabs for screen navigation
      TabPane tabs = new TabPane();

      //Initialise the TestUi
      TestUI.stage = stage;
      TestUI.app = app;
      TestUI.mainWindowTabPane = tabs;

      Tab tabMainDict = new Tab();
      tabMainDict.setText("Dictionary");
      tabMainDict.setContent(dictionaryTabElements());
      tabMainDict.setOnSelectionChanged(e -> {
         updateTable("", practiceSortByEnglish, true);
         updateTable("", dictionarySortByEnglish, false);
         prefix = "";
      });

      Tab tabPracticeList = new Tab();
      tabPracticeList.setText("Practice list");
      tabPracticeList.setContent(practiceTabElements());
      tabPracticeList.setOnSelectionChanged(e -> {
         updateTable("", practiceSortByEnglish, true);
         updateTable("", dictionarySortByEnglish, false);
         prefix = "";
      });

      Tab tabMainTest = new Tab("Full test");
      tabMainTest.setContent(TestUI.mainTabElements());

      //Prevent the three tabs from being closable by the user
      Stream.of(tabMainDict, tabPracticeList, tabMainTest).forEach(tab -> tab.setClosable(false));

      tabs.getTabs().addAll(tabMainDict, tabPracticeList, tabMainTest);

      //Adds a listener which updates the width of tabs whenever the program window is resized
      tabs.widthProperty().addListener((observable, oldValue, newValue) ->
      {
         tabs.setTabMinWidth(tabs.getWidth() / 8);
         tabs.setTabMaxWidth(tabs.getWidth() / 8);
      });

      // Setup main scene
      // Set initial size of the program window to equal the resolution of the computer's primary display
      Scene scene = new Scene(tabs, Screen.getPrimary().getBounds().getMaxX(), Screen.getPrimary().getBounds().getMaxY());

      scene.getStylesheets().add("uk/ac/aber/cs221/gp15/UI/main_stylesheet.css");

      primaryStage.setTitle("Welsh Vocabulary Tutor");
      primaryStage.setScene(scene);
      primaryStage.setMaximized(true);
      primaryStage.show();
      primaryStage.requestFocus();
   }

   /**
    * Sets up the search bar, language sorting switch, and flashcard button
    *
    * @return HBox with the relevant UI elements added to it.
    */
   private HBox initPracticeListMenuBar() {
      HBox menuBar = new HBox();

      menuBar.setAlignment(Pos.CENTER_LEFT);
      menuBar.setMaxWidth(Double.MAX_VALUE);
      menuBar.prefWidthProperty().bind(stage.widthProperty());
      menuBar.setSpacing(10);
      menuBar.getStyleClass().add("menu");

      final TextField searchBar = new TextField();

      searchBar.setPromptText("Search");
      searchBar.setMaxWidth(screen.getWidth() / 3);
      searchBar.prefWidthProperty().bind(stage.widthProperty());

      searchBar.setOnKeyTyped(e -> {
         prefix = searchBar.getText();
         //Update table with words, searching for words in the language the table is being sorted by. E.g. sorting in welsh, so search for matching welsh words
         updateTable(prefix, practiceSortByEnglish, true);
      });

      final Label englishLabel = new Label("English");
      final ToggleButton searchToggle = new ToggleButton("<-");
      searchToggle.setPrefWidth(30);

      searchToggle.setOnAction(e -> {
         practiceListTable.getColumns().get(0).setSortable(true);
         practiceListTable.getColumns().get(1).setSortable(true);
         practiceListTable.getSortOrder().clear();

         if (searchToggle.isSelected()) {
            searchToggle.setText("->");
            practiceListTable.getSortOrder().add(practiceListTable.getColumns().get(1)); //sort by welsh
         } else {
            searchToggle.setText("<-");
            practiceListTable.getSortOrder().add(practiceListTable.getColumns().get(0)); //Sort by english
         }
         //if toggle button is selected, update sort order to English, else update order to Welsh
         practiceSortByEnglish = !searchToggle.isSelected();
         updateTable(prefix, practiceSortByEnglish, true);

         practiceListTable.sort();
         practiceListTable.getColumns().get(0).setSortable(false);
         practiceListTable.getColumns().get(1).setSortable(false);
      });

      final Label welshLabel = new Label("Welsh");
      final Button flashcardButton = new Button("Flashcard");
      flashcardButton.setOnAction(e -> showFlashcard());

      Stream.of(englishLabel, welshLabel, menuBar, flashcardButton).forEach(button ->
              button.getStyleClass().add("menu"));

      menuBar.getChildren().addAll(searchBar, englishLabel, searchToggle, welshLabel, flashcardButton);

      return menuBar;
   }

   /**
    * Sets up the search bar, language sorting switch, and add word button
    *
    * @return HBox with the relevant UI elements added to it.
    */
   private HBox initDictionaryMenuBar() {
      HBox menuBar = new HBox();

      menuBar.setAlignment(Pos.CENTER_LEFT);
      menuBar.setMaxWidth(Double.MAX_VALUE);
      menuBar.prefWidthProperty().bind(stage.widthProperty());
      menuBar.setSpacing(10);
      menuBar.getStyleClass().add("menu");

      final TextField searchBar = new TextField();

      searchBar.setPromptText("Search");
      searchBar.setMaxWidth(screen.getWidth() / 3);
      searchBar.prefWidthProperty().bind(stage.widthProperty());

      searchBar.setOnKeyTyped(e -> {
         prefix = searchBar.getText();
         updateTable(prefix, dictionarySortByEnglish, false);
      });

      final Label englishLabel = new Label("English");

      final ToggleButton searchToggle = new ToggleButton("<-");
      searchToggle.setPrefWidth(30);

      searchToggle.setOnAction(e -> {
         dictionaryTable.getColumns().get(0).setSortable(true);
         dictionaryTable.getColumns().get(1).setSortable(true);
         dictionaryTable.getSortOrder().clear();

         if (searchToggle.isSelected()) {
            searchToggle.setText("->");
            dictionaryTable.getSortOrder().add(dictionaryTable.getColumns().get(1)); //sort by welsh
         } else {
            searchToggle.setText("<-");
            dictionaryTable.getSortOrder().add(dictionaryTable.getColumns().get(0)); //Sort by english
         }

         //if toggle button is not selected, update sort order to English, else update order to Welsh
         dictionarySortByEnglish = !searchToggle.isSelected();
         updateTable(prefix, dictionarySortByEnglish, false);

         dictionaryTable.sort();
         dictionaryTable.getColumns().get(0).setSortable(false);
         dictionaryTable.getColumns().get(1).setSortable(false);
      });

      final Label welshLabel = new Label("Welsh");
      final Button addButton = new Button("Add word");


      addButton.setOnAction(e -> showAddWordMenu());

      Stream.of(englishLabel, welshLabel, menuBar, addButton).forEach(button ->
              button.getStyleClass().add("menu"));

      menuBar.getChildren().addAll(searchBar, englishLabel, searchToggle, welshLabel, addButton);

      return menuBar;
   }

   /**
    * Creates the TableView, initialises its column and rows, set its data. Also adds the UI elements to add a new word.
    *
    * @return VBox with all the elements added
    */
   private VBox dictionaryTabElements() {
      //The backing for the top minibar.
      final HBox hb = new HBox();

      //The backing for the dictionaryTable.
      final VBox vbox = new VBox();

      dictionaryTable.setEditable(true);
      dictionaryTable.setPlaceholder(new Label("No matching words!"));

      //Configure columns with display names, sorting configurations

      TableColumn<Word, String> englishColumn = new TableColumn<>("English");
      englishColumn.setCellValueFactory(
              new PropertyValueFactory<>("english"));
      englishColumn.setSortType(TableColumn.SortType.ASCENDING);

      TableColumn<Word, String> welshColumn = new TableColumn<>("Welsh");
      welshColumn.setCellValueFactory(
              new PropertyValueFactory<>("welsh"));
      welshColumn.setSortType(TableColumn.SortType.ASCENDING);

      TableColumn<Word, String> wordTypeColumn = new TableColumn<>("Word Type");
      wordTypeColumn.setCellValueFactory(
              new PropertyValueFactory<>("wordType"));

      TableColumn<Word, Button> actionCol = new TableColumn<>("");

      englishColumn.setSortable(true);
      welshColumn.setSortable(true);

      //Default sort by English
      dictionaryTable.getSortOrder().add(englishColumn);
      dictionaryTable.sort();

      //Configure various properties of the columns
      Stream.of(englishColumn, welshColumn, wordTypeColumn, actionCol).forEach(column -> {
         column.setMinWidth(100);
         column.setSortable(false);
         column.setResizable(true);
         column.setReorderable(false);
      });

      actionCol.setCellFactory(TableButtonCell.forTableColumn("Add to list", (Word w) -> {
         dictionaryTable.getItems().remove(w);
         app.getDictionary().addToPracticeList(w);
         return w;
      }));

      //Add data to dictionaryTable, add the columns
      dictionaryTable.getColumns().addAll(englishColumn, welshColumn, wordTypeColumn, actionCol);

      //Sets the initial dictionaryTable data
      updateTable("", true, false); //Sort by English by default

      dictionaryTable.getColumns().get(0).setSortable(false);
      dictionaryTable.getColumns().get(1).setSortable(false);

      //Clamps the width of each column to the width of the vbox which is contains the dictionaryTable (screen size).
      Stream.of(englishColumn, welshColumn, wordTypeColumn, actionCol).forEach(column -> {
         column.prefWidthProperty().bind(vbox.widthProperty());
      });

      //Binds the preferred height to the height of the containing VBox
      dictionaryTable.prefHeightProperty().bind(vbox.heightProperty());

      vbox.prefWidthProperty().bind(stage.widthProperty());
      vbox.prefHeightProperty().bind(stage.heightProperty());

      vbox.setMaxHeight(screen.getHeight());

      //Sets max width
      dictionaryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
      welshColumn.setMaxWidth(screen.getWidth() / 3); // 1/3 screen width
      englishColumn.setMaxWidth(screen.getWidth() / 3); // 1/3 screen width
      wordTypeColumn.setMaxWidth(screen.getWidth() / 3); // 1/3 screen width
      actionCol.setMaxWidth(screen.getWidth() / 8);


      //Create elements to add data to dictionaryTable

      final TextField addWelshWord = new TextField();
      addWelshWord.setPromptText("Welsh word");
      addWelshWord.setMaxWidth(welshColumn.getPrefWidth());

      final TextField addEnglishWord = new TextField();
      addEnglishWord.setMaxWidth(englishColumn.getPrefWidth());
      addEnglishWord.setPromptText("English word");

      final TextField addWordType = new TextField();
      addWordType.setMaxWidth(wordTypeColumn.getPrefWidth());
      addWordType.setPromptText("Word type");

      final Button addButton = new Button("Add");

      //Styling

      hb.getChildren().addAll(addWelshWord, addEnglishWord, addWordType, addButton);
      hb.setSpacing(3);

      vbox.setPadding(new Insets(0, 0, 0, 0));
      vbox.getChildren().addAll(initDictionaryMenuBar(), dictionaryTable);

      vbox.getStyleClass().add("menu");

      //Setting max widths
      vbox.setMaxWidth(screen.getWidth());
      dictionaryTable.setMaxWidth(screen.getWidth());
      return vbox;
   }

   /**
    * Creates the TableView, initialises its column and rows, set its data.
    *
    * @return VBox with all the elements added
    */
   private VBox practiceTabElements() {
      //The backing for the top minibar.
      final HBox hb = new HBox();

      //The backing for the dictionaryTable.
      final VBox vbox = new VBox();

      practiceListTable.setEditable(true);
      practiceListTable.setPlaceholder(new Label("No matching words!"));

      //Rectangle2D screen = Screen.getPrimary().getBounds();

      /*Configure columns with display names, sorting configurations*/

      TableColumn<Word, String> englishColumn = new TableColumn<>("English");
      englishColumn.setCellValueFactory(
              new PropertyValueFactory<>("english"));
      englishColumn.setSortType(TableColumn.SortType.DESCENDING);

      TableColumn<Word, String> welshColumn = new TableColumn<>("Welsh");
      welshColumn.setCellValueFactory(
              new PropertyValueFactory<>("welsh"));
      welshColumn.setSortType(TableColumn.SortType.DESCENDING);

      TableColumn<Word, String> wordTypeColumn = new TableColumn<>("Word Type");
      wordTypeColumn.setCellValueFactory(
              new PropertyValueFactory<>("wordType"));

      TableColumn<Word, Button> actionCol = new TableColumn<Word, Button>("");

      //Configure various properties of the columns
      Stream.of(englishColumn, welshColumn, wordTypeColumn, actionCol).forEach(column -> {
         column.setMinWidth(100);
         column.setSortable(false);
         column.setResizable(true);
         column.setReorderable(false);
      });

      actionCol.setCellFactory(TableButtonCell.forTableColumn("Remove from list", (Word w) -> {
         practiceListTable.getItems().remove(w);
         app.getDictionary().removeFromPracticeList(w);
         return w;
      }));

      //Add data to dictionaryTable, add the columns
      practiceListTable.getColumns().addAll(englishColumn, welshColumn, wordTypeColumn, actionCol);
      //Sets the initial dictionaryTable data
      updateTable("", true, true); //Sort by English by default

      //Clamps the width of each column to the width of the vbox which is behind the dictionaryTable (screen size).
      Stream.of(englishColumn, welshColumn, wordTypeColumn, actionCol).forEach(column -> {
         column.prefWidthProperty().bind(vbox.widthProperty());
      });

      //Binds the preferred height to the height of the containing vbox
      practiceListTable.prefHeightProperty().bind(vbox.heightProperty());

      vbox.prefWidthProperty().bind(stage.widthProperty());
      vbox.prefHeightProperty().bind(stage.heightProperty());

      vbox.setMaxHeight(screen.getHeight());

      //Sets max width
      practiceListTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
      welshColumn.setMaxWidth(screen.getWidth() / 3); // 1/3 screen width
      englishColumn.setMaxWidth(screen.getWidth() / 3); // 1/3 screen width
      wordTypeColumn.setMaxWidth(screen.getWidth() / 3); // 1/3 screen width
      actionCol.setMaxWidth(screen.getWidth() / 8);

      //Create elements to add data to dictionaryTable

      final TextField addWelshWord = new TextField();
      addWelshWord.setPromptText("Welsh word");
      addWelshWord.setMaxWidth(welshColumn.getPrefWidth());

      final TextField addEnglishWord = new TextField();
      addEnglishWord.setMaxWidth(englishColumn.getPrefWidth());
      addEnglishWord.setPromptText("English word");

      final TextField addWordType = new TextField();
      addWordType.setMaxWidth(wordTypeColumn.getPrefWidth());
      addWordType.setPromptText("Word type");

      final Button addButton = new Button("Add");

      //Styling
      hb.getChildren().addAll(addWelshWord, addEnglishWord, addWordType, addButton);
      hb.setSpacing(3);

      vbox.setPadding(new Insets(0, 0, 0, 0));
      vbox.getChildren().addAll(initPracticeListMenuBar(), practiceListTable);
      vbox.getStyleClass().add("menu");

      //Setting max widths
      vbox.setMaxWidth(screen.getWidth());
      practiceListTable.setMaxWidth(screen.getWidth());
      return vbox;
   }

   /**
    * Method to display the add word window allowing the user to add a new word to the main dictionary
    */
   public void showAddWordMenu() {
      Stage window = new Stage();

      //Block events to other windows
      window.initModality(Modality.APPLICATION_MODAL);
      window.setTitle("Add word");
      window.setResizable(false);
      window.setMinWidth(250);

      Label englishLabel = new Label("ENGLISH");
      Label welshLabel = new Label("WELSH");
      Label typeLabel = new Label("TYPE");

      //Set the other two labels to scale to the same width as englishLabel
      welshLabel.prefWidthProperty().bind(englishLabel.widthProperty());
      typeLabel.prefWidthProperty().bind(englishLabel.widthProperty());

      final TextField addWelshWord = new TextField();
      addWelshWord.setPromptText("Welsh word");

      final TextField addEnglishWord = new TextField();
      addEnglishWord.setPromptText("English word");

      ArrayList<String> options = new ArrayList<>(Arrays.asList("verb", "nm", "nf", "other"));

      final ComboBox<String> addWordType = new ComboBox<>(FXCollections.observableArrayList(options));
      addWordType.prefWidthProperty().bind(window.widthProperty());           //make the width of the combo box equal to the window's width
      addWordType.prefHeightProperty().bind(addEnglishWord.heightProperty()); //match the combo box's height to the text input fields
      addWordType.setValue("verb");

      final Button addButton = new Button("ADD TO PRACTICE LIST");
      addButton.setAlignment(Pos.CENTER);

      addButton.setOnAction(e -> {
         Word word = new Word(
                 addEnglishWord.getText().trim(),
                 addWelshWord.getText().trim(),
                 addWordType.getValue());

         addWelshWord.clear();
         addEnglishWord.clear();

         System.out.println(word);

         if (checkNewWord(word.getEnglish(), word.getWelsh(), word.getWordType())) {
            //Mark the word so it is added to the practice list directly
            word.setMarked(true);
            app.getDictionary().addWord(word);

            updateTable("", practiceSortByEnglish, true);
            updateTable("", dictionarySortByEnglish, false);

         }
         window.close();
      });

      Stream.of(englishLabel, welshLabel, typeLabel, addButton).forEach(element -> element.getStyleClass().add("addword"));
      Stream.of(addEnglishWord, addWelshWord, addWordType).forEach(element -> element.getStyleClass().add("menu"));

      VBox layout = new VBox(0);

      layout.setAlignment(Pos.CENTER_LEFT);
      layout.setBackground(new Background(new BackgroundFill(Color.web("#3A3A3A"), CornerRadii.EMPTY, Insets.EMPTY)));
      layout.getChildren().addAll(englishLabel, addEnglishWord, welshLabel, addWelshWord, typeLabel, addWordType, addButton);

      Scene scene = new Scene(layout);
      scene.getStylesheets().add("uk/ac/aber/cs221/gp15/UI/main_stylesheet.css");

      window.setScene(scene);
      window.show();
   }

   /**
    * Method to check each aspect of the the word the user has entered to be added to the dictionary.
    * Characters can only be letters, or in a small selection of valid special characters.
    * Words must not have null components.
    *
    * @param english  English translation of the word
    * @param welsh    Welsh translation of the word
    * @param wordType Selected word type
    * @return true if word is fully valid
    */
   private boolean checkNewWord(String english, String welsh, String wordType) {
      if (english.length() == 0 || welsh.length() == 0) {
         showErrorMessage("Must enter content in english and welsh text boxes.");
         return false;
      }
      if (english.length() > maxWordLength || welsh.length() > maxWordLength) {
         showErrorMessage("Words cannot be longer than " + maxWordLength + "characters");
         return false;
      }
      if (wordType.equals("verb") && english.startsWith("to ")) {
         showErrorMessage("Program already prefixes verbs with 'to ', please remove the prefix");
         return false;
      }

      List<Character> validSpecial = Arrays.asList('\'', '-', '(', ')', '.', '-', '!', ';', '?', ',', ' ', '/');
      boolean flag; //flag to detect any invalid characters

      for (char c : english.toCharArray()) {
         flag = ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || validSpecial.contains(c));
         if (!flag) {
            showErrorMessage("Words can only contain valid letters,\nor these special characters: ()/-'.!;?,");
            return false;
         }
      }

      for (char c : welsh.toCharArray()) {
         flag = ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) || validSpecial.contains(c);
         if (!flag) {
            showErrorMessage("Words can only contain valid letters,\nor these special characters: ()/-'.!;?,");
            return false;
         }
      }

      return true;
   }

   /**
    * Method to display and error message if the user attempts to generate a flashcard on the practice list screen with no words in the practice list
    */
   public static void showErrorMessage(String messageText) {
      Stage window = new Stage();

      Label message = new Label(messageText);

      Scene scene = new Scene(message);
      scene.getStylesheets().add("uk/ac/aber/cs221/gp15/UI/main_stylesheet.css");

      window.setScene(scene);
      window.show();
   }

   /**
    * Method to display the flashcard menu. If there are no words in the practice list, an error message will be displayed instead.
    * If there is at least one word in the practice list, a flashcard will be displayed, showing the first word from the practice list.
    * Pressing new card will show the next word in the list, looping around to the beginning if the last word is shown.
    */
   public void showFlashcard() {
      AtomicInteger index = new AtomicInteger();
      List<Word> words = new ArrayList<>(app.getDictionary().getPracticeList(true).values());

      //end process if practice list is empty
      if (words.size() == 0) {
         showErrorMessage("There are not enough words in the practice list to complete that action.");
         return;
      }

      //As per acceptance tests, randomise the words in the practice list
      Collections.shuffle(words);

      Stage window = new Stage();
      String[] answer = new String[1];

      //Block events to other windows
      window.initModality(Modality.APPLICATION_MODAL);
      window.setTitle("Flashcard");
      window.setResizable(false);
      window.setMaximized(false);

      window.setMinWidth(250);

      VBox content = new VBox();
      content.getStyleClass().add("menu");
      content.setBackground(new Background(new BackgroundFill(Color.web("#3A3A3A"), CornerRadii.EMPTY, Insets.EMPTY)));
      content.setSpacing(10);

      Button question = new Button("");

      Button revealAnswer = new Button("Reveal answer");
      revealAnswer.getStyleClass().addAll("padded", "grey"); //by default, have grey background until answer revealed

      Button newCard = new Button("New card");
      newCard.getStyleClass().add("addword");

      newCard.setOnAction(h -> {
         index.getAndIncrement();
         if (index.get() == words.size()) {
            //Re-randomise when looping through the words again
            Collections.shuffle(words);
            index.set(0);
         }

         //Test 8: Flashcards (FR8)
         //Run the flashcards – do you get the 6 words above with no repeats until all gone through, showing first
         //English if English set, then Welsh, or vice versa if Welsh set?
         if (practiceSortByEnglish) {
            question.setText(words.get(index.get()).getEnglish());
            answer[0] = words.get(index.get()).getWelsh();
         } else {
            question.setText(words.get(index.get()).getWelsh());
            answer[0] = words.get(index.get()).getEnglish();
         }

         revealAnswer.getStyleClass().set(2, "grey"); //add grey styling
         revealAnswer.setText("Reveal answer");
      });

      Stream.of(question, revealAnswer).forEach(button -> {
         button.prefWidthProperty().bind(content.widthProperty());
         button.prefHeightProperty().bind(content.heightProperty());
         button.maxHeight(window.getMinHeight() / 2);
         button.maxWidth(window.getMaxWidth() / 2);
      });

      content.getChildren().addAll(question, revealAnswer, newCard);

      content.prefWidthProperty().bind(window.widthProperty());
      content.prefHeightProperty().bind(window.heightProperty());

      //Test 8: Flashcards (FR8)
      //Run the flashcards – do you get the 6 words above with no repeats until all gone through, showing first
      //English if English set, then Welsh, or vice versa if Welsh set?
      if (practiceSortByEnglish) {
         question.setText(words.get(index.get()).getEnglish());
         answer[0] = words.get(index.get()).getWelsh();
      } else {
         question.setText(words.get(index.get()).getWelsh());
         answer[0] = words.get(index.get()).getEnglish();
      }

      revealAnswer.setOnAction(e -> {
         revealAnswer.getStyleClass().set(2, ""); //clear the grey styling
         revealAnswer.setText(answer[0]);
      });

      Scene scene = new Scene(content);
      scene.getStylesheets().add("uk/ac/aber/cs221/gp15/UI/main_stylesheet.css");

      window.setScene(scene);
      window.sizeToScene();
      window.show();
   }

   /**
    * Method to update the table of either the main dictionary or the practice list
    *
    * @param prefix   The search term entered in the search bar e.g. ("ab"). If equal to "", all words are returned from the main dictionary / practice list
    * @param english  Is the table sorted by english (or welsh if false)
    * @param practice Updating the practice list if true (main dictionary if false)
    */
   private void updateTable(String prefix, boolean english, boolean practice) {
      ArrayList<Word> displayData = new ArrayList<>(app.getDictionary().search(prefix, english, practice).values());
      final ObservableList<Word> data = FXCollections.observableArrayList(displayData);

      if (practice) {
         practiceListTable.setItems(data);
      } else {
         dictionaryTable.setItems(data);
      }
   }
}