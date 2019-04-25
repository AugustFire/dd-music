package com.nercl.music.util;

import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class StringUtil
{
    /**
     * 生成32位主键ID
     * 
     * @return 主键ID
     */
    public static String getPrimaryKey()
    {
        return getLowUUID();
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int length(String value)
    {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++)
        {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese))
            {
                /* 中文字符长度为2 */
                valueLength += 2;
            }
            else
            {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    // 判断字符串是否是数字组成
    public static boolean isNumeric(String str)
    {
        if (StringUtils.isBlank(str))
        {
            return false;
        }
        for (int i = 0; i < str.length(); i++)
        {
            if (!Character.isDigit(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    // 判断字符串是否包含数字
    public static boolean isContainNumber(String str)
    {
        if (StringUtils.isBlank(str))
        {
            return false;
        }
        for (int i = 0; i < str.length(); i++)
        {
            if (Character.isDigit(str.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }

    // 正则表达式校验用户姓名:可以包含中文、字母、数字、“_”、“·”、和空格
    public static boolean isNotContainSpecialCharacter(String str)
    {
        if (StringUtils.isBlank(str))
        {
            return true;
        }
        Pattern pattern = Pattern.compile("^[\u4E00-\u9FA5A-Za-z0-9_·\\s]+$");
        Matcher isSpChar = pattern.matcher(str);
        if (!isSpChar.matches())
        {
            return false;
        }
        return true;
    }

    /*
     * JSON字符串特殊字符处理，比如：“\A1;1300”
     * @param s
     * @return String
     */
    public final static String stringToJson(String s)
    {
        if (StringUtils.isBlank(s))
        {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            switch (c)
            {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getLowUUID()
    {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static String getUpUUID()
    {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public static String getString(Object obj)
    {
        if (obj != null)
        {
            return obj.toString();
        }
        else
        {
            return null;
        }
    }

    // !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~ 和空格
    public static String replaceSpecialCharacter(String charSting, String replacedChar)
    {
        charSting = charSting.replaceAll("[\\p{Punct}\\p{Blank}]", replacedChar);
        return charSting;
    }

    public static String timeStampStr()
    {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR) + "" + (now.get(Calendar.MONTH) + 1) + "" + now.get(Calendar.DAY_OF_MONTH) + "" + now.get(Calendar.HOUR_OF_DAY)
               + "" + now.get(Calendar.MINUTE) + "" + now.get(Calendar.SECOND);
    }

    public static String toLikeStr(String str)
    {
        if (str != null && str.length() > 0)
        {
            str = str.trim().replace("\\", "\\\\%").replace("%", "\\%").replace("_", "\\_");
        }
        return str;

    }

}
