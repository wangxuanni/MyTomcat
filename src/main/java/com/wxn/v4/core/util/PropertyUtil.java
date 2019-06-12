package com.wxn.v4.core.util;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    private static Properties props;
    private static Logger logger = Logger.getLogger(PropertyUtil.class);
    static {
        loadProps();
    }

    private static void loadProps() {
        props = new Properties();
        InputStream in = null;
        try {
            in = PropertyUtil.class.getClassLoader().getResourceAsStream("server.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("server.properties�ļ�δ�ҵ�");
        } catch (IOException e) {
            logger.error("����IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("rpc.properties�ļ����رճ����쳣");
            }
        }
        logger.info("����properties�ļ��������...........");
        logger.info("properties�ļ����ݣ�" + props);
    }
    public static String getProperty(String key) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
}
