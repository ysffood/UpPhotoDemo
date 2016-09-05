package com.medemo.newland.upphotodemo.utils;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [字符串操作的工具类]<BR>
 * [功能详细描述]
 *
 * @author zhangrui
 * @version [ComProject, 2013-4-18]
 */
public abstract class StringUtil {

    private static final String TAG = "StringUtil";

    /**
     * 手机号码 正则验证
     */
    private static final String NUMBER_REGEX = "(\\+86|86|0086)?(13[0-9]|15[0-35-9]|14[57]|18[02356789])\\d{8}";

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述] 判断是否是电话号码
     *
     * @param resetInfo 输入的号码
     * @return true 是电话号码 false不是电话号码
     */
    public static boolean isPhoneNumber(String resetInfo) {
        return Pattern.matches(NUMBER_REGEX, resetInfo);
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述] 判断是否是电话号码
     *
     * @param resetInfo 输入的号码
     * @return true 是电话号码 false不是电话号码
     */
    public static boolean isPhoneNumberTwo(String resetInfo) {
        String num = String.valueOf(resetInfo.charAt(0));
        if (!num.equals("1"))
            return false;
        if (resetInfo.length() != 11)
            return false;
        return true;
    }

    /**
     * 判断是否为null或空值
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断str1和str2是否相同
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }

    /**
     * 判断str1和str2是否相同(不区分大小写)
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串str1是否包含字符串str2
     *
     * @param str1 源字符串
     * @param str2 指定字符串
     * @return true源字符串包含指定字符串，false源字符串不包含指定字符串
     */
    public static boolean contains(String str1, String str2) {
        return str1 != null && str1.contains(str2);
    }

    /**
     * 判断字符串是否为空，为空则返回一个空值，不为空则返回原字符串
     *
     * @param str 待判断字符串
     * @return 判断后的字符串
     */
    public static String getString(String str) {
        return str == null ? "" : str;
    }

    /**
     * 过滤HTML标签，取出文本内容
     *
     * @param inputString HTML字符串
     * @return 过滤了HTML标签的字符串
     */
    public static String filterHtmlTag(String inputString) {
        String htmlStr = inputString;
        String textStr = "";
        Pattern pScript;
        Matcher mScript;
        Pattern pStyle;
        Matcher mStyle;
        Pattern pHtml;
        Matcher mHtml;

        try {
            // 定义script的正则表达式
            String regExScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?script[\\s]*?>";
            // 定义style的正则表达式
            String regExStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?/[\\s]*?style[\\s]*?>";
            // 定义HTML标签的正则表达式
            String regExHtml = "<[^>\"]+>";

            pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
            mScript = pScript.matcher(htmlStr);
            // 过滤script标签
            htmlStr = mScript.replaceAll("");

            pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
            mStyle = pStyle.matcher(htmlStr);
            // 过滤style标签
            htmlStr = mStyle.replaceAll("");

            pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
            mHtml = pHtml.matcher(htmlStr);
            // 过滤html标签
            htmlStr = mHtml.replaceAll("");

            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;
    }

    /**
     * 将字符串数组转化为字符串，默认以","分隔
     *
     * @param values 字符串数组
     * @param split  分隔符，默认为","
     * @return 有字符串数组转化后的字符串
     */
    public static String arrayToString(String[] values, String split) {
        StringBuffer buffer = new StringBuffer();
        if (values != null) {
            if (split == null) {
                split = ",";
            }
            int len = values.length;
            for (int i = 0; i < len; i++) {
                buffer.append(values[i]);
                if (i != len - 1) {
                    buffer.append(split);
                }
            }
        }
        return buffer.toString();
    }

    /**
     * 将字符串list转化为字符串，默认以","分隔<BR>
     *
     * @param strList 字符串list
     * @param split   分隔符，默认为","
     * @return 组装后的字符串对象
     */
    public static String listToString(Collection<String> strList, String split) {
        String[] values = null;
        if (strList != null) {
            values = new String[strList.size()];
            strList.toArray(values);
        }
        return arrayToString(values, split);
    }

    /**
     * 验证字符串是否符合email格式
     *
     * @param email 需要验证的字符串
     * @return 验证其是否符合email格式，符合则返回true,不符合则返回false
     */
    public static boolean isEmail(String email) {

        // 通过正则表达式验证email是否合法
        return email != null
                && email.matches("(\\w[\\w\\.\\-]*)@\\w[\\w\\-]*[\\.(com|cn|org|edu|hk)]+[a-z]$");
    }

    /**
     * 验证字符串是否为数字
     *
     * @param str 需要验证的字符串
     * @return 不是数字返回false，是数字就返回true
     */
    public static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9]*");
    }

    /**
     * 验证字符串是否符合手机号格式 电话号码格式 +9715xxxxxxxx（备注：971为电话代码）
     *
     * @param str 需要验证的字符串
     * @return 不是手机号返回false，是手机号就返回true
     */
    public static boolean isMobile(String str) {
        //        return str != null && str.matches("\\+971\\-5[0-9]\\-[0-9]{3}\\-[0-9]{4}");
        return str != null
                && str.matches("(\\+86|86|0086)?(13[0-9]|15[0-35-9]|14[57]|18[02356789])\\d{8}");
    }

    /**
     * 验证字符串是否符合手机号格式 电话号码格式 +9715xxxxxxxx（备注：971为电话代码）
     */
    public static boolean isMobile2(String str) {
        return str != null && str.matches("\\+971\\-5[0-9]\\-[0-9]{3}\\-[0-9]{4}");
//        return str != null
//                && str.matches("(\\+86|86|0086)?(13[0-9]|15[0-35-9]|14[57]|18[02356789])\\d{8}");
    }

    /**
     * 替换字符串中特殊字符
     *
     * @param strData 源字符串
     * @return 替换了特殊字符后的字符串，如果strData为NULL，则返回空字符串
     */
    public static String encodeString(String strData) {
        if (strData == null) {
            return "";
        }
        return strData.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("'", "&apos;")
                .replaceAll("\"", "&quot;");
    }

    /**
     * 还原字符串中特殊字符
     *
     * @param strData strData
     * @return 还原后的字符串
     */
    public static String decodeString(String strData) {
        if (strData == null) {
            return "";
        }
        return strData.replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&apos;", "'")
                .replaceAll("&quot;", "\"")
                .replaceAll("&amp;", "&");
    }

    /**
     * 组装XML字符串<BR>
     * [功能详细描述]
     *
     * @param map 键值Map
     * @return XML字符串
     */
    public static String generateXml(Map<String, Object> map) {

        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<root>");
        if (map != null) {
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                String key = entry.getKey();
                xml.append("<");
                xml.append(key);
                xml.append(">");
                xml.append(entry.getValue());
                xml.append("</");
                xml.append(key);
                xml.append(">");
            }
        }
        xml.append("</root>");
        return xml.toString();
    }

    /**
     * 组装XML字符串<BR>
     * [功能详细描述]
     *
     * @param values key、value依次排列
     * @return XML字符串
     */
    public static String generateXml(String... values) {
        StringBuffer xml = new StringBuffer();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<root>");
        if (values != null) {
            int size = values.length >> 1;
            for (int i = 0; i < size; i++) {
                xml.append("<");
                xml.append(values[i << 1]);
                xml.append(">");
                xml.append(values[(i << 1) + 1]);
                xml.append("</");
                xml.append(values[i << 1]);
                xml.append(">");
            }
        }
        xml.append("</root>");
        return xml.toString();
    }

    /**
     * 将srcString按split拆分，并组装成List。默认以','拆分。<BR>
     *
     * @param srcString 源字符串
     * @param split     分隔符
     * @return 返回list
     */
    public static List<String> parseStringToList(String srcString, String split) {
        List<String> list = null;
        if (!StringUtil.isNullOrEmpty(srcString)) {
            if (split == null) {
                split = ",";
            }
            String[] strArr = srcString.split(split);
            if (strArr != null && strArr.length > 0) {
                list = new ArrayList<String>(strArr.length);
                for (String str : strArr) {
                    list.add(str);
                }
            }
        }
        return list;
    }

    /**
     * 去掉url中多余的斜杠
     *
     * @param url 字符串
     * @return 去掉多余斜杠的字符串
     */
    public static String fixUrl(String url) {
        if (null == url) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer(url);
        for (int i = stringBuffer.indexOf("//", stringBuffer.indexOf("//") + 2); i != -1; i = stringBuffer.indexOf("//",
                i + 1)) {
            stringBuffer.deleteCharAt(i);
        }
        return stringBuffer.toString();
    }

    /**
     * 按照一个汉字两个字节的方法计算字数
     *
     * @param string String
     * @return 返回字符串's count
     */
    public static int count2BytesChar(String string) {
        int count = 0;
        if (string != null) {
            for (char c : string.toCharArray()) {
                count++;
                if (isChinese(c)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 判断字符串中是否包含中文 <BR>
     * [功能详细描述] [added by 杨凡]
     *
     * @param str 检索的字符串
     * @return 是否包含中文
     */
    public static boolean hasChinese(String str) {
        boolean hasChinese = false;
        if (str != null) {
            for (char c : str.toCharArray()) {
                if (isChinese(c)) {
                    hasChinese = true;
                    break;
                }
            }
        }
        return hasChinese;
    }

    /**
     * 判断字符串中是否全为中文 <BR>
     * [功能详细描述] [added by rigor]
     *
     * @param str 检索的字符串
     * @return 是否全为中文
     */
    public static boolean allChinese(String str) {
        if (isNullOrEmpty(str)) {
            return false;
        }
        boolean allChinese = true;
        for (char c : str.toCharArray()) {
            if (!isChinese(c)) {
                allChinese = false;
                break;
            }
        }
        return allChinese;
    }

    /**
     * 截取字符串，一个汉字按两个字符来截取<BR>
     * [功能详细描述] [added by 杨凡]
     *
     * @param src        源字符串
     * @param charLength 字符长度
     * @return 截取后符合长度的字符串
     */
    public static String subString(String src, int charLength) {
        if (src != null) {
            int i = 0;
            for (char c : src.toCharArray()) {
                i++;
                charLength--;
                if (isChinese(c)) {
                    charLength--;
                }
                if (charLength <= 0) {
                    if (charLength < 0) {
                        i--;
                    }
                    break;
                }
            }
            return src.substring(0, i);
        }
        return src;
    }

    /**
     * 对字符串进行截取, 超过则以...结束
     *
     * @param originStr     原字符串
     * @param maxCharLength 最大字符数
     * @return 截取后的字符串
     */
    public static String trim(String originStr, int maxCharLength) {
        int count = 0;
        int index = 0;
        int originLen = originStr.length();
        for (index = 0; index < originLen; index++) {
            char c = originStr.charAt(index);
            int len = 1;
            if (isChinese(c)) {
                len++;
            }
            if (count + len <= maxCharLength) {
                count += len;
            } else {
                break;
            }
        }

        if (index < originLen) {
            return originStr.substring(0, index) + "...";
        } else {
            return originStr;
        }
    }

    /**
     * 判断参数c是否为中文<BR>
     * [功能详细描述] [added by 杨凡]
     *
     * @param c char
     * @return 是中文字符返回true，反之false
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;

    }

    /**
     * 检测密码强度
     *
     * @param password 密码
     * @return 密码强度（1：低 2：中 3：高）
     */
    public static int checkStrong(String password) {
        boolean num = false;
        boolean lowerCase = false;
        boolean upperCase = false;
        boolean other = false;

        int threeMode = 0;
        int fourMode = 0;

        for (int i = 0; i < password.length(); i++) {
            // 单个字符是数字
            if (password.codePointAt(i) >= 48 && password.codePointAt(i) <= 57) {
                num = true;
            }
            // 单个字符是小写字母
            else if (password.codePointAt(i) >= 97
                    && password.codePointAt(i) <= 122) {
                lowerCase = true;
            }
            // 单个字符是大写字母
            else if (password.codePointAt(i) >= 65
                    && password.codePointAt(i) <= 90) {
                upperCase = true;
            }
            // 特殊字符
            else {
                other = true;
            }
        }

        if (num) {
            threeMode++;
            fourMode++;
        }

        if (lowerCase) {
            threeMode++;
            fourMode++;
        }

        if (upperCase) {
            threeMode++;
            fourMode++;
        }

        if (other) {
            fourMode++;
        }

        // 数字、大写字母、小写字母只有一个，密码强度低
        if (threeMode == 1 && !other) {
            return 1;
        }
        // 四种格式有其中两个，密码强度中
        else if (fourMode == 2) {
            return 2;
        }
        // 四种格式有三个或者四个，密码强度高
        else if (fourMode >= 3) {
            return 3;
        }
        // 正常情况下不会出现该判断
        else {
            return 0;
        }
    }

    /**
     * 返回一个制定长度范围内的随机字符串
     *
     * @param min 范围下限
     * @param max 范围上限
     * @return 字符串
     */
    public static String createRandomString(int min, int max) {
        StringBuffer strB = new StringBuffer();
        Random random = new Random();
        int lenght = min;
        if (max > min) {
            lenght += random.nextInt(max - min + 1);
        }
        for (int i = 0; i < lenght; i++) {
            strB.append((char) (97 + random.nextInt(26)));
        }
        return strB.toString();
    }

    /**
     * [用于获取字符串中字符的个数]<BR>
     * [功能详细描述]
     *
     * @param content 文本内容
     * @return 返回字符的个数
     */
    public static int getStringLeng(String content) {
        return (int) Math.ceil(count2BytesChar(content) / 2.0);
    }

    /**
     * 根据参数tag（XML标签）解析该标签对应的值<BR>
     * 本方法针对简单的XML文件，仅通过字符串截取的方式获取标签值
     *
     * @param xml XML文件字符串
     * @param tag XML标签名，说明：标签名不需加“<>”，方法中已做处理
     * @return 标签对应的值
     */
    public static String getXmlValue(String xml, String tag) {
        if (xml == null || tag == null) {
//            Logger.e(TAG, "XML OR TAG is null!");
            return null;
        }

        // 如果标签中包含了"<"或">"，先去掉
        tag = tag.replace("<", "").replace(">", "");

        // 截取值
        int index = xml.indexOf(tag);
        if (index != -1) {
            return xml.substring(index + tag.length() + 1,
                    xml.indexOf('<', index));
        }

//        Logger.e(TAG, "No such tag : " + tag + " was found!");
        return null;
    }

    /**
     * 根据业务拼装电话号码<BR>
     *
     * @param number 电话号码
     * @return 拼装后的电话号码
     */
    public static String fixPortalPhoneNumber(String number) {
        if (StringUtil.isNullOrEmpty(number)) {
            return number;
        }
        String retPhoneNumber = number.replaceAll(" ", "");
        //这个方法只能去头尾的空格
//        String retPhoneNumber = number.trim();

        // 确定是否是手机号码，然后将前缀去除，只保留纯号码
        if (isMobile(retPhoneNumber)) {
            if (retPhoneNumber.startsWith("+86")) {
                retPhoneNumber = retPhoneNumber.substring(3);
            } else if (retPhoneNumber.startsWith("86")) {
                retPhoneNumber = retPhoneNumber.substring(2);
            } else if (retPhoneNumber.startsWith("0086")) {
                retPhoneNumber = retPhoneNumber.substring(4);
            }
        }

        return retPhoneNumber;
    }

    /**
     * 生成唯一的字符串对象<BR>
     *
     * @return 唯一的字符串
     */
    public static String generateUniqueID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * [一句话功能简述]<BR>
     * [功能详细描述]
     *
     * @param jid jid
     * @return String
     */
    public static String[] splitJidAndRealmName(String jid) {
        if (null != jid) {
            return jid.split("@");
        }
        return null;
    }

    /**
     * * 检测是否有emoji表情 *
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                // 如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * encodeBase64File:(将文件转成base64 字符串). <br/>
     *
     * @param file 文件
     * @return
     * @throws Exception
     * @author guhaizhou@126.com
     * @since JDK 1.6
     */
    public static String encodeBase64File(File file) throws Exception {
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * decoderBase64File:(将base64字符解码保存文件). <br/>
     *
     * @param base64Code 编码后的字串
     * @param savePath   文件保存路径
     * @throws Exception
     * @author guhaizhou@126.com
     * @since JDK 1.6
     */
    public static void decoderBase64File(String base64Code, String savePath) throws Exception {
//byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }
}
