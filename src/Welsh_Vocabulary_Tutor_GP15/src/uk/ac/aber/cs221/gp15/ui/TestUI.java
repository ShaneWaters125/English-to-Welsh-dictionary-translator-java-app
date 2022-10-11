package uk.ac.aber.cs221.gp15.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uk.ac.aber.cs221.gp15.BaseApplication;
import uk.ac.aber.cs221.gp15.tests.MatchWordsTest;
import uk.ac.aber.cs221.gp15.tests.MultiChoiceTest;
import uk.ac.aber.cs221.gp15.tests.Test;
import uk.ac.aber.cs221.gp15.tests.TestType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Contains UI elements for the test tab
 * <p>
 * version 1.0 Extracted code from InitUI to improve readability and maintainability
 * version 1.1 Implemented tabs for the three test screens
 * version 2 Merge the three test tabs into one test screen
 * version 2.1 Added UI to allow each test type to be generated
 * version 2.2 Added logic to run full test
 *
 * @author Ieuan Boucher
 * @author Shane Waters
 * @version 2.3 Added UI to display final score
 */
public class TestUI {
   static Stage stage;
   static BaseApplication app;

   static float correctTally = 0f;
   static AtomicInteger numQuestions = new AtomicInteger();
   static int currentQuestionNumber = 0;
   static boolean inFullTest = false;

   static VBox testScreenVBox;
   static VBox[] currentTest;

   static List<Test> tests;
   static TabPane mainWindowTabPane;

   /**
    * Creates the UI elements for the "Choose the correct translation from 6 options" test.
    *
    * @return VBox with all the elements added
    */
   static VBox multipleChoiceTabElements(Test test) {
      GridPane grid = new GridPane();
      Rectangle2D screen = Screen.getPrimary().getBounds();

      //Configure GridPane containing Question, and answer buttons
      grid.widthProperty().addListener((observable, oldValue, newValue) ->
              grid.prefWidth(TestUI.stage.getWidth() / 2));

      grid.setAlignment(Pos.CENTER);

      Button question = new Button("August");
      question.setAlignment(Pos.TOP_CENTER);
      question.getStyleClass().add("extra-padded");

      Button answer1 = new Button();
      Button answer2 = new Button();
      Button answer3 = new Button();
      Button answer4 = new Button();
      Button answer5 = new Button();
      Button answer6 = new Button();

      Stream.of(answer1, answer2, answer3, answer4, answer5, answer6).forEach(button -> {
         button.getStyleClass().add("padded");
         button.setAlignment(Pos.CENTER);
         button.setMaxWidth(Double.MAX_VALUE);
      });

      //add (Object, Xpos, Ypos)
      grid.add(answer1, 0, 0);
      grid.add(answer2, 1, 0);
      grid.add(answer3, 0, 2);
      grid.add(answer4, 1, 2);
      grid.add(answer5, 0, 4);
      grid.add(answer6, 1, 4);

      grid.setVgap(10);
      grid.setHgap(50);

      final VBox content = new VBox();

      content.setAlignment(Pos.TOP_CENTER);
      content.setSpacing(20);

      content.prefWidthProperty().bind(stage.widthProperty().divide(2));

      content.setMaxHeight(screen.getHeight());
      content.setMaxWidth(screen.getWidth());

      content.setPadding(new Insets(0, 0, 0, 0));
      content.getChildren().addAll(question, grid);
      content.getStyleClass().add("menu");

      if (!genMultiChoice(test, question, answer1, answer2, answer3, answer4, answer5, answer6)) return null;

      return content;
   }

   /**
    * Method to generate a test, and set the elements of the Multiple Choice to display the question and answers
    *
    * @param question The button to display the question
    * @param answers  The buttons to display the possible answers (including the correct answer)
    * @return A boolean indicating if the test could be generated successfully
    */
   public static boolean genMultiChoice(Test test, Button question, Button... answers) {
      if (test == null) {
         InitUI.showErrorMessage("Could not create test: not enough words in the practice list.");
         return false;
      }

      //Reset buttons as the elements are a static instance
      Stream.of(answers).forEach(element -> {
         element.getStyleClass().removeAll("incorrect", "correct");
         element.setDisable(false);
      });


      List<String> testAnswers = new ArrayList<>(test.getAnswer());
      Collections.shuffle(testAnswers);

      //Setup question and answers
      question.setText(test.getQuestion().get(0));
      for (int index = 0; index < 6; index++) {
         answers[index].setId("");
         answers[index].setText(testAnswers.get(index));

         int finalIndex = index;
         answers[index].setOnAction(e -> {
            boolean answeredCorrectly = checkMultiAnswers(question, answers[finalIndex], test, answers);

            if (inFullTest) {
               if (answeredCorrectly) {
                  correctTally += 1;
               }
               currentQuestionNumber++;
               showNextQuestionButton();
            }
         });
      }
      return true;
   }

