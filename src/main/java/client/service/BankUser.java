package client.service;

public class BankUser {
    private String username;
    private float balance;
    private String bankpwd;

    // 构造函数、getter和setter方法
    // BankUser的getter方法
    public String getUsername() {
        return username;
    }

    public float getBalance() {
        return balance;
    }

    public String getBankpwd() {
        return bankpwd;
    }

    // BankUser的setter方法
    public void setUsername(String username) {
        this.username = username;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setBankpwd(String bankpwd) {
        this.bankpwd = bankpwd;
    }

    // 内部类
    public class BankRecord {
        private String username;
        private float balanceChange;
        private String balanceReason;

        // 构造函数、getter和setter方法
        // BankRecord的构造函数
        public BankRecord(String username, float balanceChange, String balanceReason) {
            this.username = username;
            this.balanceChange = balanceChange;
            this.balanceReason = balanceReason;
        }

        // BankRecord的getter方法
        public String getUsername() {
            return username;
        }

        public float getBalanceChange() {
            return balanceChange;
        }

        public String getBalanceReason() {
            return balanceReason;
        }

        // BankRecord的setter方法
        public void setUsername(String username) {
            this.username = username;
        }

        public void setBalanceChange(float balanceChange) {
            this.balanceChange = balanceChange;
        }

        public void setBalanceReason(String balanceReason) {
            this.balanceReason = balanceReason;
        }
    }
}
