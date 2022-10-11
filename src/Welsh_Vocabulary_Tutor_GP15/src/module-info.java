/**
 * Allows JavaFX to access other packages without setting any VM options
 */
module JavaFX {

   requires javafx.fxml;
   requires javafx.controls;
   requires com.google.gson;

   exports uk.ac.aber.cs221.gp15.ui;
   exports uk.ac.aber.cs221.gp15.tests;
   exports uk.ac.aber.cs221.gp15.dictionary;

   opens uk.ac.aber.cs221.gp15.ui to javafx.graphics;
   opens uk.ac.aber.cs221.gp15.dictionary to javafx.base, com.google.gson;
}