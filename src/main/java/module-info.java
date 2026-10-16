module com.tuempresa.papeleria_campus {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.tuempresa.papeleria_campus to javafx.fxml;
    opens com.tuempresa.papeleria_campus.controller to javafx.fxml;

    exports com.tuempresa.papeleria_campus;

    requires java.sql;
}