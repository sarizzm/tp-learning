package socket;

import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author zhouzhm
 * @description
 * @create 2021-07-31 15:18
 * @project_name training
 */
public class ClientTest {
    @Test // 启动服务器接收客户端发送的文件，需手动终止运行
    public void testServerUpFile() {
        Server server = new Server();
        server.upFile();
    }

    @Test // 启动服务器处理客户端下载请求，需手动终止运行
    public void testServerDownload(){
        Server server = new Server();
        server.downFile();
    }

    @Test  // 测试客户端上传文件
    public void testClientUpFiles(){
        Client client = new Client();
        Server server = new Server();
        //407B
        client.upFile("E:\\client_files\\SuperClass.class");
        //504KB
        client.upFile("E:\\client_files\\Java第一次作业.pdf");
        // 1.02G
        client.upFile("E:\\client_files\\Office_Professional_Plus_2016_64Bit_English_MLF_X20-42432.iso");
        //5.42M
        client.upFile("E:\\client_files\\21年应届生电脑设置指引.pptx");
//
    }
    @Test // 列出客户端已经上传的文件列表
    public void testClientUpFilesList(){
        Client client = new Client();
        try {
            client.listAllUpFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test // 测试 输出 服务器端现有的文件列表
    public void testServerFilesList(){
        Server server = new Server();
        try {
            server.serverFileList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test // 测试 从服务器端 下载文件到客户端不同目录
    public void testClientDownloadFiles(){
        Client client = new Client();
        Server server = new Server();
        client.downFile("E:\\","SuperClass.class");
        client.downFile("F:\\","Office_Professional_Plus_2016_64Bit_English_MLF_X20-42432.iso");
        client.downFile("D:\\","21年应届生电脑设置指引.pptx");
    }
}
