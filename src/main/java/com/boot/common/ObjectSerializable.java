package com.boot.common;

import java.io.*;

/**
 * Java对象序列化和反序列化的工具方法
 */
public class ObjectSerializable<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Java对象序列化
     */
    public void serialization(String filePath, T data) throws Exception {
        // 1. 检查该路径文件是否存在,不存在则创建
        File file = new File(filePath);
        createFileIfNotExists(file);

        // 2. 创建输出流,将数据写入文件
        ObjectOutputStream out = null;
        try {
            // 流创建
            out = new ObjectOutputStream(new FileOutputStream(file));

            // 序列化对象
            out.writeObject(data);
        } finally {
            // 关闭流
            if (null != out) {
                out.close();
            }
        }
    }

    // 如果文件不存在,生成一个新文件
    public void createFileIfNotExists(File file) throws IOException {
        // 如果文件不存在,进行创建
        if (!file.exists()) {
            // 如果父目录不存在，创建父目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            // 创建一个新的文件
            file.createNewFile();
        }
    }

    /**
     * Java对象反序列化
     */
    public T deserialization(String filePath) throws Exception {
        // 如果文件不存在,抛个异常
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }

        // 创建输入流,从物件中读取数据
        ObjectInputStream in = null;
        Object object = null;
        try {
            // 流创建
            in = new ObjectInputStream(new FileInputStream(file));

            // 对象反序列化
            object = in.readObject();
        } finally {
            // 关闭流
            if (null != in) {
                in.close();
            }
        }

        // 返回反序列化后的对象
        return (T)object;
    }

}