package site.metacoding.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocket {

    ServerSocket serverSocket; // 리스너(연결=세션)
    Socket socket; // 메시지 통신
    BufferedReader reader;

    public MyServerSocket() {
        try {
            // 1.서버소켓 생성(리스너)
            serverSocket = new ServerSocket(1077); // . well-known port 는 사용하면 안된다. 포트가 충돌 남!!
            System.out.println("서버소켓이 1077번 기다리다는 중");
            socket = serverSocket.accept(); // while을 돌면서 대기 시킨다.(랜덤포트 지정해준다.). 포트 충돌 막을려고 ->Demon 프로그램.
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputData = reader.readLine();
            System.out.println("받은메시지:" + inputData);
            System.out.println("클라이언트 연결됨");
        } catch (Exception e) {
            System.out.println("통신오류발생" + e.getMessage());
            // e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyServerSocket();
        System.out.println("메인종료됨");
    }
}