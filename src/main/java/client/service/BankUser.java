package client.service;

/**
 * 该类表示银行用户及其相关的账户信息和操作。
 */
public class BankUser {
    private String username;
    private double balance;
    private double currentBalance;
    private String bankpwd;

    /**
     * BankUser 构造函数，用于初始化银行用户对象。
     *
     * @param username       用户名
     * @param balance        用户总余额
     * @param bankpwd        银行密码
     * @param currentBalance 用户活期余额
     */
    public BankUser(String username, double balance, String bankpwd, double currentBalance) {
        this.username = username;
        this.balance = balance;
        this.bankpwd = bankpwd;
        this.currentBalance = currentBalance;
    }

    /**
     * 获取用户名。
     *
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 获取用户总余额。
     *
     * @return 用户总余额
     */
    public double getBalance() {
        return balance;
    }

    /**
     * 获取银行密码。
     *
     * @return 银行密码
     */
    public String getBankpwd() {
        return bankpwd;
    }

    /**
     * 获取用户活期余额。
     *
     * @return 用户活期余额
     */
    public double getCurrentBalance() {
        return currentBalance;
    }

    /**
     * 设置用户名。
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 设置用户账户余额。
     *
     * @param balance 用户总余额
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * 设置银行密码。
     *
     * @param bankpwd 银行密码
     */
    public void setBankpwd(String bankpwd) {
        this.bankpwd = bankpwd;
    }

    /**
     * 设置用户当前余额。
     *
     * @param currentBalance 用户当前余额
     */
    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    /**
     * 该类表示银行账户的交易记录，包括用户名、余额变动、交易原因及交易日期。
     */
    public static class BankRecord {
        private String username;
        private float balanceChange;
        private String balanceReason;
        private String curDate;

        /**
         * BankRecord 构造函数，用于初始化银行记录对象。
         *
         * @param username      用户名
         * @param balanceChange 账户余额变动
         * @param balanceReason 余额变动的原因
         * @param curDate       交易日期
         */
        public BankRecord(String username, float balanceChange, String balanceReason, String curDate) {
            this.username = username;
            this.balanceChange = balanceChange;
            this.balanceReason = balanceReason;
            this.curDate = curDate;
        }

        /**
         * 获取交易日期。
         *
         * @return 交易日期
         */
        public String getCurDate() {
            return curDate;
        }

        /**
         * 设置交易日期。
         *
         * @param curDate 交易日期
         */
        public void setCurDate(String curDate) {
            this.curDate = curDate;
        }

        /**
         * 获取用户名。
         *
         * @return 用户名
         */
        public String getUsername() {
            return username;
        }

        /**
         * 获取账户余额变动金额。
         *
         * @return 账户余额变动金额
         */
        public float getBalanceChange() {
            return balanceChange;
        }

        /**
         * 获取余额变动的原因。
         *
         * @return 余额变动的原因
         */
        public String getBalanceReason() {
            return balanceReason;
        }

        /**
         * 设置账户余额变动金额。
         *
         * @param balanceChange 账户余额变动金额
         */
        public void setBalanceChange(float balanceChange) {
            this.balanceChange = balanceChange;
        }

        /**
         * 设置余额变动的原因。
         *
         * @param balanceReason 余额变动的原因
         */
        public void setBalanceReason(String balanceReason) {
            this.balanceReason = balanceReason;
        }
    }
}
