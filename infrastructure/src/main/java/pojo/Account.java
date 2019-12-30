package pojo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        return getAccountNo() + ", " + getAccountName() + ", " + getAccountToken() + ", " + getAccountEsIndexName();
    }

    public String toJsonString() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

}
