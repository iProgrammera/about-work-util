package com.dailintong;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;


/**
 *
 * <p>Title: �������õĹ�����</p>
 *
 * <p>Description: �����ļ�����KEY��VALUE����ʽ��֯��KEY=VALUE���ģ�����
 * ����Ҫ�������ṩ���Կ���</p>
 *
 * <p>Copyright: Copyright (c) 2003</p>
 *
 * <p>Company: </p>
 *
 * @author cjp
 * @version 1.0
 */
public class ConfigManager {
    /**
     * �����ļ�����properties�ļ�
     */
    private static final String PFILE = "/config.properties" ;
    /**
     * �������ʵ��
     */
    private static ConfigManager manager = null ;
    /**
     * ����ͬ����
     */
    private static Object managerLock = new Object();
    /**
     * ���Լ���
     */
    private Properties properties = null;
    /**
     * ����ͬ����
     */
    private Object propertiesLock = new Object();
    /**
     * ������Դ
     */
    private String resourceURI;

    /**
     * ˽�й��췽��
     * @param resourceURI String
     */
    private ConfigManager(String resourceURI)
    {
        this.resourceURI = resourceURI;
    }

    /**
     * ��properties�ļ��У���ȡ�����Ϊ����ԭ�е�ConfigManager����һ�¶�����
     * @param key �����ļ���key��
     * @param defaultValue ��Ӧkey����ֵ
     * @return ����key��ֵȡ��value�����������key�򷵻�ȱʡֱ
     */
    final public String getConfigValue(String key , String defaultValue) {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new ConfigManager(PFILE) ;
                }
            }
        }
        String value = manager.getProp(key) ;
        if (value == null) {
            return defaultValue ;
        }
        else {
            return value ;
        }
    }
    /**
     * ��ȡ����ֵ
     * @param name String ����
     * @return String  ����ֵ
     */
    public static String getProperty(String name) {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new ConfigManager(PFILE) ;
                }
            }
        }
        return manager.getProp(name) ;
    }
    /**
     * ��������ֵ�����Ԫ�ز����ڣ��򴴽�һ��
     * @param name String ������
     * @param value String ����ֵ
     */
    public static void setProperty(String name, String value) {
      if (manager == null) {
        synchronized (managerLock) {
          if (manager == null) {
            manager = new ConfigManager(PFILE);
          }
        }
      }
      manager.setProp(name, value);
    }
    /**
     * ɾ������
     * @param name String ������
     */
    public static void deleteProperty(String name) {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new ConfigManager(PFILE) ;
                }
            }
        }
        manager.deleteProp(name) ;
    }
    /**
     * ��ȡ���Լ���
     * @return Enumeration ���Լ���
     */
    @SuppressWarnings("rawtypes")
	public static Enumeration propertyNames() {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new ConfigManager(PFILE) ;
                }
            }
        }
        return manager.propNames() ;
    }
    /**
     * �ж������ļ��Ƿ�Ϊֻ��
     * @return boolean
     */
    public static boolean propertyFileIsReadable() {
      if (manager == null) {
        synchronized (managerLock) {
          if (manager == null) {
            manager = new ConfigManager(PFILE) ;
          }
        }
      }
      return manager.propFileIsReadable();
    }
    /**
     * �ж������ļ��Ƿ�Ϊ��д
     * @return boolean
     */
    public static boolean propertyFileIsWritable() {
      if (manager == null) {
        synchronized (managerLock) {
          if (manager == null) {
            manager = new ConfigManager(PFILE) ;
          }
        }
      }
      return manager.propFileIsWritable();
    }
    /**
     * �ж��ļ��Ƿ����
     * @return boolean
     */
    public static boolean propertyFileExists() {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new ConfigManager(PFILE) ;
                }
            }
        }
        return manager.propFileExists() ;
    }
    /**
     * ��ȡ���Զ���
     * @return Properties
     */
    private Properties getProps() {
        //�����û�����룬��������
        if (properties == null) {
            synchronized (propertiesLock) {
                if (properties == null) {
                    loadProps() ;
                }
            }
        }
        return properties ;
    }
    /**
     * ��ȡ���Զ���
     * @return Properties
     */
    public static Properties getProperties() {
        if (manager == null) {
            synchronized (managerLock) {
                if (manager == null) {
                    manager = new ConfigManager(PFILE) ;
                }
            }
        }
        return manager.getProps() ;
    }
    /**
     * ��ȡһ������ֵ
     * @param name String ������
     * @return String ����ֵ
     */
    protected String getProp(String name) {
        //�����û�����룬��������
        if (properties == null) {
            synchronized (propertiesLock) {
                if (properties == null) {
                    loadProps() ;
                }
            }
        }
        String property = properties.getProperty(name) ;
        if (property == null) {
            return null ;
        }
        else {
            return property.trim() ;
        }
    }
    /**
     * ��������ֵ��ͬʱ������д������ļ�
     * @param name String ����ֵ
     * @param value String ������
     */
    protected void setProp(String name , String value) {
        synchronized (propertiesLock) {
            if (properties == null) {
                loadProps() ;
            }
            properties.setProperty(name , value) ;
            saveProps() ;
        }
    }
    /**
     * ɾ������
     * @param name String ������
     */
    protected void deleteProp(String name) {
        synchronized (propertiesLock) {
            if (properties == null) {
                loadProps() ;
            }
            properties.remove(name) ;
            saveProps() ;
        }
    }
    /**
     * ��ȡ���Լ���
     * @return Enumeration ���Լ���
     */
    protected Enumeration<?> propNames() {
        if (properties == null) {
            synchronized (propertiesLock) {
                if (properties == null) {
                    loadProps() ;
                }
            }
        }
        return properties.propertyNames() ;
    }
    /**
     * �Ӵ�����������
     */
    private void loadProps() {
        properties = new Properties() ;
        InputStream in = null ;
        try {
            in = getClass().getResourceAsStream(resourceURI) ;
            properties.load(in) ;
        }
        catch (Exception e) {
           
            e.printStackTrace() ;
            throw new RuntimeException("װ�������ļ�ʱ���� " + e.getMessage());
        }
        finally {
            try {
                in.close() ;
            }
            catch (Exception e) {
             
                throw new RuntimeException("�ر�������ʱ���� " + e.getMessage());
            }
        }
    }

    /**
     * �����Դ������
     */
    private void saveProps() {
        //�����Ա��浽Ӳ�̣���Ҫ�����������á�path�����ԣ�ָ�������·��
        //��ȡ·������
        String path = properties.getProperty("path").trim() ;
        OutputStream out = null ;
        try {
            out = new FileOutputStream(path) ;
            properties.store(out , "properties -- " + (new java.util.Date())) ;
        }
        catch (Exception ioe) {
          
            ioe.printStackTrace() ;
            throw new RuntimeException("���������ļ��� " +
                path + ". " +
                " ���������ļ���·�����Ƿ���дȨ�ޣ� " + ioe.getMessage());
        }
        finally {
            try {
                out.close() ;
            }
            catch (Exception e) {
               
                throw new RuntimeException("�ر������ʱ���� " + e.getMessage());
            }
        }
    }
    /**
     * �ж������ļ��Ƿ�ɶ�
     * @return boolean
     */
    public boolean propFileIsReadable() {
        try {
            InputStream in = getClass().getResourceAsStream(resourceURI) ;
            in.close();
            return true ;
        }
        catch (Exception e) {
            return false ;
        }
    }
    /**
     * �ж������ļ��Ƿ����
     * @return boolean
     */
    public boolean propFileExists() {
        String path = getProp("path") ;
        if (path == null) {
            return false ;
        }
        File file = new File(path) ;
        if (file.isFile()) {
            return true ;
        }
        else {
            return false ;
        }
    }
    /**
     * �ж������ļ��Ƿ��д
     * @return boolean
     */
    public boolean propFileIsWritable() {
        String path = getProp("path") ;
        File file = new File(path) ;
        if (file.isFile()) {
            if (file.canWrite()) {
                return true ;
            }
            else {
                return false ;
            }
        }
        else {
            return false ;
        }
    }

    /**
     * ��properties�ļ��У���ȡ������(Ϊ�˼��ݾ�ϵͳ���ö�����)
     *@param key �����ļ���key��
     * @return ����key��ֵȡ����value
     * @exception NullException ��ָ��
     * @throws IllArgumentException �Ƿ�����
     */
    final public String getConfigValue(String key) {
        return getProperty(key) ;
    }

    public static void main(String []args)
    {
   
    }
}
