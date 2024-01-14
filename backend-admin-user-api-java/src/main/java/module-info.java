module boo.ecrodrigues.user.backendadminuserapijava {
    requires javafx.controls;
    requires javafx.fxml;


    opens boo.ecrodrigues.user.backendadminuserapijava to javafx.fxml;
    exports boo.ecrodrigues.user.backendadminuserapijava;
}