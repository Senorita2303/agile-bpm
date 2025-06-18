package com.dstz.base.enums;

import java.util.Arrays;

import org.springframework.util.StringUtils;

/**
 * Database Type
 *
 */
public enum AbDbType {

    /**
     * MYSQL
     */
    MYSQL("MySQL", "mysql", "MySQL database","com.mysql.jdbc.Driver", "jdbc:mysql://host:3306/database_name?useUnicode=true&characterEncoding=utf-8"),
    /**
     * MARIADB
     */
    MARIADB("Mariadb", "mariadb", "Mariadb database","org.mariadb.jdbc.Driver", "jdbc:mariadb://host:3306/database_name?useUnicode=true&characterEncoding=utf-8"),

    /**
     * ORACLE
     */
    ORACLE("Oracle", "oracle", "Oracle Database","oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@host:1521:database_instance"),

    /**
     * SQLSERVER
     */
    SQLSERVER("Microsoft SQL Server", "sqlserver", "SQL Server database","com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://host:1433;databaseName=database_name"),

    /**
     * POSTGRE
     */
    POSTGRESQL("PostgreSQL", "postgresql", "Postgre database","org.postgresql.Driver", "jdbc:postgresql://host:5432/postgres"),

    /**
     * Kingbase
     */
    KINGBASE("kingbasees", "kingbasees", "Renmin University of China Jincang Database","com.kingbase8.Driver", "jdbc:kingbase://host:54321/bpm"),

    /**
     * DAMO Database
     */
    DM("DM DBMS", "DM", "DAMO Database","dm.jdbc.driver.DmDriver","jdbc:dm://host:5236/bpm"),

    /**
     * H2 database
     */
    H2("H2", "H2", "H2 Database", "org.h2.Driver", "jdbc:h2://path"),

    /**
     * Unknown database
     */
    UNKNOW(null, "UNKNOW", "Unknown database",null,null);


    /**
     * Database vendor name
     */
    private final String productName;

    /**
     * Database Name
     */
    private final String db;

    /**
     * Database Description
     */
    private final String desc;

    /**
     * Drive type
     */
    private final String driverClassName;
    /**
     * Default URL
     */
    private final String defaultUrl;

    AbDbType(String productName, String db, String desc, String driverClassName, String defaultUrl) {
        this.productName = productName;
        this.db = db;
        this.desc = desc;
        this.driverClassName = driverClassName;
        this.defaultUrl = defaultUrl;
    }

    public String getProductName() {
        return productName;
    }

    public String getDb() {
        return db;
    }

    public String getDesc() {
        return desc;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    /**
     * Get the database type
     *
     * @param dbType String database type
     * @return Database Type
     */
    public static AbDbType getDbType(String dbType) {
        return Arrays.stream(values()).filter(item -> item.getDb().equalsIgnoreCase(dbType)).findFirst().orElse(AbDbType.UNKNOW);

    }

    boolean matchProductName(String productName) {
        return this.productName != null && this.productName.equalsIgnoreCase(productName);
    }

    public boolean equalsAny(AbDbType... any) {
        for (AbDbType target : any) {
            if (equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Database vendor name Get database type
     * @param productName product name
     * @return the database driver or {@link #UNKNOW} if not found
     */
    public static AbDbType fromProductName(String productName) {
        if (StringUtils.hasLength(productName)) {
            for (AbDbType candidate : values()) {
                if (candidate.matchProductName(productName)) {
                    return candidate;
                }
            }
        }
        return UNKNOW;
    }
}
