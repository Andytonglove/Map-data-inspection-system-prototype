package util;

/**
 * 字符串判断工具类
 * 
 * @author guan
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

    // 判断上报字符串是否合法
    public static boolean isEffectiveReport(String string) {
        String info[] = string.split("&");
        for (int i = 0; i < info.length; i++) {
            if (info[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
