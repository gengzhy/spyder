package xin.cosmos.common.util;

import xin.cosmos.common.function.ICallFunction;
import xin.cosmos.common.function.ICallbackFunction;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;

/**
 * 工具类
 */
public class ObjectsUtil {
    final static SecureRandom RANDOM = new SecureRandom();

    /**
     * 不为空执行call
     *
     * @param obj
     * @param call
     * @param <T>
     */
    public static <T> void nonNullTodo(T obj, ICallFunction call) {
        if (isNull(obj)) {
            return;
        }
        call.call();
    }

    /**
     * 不为空执行call并返回结果
     *
     * @param obj
     * @param call
     * @param <T>
     */
    public static <T> T nonNullTodoWithBack(T obj, ICallbackFunction call) {
        if (isNull(obj)) {
            return null;
        }
        return call.call();
    }

    /**
     * 判断输入参数是否为空
     *
     * @param obj 输入参数
     * @return null is true
     */
    public static boolean isNull(Object obj) {
        if (obj == null) {
            return true;
        }
        boolean isNull;
        if (obj instanceof String) {
            isNull = StringUtils.isEmpty((String) obj);
        } else if (obj instanceof Map) {
            isNull = ((Map<?, ?>) obj).isEmpty();
        } else if (obj instanceof Collection) {
            isNull = ((Collection<?>) obj).isEmpty();
        } else {
            isNull = false;
        }
        return isNull;
    }

    /**
     * 数字随机数
     *
     * @param length 随机数长度
     * @return
     */
    public static String randomNumber(int length) {
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < length; i++) {
            retVal.append(RANDOM.nextInt(10));
        }
        return retVal.toString();
    }

    /**
     * 字母随机数
     *
     * @param length 随机数长度
     * @return
     */
    public static String randomLetter(int length) {
        char[] letters = "ABCDEFGHIJKLMNOPKRSTUVWXYZabcdefghijklmnopkrstuvwxyz".toCharArray();
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < length; i++) {
            retVal.append(letters[RANDOM.nextInt(letters.length)]);
        }
        return retVal.toString();
    }

    /**
     * 数字字母混合随机数
     *
     * @param length 随机数长度
     * @return
     */
    public static String randomMix(int length) {
        char[] numberMixLetters = "0123456789ABCDEFGHIJKLMNOPKRSTUVWXYZabcdefghijklmnopkrstuvwxyz".toCharArray();
        StringBuilder retVal = new StringBuilder();
        for (int i = 0; i < length; i++) {
            retVal.append(numberMixLetters[RANDOM.nextInt(numberMixLetters.length)]);
        }
        return retVal.toString();
    }

}
