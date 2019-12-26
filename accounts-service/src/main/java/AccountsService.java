import com.google.inject.Guice;
import io.logz.guice.jersey.JerseyServer;
import juice.modules.AccountsServiceModule;
import juice.modules.RequireExplicitBindingsModule;

public class AccountsService {


    public static void main(String[] args) {
        try {
            Guice.createInjector(new AccountsServiceModule(), new RequireExplicitBindingsModule())
                    .getInstance(JerseyServer.class).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
