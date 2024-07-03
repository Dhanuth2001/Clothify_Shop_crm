import edu.icet.crm.config.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppInitializer extends Application {
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginPage.fxml"));
        loader.setControllerFactory(context::getBean);

        stage.setScene(new Scene(loader.load()));
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/5.png")));
        stage.setTitle("");
        stage.show();
    }
}
