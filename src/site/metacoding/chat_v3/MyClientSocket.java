package site.metacoding.chat_v2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class MyClientSocket {

    Socket socket;

    // 키보드로부터 받아서 바로 쓸 스레드 하나 필요
    Scanner sc;
    BufferedWriter writer;

    // 스레드
    BufferedReader reader;

    public MyClientSocket() {
        try {

            socket = new Socket("localhost", 2000); // 1번 소켓 연결

            sc = new Scanner(System.in);
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // 새로운 스레드는 읽기 전용, 메인스레드가 while을 돌면 밑에 코드가 실행을 못하기 때문에 새로운 스레드 먼저 실행
            new Thread(new 읽기전담스레드()).start(); // 스레드 실행 시켜줘야함

            // 메인스레드는 쓰기 전용
            while (true) {
                String KeyboardInputData = sc.nextLine();
                writer.write(KeyboardInputData + "\n");
                writer.flush(); // 버퍼 담긴 것을 stream으로 흘려보내기
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class 읽기전담스레드 implements Runnable { // 내부클래스를 쓰면, MyclientSocket의 전역변수를 모두 다 쓸 수 있음.

        @Override
        public void run() {
            try {
                while (true) {
                    String inputData = reader.readLine();
                    System.out.println("받은 메세지 :" + inputData);
                }
            } catch (Exception e) {

            }

        }

    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
