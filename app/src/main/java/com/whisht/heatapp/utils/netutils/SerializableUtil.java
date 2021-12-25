package com.whisht.heatapp.utils.netutils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableUtil {

    /**
     * 对象转数组
     *
     * @param obj
     * @return
     */
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * Btye[]转对实体
     *
     * @param objBytes
     * @return
     * @throws Exception
     */
    public static Object getObjectFromBytes(byte[] objBytes) throws Exception {
        if (objBytes == null || objBytes.length == 0) {
            return null;
        }
        ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);
        ObjectInputStream ois = new ObjectInputStream(bi);
        Object object = ois.readObject();
        bi.close();
        ois.close();
        if (object != null)
            return object;
        return null;
    }

    /**
     * Serializable 对象转byte[]
     *
     * @param serializable
     * @return
     * @throws Exception
     */
    public static byte[] getBytesFromObject(Serializable serializable) throws Exception {

        if (serializable == null) {
            return null;
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bo);
        oos.writeObject(serializable);
        byte[] datas = bo.toByteArray();
        bo.close();
        oos.close();
        if (datas != null)
            return datas;
        return null;
    }
}
