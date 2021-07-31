package socket;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author zhouzhm
 * @description
 * @create 2021-07-30 9:18
 * @project_name training
 */
public class Client {

    private final String ip = "127.0.0.1";
    private final int port = 9099;
    private String fileListPath = "E:\\client_files\\upClientFiles.db";

    public Client() {
    }

    /**
     *
     * @param filePath
     */
    public void upFile(String filePath) {
        //上传文件，将本地文件传输到服务器端
        try {
            Socket socket = new Socket(ip, port);
//            while (true) {
            // 选择进行传输的文件
            File fi = new File(filePath);

            System.out.println("文件长度:" + (int) fi.length());
            //添加本地到已上传文件
            addListFiles(fi.getName());
               /* DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                dis.readByte();
                */
            // 获取本地文件输入流和Socket输出流
            DataInputStream fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
            DataOutputStream ps = new DataOutputStream(socket.getOutputStream());

            //将文件名及长度传给客户端。这里要真正适用所有平台，例如中文名的处理，还需要加工，具体可以参见Think In Java 4th里有现成的代码。
            ps.writeUTF(fi.getName());
            ps.flush();
            //将文件大小传过去，用于简单校验
            ps.writeLong(fi.length());
            ps.flush();

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                ps.write(buffer, 0, len);
            }
            ps.flush();
            // 注意关闭socket链接哦，不然客户端会等待server的数据过来，
            // 直到socket超时，导致数据不完整。
            fis.close();
            socket.close();
            System.out.println("文件传输完成");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param filePath
     * @param filename
     */
    public void downFile(String filePath, String filename) {
        //从服务器端下载文件
        try {
            Socket socket = new Socket(ip, port + 10);
//            while (true) {

            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            os.writeUTF(filename);
            os.flush();

            DataInputStream is = new DataInputStream(socket.getInputStream());
            // 获取远程文件大小
            long fileServerSize = is.readLong();
            //1、得到文件名
            String fileName = is.readUTF();
            filePath += fileName;
            System.out.println("新生成的文件名为:" + filePath);
            FileOutputStream fos = new FileOutputStream(filePath);
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = is.read(b)) != -1) {
                //2、把socket输入流写到文件输出流中去
                fos.write(b, 0, length);
            }
            long size = fos.getChannel().size();
            if (fileServerSize == size) {
                System.out.println("文件:" + filePath + "下载成功");
            }
            os.close();
            fos.flush();
            fos.close();
            is.close();
            socket.close();
//            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void localFileList(File dir) {
        // 打印目录的子文件
        if (dir.isDirectory()) {
            File[] subfiles = dir.listFiles();

            assert subfiles != null;
            for (File f : subfiles) {
                System.out.println(f.getAbsolutePath());
            }
        } else {
            System.out.println("请输入文件目录");
        }

    }

    /**
     *
     * @param filename
     * @throws IOException
     */
    public void addListFiles(String filename) throws IOException {
        FileOutputStream fileDB = new FileOutputStream(fileListPath, true);
        String fileInputInfo = filename + "\n";
        fileDB.write(fileInputInfo.getBytes(StandardCharsets.UTF_8));
        fileDB.close();
    }

    /**
     *
     * @throws IOException
     */
    public void listAllUpFiles() throws IOException {
        FileOutputStream fileDB = new FileOutputStream(fileListPath, true);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileListPath));
        System.out.println("本地已上传文件");
        String str;
        while ((str = bufferedReader.readLine()) != null) { // 一次读取字符文本文件的一行字符
            System.out.println(str);
        }
        bufferedReader.close();
    }

}


