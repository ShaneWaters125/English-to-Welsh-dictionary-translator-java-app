package uk.ac.aber.cs221.gp15;

import javafx.application.Application;
import uk.ac.aber.cs221.gp15.dictionary.Dictionary;
import uk.ac.aber.cs221.gp15.json.Parser;
import uk.ac.aber.cs221.gp15.tests.TestBuilder;
import uk.ac.aber.cs221.gp15.ui.InitUI;

/**
 * This class is responsible for initialising all other classes and launching the JavaFX application. It also stores
 * the dictionary, parser and testbuilder.
 *
 * @author Shane Waters (shw30@aber.ac.uk)
 * @author Ieuan Boucher (ieb7@aber.ac.uk)
 * @version 1.0 (28/04/2020)
 */

public class BaseApplication {

   private Dictionary dictionary = new Dictionary();
   private Parser parser = new Parser();
   private TestBuilder testBuilder = new TestBuilder();

   public static void main(String[] args) {
      Application.launch(InitUI.class);
   }

   /**
    * On start, fills the dictionary with all the words which was deserialized by the parser.
    */

   public void start() {
      dictionary.fill(parser.deserializeJson());
   }

   /**
    * @return The testbuilder so the UI can access it.
    */

   public TestBuilder getTestBuilder() {
      return testBuilder;
   }

   /**
    * @return The dictionary so the UI can access it.
    */

   public Dictionary getDictionary() {
      return dictionary;
   }

   /**
    * @return The parser so the UI can access it.
    */

   public Parser getParser() {
      return parser;
   }

}
