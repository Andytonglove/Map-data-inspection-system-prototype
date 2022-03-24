package util;

/**
 * 字符串工具类
 * 
 * @author Amos0602
 *
 */
public class StringUtil {
    public static boolean isEmpty(String string) {
        if (string == null || "".equals(string.trim())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotEmpty(String string) {
        if (string != null && "".equals(string.trim())) {
            return true;
        } else {
            return false;
        }
    }
}
