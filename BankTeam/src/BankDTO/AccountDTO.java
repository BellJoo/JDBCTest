package BankDTO;

public class AccountDTO {
    public String ACNUM;
    public int CODENUM;
    public String CID;
    public int BALANCE;

    public String getACNUM() {
        return ACNUM;
    }

    public void setACNUM(String ACNUM) {
        this.ACNUM = ACNUM;
    }

    public int getCODENUM() {
        return CODENUM;
    }

    public void setCODENUM(int CODENUM) {
        this.CODENUM = CODENUM;
    }

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public int getBALANCE() {
        return BALANCE;
    }

    public void setBALANCE(int BALANCE) {
        this.BALANCE = BALANCE;
    }

    @Override
    public String toString() {
        return "Account[" + "계좌번호'" + ACNUM + ", 코드번호=" + CODENUM + // 계좌유형
                ", 아이디='" + CID + ", 계좌잔액=" + BALANCE + ']';
    }
}
