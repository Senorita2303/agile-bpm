import cn.hutool.core.util.StrUtil;
import com.dstz.base.enums.AbDbType;
import com.dstz.base.utils.AbDataSourceUtils;

/**
 * Extend the tool class for mybatis mapper.
 *
 */
public class Ognl {

    /**
     * Is the string empty?
     *
     * @param str
     * @return
     */
    public static boolean isStrEmplty(String str) {
        return StrUtil.isEmpty(str);
    }


    /**
     * Whether the current thread request is SQL Server
     *
     * @return
     */
    public static boolean isSqlServer() {
        return AbDbType.SQLSERVER.equals(AbDataSourceUtils.getCurrentDataSource().getDbType());
    }

    /**
     * Is the current thread request postgresql
     *
     * @return
     */
    public static boolean isPostgreSql() {
        return AbDbType.POSTGRESQL.equals(AbDataSourceUtils.getCurrentDataSource().getDbType());
    }
    
    public static String $aaaaaaa$(){
        return "001";
    }
}
