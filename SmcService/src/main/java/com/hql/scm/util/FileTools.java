package com.hql.scm.util;

import java.io.*;
import java.util.Arrays;

public class FileTools {
    public static File getFileMakePath(String parent, String name) {
        File file = new File(parent);
        file.mkdirs();
        return new File(file, name);
    }

    public static File makeImagePath(String parent, int id) {
        File file = new File(parent, String.valueOf(id));
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return new File(file, "0");
        } else {
            try {
                Arrays.sort(files, (o1, o2) -> {
                    try {
                        int i1 = Integer.parseInt(o1.getName());
                        int i2 = Integer.parseInt(o2.getName());
                        return i1 - i2;
                    } catch (Exception e) {
                        return o1.compareTo(o2);
                    }
                });
                File last = files[files.length - 1];
                int lastId = Integer.parseInt(last.getName());
                return new File(file, String.valueOf(lastId + 1));
            } catch (Exception e) {
                e.printStackTrace();
                return makeImagePath(parent, id);
            }
        }
    }

    public static File getVideoFilePath(String parent, int id) {
        File path = new File(parent, String.valueOf(id));
        path.mkdirs();
        return new File(path, "video");
    }

    public static File getHeadImagePath(String parent, Integer id) {
        File file = new File(parent);
        file.mkdirs();
        return new File(parent, String.valueOf(id));
    }

    public static File getImagePath(String parent, int id) {
        return new File(parent, String.valueOf(id));
    }

    public static File getImagePath(String parent, int id, int index) {
        return new File(getImagePath(parent, id), String.valueOf(index));
    }

    public static void write(InputStream in, File outPath) throws IOException {
        OutputStream out = new FileOutputStream(outPath);
        byte[] bytes = new byte[1024];
        int len;
        while ((len = in.read(bytes)) != -1) {
            out.write(bytes, 0, len);
        }
        out.flush();
        in.close();
        out.close();
    }

    public static void deleteDir(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                File[] files = file.listFiles();
                for (File path : files) {
                    deleteDir(path);
                }
            }
        }
    }
}
