package com.ChongQi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class File1 {



        public static void main(String[] args) {
            List<String> paths = new ArrayList<>();
            DirectoryScanner("E://huyl//0bat", paths);
            System.out.println();
        }

        public static void DirectoryScanner(String dangQianMuLu, List<String> paths) {
            File currentDir = new File(dangQianMuLu);
            File[] files = currentDir.listFiles();

            for (File file : files) {
                if (file.isFile() && file.getName().equals("info.json")) {
                    paths.add(file.getPath());
                    break;
                }else if(file.isDirectory()){
                    DirectoryScanner(file.getPath(), paths);
                }
            }
        }

}
