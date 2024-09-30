package BankDAO;

import BankDTO.AccountDTO;
import BankDTO.ClientDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankSQL {

    Connection con;               // 전역 변수로 정의된 Connection
    PreparedStatement pstmt;      // 전역 변수로 정의된 PreparedStatement
    ResultSet rs;                 // 전역 변수로 정의된 ResultSet

    // 데이터베이스 연결
    public void connect() {
        con = DBC.DBConnect();
    }

    // 데이터베이스 연결 종료
    public void closeConnection() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("연결 종료 중 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // PreparedStatement 자원 해제
    private void closePreparedStatement() {
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            System.out.println("PreparedStatement 종료 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 고객 회원가입 메소드
    public void registerClient(ClientDTO client) {
        String sql = "INSERT INTO CLIENT (CID, CPW, CNAME, CPHONE) VALUES (?, ?, ?, ?)";

        try {
            connect(); // 먼저 연결을 열고
            pstmt = con.prepareStatement(sql); // 전역 변수 pstmt 사용

            // PreparedStatement에 각 파라미터 설정
            pstmt.setString(1, client.getCID());    // 고객 ID
            pstmt.setString(2, client.getCPW());    // 고객 비밀번호
            pstmt.setString(3, client.getCNAME());  // 고객 이름
            pstmt.setString(4, client.getCPHONE()); // 고객 전화번호

            // SQL 실행
            int result = pstmt.executeUpdate();

            // 결과 확인
            if (result > 0) {
                System.out.println("\n회원가입이 성공적으로 완료되었습니다.");
            } else {
                System.out.println("회원가입에 실패하였습니다. 다시 시도해주세요.");
            }
        } catch (SQLException e) {
            // SQLException 메시지를 출력한 후 예외를 던짐
            System.out.println("데이터베이스 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            // 자원 해제 및 연결 종료
            closePreparedStatement(); // PreparedStatement 종료
            closeConnection();        // 연결 종료
        }
    }

    // 고객 로그인 메소드
    public ClientDTO loginClient(ClientDTO client) {
        String sql = "SELECT * FROM CLIENT WHERE CID = ?"; // 아이디로만 검색
        ClientDTO loggedInClient = null; // 로그인된 사용자 정보를 저장할 변수

        try {
            connect(); // 데이터베이스 연결

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, client.getCID());

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // 아이디로 사용자를 찾았을 때 비밀번호 비교
                String storedPassword = rs.getString("CPW");
                if (storedPassword.equals(client.getCPW())) {
                    // 로그인 성공 시 ClientDTO 객체에 데이터 설정
                    loggedInClient = new ClientDTO();
                    loggedInClient.setCID(rs.getString("CID"));
                    loggedInClient.setCPW(storedPassword);
                    loggedInClient.setCNAME(rs.getString("CNAME"));
                    loggedInClient.setCPHONE(rs.getString("CPHONE"));
                    System.out.println("로그인이 성공적으로 완료되었습니다.");
                } else {
                    System.out.println("비밀번호가 잘못되었습니다.");
                }
            } else {
                System.out.println("아이디가 존재하지 않습니다.");
            }

        } catch (Exception e) {
            System.out.println("로그인 과정 중 오류 발생: " + e.getMessage());
            throw new RuntimeException(e);

        } finally {
            closePreparedStatement(); // PreparedStatement 종료
            closeConnection();        // 연결 종료
        }

        return loggedInClient; // 로그인 성공 시 사용자 정보 반환, 실패 시 null 반환
    }

    // 특정 아이디의 계좌 수를 조회하는 메서드
    public int getAccountCountByCID(String cid) {
        String sql = "SELECT COUNT(*) FROM ACCOUNT WHERE CID = ?"; // 해당 CID의 계좌 수 조회
        int count = 0;

        try {
            connect(); // 데이터베이스 연결

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, cid); // CID 설정

            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1); // 조회된 계좌 수를 가져옴
            }

        } catch (SQLException e) {
            System.out.println("계좌 수 조회 중 오류 발생: " + e.getMessage());
            throw new RuntimeException(e);

        } finally {
            closePreparedStatement(); // PreparedStatement 종료
            closeConnection();        // 연결 종료
        }

        return count; // 계좌 수 반환
    }

    // 고객 계좌 생성 메소드
    public void AccountClient(AccountDTO account) {
        // 계좌 수를 조회하는 메서드 호출
        int accountCount = getAccountCountByCID(account.getCID());

        // 계좌 수가 3개 이상인 경우 계좌 생성 제한
        if (accountCount >= 3) {
            System.out.println("해당 아이디로는 최대 3개의 계좌만 생성할 수 있습니다.");
            return; // 계좌 생성 중단
        }

        // 계좌 정보를 데이터베이스에 삽입하는 메서드
        String sql = "INSERT INTO ACCOUNT (ACNUM, CODENUM, CID, BALANCE) VALUES (?, ?, ?, ?)"; // SQL 쿼리: ACCOUNT 테이블에 데이터를 삽입

        try {
            connect(); // 데이터베이스 연결을 시작

            pstmt = con.prepareStatement(sql); // PreparedStatement 객체를 생성하여 SQL 쿼리를 준비
            pstmt.setString(1, account.getACNUM()); // 첫 번째 ?에 계좌 번호 (ACNUM) 설정
            pstmt.setInt(2, account.getCODENUM()); // 두 번째 ?에 코드 번호 (CODENUM) 설정
            pstmt.setString(3, account.getCID()); // 세 번째 ?에 고객 ID (CID) 설정
            pstmt.setInt(4, account.getBALANCE()); // 네 번째 ?에 계좌 잔액 (BALANCE) 설정

            pstmt.executeUpdate(); // 쿼리를 실행하여 데이터를 삽입
            System.out.println("계좌가 성공적으로 생성되었습니다.");

        } catch (SQLException e) {
            // SQL 예외 발생 시 처리
            System.out.println("계좌 생성 중 오류가 발생했습니다: " + e.getMessage()); // 예외 메시지 출력

        } finally {
            closePreparedStatement(); // PreparedStatement 객체 종료
            closeConnection();        // 데이터베이스 연결 종료
        }
    }

    // 입금에 관한 메소드
    public void deposit(String acnum, int amount) {
        // SQL 쿼리: 입금액을 계좌의 현재 잔액에 더하는 업데이트 쿼리
        String sqlSelect = "SELECT BALANCE FROM ACCOUNT WHERE ACNUM = ?";
        String sqlUpdate = "UPDATE ACCOUNT SET BALANCE = BALANCE + ? WHERE ACNUM = ?";

        try {
            connect(); // 데이터베이스 연결

            // 현재 잔액 조회
            pstmt = con.prepareStatement(sqlSelect);
            pstmt.setString(1, acnum);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int currentBalance = rs.getInt("BALANCE"); // 현재 잔액 가져오기
                System.out.println("현재 잔액: " + currentBalance);

                // 잔액 업데이트
                pstmt = con.prepareStatement(sqlUpdate);
                pstmt.setInt(1, amount); // 입금할 금액 설정
                pstmt.setString(2, acnum); // 계좌 번호 설정

                int result = pstmt.executeUpdate(); // 업데이트 실행

                if (result > 0) {
                    System.out.println("입금이 성공적으로 완료되었습니다.");
                    System.out.println("입금 후 잔액: " + (currentBalance + amount)); // 새 잔액 출력
                } else {
                    System.out.println("입금에 실패했습니다.");
                }
            } else {
                System.out.println("계좌 번호를 찾을 수 없습니다.");
            }

        } catch (SQLException e) {
            // SQL 예외 처리
            System.out.println("입금 중 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException(e);

        } finally {
            // 자원 해제 및 연결 종료
            closePreparedStatement(); // PreparedStatement 종료
            closeConnection();        // 연결 종료
        }
    }


    // 출금 메소드
    public void withdraw(String acnum, int amount) {

        String sqlSelect = "SELECT BALANCE FROM ACCOUNT WHERE ACNUM = ?";
        String sqlUpdate = "UPDATE ACCOUNT SET BALANCE = BALANCE - ? WHERE ACNUM = ?";

        try {
            connect(); // 데이터베이스 연결

            // 현재 잔액 조회
            pstmt = con.prepareStatement(sqlSelect);
            pstmt.setString(1, acnum);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int currentBalance = rs.getInt("BALANCE"); // 현재 잔액 가져오기
                System.out.println("현재 잔액: " + currentBalance);

                // 출금액이 현재 잔액보다 큰 경우
                if (amount > currentBalance) {
                    System.out.println("잔액이 부족하여 출금할 수 없습니다.");
                    return;
                }

                // 잔액 업데이트
                pstmt = con.prepareStatement(sqlUpdate);
                pstmt.setInt(1, amount); // 출금할 금액 설정
                pstmt.setString(2, acnum); // 계좌 번호 설정

                int result = pstmt.executeUpdate(); // 업데이트 실행

                if (result > 0) {
                    System.out.println("출금이 성공적으로 완료되었습니다.");
                    System.out.println("출금 후 잔액: " + (currentBalance - amount)); // 잔액 출력
                } else {
                    System.out.println("출금에 실패했습니다.");
                }
            } else {
                System.out.println("계좌 번호를 찾을 수 없습니다.");
            }

        } catch (SQLException e) {
            // SQL 예외 처리
            System.out.println("출금 중 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException(e);

        } finally {
            // 자원 해제 및 연결 종료
            closePreparedStatement(); // PreparedStatement 종료
            closeConnection();        // 연결 종료
        }
    }

    public void transfer(String fromAccount, String toAccount, int amount) {
        // 출금 계좌의 잔액을 조회하는 SQL
        String checkBalanceSql = "SELECT BALANCE FROM ACCOUNT WHERE ACNUM = ?";
        // 출금 계좌에서 금액을 차감하는 SQL
        String withdrawSql = "UPDATE ACCOUNT SET BALANCE = BALANCE - ? WHERE ACNUM = ?";
        // 입금 계좌에 금액을 추가하는 SQL
        String depositSql = "UPDATE ACCOUNT SET BALANCE = BALANCE + ? WHERE ACNUM = ?";

        try {
            connect(); // 데이터베이스 연결

            // 출금 계좌의 잔액 확인
            pstmt = con.prepareStatement(checkBalanceSql);
            pstmt.setString(1, fromAccount);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int currentBalance = rs.getInt("BALANCE");

                // 출금할 금액이 현재 잔액보다 많을 경우 송금 중단
                if (currentBalance < amount) {
                    System.out.println("잔액이 부족하여 송금할 수 없습니다.");
                    return; // 송금 중단
                }
            } else {
                System.out.println("출금 계좌를 찾을 수 없습니다.");
                return; // 송금 중단
            }

            // 출금 처리
            pstmt = con.prepareStatement(withdrawSql);
            pstmt.setInt(1, amount);
            pstmt.setString(2, fromAccount);
            int withdrawResult = pstmt.executeUpdate();

            // 입금 처리
            pstmt = con.prepareStatement(depositSql);
            pstmt.setInt(1, amount);
            pstmt.setString(2, toAccount);
            int depositResult = pstmt.executeUpdate();

            // 출금과 입금이 모두 성공했는지 확인
            if (withdrawResult > 0 && depositResult > 0) {
                System.out.println("송금이 성공적으로 완료되었습니다.");
            } else {
                System.out.println("송금 과정에서 오류가 발생했습니다.");
            }

        } catch (SQLException e) {
            // SQL 예외 발생 시 처리
            System.out.println("송금 중 오류가 발생했습니다: " + e.getMessage()); // 예외 메시지 출력
            throw new RuntimeException(e);
        } finally {
            closePreparedStatement(); // PreparedStatement 종료
            closeConnection();        // 데이터베이스 연결 종료
        }
    }

    // 특정 사용자의 계좌 목록을 조회하는 메서드
    public List<AccountDTO> getAccountsByCID(String cid) {
        String sql = "SELECT ACNUM, CODENUM, BALANCE FROM ACCOUNT WHERE CID = ?"; // 해당 CID의 계좌 정보 조회
        List<AccountDTO> accounts = new ArrayList<>(); // 계좌 목록을 저장할 리스트

        try {
            connect(); // 데이터베이스 연결

            pstmt = con.prepareStatement(sql); // SQL 쿼리 준비
            pstmt.setString(1, cid); // CID 설정
            rs = pstmt.executeQuery();

            // 결과 집합에서 계좌 정보를 읽어와 리스트에 추가
            while (rs.next()) {
                AccountDTO account = new AccountDTO();
                account.setACNUM(rs.getString("ACNUM")); // 계좌 번호 설정
                account.setCODENUM(rs.getInt("CODENUM")); // 계좌 종류 설정
                account.setBALANCE(rs.getInt("BALANCE")); // 계좌 잔액 설정
                accounts.add(account); // 리스트에 계좌 추가
            }

        } catch (SQLException e) {
            System.out.println("계좌 조회 중 오류가 발생했습니다: " + e.getMessage()); // 예외 메시지 출력
            throw new RuntimeException(e);

        } finally {
            closePreparedStatement(); // PreparedStatement 종료
            closeConnection();        // 데이터베이스 연결 종료
        }

        return accounts; // 조회된 계좌 목록 반환
    }

}