   /**
    * Method to check if answer the user has selected is correct
    *
    * @param question     The button with the word in one language
    * @param chosenAnswer The answer the user has chosen
    * @param test         The test that was generated to initialise the UI elements
    * @param answers      All the answers buttons, for use if an incorrect answer is selected
    * @return True if the user selected the correct answer
    */
   private static boolean checkMultiAnswers(Button question, Button chosenAnswer, Test test, Button... answers) {
      if (chosenAnswer.getText().equals(test.getAnswer().get(0))) {
         //If the user chose the correct answer: highlight the question and the chosen answer in green
         question.setText("Correct");
         question.getStyleClass().add("correct");
         chosenAnswer.getStyleClass().add("correct");

         for (Button b : answers) {
            b.setDisable(true);
         }

         return true;
      } else {
         //If the user chose an incorrect answer: highlight the question in red,
         // and apply highlighting to all answers indicating which one was correct and which are incorrect
         question.setText("Incorrect");
         question.getStyleClass().add("incorrect");
         chosenAnswer.getStyleClass().add("incorrect");

         for (Button b : answers) {
            b.setDisable(true);
            if (b.getText().equals(test.getAnswer().get(0))) {
               b.getStyleClass().add("correct");
            } else {
               b.getStyleClass().add("incorrect");
            }
         }
      }
      return false;
   }

