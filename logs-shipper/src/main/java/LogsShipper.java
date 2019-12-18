import com.google.inject.Guice;
import io.logz.guice.jersey.JerseyServer;
import juice.modules.LogsShipperModule;
import juice.modules.RequireExplicitBindingsModule;

public class LogsShipper {
    public static void main(String[] args) {
        try {
            Guice.createInjector(new LogsShipperModule(), new RequireExplicitBindingsModule())
                    .getInstance(JerseyServer.class).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
