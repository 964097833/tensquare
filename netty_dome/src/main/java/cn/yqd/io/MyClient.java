package cn.yqd.io;

import java.net.Socket;

public class MyClient {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new ClientDemo().start();
        }
    }

    static class ClientDemo extends Thread {
        @Override
        public void run() {
            try {
                Socket socket = new Socket("127.0.0.1",8000);

                while (true) {
                    socket.getOutputStream().write(("测试数据").getBytes());
                    socket.getOutputStream().flush();
                    Thread.sleep(2000);
                }
            } catch (Exception e) {

            }
        }
    }
}
