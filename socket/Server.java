package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;


/**
 * @author zhouzhm
 * @description
 * @create 2021-07-30 9:30
 * @project_name training
 */
public class Server {
    private final int port = 9099;
    private final String filePath = "E:\\server_files\\";

//    public static void main(String[] args) {
//        Server server = new Server();
////        server.UpFile();
////        server.DownFile();
//        server.DownFile();
//    }

    /**
     * @param file
     * @param socket
     * @param filename
     * @param size
     * @throws IOException
     */
    public void serverDB(File file, Socket socket, String filename, long size) throws IOException {
        String sizeName;
        if (size > 1024 * 1024 * 1024) {
            sizeName = String.format("%.2f", size / Math.pow(1024, 3)) + "G";
        } else if (size > 1024 * 1024) {
            sizeName = String.format("%.2f", size / Math.pow(1024, 2)) + "M";
        } else if (size > 1024) {
            sizeName = String.format("%.2f", (float) (size / 1024)) + "KB";
        } else {
            sizeName = size + "B";
        }

        FileOutputStream fileDB = new FileOutputStream(file, true);
        String fileInputInfo = filename + " \t" + socket.getInetAddress().getHostAddress() + " \t"
                + socket.getPort() + " \t" + sizeName + "\n";
        fileDB.write(fileInputInfo.getBytes(StandardCharsets.UTF_8));
        fileDB.close();
    }

    /**
     *
     */
    public void upFile() {//接受客户端上传的文件，并保存
        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                Socket socket = server.accept();
                DataInputStream is = new DataInputStream(socket.getInputStream());
                OutputStream os = socket.getOutputStream();
                //1、得到文件名

                String newFileName = is.readUTF();
                String pathname = filePath + newFileName;
                //2. 获取文件大小
                long fileSizeClient = is.readLong();
                System.out.println("新生成的文件名为:" + pathname);
                File file = new File("E:\\server_files\\Server.db");
                FileOutputStream fos = new FileOutputStream(pathname);
                byte[] b = new byte[1024];
                int length = 0;
                while ((length = is.read(b)) != -1) {
                    //2、把socket输入流写到文件输出流中去
                    fos.write(b, 0, length);
                }
                FileChannel fileC = fos.getChannel();
                long size = fileC.size();
                if (size == fileSizeClient) {
                    serverDB(file, socket, newFileName, size);
                }

                fos.flush();
                fos.close();
                is.close();
                socket.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
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

            while (true) {


                Socket socket = server.accept();
                System.out.println("建立socket链接");

                DataInputStream is = new DataInputStream(socket.getInputStream());
                String fileName = is.readUTF();
                System.out.println(fileName);

                File fi = new File(filePath + fileName);
                System.out.println("文件长度:" + fi.length());

                DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath + fileName)));
                DataOutputStream ps = new DataOutputStream(socket.getOutputStream());

                ps.writeLong(fi.length());
                ps.flush();

                //将文件名及长度传给客户端。这里要真正适用所有平台，例如中文名的处理，还需要加工，具体可以参见Think In Java 4th里有现成的代码。
                ps.writeUTF(fi.getName());
                System.out.println(fi.getName());
                ps.flush();

                int length = 0;
                int bufferSize = 1024;
                byte[] buf = new byte[bufferSize];
                while ((length = fis.read(buf)) != -1) {
                    ps.write(buf, 0, length);
                }
                ps.flush();

                // 注意关闭socket链接哦，不然客户端会等待server的数据过来，
                // 直到socket超时，导致数据不完整。
                is.close();
                fis.close();
//                ps.close();
                socket.close();
                System.out.println("文件传输完成");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @throws IOException
     */
    public void serverFileList() throws IOException {
        File file = new File(filePath + "Server.db");
        if(file.exists()&&file.length()>1){
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            System.out.println("文件名" + " \t" + "上传者 IP" + " \t" + "上传者端口号" + " \t" + "文件大小");
            String str;
            while ((str = bufferedReader.readLine()) != null) { // 一次读取字符文本文件的一行字符
                System.out.println(str);
            }
            bufferedReader.close();
        }else {
            System.out.println("服务器为空，请上传文件");
        }
//

    }

    public boolean isFileExit(String fileName){
        if(fileName!=null){
            File file = new File(filePath + fileName);
            return file.exists();
        }
        return false;

    }

}
