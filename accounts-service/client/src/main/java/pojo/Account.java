package pojo;

public class Account {

    private int accountId;
    private String accountName;
    private String accountToken;
    private String accountEsIndexName;

    public Account() { }

    public Account(String accountName, String accountToken, String accountEsIndexName) {
        this.accountName = accountName;
        this.accountToken = accountToken;
        this.accountEsIndexName = accountEsIndexName;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountToken() {
        return accountToken;
    }

    public String getAccountEsIndexName() {
        return accountEsIndexName;
    }

    public String toString() {
        return getAccountId() + ", " + getAccountName() + ", " + getAccountToken() + ", " + getAccountEsIndexName();
    }
}
