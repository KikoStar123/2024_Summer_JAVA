package server;

import org.h2.tools.Server;
import server.handler.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.net.InetAddress;
/**
 * 主服务器类，负责启动主要服务，包括 H2 数据库服务器和文件存储服务。
 * 该类还负责监听客户端连接并处理这些连接。
 */
public class MainServer {
    private static final int SERVER_PORT = 8080; // 服务器端口
    private static final int THREAD_POOL_SIZE = 10; // 线程池大小
    private static final Logger logger = Logger.getLogger(MainServer.class.getName());

    /**
     * 服务器主入口。
     * @param args 命令行参数。
     */
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try {
            // 设置日志记录到文件
            FileHandler fileHandler = new FileHandler("server.log", true); // true 表示追加模式
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            // 日志级别设置
            logger.setLevel(Level.INFO);
            logger.info("Server is starting...");

            // 启动 H2 数据库服务器
            startH2Server();

            // 启动文件存储服务
            new Thread(FileStorageServer::startFileServer).start();

            try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT, 50, InetAddress.getByName("0.0.0.0"))) {
                logger.info("Server is running and listening on port " + SERVER_PORT + "...");

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    logger.info("Accepted new client connection from " + clientSocket.getInetAddress());
                    threadPool.execute(new ClientHandler(clientSocket));
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Server encountered an error: ", e);
            } finally {
                threadPool.shutdown();
                logger.info("Server is shutting down...");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize the logger: ", e);
        }
    }
    /**
     * 启动 H2 数据库服务器。
     * 数据库服务器在本地端口 9092 上启动，允许远程连接。
     */
    private static void startH2Server() {
        try {
            // 启动 H2 数据库服务器，指定相对路径
            Server server = Server.createTcpServer(
                    "-tcpAllowOthers",
                    "-tcpPort", "9092",
                    "-baseDir", "./"
            ).start();
            System.out.println("H2 server started and listening on port 9092.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
