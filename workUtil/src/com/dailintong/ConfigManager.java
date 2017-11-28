package com.dailintong;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;


/**
 *
 * <p>Title: 处理配置的工具类</p>
 *
 * <p>Description: 配置文件是以KEY、VALUE的形式组织（KEY=VALUE）的，本类
 * 的主要功能是提供属性控制</p>
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
     * 属性文件即：properties文件
     */
    private static final String PFILE = "/config.properties" ;
    /**
     * 控制类的实例
     */
    private static ConfigManager manager = null ;
    /**
     * 控制同步锁
     */
    private static Object managerLock = new Object();
    /**
     * 属性集合
     */
    private Properties properties = null;
    /**
     * 属性同步锁
     */
    private Object propertiesLock = new Object();
    /**
     * 属性资源
     */
    private String resourceURI;

    /**
     * 私有构造方法
     * @param resourceURI String
     */
    private ConfigManager(String resourceURI)
    {
        this.resourceURI = resourceURI;
    }

    /**
     * 在properties文件中，读取配置项，为了与原有的ConfigManager保持一致而保留
     * @param key 属性文件的key键
     * @param defaultValue 对应key键的值
     * @return 根据key的值取到value，如果不存在key则返回缺省直
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
     * 获取属性值
     * @param name String 属性
     * @return String  属性值
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
     * 设置属性值，如果元素不存在，则创建一个
     * @param name String 属性名
     * @param value String 属性值
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
     * 删除属性
     * @param name String 属性名
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
     * 获取属性集合
     * @return Enumeration 属性集合
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
     * 判断属性文件是否为只读
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
     * 判断属性文件是否为可写
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
     * 判断文件是否存在
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
     * 获取属性对象
     * @return Properties
     */
    private Properties getProps() {
        //如果还没有载入，载入属性
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
     * 获取属性对象
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
     * 获取一个属性值
     * @param name String 属性名
     * @return String 属性值
     */
    protected String getProp(String name) {
        //如果还没有载入，载入属性
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
     * 设置属性值，同时将属性写入磁盘文件
     * @param name String 属性值
     * @param value String 属性名
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
     * 删除属性
     * @param name String 属性名
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
     * 获取属性集合
     * @return Enumeration 属性集合
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
     * 从磁盘载入属性
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
            throw new RuntimeException("装入属性文件时出错： " + e.getMessage());
        }
        finally {
            try {
                in.close() ;
            }
            catch (Exception e) {
             
                throw new RuntimeException("关闭输入流时出错： " + e.getMessage());
            }
        }
    }

    /**
     * 将属性存入磁盘
     */
    private void saveProps() {
        //将属性保存到硬盘，需要在属性中设置“path”属性，指明保存的路径
        //获取路径属性
        String path = properties.getProperty("path").trim() ;
        OutputStream out = null ;
        try {
            out = new FileOutputStream(path) ;
            properties.store(out , "properties -- " + (new java.util.Date())) ;
        }
        catch (Exception ioe) {
          
            ioe.printStackTrace() ;
            throw new RuntimeException("保存属性文件： " +
                path + ". " +
                " 出错，请检查文件的路径及是否有写权限： " + ioe.getMessage());
        }
        finally {
            try {
                out.close() ;
            }
            catch (Exception e) {
               
                throw new RuntimeException("关闭输出流时出错： " + e.getMessage());
            }
        }
    }
    /**
     * 判断属性文件是否可读
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
     * 判断属性文件是否存在
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
     * 判断属性文件是否可写
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
     * 在properties文件中，读取配置项(为了兼容旧系统调用而保留)
     *@param key 属性文件的key键
     * @return 根据key的值取到的value
     * @exception NullException 空指针
     * @throws IllArgumentException 非法参数
     */
    final public String getConfigValue(String key) {
        return getProperty(key) ;
    }

    public static void main(String []args)
    {
   
    }
}
