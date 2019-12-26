package mybatis.account;

import org.apache.ibatis.annotations.Param;
import pojo.Account;

import java.util.List;

public interface AccountMapper {
    List<Account> getAccountList();

    Account getAccountByToken(@Param("accountToken") String accountToken);
//    boolean isAccountTokenExists(@Param("accountToken") String accountToken);

    Account getAccountByName(@Param("accountName") String accountName);
//    boolean isAccountNameExists(@Param("accountName") String accountName);

    Account getAccountByEsIndexName(@Param("accountEsIndexName") String accountEsIndexName);
//    boolean isAccountEsIndexNameExists(@Param("accountEsIndexName") String accountEsIndexName);

    int insertAccount(Account ac);

    int deleteAccountById(@Param("accountNo") int accountNo);

}