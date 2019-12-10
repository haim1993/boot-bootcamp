import com.google.inject.Guice;
import io.logz.guice.jersey.JerseyServer;
import juice.modules.ServerModule;

public class SimpleServer {
    public static void main(String[] args) {
        try {
            Guice.createInjector(new ServerModule())
                    .getInstance(JerseyServer.class).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
