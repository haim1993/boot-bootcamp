package juice.modules;

import com.google.inject.name.Names;
import config.ConfigurationFactory;
import mybatis.account.AccountMapper;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import java.util.Properties;

public class BaseMyBatisModule extends MyBatisModule {

//    DEBUGGING ON LOCALHOST
//    private static final String MYBATIS_CONFIGURATION_FILE_NAME = "./accounts-service/build/resources/main/mybatis/mybatis.config";
    private static final String MYBATIS_CONFIGURATION_FILE_NAME = "mybatis.config";

    @Override
    protected void initialize() {
        install(JdbcHelper.MySQL);

        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        Names.bindProperties(this.binder(), getMyBatisProperties());

        addMapperClass(AccountMapper.class);

        // This is for the mapper
        bind(DefaultObjectWrapperFactory.class);
        bind(DefaultObjectFactory.class);
    }

    private Properties getMyBatisProperties() {
        return ConfigurationFactory.load(MYBATIS_CONFIGURATION_FILE_NAME, Properties.class);
    }
}