   /**
    * Creates the UI elements for the "Match 4 translations to 4 words" test.
    *
    * @return VBox with all the elements added
    */
   static VBox matchWordsTabElements(Test test) {
      Rectangle2D screen = Screen.getPrimary().getBounds();
      // Reference to the ID (0-4) of the last Answer button pressed. Set to -1 to indicate no button initially selected
      AtomicInteger lastSelected = new AtomicInteger(-1);

      //List storing the answers selected by the user to check against the actual answers
      List<String> userAnswers = Arrays.asList("", "", "", "");
      List<Button> listOfButtonsSubmitted = Arrays.asList(null, null, null, null);

      GridPane questionElementsPane = new GridPane();
      questionElementsPane.setAlignment(Pos.TOP_CENTER);  // Override default
      questionElementsPane.prefWidthProperty().bind(stage.widthProperty()/*.multiply(0.8)*/);

      questionElementsPane.setHgap(1);
      questionElementsPane.setVgap(20);

      Button question1 = new Button();
      Button question2 = new Button();
      Button question3 = new Button();
      Button question4 = new Button();

      Button answer1 = new Button();
      Button answer2 = new Button();
      Button answer3 = new Button();
      Button answer4 = new Button();

      Button socket1 = new Button();
      Button socket2 = new Button();
      Button socket3 = new Button();
      Button socket4 = new Button();

      socket1.setId("0");
      socket2.setId("1");
      socket3.setId("2");
      socket4.setId("3");

      Button checkAnswers = new Button("Check answers");
      checkAnswers.getStyleClass().add("addword");
      checkAnswers.prefWidthProperty().bind(stage.widthProperty().divide(6));
      checkAnswers.setAlignment(Pos.CENTER);

      question1.prefWidthProperty().bind(stage.widthProperty().divide(4));
      question1.prefWidth(stage.getWidth() / 4);

      //Scale all elements to match the width of the first Welsh button for consistency
      Stream.of(question2, question3, question4, socket1, socket2, socket3, socket4, answer1, answer2, answer3, answer4).forEach(button -> {
         button.prefWidthProperty().bind(stage.widthProperty().divide(4));
         button.prefWidth(stage.getWidth() / 4);
      });

      //Apply the appropriate CSS styling to the UI elements
      Stream.of(question1, question2, question3, question4).forEach(button ->
              button.getStyleClass().addAll("dark", "padded", "largetext"));

      Stream.of(answer1, answer2, answer3, answer4).forEach(button ->
              button.getStyleClass().addAll("light", "padded", "largetext"));

      Stream.of(socket1, socket2, socket3, socket4).forEach(button ->
              button.getStyleClass().addAll("socket", "padded"));

      //Button necessary to prevent grid resizing when all answers have been chosen
      Button hiddenButton = new Button("");

      hiddenButton.toBack();
      hiddenButton.setVisible(false);
      //despite being invisible, allow the button to prevent the VBox container from resizing when all answers are in sockets
      hiddenButton.setManaged(true);
      hiddenButton.prefWidthProperty().bind(question1.widthProperty());

      questionElementsPane.add(hiddenButton, 4, 0);

      questionElementsPane.add(question1, 0, 0);
      questionElementsPane.add(question2, 0, 1);
      questionElementsPane.add(question3, 0, 2);
      questionElementsPane.add(question4, 0, 3);

      questionElementsPane.add(socket1, 2, 0);
      questionElementsPane.add(socket2, 2, 1);
      questionElementsPane.add(socket3, 2, 2);
      questionElementsPane.add(socket4, 2, 3);

      questionElementsPane.add(answer1, 4, 0);
      questionElementsPane.add(answer2, 4, 1);
      questionElementsPane.add(answer3, 4, 2);
      questionElementsPane.add(answer4, 4, 3);

      //Set all Answer buttons to update the lastSelected var storing the ID of the last selected Answer button not assigned to a socket
      answer1.setOnAction(e -> lastSelected.set(0));
      answer2.setOnAction(e -> lastSelected.set(1));
      answer3.setOnAction(e -> lastSelected.set(2));
      answer4.setOnAction(e -> lastSelected.set(3));

      List<Button> answerButtons = Arrays.asList(answer1, answer2, answer3, answer4);
      List<Button> questionButtons = Arrays.asList(question1, question2, question3, question4);

      //Set all the sockets so that if an Answer button is pressed, then a socket is pressed, the Answer will be moved to the socket and disabled
      socket1.setOnAction(e -> snapButton(socket1, lastSelected, userAnswers, answerButtons, listOfButtonsSubmitted));
      socket2.setOnAction(e -> snapButton(socket2, lastSelected, userAnswers, answerButtons, listOfButtonsSubmitted));
      socket3.setOnAction(e -> snapButton(socket3, lastSelected, userAnswers, answerButtons, listOfButtonsSubmitted));
      socket4.setOnAction(e -> snapButton(socket4, lastSelected, userAnswers, answerButtons, listOfButtonsSubmitted));

      final VBox content = new VBox();

      content.setPadding(new Insets(0, 0, 0, 0)); //        vbox.setPadding(new Insets(10, 0, 0, 10));

      content.setMaxWidth(screen.getWidth());
      content.setMaxHeight(screen.getHeight());

      content.getStyleClass().add("menu");
      content.setAlignment(Pos.TOP_CENTER);
      content.setSpacing(20);
      content.prefWidthProperty().bind(stage.widthProperty());

      content.getChildren().addAll(questionElementsPane, checkAnswers);

      if (genMatchWords(test, userAnswers, checkAnswers, questionButtons, answerButtons, listOfButtonsSubmitted))
         return null;

      return content;
   }

   /**
    * Method to generate a test (TestType.MatchWords), and set the elements of the Match Words to display the question and answers
    *
    * @param userAnswers     The answers the user submitted, ordered by which socket the answers were put in to e.g.
    *                        Socket 1: abbey, Socket 2: adventure, Socket 3: adventurous, Socket 4: advice would result in {"abbey","adventure","adventurous","advice"}
    * @param checkAnswers    Button used to submit the answers once all answers have been entered
    * @param questionButtons All buttons to display the 4 words in the other language
    * @param answerButtons   All answer buttons to display the 4 words in one language
    * @return A boolean indicating if the test could be generated successfully
    */
   private static boolean genMatchWords(Test test, List<String> userAnswers, Button checkAnswers, List<Button> questionButtons, List<Button> answerButtons, List<Button> buttonsSubmitted) {
      if (test == null) {
         InitUI.showErrorMessage("Could not create test: not enough words in the practice list.");
         return true;
      }

      setUpMatchWords(questionButtons, answerButtons, test);

      checkAnswers.setOnAction(e -> {
         boolean allAnswerSlotted = true;
         for (String s : userAnswers) {
            if (s.equals("")) {
               allAnswerSlotted = false;
               break;
            }
         }
         if (allAnswerSlotted) {
            int numCorrect = checkMatchAnswers(userAnswers, test, buttonsSubmitted);
            if (inFullTest) {
               correctTally += (float) numCorrect / (float) 4;
               currentQuestionNumber++;
               showNextQuestionButton();
            }
            checkAnswers.setDisable(true);
         }

      });
      return false;
   }

