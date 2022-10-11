package uk.ac.aber.cs221.gp15.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;

/**
 * Class to implement a custom extension of the TableCell class to allow buttons to be added in TableView rows
 * Code adapted from a source on StackOverflow https://stackoverflow.com/a/49066796
 *
 * @param <S> Generic type parameter used to pass in the function
 * @author Ieuan Boucher
 * @author Shane Waters
 * @version 1.0 Code adapted from source
 */
public class TableButtonCell<S> extends TableCell<S, Button> {

   private final Button actionButton;

   /**
    * Constructor. Sets up initial styling
    *
    * @param label    Text to display on the button
    * @param function The function the button will run when pressed
    */
   public TableButtonCell(String label, Function<S, S> function) {
      this.getStyleClass().add("action-button-table-cell");

      this.actionButton = new Button(label);
      this.actionButton.setOnAction((ActionEvent e) -> function.apply(getCurrentItem()));
      this.actionButton.setMaxWidth(Double.MAX_VALUE);
      this.actionButton.getStyleClass().add("select");
   }

   /**
    * Gets the data of the row the button is in
    *
    * @return Item in the row
    */
   public S getCurrentItem() {
      return getTableView().getItems().get(getIndex());
   }

   /**
    * Overload the callback of the TableCell with a button call, which uses the function passed in the constructor
    *
    * @param label    Name of column title
    * @param function Function for button to carry out
    * @param <S>      Abstract type - in our program it's Word
    * @return Callback to overload
    * @see uk.ac.aber.cs221.gp15.dictionary.Word
    */
   public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, Function<S, S> function) {
      return new Callback<TableColumn<S, Button>, TableCell<S, Button>>() {
         @Override
         public TableCell<S, Button> call(TableColumn<S, Button> param) {
            return new TableButtonCell<>(label, function);
         }
      };
   }

   /**
    * Allow update of button graphic
    *
    * @param item  The button of this TableButtonCell
    * @param empty If there will be no button
    */
   @Override
   public void updateItem(Button item, boolean empty) {
      super.updateItem(item, empty);

      if (empty) {
         setGraphic(null);
      } else {
         setGraphic(actionButton);
      }
   }
}
