package BankDTO;

public class CODEDTO {
    public int CODENUM;
    public String CODETYBE;

    public int getCODENUM() {
        return CODENUM;
    }

    public void setCODENUM(int CODENUM) {
        this.CODENUM = CODENUM;
    }

    public String getCODETYBE() {
        return CODETYBE;
    }

    public void setCODETYBE(String CODETYBE) {
        this.CODETYBE = CODETYBE;
    }

    @Override
    public String toString() {
        return "CODEDTO[" +
                "코드번호" + CODENUM + // 각 코드의 고유번호
                ", 코드유형='" + CODETYBE +
                ']';
    }
}