   /**
    * Method used to initialise the questions and answers buttons with the data from the generated test
    *
    * @param questionButtons All buttons to display the 4 words in the other language
    * @param answerButtons   All answer buttons to display the 4 words in one language
    * @param test            The generated test used to set the UI elements
    */
   private static void setUpMatchWords(List<Button> questionButtons, List<Button> answerButtons, Test test) {
      List<String> questions = test.getQuestion();
      List<String> answers = new ArrayList<>(test.getAnswer());

      Collections.shuffle(answers);

      for (int i = 0; i < 4; i++) {
         questionButtons.get(i).setText(questions.get(i));
         answerButtons.get(i).setText(answers.get(i));
      }
   }

   /**
    * Method to check the user answers and return how many they got correct out of four
    *
    * @param userAnswers The answers the user submitted, ordered by which socket the answers were put in to e.g.
    *                    Socket 1: abbey, Socket 2: adventure, Socket 3: adventurous, Socket 4: advice would result in {"abbey","adventure","adventurous","advice"}
    * @param test        The generated test used to set the UI elements
    * @return Number of answers the user got correct: [0-4]
    */
   private static int checkMatchAnswers(List<String> userAnswers, Test test, List<Button> buttonsSubmitted) {
      int numCorrect = 0;

      for (int index = 0; index < 4; index++) {
         if (userAnswers.get(index).equals(test.getAnswer().get(index))) {
            buttonsSubmitted.get(index).getStyleClass().add("correct");
            numCorrect++;
         } else {
            buttonsSubmitted.get(index).getStyleClass().add("incorrect");
         }
      }

      return numCorrect;
   }

   /**
    * Creates the UI elements for the "Translate one word" test and initialises them with a generated test
    *
    * @return VBox with all the elements added
    */
   static VBox translateTabElements(Test test) {
      VBox content = new VBox();
      content.setSpacing(20); //Add 20px of spacing between elements
      content.setAlignment(Pos.TOP_CENTER);

      Button question = new Button("August");
      TextField answer = new TextField();
      answer.maxWidthProperty().bind(stage.widthProperty().divide(4));

      Button submit = new Button("Submit");

      Label result = new Label();
      result.setVisible(false);
      result.getStyleClass().add("correct");

      question.getStyleClass().addAll("extra-padded");
      answer.getStyleClass().addAll("menu", "padded");
      submit.getStyleClass().addAll("addword");

      Rectangle2D screen = Screen.getPrimary().getBounds();

      content.setPadding(new Insets(0, 0, 0, 0));
      content.setMaxWidth(screen.getWidth());
      content.setMaxHeight(screen.getHeight());

      content.prefWidthProperty().bind(stage.widthProperty());

      content.getStyleClass().add("menu");

      content.getChildren().addAll(question, result, answer, submit);

      if (genTranslate(test, question, answer, submit, result)) return null;

      return content;
   }

