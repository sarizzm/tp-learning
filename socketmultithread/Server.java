package socketmultithread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author zhouzhm
 * @description
 * @create 2021-07-30 9:30
 * @project_name training
 */
public class Server {
    private final int port = 9099;
    private String filePath = "E:\\server_files\\";

//    public static void main(String[] args) {
//        Server server = new Server();
////        server.UpFile();
////        server.DownFile();
//        server.DownFile();
//    }

//    /**
//     * @param file
//     * @param socket
//     * @param filename
//     * @param size
//     * @throws IOException
//     */
//    public void serverDB(File file, Socket socket, String filename, long size) throws IOException {
//        String sizeName;
//        if (size > 1024 * 1024 * 1024) {
//            sizeName = String.format("%.2f", size / Math.pow(1024, 3)) + "G";
//        } else if (size > 1024 * 1024) {
//            sizeName = String.format("%.2f", size / Math.pow(1024, 2)) + "M";
//        } else if (size > 1024) {
//            sizeName = String.format("%.2f", (float) (size / 1024)) + "KB";
//        } else {
//            sizeName = size + "B";
//        }
//
//        FileOutputStream fileDB = new FileOutputStream(file, true);
//        String fileInputInfo = filename + " \t" + socket.getInetAddress().getHostAddress() + " \t"
//                + socket.getPort() + " \t" + sizeName + "\n";
//        fileDB.write(fileInputInfo.getBytes(StandardCharsets.UTF_8));
//        fileDB.close();
//    }

    /**
     *
     */
    public void upFile() {//接受客户端上传的文件，并保存
        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket = null;
            while (true) {
                socket = server.accept();
                Thread ServerThread = new Thread(new ServerHandleThreadUpFile(socket, filePath));
                System.out.println(ServerThread.getName());
                ServerThread.start();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    /**
     *
     */
    public void downFile() {
//接受客户端的下载请求，将本地文件传输给客户端
        try {
            ServerSocket server = new ServerSocket(port + 10);
            Socket socket =null;

            while (true) {


                socket = server.accept();
                System.out.println("建立socket链接");
                Thread thread = new Thread(new ServerHandleThreadDownloadFile(socket, filePath));
                System.out.println(thread.getName());
                thread.start();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @throws IOException
     */
    public void serverFileList() throws IOException {
        File file = new File(filePath + "Server.db");
        if (file.exists() && file.length() > 1) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            System.out.println("文件名" + " \t" + "上传者 IP" + " \t" + "上传者端口号" + " \t" + "文件大小");
            String str;
            while ((str = bufferedReader.readLine()) != null) { // 一次读取字符文本文件的一行字符
                System.out.println(str);
            }
            bufferedReader.close();
        } else {
            System.out.println("服务器为空，请上传文件");
        }
//

    }

    public boolean isFileExit(String fileName) {
        if (fileName != null) {
            File file = new File(filePath + fileName);
            return file.exists();
        }
        return false;

    }

    public int getPort() {
        return port;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
