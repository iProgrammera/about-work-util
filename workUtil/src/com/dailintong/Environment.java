package com.dailintong ;

/**
 *
 * <p>Title: 系统环境变量映射类</p>
 *
 * <p>Description: 将配置文件key映射成为系统常量</p>
 *
 * <p>Copyright: Copyright (c) 2004</p>
 *
 * <p>Company: </p>
 *
 * @author cjp
 * @version 1.0
 */
public final class Environment {
   

    public static final String USER = "connection.username";

    public static final String PSW = "connection.password";
   
    public static final String DB_DRIVER = "db.driver";

    public static final String DB_URL = "db.url";

    public static final String DB_USER = "db.username";

    public static final String DB_PSW = "db.password";

    public static final String DB_INITIAL_SIZE = "db.initial_size";
    public static final String DB_MAX_SIZE = "db.max_size";
    public static final String DB_MIN_SIZE = "db.min_size";
    public static final String DB_MAX_IDLE_TIME = "db.max_idle_time";
    public static final String DB_MAX_STMT = "db.max_stmt";
    
    
    public static final String ALLCOL = "allcol";
    public static final String EXCELSTARTROW = "excelstartrow";
    public static final String SQLTEMPLET = "sqltemplet";
    public static final String DELSQLTEMPLET = "delsqltemplet";

   
}
