package socketmultithread;

import java.io.*;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author zhouzhm
 * @description
 * @create 2021-08-01 20:49
 * @project_name training
 */
public class ServerHandleThreadUpFile implements Runnable {
    Socket socket = null;// 和本线程相关的Socket
    String filePath = null;

    public ServerHandleThreadUpFile(Socket socket, String filePath) {
        super();
        this.filePath = filePath;
        this.socket = socket;
    }

    @Override
    public void run() {

        DataInputStream is = null;
        FileOutputStream fos = null;
        try {
            is = new DataInputStream(socket.getInputStream());
            OutputStream os = socket.getOutputStream();
            //1、得到文件名

            String newFileName = is.readUTF();
            String pathname = filePath + newFileName;
            //2. 获取文件大小
            long fileSizeClient = is.readLong();
            System.out.println("新生成的文件名为:" + pathname);
            File file = new File("E:\\server_files\\Server.db");
            fos = new FileOutputStream(pathname);
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    /**
     * @param file
     * @param socket
     * @param filename
     * @param size
     * @throws IOException
     */
    public synchronized void serverDB(File file, Socket socket, String filename, long size) throws IOException {
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

}
