package vip.creatio.common.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public final class FileUtil {

    private static void getFiles0(File sourceFile, List<File> sourceList, FileFilter filter, boolean includeDir, int depth, int maxDepth) {
        // Not null check
        if (sourceFile.exists() && sourceList != null) {
            // If sourceFile is a directory
            if (sourceFile.isDirectory() && depth < maxDepth) {

                if (includeDir) sourceList.add(sourceFile);
                // If File is directory
                File[] files = sourceFile.listFiles(file -> file.isDirectory() || filter.accept(file));
                if (files == null) return;
                for (File childFile : files) {
                    getFiles0(childFile, sourceList, filter, includeDir, depth + 1, maxDepth);
                }
                // If sourceFile is a realFile
            } else if (filter.accept(sourceFile)) sourceList.add(sourceFile);
        }
    }

    public static File[] listFilesRecursively(File source, FileFilter filter, boolean includeDir, int maxDepth) {
        List<File> list = new ArrayList<>();
        getFiles0(source, list, filter, includeDir, 0, maxDepth);
        return list.toArray(new File[0]);
    }

    public static File[] listFilesRecursively(File source, FileFilter filter, boolean includeDir) {
        return listFilesRecursively(source, filter, includeDir, 32);
    }

    public static File[] listFilesRecursively(File source, FileFilter filter) {
        return listFilesRecursively(source, filter, true, 32);
    }

    public static File[] listFilesRecursively(File source) {
        return listFilesRecursively(source, f -> true);
    }

    public static String toClassName(File root, File f) {
        return toClassName(root.getAbsolutePath(), f.getAbsolutePath());
    }

    public static String toClassName(String root, String f) {
        if (!f.endsWith(".class")) throw new RuntimeException("File " + f + " is not a class");
        return f.substring(root.length() + 1, f.length() - 6).replace('/', '.');
    }

}
