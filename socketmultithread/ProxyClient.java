package socketmultithread;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class ProxyClient {

    public static void main(String[] args) {
        ProxyClient proxyClient = new ProxyClient();
        Client client = new Client();
        while (true) {
            System.out.print("请输入数字：\n" + "0: 上传文件\n" + "1: 下载文件\n" + "2: 列出服务器已有文件\n"
                    + "3: 列出本地已上传文件\n" + "4: 退出\n");
            String s = proxyClient.testReadLine();
            if ("0".equals(s) || "1".equals(s) || "2".equals(s) || "3".equals(s)) {
                switch (s) {
                    case "0":
                        proxyClient.upFiles(client);
                        break;
                    case "1":
                        proxyClient.downLoadFiles(client);
                        break;
                    case "2":
                        proxyClient.listServerFiles();
                        break;
                    case "3":
                        proxyClient.listLocalFileList(client);
                        break;
                }
            } else if ("4".equals(s)) {
                break;
            } else {
                System.out.println("输入错误！");
            }


        }
//        proxyClient.upFiles();

    }

    /**
     * @param client 服务端对象
     */
    public void upFiles(Client client) {
//        Client client = new Client();
        while (true) {

            System.out.println("请输入文件绝对路径,包含文件名，例如：D:/1.txt");
            String filePath = testReadLine();
            System.out.println("文件路径为：" + filePath);
            File file = new File(filePath);
            if (file.exists()) {
                if (!file.isDirectory()) {
                    System.out.println("文件：" + file.getName() + "  正在上传");
                    client.upFile(filePath);
                    System.out.println("文件：" + file.getName() + "  上传成功");
                    break;
                }

            } else {
                System.out.println("输入文件不存在,请重新输入");
            }
        }

    }

    public void listServerFiles() {
        Server server = new Server();
        try {
            server.serverFileList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param client 服务端对象
     */
    public void downLoadFiles(Client client) {
//        Client client = new Client();
        String filePath = null;
        String fileName = null;

        while (true) {
            System.out.println("请输入下载文件保存的路径：");
            filePath = testReadLine();
            File fileDir = new File(filePath);
            if (fileDir.exists()) {
                if (fileDir.isDirectory()) {
                    break;
                }
                System.out.println("输入错误，输入文件不存在或不是文件夹!");
            } else {
                System.out.println("输入错误，输入文件不存在或不是文件夹!");
            }
        }
        Server server = new Server();
        while (true) {
            System.out.println("请输入下载文件名称：");
            fileName = testReadLine();
            if (server.isFileExit(fileName)) {
                System.out.println("文件：" + fileName + "  正在下载到文件夹： " + filePath);
                client.downFile(filePath,fileName);
                System.out.println("文件：" + fileName + "  下载成功");
                break;

            } else {
                System.out.println("输入错误，输入文件不存在或不是文件夹!");
            }
        }

    }

    public String testReadLine() {
        Scanner scanner = new Scanner(System.in);

        String readLine = null;
        readLine = scanner.nextLine();
        return readLine;
    }

    /**
     * @param client 服务端对象
     */
    public void listLocalFileList(Client client) {
        try {
            client.listAllUpFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
