package mybatis.account;

import org.apache.ibatis.annotations.Param;
import pojo.Account;

public interface AccountMapper {
    Account getAccountByToken(@Param("accountToken") String accountToken);

    Account getAccountByName(@Param("accountName") String accountName);

    Account getAccountByEsIndexName(@Param("accountEsIndexName") String accountEsIndexName);

    int insertAccount(Account ac);
}