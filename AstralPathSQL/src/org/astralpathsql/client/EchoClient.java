package org.astralpathsql.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EchoClient {
    public static int PORT = 9999; 								// 绑定端口
    public static String HOST = "47.96.154.95"; 					// 连接主机
    public static void main(String[] args) throws Exception {
        try {
            System.out.println("欢迎使用AstralPathSQL!\n请先输入连接IP和端口!\n例如:127.0.0.1:9999");
            String msg = InputUtil.getString(">");	// 提示信息
            String resl[] = msg.split(":");
            HOST = resl[0];
            PORT = Integer.parseInt(resl[1]);
            SocketChannel clientChannel = SocketChannel.open(); 			// 获取客户端通道
            clientChannel.connect(new InetSocketAddress(HOST, PORT)); 		// 连接服务端
            System.out.println("[INFO]连接成功!Connected successfully");
            ByteBuffer buffer = ByteBuffer.allocate(10000000); 					// 开辟缓存
            boolean flag = true;
            while (flag) { 										 		// 持续输入信息
                buffer.clear(); 											// 清空缓冲区
                msg = InputUtil.getString(">");	// 提示信息
                buffer.put(msg.getBytes());								// 数据保存在缓冲区
                buffer.flip(); 											// 重设缓冲区
                clientChannel.write(buffer);								// 发送消息
                buffer.clear();											// 清空缓冲区
                int readCount = clientChannel.read(buffer);				// 读取服务端回应
                buffer.flip(); 											// 重置缓冲区
                String a = new String(buffer.array(), 0, readCount);
                if (msg.equals("getall")) {
                    String res[] = a.split("§");
                    for (int x = 0;x < res.length ;x ++) {
                        System.out.println(res[x]);
                    }
                } else {
                    System.out.println(a);
                }
                if ("exit".equals(msg)) {									// 结束指令
                    flag = false; 										// 结束循环
                }
            }
            clientChannel.close();										// 关闭通道
        } catch (Exception e) {
            System.err.println("出现了错误!");
            e.printStackTrace();
            Thread.sleep(1000);
            String msg = InputUtil.getString("按Enter退出",1);	// 提示信息
            System.exit(1);
        }

    }

}
