package site.metacoding.chat_v2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MyServerSocket {

    // 리스너 (연결받기) - 메인스레드
    ServerSocket serverSocket;
    List<고객전담스레드> 고객리스트; // socket를 저장하기 위한 리스트.

    // 서버는 메시지 받아서 보내기 (클라이언트 수마다)

    public MyServerSocket() {
        try {
            serverSocket = new ServerSocket(2000); // 포트번호 2000을 가진 서버소켓 생성
            // 연결이 되더라도 다른 사용자와 또 연결되야하므로 while 돌리기
            고객리스트 = new Vector<>();
            // 고객리스트 = new ArrayList<>();
            while (true) {
                Socket socket = serverSocket.accept(); // main 스레드
                // 연결할때마다 socket을 생성해야하므로 전역 변수로 만들수 없음.
                System.out.println("클라이언트 연결됨");
                고객전담스레드 t = new 고객전담스레드(socket);
                // 스택이 끝나면 socket이 사라지게 됨 => 새로운 클래스에 socket을 넘김.
                고객리스트.add(t); // 리스트에 클래스를 추가 = 리스트에다 클래스에 연결된 socket을 보관
                new Thread(t).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 내부 클래스 = 클래스 안에 클래스가 있음.
    class 고객전담스레드 implements Runnable { // socket을 받는 클래스.

        Socket socket;
        BufferedReader reader;
        BufferedWriter writer;
        boolean isLogin = true;

        public 고객전담스레드(Socket socket) { // socket을 받아서 본인 socket에 저장, 생성자는 main thread가 실행
            this.socket = socket;

            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String inputData = reader.readLine();
                    System.out.println("from 클라이언트 :" + inputData);
                    for (고객전담스레드 t : 고객리스트) { // for each 문, 컬렉션 크기만큼만 돌게함. 코드가 깔끔함.
                        t.writer.write(inputData + "\n");
                        t.writer.flush();
                        // 왼쪽: 컬렉션 타입, 오른쪽: 컬렉션
                    }
                }
                // 메세지 받았으니까 list<고객전담스레드> 고객리스트 <==여기에 담김
                // 모든 클라이언트에게 메세지 전송 (for문 돌려서)

                catch (Exception e) {
                    try {
                        System.out.println("통신실패" + e.getMessage()); // 통신하다가 오류가 터지면 catch가 되는데 while을 돌면 계속 catch가 잡기
                                                                     // 때문에, boolean 하나 만들어짜!
                        isLogin = false;
                        고객리스트.remove(this); // 고객리스트가 담고 있는 주소를 가지고 있음.
                        reader.close();
                        writer.close();
                        socket.close(); // close()를 안적어도 garbage collection을 하나 통신의 부하를 줄이기 위해 적어줌!
                        // 통신은 I/O접근이기 때문이다.
                    } catch (Exception e1) {
                        System.out.println("연결해제 프로세스 실패" + e1.getMessage());
                    }
                }

            }
        }

    }

    public static void main(String[] args) {
        new MyServerSocket();
    }
}