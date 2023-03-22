package lk.ijse.dep10.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.ijse.dep10.app.db.DBConnection;

import java.io.*;
import java.net.URL;
import java.sql.*;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                System.out.println("Data connection is about to close");
                if (DBConnection.getInstance().getConnection() != null && !DBConnection.getInstance().getConnection().isClosed()) {
                    DBConnection.getInstance().getConnection().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        loadTables();
        primaryStage.setScene(new Scene(new FXMLLoader(getClass().getResource("/view/MainView.fxml")).load()));
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    private void loadTables() {
        Connection connection = DBConnection.getInstance().getConnection();
        try {

            InputStream is = getClass().getResourceAsStream("/schema.sql");

            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder script = new StringBuilder();
            while ((line=br.readLine())!=null) {
                script.append(line);
            }
            Statement stm = connection.createStatement();
            br.close();
            stm.execute(script.toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
