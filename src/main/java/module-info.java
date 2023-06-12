module aoc.simulandoprocessador {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;

    opens aoc.simulandoprocessador to javafx.fxml;
    exports aoc.simulandoprocessador;
}