   /**
    * Method to initialise the UI elements of the Translate test
    *
    * @param question    The button to display the word in one language
    * @param answer      The TextField the user will use to enter their answer
    * @param checkAnswer Button used to submit the answer
    * @param result      Label used to display if the entered answer is correct / incorrect
    * @return A boolean indicating if the test could be generated successfully
    */
   private static boolean genTranslate(Test test, Button question, TextField answer, Button checkAnswer, Label result) {
      if (test == null) {
         InitUI.showErrorMessage("Could not create test: not enough words in the practice list.");
         return true;
      }

      question.setText(test.getQuestion().get(0));

      checkAnswer.setOnAction(e -> {
         if (answer.getText() != null) {
            //Disable button so cant alter answer then recheck
            checkAnswer.setDisable(true);

            if (checkTranslateAnswer(answer, test)) {
               result.getStyleClass().clear();
               result.getStyleClass().addAll("correct", "padded");
               result.setText("Correct");

               result.setVisible(true);

               if (inFullTest) {
                  correctTally += 1;
                  currentQuestionNumber++;
                  showNextQuestionButton();
               }
            } else {
               result.getStyleClass().clear();
               result.getStyleClass().addAll("incorrect", "padded");
               result.setText("Incorrect\n" + "Correct answer was: " + test.getAnswer().get(0));

               result.setVisible(true);

               if (inFullTest) {
                  currentQuestionNumber++;
                  showNextQuestionButton();
               }
            }
         }

      });

      return false;
   }

   /**
    * Method to check if the user's answer to the Translate test is correct
    *
    * @param answer TextField user entered their answer in
    * @param test   Test that contains the correct answer
    * @return If answer is not null, and exactly matches the correct answer return true
    */
   private static boolean checkTranslateAnswer(TextField answer, Test test) {
      return answer.getText() != null && answer.getText().equals(test.getAnswer().get(0));
   }

   /**
    * Helper function to move an Answer button to the position of the the selected "Socket" button.
    *
    * @param socket        "Socket" button which was pressed, used to move the selected answer onto the socket
    * @param lastSelected  Stores the ID of the last Answer button pressed
    * @param answerButtons All answer buttons that could have been pressed
    */
   static void snapButton(Button socket, AtomicInteger lastSelected, List<String> answers, List<Button> answerButtons, List<Button> buttonsSubmitted) {
      int index = lastSelected.get();

      //If an answer button was selected and set its the lastSelected int to the button's index (0-4)
      if (index > -1) {
         // Move the selected Answer button to the same row and column as the Socket button pressed.
         Button b = answerButtons.get(index);

         GridPane.setColumnIndex(b, GridPane.getColumnIndex(socket));  //move the Answer button to the same column as the selected socket
         GridPane.setRowIndex(b, GridPane.getRowIndex(socket));        //move the Answer button to the same row as the selected socket

         b.setDisable(true);                                           //disable the answer so it cant be moved again until the question is reset or submitted.
         socket.setDisable(true);

         //add the selected answer to the answers array, based on the chosen socket
         answers.set(Integer.parseInt(socket.getId()), b.getText());
         buttonsSubmitted.set(Integer.parseInt(socket.getId()), b);

         //Set lastSelected to -1 so this function will only do work when a new, non-disabled answer button is pressed
         lastSelected.set(-1);
      }
   }

