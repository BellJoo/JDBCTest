package BankDTO;

public class ClientDTO {
    public String CID;
    public String CPW;
    public String CNAME;
    public String CPHONE;

    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getCPW() {
        return CPW;
    }

    public void setCPW(String CPW) {
        this.CPW = CPW;
    }

    public String getCNAME() {
        return CNAME;
    }

    public void setCNAME(String CNAME) {
        this.CNAME = CNAME;
    }

    public String getCPHONE() {
        return CPHONE;
    }

    public void setCPHONE(String CPHONE) {
        this.CPHONE = CPHONE;
    }

    @Override
    public String toString() {
        return "ClientDTO[" +
                "아이디'" + CID + '\'' +
                ", 비밀번호'" + CPW + '\'' +
                ", 이름'" + CNAME + '\'' +
                ", 연락처'" + CPHONE + '\'' +
                ']';
    }
}
