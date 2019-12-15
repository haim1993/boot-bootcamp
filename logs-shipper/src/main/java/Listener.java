import com.google.inject.Guice;
import io.logz.guice.jersey.JerseyServer;
import juice.modules.ListenerModule;
import juice.modules.RequireExplicitBindingsModule;

public class Listener {
    public static void main(String[] args) {
        try {
            Guice.createInjector(new ListenerModule(), new RequireExplicitBindingsModule())
                    .getInstance(JerseyServer.class).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
