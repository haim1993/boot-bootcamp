import com.google.inject.Guice;
import io.logz.guice.jersey.JerseyServer;
import juice.modules.IndexerModule;
import juice.modules.RequireExplicitBindingsModule;

public class LogEngine {

    public static void main(String[] args) {
        try {
            Guice.createInjector(new IndexerModule(), new RequireExplicitBindingsModule())
                    .getInstance(JerseyServer.class).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
