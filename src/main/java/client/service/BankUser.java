package client.service;

    public class BankUser {
        private String username;
        private float balance;
        private String bankpwd;
        //private BankRecord[] bankRecords; // 声明一个BankRecord数组

        public BankUser(String username, float balance, String bankpwd) {
            this.username = username;
            this.balance = balance;
            this.bankpwd = bankpwd;
        }

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

        // 添加银行记录的方法
//        public void addBankRecord(BankRecord record) {
//            for (int i = 0; i < bankRecords.length; i++) {
//                if (bankRecords[i] == null) {
//                    bankRecords[i] = record;
//                    return;
//                }
//            }
//            System.out.println("记录数组已满，无法添加更多记录。");
//        }

        // 获取银行记录的方法
//        public BankRecord[] getBankRecords() {
//            return bankRecords;
//        }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setBankpwd(String bankpwd) {
        this.bankpwd = bankpwd;
    }

    // 内部类
    public static class BankRecord {
        private String username;
        private float balanceChange;
        private String balanceReason;
        private String curDate;//添加收支日期；

        // 构造函数、getter和setter方法
        // BankRecord的构造函数
        public BankRecord(String username,float balanceChange, String balanceReason, String curDate) {
            this.username = username;
            this.balanceChange = balanceChange;
            this.balanceReason = balanceReason;
            this.curDate = curDate;
        }

        // BankRecord的getter方法

        public String getCurDate() {
            return curDate;
        }
        public void setCurDate(String curDate) {
            this.curDate = curDate;
        }

        //public String getUsername() {
        //    return username;
        //}

        public float getBalanceChange() {
            return balanceChange;
        }

        public String getBalanceReason() {
            return balanceReason;
        }

        // BankRecord的setter方法
//        public void setUsername(String username) {
//            this.username = username;
//        }

        public void setBalanceChange(float balanceChange) {
            this.balanceChange = balanceChange;
        }

        public void setBalanceReason(String balanceReason) {
            this.balanceReason = balanceReason;
        }
    }
}
