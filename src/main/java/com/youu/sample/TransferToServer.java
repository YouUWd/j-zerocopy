package com.youu.sample;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TransferToServer {
    ServerSocketChannel listener = null;

    protected void mySetup() {
        InetSocketAddress listenAddr = new InetSocketAddress(9026);

        try {
            listener = ServerSocketChannel.open();
            ServerSocket ss = listener.socket();
            ss.setReuseAddress(true);
            ss.bind(listenAddr);
            System.out.println("Listening on port : " + listenAddr.toString());
        } catch (IOException e) {
            System.out.println("Failed to bind, is port : " + listenAddr.toString()
                + " already in use ? Error Msg : " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        TransferToServer dns = new TransferToServer();
        dns.mySetup();
        dns.readData();
    }

    private void readData() {
        ByteBuffer dst = ByteBuffer.allocate(4096);
        try {
            while (true) {
                SocketChannel conn = listener.accept();
                System.out.println("Accepted : " + conn);
                conn.configureBlocking(true);
                String dstName = "src/dst/transfer.txt";
                FileChannel fout = new FileOutputStream(dstName).getChannel();
                int nread = 0;
                while (nread != -1) {
                    try {
                        fout.transferFrom(conn, nread, Long.MAX_VALUE);
                        nread = conn.read(dst);
                    } catch (IOException e) {
                        e.printStackTrace();
                        nread = -1;
                    }
                    dst.rewind();
                }
                fout.close();
                System.out.println("Connection closed by client");
                conn.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
