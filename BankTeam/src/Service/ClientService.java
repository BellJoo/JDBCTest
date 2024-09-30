package Service;

import BankDAO.BankSQL;
import BankDTO.ClientDTO;

import java.util.Scanner;
import java.util.regex.Pattern;

public class ClientService {

    // 정규식 패턴 정의
    private static final String ID_PATTERN = "^[a-zA-Z0-9]{5,15}$";  // 아이디: 영문자와 숫자, 5~15자
    private static final String PW_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";  // 비밀번호: 대소문자, 숫자, 특수문자 포함 최소 8자
    private static final String NAME_PATTERN = "^[가-힣]{2,5}$";  // 이름: 한글 2~5자
    private static final String PHONE_PATTERN = "^(01[0-9]\\d{7,8})|(0[2-6][0-9]\\d{7,8})$"; // 전화번호 : 숫자만 허용

    private final Scanner sc = new Scanner(System.in);
    private final BankSQL bankSQL = new BankSQL();
    private final ClientDTO client = new ClientDTO();

    // 회원가입 메서드
    public void register() {

        ClientDTO client = new ClientDTO();
        boolean validInput = false;

        while (!validInput) {
            try {
                // 사용자로부터 회원 정보를 입력 받음
                System.out.println("회원정보를 입력하세요");

                // 아이디 입력 및 검증
                System.out.println("\n아이디는 영문자와 숫자로 구성된 5~15자로 입력해주세요.");
                System.out.print("아이디 >> ");
                String cid = sc.next();
                if (!Pattern.matches(ID_PATTERN, cid)) {
                    throw new IllegalArgumentException("아이디는 영문자와 숫자로 구성된 5~15자로 입력해주세요.");
                }
                client.setCID(cid);

                // 비밀번호 입력 및 검증
                System.out.println("\n비밀번호는 대소문자, 숫자, 특수문자를 포함한 최소 8자로 입력해주세요.");
                System.out.print("비밀번호 >> ");
                String cpw = sc.next();
                if (!Pattern.matches(PW_PATTERN, cpw)) {
                    throw new IllegalArgumentException("비밀번호는 대소문자, 숫자, 특수문자를 포함한 최소 8자로 입력해주세요.");
                }
                client.setCPW(cpw);

                // 이름 입력 및 검증
                System.out.println("\n이름은 한글 2~5자로 입력해주세요");
                System.out.print("이름 >> ");
                String cname = sc.next();
                if (!Pattern.matches(NAME_PATTERN, cname)) {
                    throw new IllegalArgumentException("이름은 한글 2~5자로 입력해주세요.");
                }
                client.setCNAME(cname);

                // 전화번호 검증
                System.out.println("\n전화번호를 입력해주세요 (예: 01012345678 또는 021234567)");
                System.out.print("전화번호 >> ");
                String cphone = sc.next();
                if (!Pattern.matches(PHONE_PATTERN, cphone)) {
                    throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다. 01012345678 또는 021234567 형식으로 입력해주세요.");
                }
                client.setCPHONE(cphone);

                // 유효한 입력을 받았을 때
                validInput = true;

                // 회원가입 처리
                BankSQL bankSQL = new BankSQL();
                bankSQL.registerClient(client);

            } catch (IllegalArgumentException e) {
                // 유효성 검사 실패 시 메시지 출력 후 재입력 유도
                System.out.println(e.getMessage());
                System.out.println("다시 시도해주세요.");
                sc.nextLine(); // 입력 버퍼 비우기
            } catch (Exception e) {
                // 기타 예외 처리
                System.out.println("오류가 발생했습니다: " + e.getMessage());
                System.out.println("다시 시도해주세요.");
                sc.nextLine(); // 입력 버퍼 비우기
            }
        }
    }

    // 로그인 메서드
    public ClientDTO clientLogin() {

        // 사용자로부터 로그인 정보를 입력받음
        System.out.print("아이디를 입력하세요: ");
        client.setCID(sc.next());

        System.out.print("비밀번호를 입력하세요: ");
        client.setCPW(sc.next());

        // 로그인 시도
        ClientDTO loggedInClient = bankSQL.loginClient(client);

        if (loggedInClient != null) {
            System.out.println("로그인 성공: " + loggedInClient.getCNAME() + "님 환영합니다.");
            return loggedInClient; // 로그인 성공 시 로그인된 사용자 정보 반환
        } else {
            System.out.println("로그인에 실패했습니다. 아이디 또는 비밀번호를 확인해주세요.");
            return null; // 로그인 실패 시 null 반환
        }

    }
}
