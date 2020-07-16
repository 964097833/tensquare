package cn.yqd.io;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IOServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8000);

        while (true) {
            Socket socket = serverSocket.accept();

            new Thread() {
                @Override
                public void run() {
                    String name = Thread.currentThread().getName();
                    try {
                        byte[] data = new byte[1024];
                        InputStream inputStream = socket.getInputStream();

                        while (true) {
                            int len;
                            if ((len = inputStream.read(data)) != -1) {
                                System.out.println("线程"+name+":"+new String(data,0,len));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
