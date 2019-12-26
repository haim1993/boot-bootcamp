package juice.modules;

import com.google.inject.name.Names;
import config.ConfigurationFactory;
import config.MyBatisConfiguration;
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

        // For using Properties directly without class configuration handler
//        bind(ClassLoader.class);
//        bind(SqlSessionFactory.class);
    }

    private Properties getMyBatisProperties() {
        Properties myBatisProperties = new Properties();

        MyBatisConfiguration myBatisConfiguration = ConfigurationFactory.load(MYBATIS_CONFIGURATION_FILE_NAME, MyBatisConfiguration.class);
        myBatisProperties.setProperty(MyBatisConfiguration.MYBATIS_ENVIRONMENT_ID, myBatisConfiguration.getEnvironmentId());
        myBatisProperties.setProperty(MyBatisConfiguration.JDBC_HOST, myBatisConfiguration.getHost());
        myBatisProperties.setProperty(MyBatisConfiguration.JDBC_PORT, myBatisConfiguration.getPort());
        myBatisProperties.setProperty(MyBatisConfiguration.JDBC_SCHEMA, myBatisConfiguration.getSchema());
        myBatisProperties.setProperty(MyBatisConfiguration.JDBC_USERNAME, myBatisConfiguration.getUsername());
        myBatisProperties.setProperty(MyBatisConfiguration.JDBC_PASSWORD, myBatisConfiguration.getPassword());
        myBatisProperties.setProperty(MyBatisConfiguration.JDBC_AUTO_COMMIT, myBatisConfiguration.isAutoCommit());
        return  myBatisProperties;
    }
}
