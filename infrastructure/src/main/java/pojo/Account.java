package pojo;

public class Account {

    private int accountNo;
    private String accountName;
    private String accountToken;
    private String accountEsIndexName;

    public Account() { }

    public Account(String accountName, String accountToken, String accountEsIndexName) {
        this.accountName = accountName;
        this.accountToken = accountToken;
        this.accountEsIndexName = accountEsIndexName;
    }

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }

    public String getAccountEsIndexName() {
        return accountEsIndexName;
    }

    public void setAccountEsIndexName(String accountEsIndexName) {
        this.accountEsIndexName = accountEsIndexName;
    }

    public String toString() {
        return getAccountNo() + ", " + getAccountName() + ", " + getAccountToken() + ", " + getAccountEsIndexName();
    }

}
