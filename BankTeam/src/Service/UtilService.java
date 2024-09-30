package Service;

import BankDAO.BankSQL;
import BankDTO.AccountDTO;

import java.util.*;

public class UtilService {

    private final Scanner sc = new Scanner(System.in);
    private final BankSQL bankSQL = new BankSQL();
    private final AccountDTO account = new AccountDTO();

    // 계좌 생성 메서드
    public void createAccount(String cid) {
        System.out.println("계좌 생성을 시작합니다.");
        System.out.print("계좌 종류를 입력하세요 (1 ~ 4): ");
        System.out.println("1.예금 2.적금 3.청약 4.주식");

        try {
            System.out.print("계좌 종류 >> ");
            account.setCODENUM(sc.nextInt());
        } catch (Exception e) {
            System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
            sc.nextLine();  // 잘못된 입력 버퍼 비우기
            return;
        }

        // CID 값을 AccountDTO에 설정
        account.setCID(cid);

        // 숫자만 포함된 계좌 번호 생성
        String generatedAccountNumber = generateAccountNumberFromUUID();
        account.setACNUM(generatedAccountNumber);

        // 초기 입금액은 입력받지 않고 기본값 0을 사용
        account.setBALANCE(0);

        // 데이터베이스에 계좌 정보 삽입
        bankSQL.AccountClient(account);
    }

    // 입금 메서드
    public void deposit() {
        // 사용자로부터 입금 정보를 입력 받음
        System.out.println("입금 정보를 입력하세요");

        System.out.print("계좌번호 >> ");
        String ACNUM = sc.next();

        // 계좌번호 형식 유효성 검사 (예시: 계좌번호는 333-로 시작하고 10자리 숫자)
        if (!ACNUM.matches("^333-\\d{10}$")) {
            System.out.println("잘못된 계좌번호 형식입니다. 333-로 시작하는 10자리 숫자를 입력하세요.");
            return;
        }

        System.out.print("입금액 >> ");
        try {
            int amount = sc.nextInt();

            // 입금액이 0보다 큰지 확인
            if (amount <= 0) {
                System.out.println("입금액은 0보다 커야 합니다.");
                return;
            }

            // 입금 처리
            bankSQL.deposit(ACNUM, amount);

        } catch (InputMismatchException e) {
            System.out.println("잘못된 금액 입력입니다. 숫자를 입력해주세요.");
            sc.nextLine(); // 입력 버퍼 비우기
        } catch (RuntimeException e) {
            System.out.println("입금 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // UUID 숫자 추출 메서드
    public String generateAccountNumberFromUUID() {
        // UUID 생성 후 숫자만 추출
        StringBuilder uuid = new StringBuilder(UUID.randomUUID().toString().replaceAll("[^0-9]", "")); // 숫자만 남기기

        // 필요한 길이만큼 잘라 사용 (10자리 사용, 부족할 경우 앞에 0 추가)
        while (uuid.length() < 10) {
            uuid.append("0"); // 숫자 길이가 10자리 미만일 경우 0으로 채움
        }
        String uniqueNumber = uuid.substring(0, 10); // 10자리로 자르기

        return "333-" + uniqueNumber;
    }

    // 출금 메서드
    public void withdraw() {
        // 사용자로부터 출금 정보를 입력 받음
        System.out.println("출금 정보를 입력하세요");

        System.out.print("계좌번호 >> ");
        String ACNUM = sc.next();

        // 계좌번호 형식 유효성 검사 (예시: 계좌번호는 333-로 시작하고 10자리 숫자)
        if (!ACNUM.matches("^333-\\d{10}$")) {
            System.out.println("잘못된 계좌번호 형식입니다. 333-로 시작하는 10자리 숫자를 입력하세요.");
            return;
        }

        System.out.print("출금액 >> ");
        try {
            int amount = sc.nextInt();

            // 출금액이 0보다 큰지 확인
            if (amount <= 0) {
                System.out.println("출금액은 0보다 커야 합니다.");
                return;
            }

            // 출금 처리
            bankSQL.withdraw(ACNUM, amount);

        } catch (InputMismatchException e) {
            System.out.println("잘못된 금액 입력입니다. 숫자를 입력해주세요.");
            sc.nextLine(); // 입력 버퍼 비우기
        } catch (RuntimeException e) {
            System.out.println("출금 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 송금 메서드 호출
    public void performTransfer() {
        // 사용자로부터 송금 정보를 입력 받음
        System.out.println("송금 정보를 입력하세요");

        // 출금 계좌 입력
        System.out.print("출금할 계좌번호를 입력하세요: ");
        String fromAccount = sc.next();

        // 계좌번호 형식 유효성 검사 (예시: 계좌번호는 333-로 시작하고 10자리 숫자)
        if (!fromAccount.matches("^333-\\d{10}$")) {
            System.out.println("잘못된 계좌번호 형식입니다. 333-로 시작하는 10자리 숫자를 입력하세요.");
            return;
        }

        // 입금 계좌 입력
        System.out.print("입금할 계좌번호를 입력하세요: ");
        String toAccount = sc.next();

        // 계좌번호 형식 유효성 검사
        if (!toAccount.matches("^333-\\d{10}$")) {
            System.out.println("잘못된 계좌번호 형식입니다. 333-로 시작하는 10자리 숫자를 입력하세요.");
            return;
        }

        // 송금 금액 입력
        System.out.print("송금할 금액을 입력하세요: ");
        try {
            int amount = sc.nextInt();

            // 송금 금액이 0보다 큰지 확인
            if (amount <= 0) {
                System.out.println("송금액은 0보다 커야 합니다.");
                return;
            }

            // 송금 메서드 호출
            bankSQL.transfer(fromAccount, toAccount, amount);

        } catch (InputMismatchException e) {
            System.out.println("잘못된 금액 입력입니다. 숫자를 입력해주세요.");
            sc.nextLine(); // 입력 버퍼 비우기
        } catch (RuntimeException e) {
            System.out.println("송금 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 계좌 조회 메서드
    public void viewAccounts(String cid) {
        List<AccountDTO> accounts = bankSQL.getAccountsByCID(cid); // 계좌 조회 메서드 호출

        // 조회된 계좌 정보 출력
        // 계좌 종류 매핑을 위한 Map
        Map<Integer, String> category = new HashMap<>();
        category.put(1, "예금");
        category.put(2, "적금");
        category.put(3, "청약");
        category.put(4, "주식");

        // 조회된 계좌 정보 출력
        if (accounts.isEmpty()) {
            System.out.println("등록된 계좌가 없습니다.");
        } else {
            System.out.println("조회된 계좌 목록:");
            for (AccountDTO account : accounts) {
                String accountType = category.getOrDefault(account.getCODENUM(), "알 수 없음"); // (getOrDefault) https://junghn.tistory.com/entry/JAVA-Map-getOrDefault-%EC%9D%B4%EB%9E%80-%EC%82%AC%EC%9A%A9%EB%B2%95-%EB%B0%8F-%EC%98%88%EC%A0%9C
                // 계좌 출력
                System.out.printf("계좌종류: %s, 계좌번호: %s, 잔액: %d\n", accountType, account.getACNUM(), account.getBALANCE());
            }
        }
    }
}

