package ex2;


import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

public class Fileswork {

    public static void main(String[] args) {

        Fileswork fw = new Fileswork();
//        fw.moveFile("Java培训习题.doc", "123.doc");
//        fw.listFiles("D:\\ucas_onedrive\\OneDrive - mails.ucas.ac.cn\\tp-training\\tp-learning\\java作业\\培新1"
//        ,"doc","Java");
        fw.listFiles("D:\\ucas_onedrive\\OneDrive - mails.ucas.ac.cn\\tp-training\\tp-learning\\java作业\\培新1");



    }

    public void moveFile(String fromFile, String toFile) {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            File file = new File(fromFile);
            File file1 = new File(toFile);
            fos = new FileOutputStream(file1);
            fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = fis.read(bytes)) != -1) {
                //2、把socket输入流写到文件输出流中去
                fos.write(bytes, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void listFiles(String filePath, String suffix, String fileName) {
        File file = new File(filePath);
        int index = suffix.length();
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (!(files == null)) {

                    for (File value : files) {
                        String name = value.getName();
                        String[] split = name.split("\\.");

//                        System.out.println(name);
//                        System.out.println(name.lastIndexOf(suffix));
//                        System.out.println(index);
//                        System.out.println(name.length());
                        if (name.contains(fileName) && (name.lastIndexOf(suffix) + index == name.length())) {
                            System.out.println(name);
                        }
                    }
                }
            }
        }else {
            String name = file.getName();
            String[] split = name.split("\\.");
            if (name.contains(fileName) && (name.lastIndexOf(suffix) + index == name.length())) {
                System.out.println(name);
            }
        }

    }
//    public void listFiles(String filePath, String suffix, String fileName) {
//        File file = new File(filePath);
//        int index = suffix.length();
//        if (file.exists()) {
//            if (file.isDirectory()) {
//                File[] files = file.listFiles();
//                if (!(files == null)) {
//
//                    for (File value : files) {
//                        String name = value.getName();
//                        String[] split = name.split("\\.");
//
////                        System.out.println(name);
////                        System.out.println(name.lastIndexOf(suffix));
////                        System.out.println(index);
////                        System.out.println(name.length());
//                        if (name.contains(fileName) && (name.lastIndexOf(suffix) + index == name.length())) {
//                            System.out.println(name);
//                        }
//                    }
//                }
//            }
//        }else {
//            String name = file.getName();
//            String[] split = name.split("\\.");
//            if (name.contains(fileName) && (name.lastIndexOf(suffix) + index == name.length())) {
//                System.out.println(name);
//            }
//        }
//
//    }
    public void listFiles(String filePath) {
        File file = new File(filePath);

        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (!(files == null)) {

                    for (File value : files) {
                        String name = value.getName();
                        System.out.println(name);
                        }
                    }
                }
            }
        }



}
