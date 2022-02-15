package site.metacoding.chat;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClientSocket {

    Socket socket;
    BufferedWriter writer;
    PrintWriter pw;

    public MyClientSocket() {
        try {
            socket = new Socket("localhost", 1077); // localhost: 아이피 주소, 1077: 포트 번호.
            // writer = new BufferedWriter(
            // new OutputStreamWriter(socket.getOutputStream()));
            // writer.write("안녕\n"); // 메세지 끝이라는걸 알려줘야함 "\n"으로,, 버퍼에 담은 거다.
            // writer.flush();
            pw = new PrintWriter(socket.getOutputStream(), true); // true라고 걸어주면 자동 flush함, 오우 버퍼드롸이터구나!
            pw.println("유승현");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MyClientSocket();
    }
}
