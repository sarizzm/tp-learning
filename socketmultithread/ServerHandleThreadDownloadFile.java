package socketmultithread;

import java.io.*;
import java.net.Socket;

/**
 * @author zhouzhm
 * @description
 * @create 2021-08-01 21:29
 * @project_name training
 */
public class ServerHandleThreadDownloadFile implements Runnable {
    Socket socket = null;
    String filePath = null;

    public ServerHandleThreadDownloadFile(Socket socket, String filePath) {
        super();
        this.socket = socket;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        DataInputStream is = null;
        DataInputStream fis = null;
        try {
            is = new DataInputStream(socket.getInputStream());
            String fileName = is.readUTF();
            System.out.println(fileName);

            File fi = new File(filePath + fileName);
            System.out.println("文件长度:" + fi.length());

            fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath + fileName)));
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
            ps.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
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
        System.out.println("文件传输完成");
    }
}
