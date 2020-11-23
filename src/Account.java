import java.io.Serializable;

public class Account implements Serializable {
    private String userName;    //Username for a password
    private String password;    //Password for an account

    public Account(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof Account) {
            Account account = (Account) o;
            return (userName.equals(account.getUserName()) &&
                    password.equals(account.getPassword()));
        }
        return false;
    }
}