   /**
    * Initialises the UI elements of the Test screen, allowing the three test types to be generated via buttons and a full test to be undertaken
    *
    * @return VBox with all UI elements created and initialised
    */
   static VBox mainTabElements() {
      //VBox to contain the test menu bar, score readout, and the second VBox to display the current test
      VBox allContent = new VBox();
      VBox content = new VBox();

      Button nextQuestionButton = new Button("Next question");
      nextQuestionButton.getStyleClass().add("padded");
      testScreenVBox = allContent;

      //Required to be final so that the content can be changed in a button action. All variables in a button action must be final.
      currentTest = new VBox[]{content};

      allContent.setSpacing(20); //Add 20px of spacing between elements
      allContent.setAlignment(Pos.TOP_CENTER);

      Rectangle2D screen = Screen.getPrimary().getBounds();

      allContent.setPadding(new Insets(0, 0, 0, 0));
      allContent.setMaxWidth(screen.getWidth());
      allContent.setMaxHeight(screen.getHeight());

      allContent.prefWidthProperty().bind(stage.widthProperty());
      allContent.prefHeightProperty().bind(stage.heightProperty());

      //#######################################################################
      //Initialise test menu bar

      HBox menuBar = new HBox();
      menuBar.setSpacing(10);

      Button multiChoice = new Button("Multiple Choice");
      Button translate = new Button("Translate");
      Button match = new Button("Match Words");
      Button fullTest = new Button("Generate Full Test");

      multiChoice.setOnAction(e -> {
         Test test = app.getTestBuilder().createTest(app.getDictionary(), TestType.MultipleChoice);
         VBox temp = multipleChoiceTabElements(test);
         if (!(temp == null)) {
            currentTest[0] = temp;
            testScreenVBox.getChildren().set(2, new VBox());
            testScreenVBox.getChildren().set(2, currentTest[0]);

         }
      });
      translate.setOnAction(e -> {
         Test test = app.getTestBuilder().createTest(app.getDictionary(), TestType.SingleWord);
         VBox temp = translateTabElements(test);
         if (!(temp == null)) {
            currentTest[0] = temp;
            testScreenVBox.getChildren().set(2, new VBox());
            testScreenVBox.getChildren().set(2, currentTest[0]);
         }
      });
      match.setOnAction(e -> {
         Test test = app.getTestBuilder().createTest(app.getDictionary(), TestType.MatchWords);
         VBox temp = matchWordsTabElements(test);
         if (!(temp == null)) {
            currentTest[0] = temp;
            testScreenVBox.getChildren().set(2, new VBox());
            testScreenVBox.getChildren().set(2, currentTest[0]);
         }
      });
      fullTest.setOnAction(e -> {

         if (app.getDictionary().getPracticeList(true).size() > 0) {
            inFullTest = true;
            menuBar.setDisable(true);

            testScreenVBox.getChildren().set(2, new VBox());
            testScreenVBox.getChildren().set(2, fullTestMenuScreen());
         } else {
            InitUI.showErrorMessage("Cannot start test with no words in practice list.");
         }
      });

      menuBar.setAlignment(Pos.CENTER_LEFT);
      menuBar.prefWidthProperty().bind(stage.widthProperty());

      Stream.of(multiChoice, translate, match, fullTest, menuBar).forEach(element ->
              element.getStyleClass().add("menu"));

      //Maintainance note: Could not get CSS to work. NOTE: Inheriting from VBox parents?
      menuBar.setStyle("-fx-background-color: #4C7AB4;fx-padding: 0 0 0 0;");
      menuBar.getChildren().addAll(multiChoice, translate, match, fullTest);

      //#######################################################################
      //Initialise the full test menu score readout

      HBox scoreReadout = new HBox();

      scoreReadout.setAlignment(Pos.TOP_LEFT);
      scoreReadout.getStyleClass().add("menu");
      Label score = new Label("Score : ");
      Label progress = new Label("1/" + numQuestions);

      progress.setMinWidth(200);
      score.setMinWidth(50);

      progress.setAlignment(Pos.CENTER);
      score.setAlignment(Pos.CENTER);

      score.getStyleClass().add("blue-background");
      progress.getStyleClass().add("dark");

      scoreReadout.getChildren().addAll(score, progress);
      scoreReadout.setAlignment(Pos.TOP_LEFT);
      scoreReadout.setVisible(false);

      Button showNextQuestionButton = new Button("Next question");

      showNextQuestionButton.setVisible(false);
      showNextQuestionButton.getStyleClass().add("addbutton");
      showNextQuestionButton.setAlignment(Pos.TOP_CENTER);

      showNextQuestionButton.setOnAction(e -> {
         nextQuestion();
         showNextQuestionButton.setVisible(false);
      });

      //#######################################################################

      allContent.getChildren().addAll(menuBar, scoreReadout, currentTest[0]);
      allContent.getChildren().add(3, showNextQuestionButton);

      return allContent;
   }

   /**
    * Method to set the program into full test mode, where various menus are disabled (dictionary, practice screens and the test menus)
    *
    * @param questionCount how many questions does the user want to do
    */
   public static void runFullTest(int questionCount) {
      //Make the score readout visible
      testScreenVBox.getChildren().get(1).setVisible(true);
      numQuestions.set(questionCount);
      currentQuestionNumber = 0;
      correctTally = 0;

      int numQ = numQuestions.get();
      tests = app.getTestBuilder().createFullTest(app.getDictionary(), numQ);

      HBox scoreBar = (HBox) testScreenVBox.getChildren().get(1);
      Label score = (Label) scoreBar.getChildren().get(0);
      score.setText("Score: " + correctTally);
      Label progress = (Label) scoreBar.getChildren().get(1);
      progress.setText(currentQuestionNumber + "/" + numQuestions);

      nextQuestion();
   }

