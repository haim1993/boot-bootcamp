package juice.modules;

import com.google.inject.AbstractModule;

public class AccountsServiceModule extends AbstractModule {

//    DEBUGGING ON LOCALHOST
//    private static final String SERVER_CONFIGURATION_FILE_NAME = "./infrastructure/build/resources/main/server.config";

    private static final String SERVER_CONFIGURATION_FILE_NAME = "server.config";

    @Override
    protected void configure() {
        install(new ServerJerseyModule(SERVER_CONFIGURATION_FILE_NAME));
        install(new BaseMyBatisModule());
    }


}
