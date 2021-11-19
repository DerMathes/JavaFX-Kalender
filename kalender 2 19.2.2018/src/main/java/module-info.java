/**
 * Title: module-info.
 * Description:
 * @author Niklas Mathes
 */
module kalender {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires h2;
    requires java.desktop;

    opens de.mathes to javafx.fxml;
    exports de.mathes;
}