   /**
    * Method to display the next question in the full test
    * If end of test reached, display the final results screen and exit full test mode
    */
   public static void nextQuestion() {
      if (currentQuestionNumber < tests.size()) {
         if (tests.get(currentQuestionNumber) == null) {
            inFullTest = false;
            testScreenVBox.getChildren().get(0).setDisable(false);
            testScreenVBox.getChildren().get(1).setVisible(false);
            displayScore();
         }

         if (tests.get(currentQuestionNumber).getClass() == MatchWordsTest.class) {
            currentTest[0] = matchWordsTabElements(tests.get(currentQuestionNumber));

            testScreenVBox.getChildren().set(2, new VBox());
            testScreenVBox.getChildren().set(2, currentTest[0]);

         } else if (tests.get(currentQuestionNumber).getClass() == MultiChoiceTest.class) {
            currentTest[0] = multipleChoiceTabElements(tests.get(currentQuestionNumber));

            testScreenVBox.getChildren().set(2, new VBox());
            testScreenVBox.getChildren().set(2, currentTest[0]);
         } else {
            currentTest[0] = translateTabElements(tests.get(currentQuestionNumber));

            testScreenVBox.getChildren().set(2, new VBox());
            testScreenVBox.getChildren().set(2, currentTest[0]);
         }
      } else {
         inFullTest = false;

         //Hide the score readout
         testScreenVBox.getChildren().get(1).setVisible(false);
         //Clear the last test, to make way to the final score readout
         testScreenVBox.getChildren().set(2, new VBox());

         //Re-enable the Dictionary and Practice list tabs
         mainWindowTabPane.getTabs().get(0).setDisable(false);
         mainWindowTabPane.getTabs().get(1).setDisable(false);
         displayScore();
      }
   }

   /**
    * Display the final score UI elements to the user
    */
   public static void displayScore() {
      testScreenVBox.getChildren().get(0).setDisable(false);

      VBox scoreDisplay = new VBox();
      Label finalScore = new Label("YOU SCORED\n      " + correctTally + "/" + numQuestions);
      scoreDisplay.getChildren().addAll(finalScore);
      finalScore.getStyleClass().addAll("blue-background", "score-display");
      finalScore.setStyle("-fx-font-size: 50px;");

      finalScore.prefWidthProperty().bind(stage.widthProperty().divide(5).multiply(3));
      finalScore.prefHeightProperty().bind(stage.heightProperty().divide(5).multiply(2));
      finalScore.setAlignment(Pos.TOP_CENTER);

      scoreDisplay.setAlignment(Pos.TOP_CENTER);

      currentTest[0] = scoreDisplay;

      testScreenVBox.getChildren().set(2, new VBox());
      testScreenVBox.getChildren().set(2, currentTest[0]);
   }

   /**
    * Method to make the next question button visible and initialise/display the score readout UI
    */
   public static void showNextQuestionButton() {
      //make the next question button visible
      testScreenVBox.getChildren().get(3).setVisible(true);

      //Update the display text of the score and progress buttons
      HBox scoreBar = (HBox) testScreenVBox.getChildren().get(1);
      Label score = (Label) scoreBar.getChildren().get(0);
      score.setText("Score: " + (int) correctTally);
      Label progress = (Label) scoreBar.getChildren().get(1);

      progress.setText(currentQuestionNumber + "/" + numQuestions);
   }

   /**
    * Method to show the menu to choose the number of questions for the full test
    *
    * @return VBox with all UI elements added
    */
   public static VBox fullTestMenuScreen() {
      //Disable the Dictionary and Practice list tabs to prevent data being altered during a full test
      mainWindowTabPane.getTabs().get(0).setDisable(true);
      mainWindowTabPane.getTabs().get(1).setDisable(true);

      VBox content = new VBox();
      Label explanation = new Label("Enter the number of questions\n(max is equal to number of words in practice list):");

      int max = app.getDictionary().getPracticeList(true).size();

      List<Integer> range = IntStream.rangeClosed(1, max).boxed().collect(Collectors.toList());

      ComboBox<Integer> numSelector = new ComboBox<>(FXCollections.observableArrayList(range));

      numSelector.setValue(1);
      Button submit = new Button("submit");
      content.getChildren().addAll(explanation, numSelector, submit);

      submit.setOnAction(handle -> {
         runFullTest(numSelector.getValue());
      });
      return content;
   }
}