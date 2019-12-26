import pojo.Account;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;

public class TestDBConnection {

    private static SqlSessionFactory sf;
    private static JdbcBaseBatisDAO<Account, Integer> db;

    public static void print(String arg) {
        System.out.println(arg);
    }

    @BeforeClass
    public static void init() throws IOException {
        print("Mybatis setup is in progress...");
        String resource = "mybatis/mybatis.xml";
        Reader reader = Resources.getResourceAsReader(resource);
        sf = new SqlSessionFactoryBuilder().build(reader, "development");
        db = new AccountDAO(sf);
        print("Connection Established Successfully.");
    }

    @Test
    public void insertAccountsTest() {
        List<Account> l = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            Account a = new Account();
            a.setAccountName("Name" + (i+1));
            a.setAccountToken("Token" + (i+1));
            a.setAccountEsIndexName("Es" + (i+1));
            l.add(a);
        }

        insertAccount(l);
    }

    @Test
    public void printAccount() {
        printAccounts();
    }

    private void insertAccount(Account a) {
        db.insertAccount(a);
    }

    private void insertAccount(List<Account> l) {
        for (Account a : l) {
            db.insertAccount(a);
        }
    }

//    private Account generateRandomAccount() {
//        Account a = new Account();
//
//    }

    private void printAccounts() {
        ArrayList<Account> list = db.getAccountList();
        print("Account List: ");
        for (Account a : list) {
            print(a.toString());
        }
    }

    @Test
    public void isAccountTokenExistsTest() {
        assertTrue(db.isAccountTokenExists("Token11"));
    }

    @Test
    public void isAccountNameExistsTest() {
        assertTrue(db.isAccountNameExists("Account2"));
    }

    private String generateName(Random random, String characters, int length) {
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(random.nextInt(characters.length()));
        }
        return new String(text);
    }

}
