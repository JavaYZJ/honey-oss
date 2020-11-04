package com.eboy.honey.oss.api.dto;

import java.io.*;

/**
 * @author yangzhijie
 * @date 2020/8/13 15:34
 */
public class HoneyStream implements Serializable {

    private static final long serialVersionUID = 15435516377587663L;

    private final static int LENGTH = 1024;

    private transient InputStream inputStream;

    public HoneyStream(InputStream is) {
        this.inputStream = is;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    private void writeObject(ObjectOutputStream oos) throws Exception {
        oos.defaultWriteObject();

        byte[] buff = new byte[LENGTH];
        int tmp;
        while ((tmp = inputStream.read(buff, 0, LENGTH)) != -1) {
            oos.write(buff, 0, tmp);
        }
    }

    private void readObject(ObjectInputStream ois) throws Exception {
        ois.defaultReadObject();

        byte[] buf = new byte[LENGTH];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int tmp;
        while ((tmp = ois.read(buf, 0, LENGTH)) != -1) {
            bos.write(buf, 0, tmp);
        }

        inputStream = new ByteArrayInputStream(bos.toByteArray());
    }
}
