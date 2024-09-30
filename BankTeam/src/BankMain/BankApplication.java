package BankMain;

import BankDAO.BankSQL;
import BankDTO.ClientDTO;
import Service.ClientService;
import Service.UtilService;

import java.util.Scanner;

public class BankApplication {

    public void startApplication() {
        try (Scanner sc = new Scanner(System.in)) {
            boolean run = true;
            ClientDTO loggedInClient = null; // 로그인된 사용자 정보를 저장할 변수
            int menu = 0;
            BankSQL sql = new BankSQL();
            ClientService clientService = new ClientService();
            UtilService utilService = new UtilService();

            System.out.println("은행에 오신 것을 환영합니다.");
            System.out.println("무엇을 도와드릴까요?");

            // 데이터베이스 연결
            sql.connect();

            while (run) {
                if (loggedInClient == null) { // 로그인되지 않은 상태
                    System.out.println("┌───────────────────────────────────┐");
                    System.out.println("  [1] 로그인   [2] 회원가입   [3] 종료  ");
                    System.out.println("└───────────────────────────────────┘");
                    System.out.print("선택 >> ");

                    try {
                        menu = sc.nextInt();
                    } catch (Exception e) {
                        System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                        sc.nextLine();  // 잘못된 입력 버퍼 비우기
                        continue;
                    }

                    switch (menu) {
                        case 1:
                            // 로그인
                            loggedInClient = clientService.clientLogin(); // 로그인 성공 시 사용자 정보 반환
                            if (loggedInClient == null) {
                                System.out.println("다시 시도해주세요.");
                            }
                            break;

                        case 2:
                            // 회원가입
                            clientService.register();
                            break;

                        case 3:
                            // 종료
                            run = false;
                            System.out.println("프로그램을 종료합니다.");
                            break;

                        default:
                            System.out.println("다시 입력하세요");
                            break;
                    }

                } else {
                    // 로그인된 상태에서만 계좌 관련 메뉴 제공
                    System.out.println("┌────────────────────────────────────────────┐");
                    System.out.println("  [1] 생성   [2] 입금   [3] 출금                ");
                    System.out.println("  [4] 송금   [5] 조회   [6] 로그아웃             ");
                    System.out.println("└────────────────────────────────────────────┘");
                    System.out.print("선택 >> ");

                    try {
                        menu = sc.nextInt();
                    } catch (Exception e) {
                        System.out.println("잘못된 입력입니다. 숫자를 입력해주세요.");
                        sc.nextLine();  // 잘못된 입력 버퍼 비우기
                        continue;
                    }

                    switch (menu) {
                        case 1:
                            // 계좌 생성
                            utilService.createAccount(loggedInClient.getCID());
                            break;

                        case 2:
                            // 입금
                            utilService.deposit();
                            break;

                        case 3:
                            // 출금
                            utilService.withdraw();
                            break;

                        case 4:
                            // 송금
                            utilService.performTransfer();
                            break;

                        case 5:
                            // 계좌 조회
                            utilService.viewAccounts(loggedInClient.getCID());
                            break;

                        case 6:
                            // 로그아웃
                            loggedInClient = null; // 로그아웃 시 로그인 정보 초기화
                            System.out.println("로그아웃 되었습니다.");
                            break;

                        default:
                            System.out.println("다시 입력하세요");
                            break;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("프로그램 실행 중 오류가 발생했습니다: " + e.getMessage());
        }

    }
}
