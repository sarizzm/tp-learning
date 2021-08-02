package socketmultithread;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author zhouzhm
 * @description 客户端实现文件上传和下载
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
     * @param filePath 读取filePath路径中的文件上传到服务器
     */
    public void upFile(String filePath) {
        //上传文件，将本地文件传输到服务器端
        Socket socket = null;
        DataInputStream fis = null;
        DataOutputStream ps = null;
        try {
            socket = new Socket(ip, port);

            // 选择进行传输的文件
            File fi = new File(filePath);

            System.out.println("文件长度:" + (int) fi.length());
            //添加本地到已上传文件
            addListFiles(fi.getName());
               /* DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
                dis.readByte();
                */
            // 获取本地文件输入流和Socket输出流
            fis = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
            ps = new DataOutputStream(socket.getOutputStream());

            //将文件名及长度传给客户端。
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
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
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
        System.out.println("文件传输完成");


    }

    /**
     * @param filePath 本地文件夹
     * @param filename 远程文件名
     */
    public void downFile(String filePath, String filename) {
        Socket socket = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        FileOutputStream fos = null;
        try {
            //从服务器端下载文件
            socket = new Socket(ip, port + 10);

            os = new DataOutputStream(socket.getOutputStream());
            os.writeUTF(filename);
            os.flush();

            is = new DataInputStream(socket.getInputStream());
            // 获取远程文件大小
            long fileServerSize = is.readLong();
            //1、得到文件名
            String fileName = is.readUTF();
            filePath += fileName;
            System.out.println("新生成的文件名为:" + filePath);
            fos = new FileOutputStream(filePath);
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
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
     * @param filename 本地上传的文件名
     *
     */
    public void addListFiles(String filename) throws IOException {
        FileOutputStream fileDB = new FileOutputStream(fileListPath, true);
        String fileInputInfo = filename + "\n";
        fileDB.write(fileInputInfo.getBytes(StandardCharsets.UTF_8));
        fileDB.close();
    }

    /**
     * 列出所有的已上传文件名
     */
    public void listAllUpFiles() throws IOException {
        File file = new File(fileListPath);
        if (file.exists() && file.length() > 1) {
            FileOutputStream fileDB = new FileOutputStream(fileListPath, true);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileListPath));
            System.out.println("本地已上传文件");
            String str;
            while ((str = bufferedReader.readLine()) != null) { // 一次读取字符文本文件的一行字符
                System.out.println(str);
            }
            bufferedReader.close();
        } else {
            System.out.println("还未上传文件，请上传文件");
        }

    }

    public String getFileListPath() {
        return fileListPath;
    }

    public void setFileListPath(String fileListPath) {
        this.fileListPath = fileListPath;
    }

}


