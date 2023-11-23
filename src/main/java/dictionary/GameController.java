package dictionary;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class GameController extends Controller {
    public abstract void restart() throws Exception;
    public abstract void handleBackScene () throws Exception;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }
}
