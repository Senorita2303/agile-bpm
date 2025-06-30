package com.dstz.base.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Name HanyuPinyinUtil
 */
public class HanYuPinyinUtil {

    protected static final Logger logger = LoggerFactory.getLogger(HanYuPinyinUtil.class);

    /**
     * Convert text to Chinese Pinyin
     *
     * @param ChineseLanguage To convert to Chinese pinyin
     */
    public String toHanYuPinyin(String ChineseLanguage) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// Output pinyin all lowercase
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// Without tone
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        try {
            for (int i = 0; i < cl_chars.length; i++) {
                if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")) {// If the character is Chinese, convert it to Chinese Pinyin
                    hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
                } else {// If the character is not Chinese, it will not be converted
                    hanyupinyin += cl_chars[i];
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            logger.warn("ChineseLanguage:{} The characters cannot be converted into Chinese Pinyin, {}", ChineseLanguage, e.getMessage());
        }
        return hanyupinyin;
    }

    public static String getFirstLettersUp(String ChineseLanguage) {
        return getFirstLetters(ChineseLanguage, HanyuPinyinCaseType.UPPERCASE);
    }

    public static String getFirstLettersLo(String ChineseLanguage) {
        return getFirstLetters(ChineseLanguage, HanyuPinyinCaseType.LOWERCASE);
    }

    public static String getFirstLetters(String ChineseLanguage, HanyuPinyinCaseType caseType) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(caseType);// Output pinyin in all uppercase letters
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// Without tone
        try {
            for (int i = 0; i < cl_chars.length; i++) {
                String str = String.valueOf(cl_chars[i]);
                if (str.matches("[\u4e00-\u9fa5]+")) {// If the character is Chinese, convert it to Chinese Pinyin and take the first letter
                    hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0].substring(0, 1);
                } else if (str.matches("[0-9]+")) {// If the character is a number, take the number
                    hanyupinyin += cl_chars[i];
                } else if (str.matches("[a-zA-Z]+")) {// If the character is a letter, take the letter
                    hanyupinyin += cl_chars[i];
                } else {// Otherwise do not convert
                    hanyupinyin += cl_chars[i];// If it is punctuation, it has
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            logger.warn("ChineseLanguage:{} The characters cannot be converted into Chinese Pinyin, {}", ChineseLanguage, e.getMessage());
        }
        return hanyupinyin;
    }

    public static String getPinyinString(String ChineseLanguage) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// Output pinyin in all uppercase letters
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// Without tone
        try {
            for (int i = 0; i < cl_chars.length; i++) {
                String str = String.valueOf(cl_chars[i]);
                if (str.matches("[\u4e00-\u9fa5]+")) {// If the character is Chinese, convert it to Chinese Pinyin and take the first letter
                    hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(
                            cl_chars[i], defaultFormat)[0];
                } else if (str.matches("[0-9]+")) {// If the character is a number, take the number
                    hanyupinyin += cl_chars[i];
                } else if (str.matches("[a-zA-Z]+")) {// If the character is a letter, take the letter

                    hanyupinyin += cl_chars[i];
                } else {// Otherwise do not convert
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            logger.warn("ChineseLanguage:{} The characters cannot be converted into Chinese Pinyin, {}", ChineseLanguage, e.getMessage());
        }
        return hanyupinyin;
    }

    /**
     * Get the first character of the first Chinese character
     *
     * @return String
     * @throws
     * @Title: getFirstLetter
     * @Description: TODO
     */
    public static String getFirstLetter(String ChineseLanguage) {
        char[] cl_chars = ChineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);// Output pinyin in all uppercase letters
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// Without tone
        try {
            String str = String.valueOf(cl_chars[0]);
            if (str.matches("[\u4e00-\u9fa5]+")) {// If the character is Chinese, convert it to Chinese Pinyin and take the first letter
                hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(
                        cl_chars[0], defaultFormat)[0].substring(0, 1);
                ;
            } else if (str.matches("[0-9]+")) {// If the character is a number, take the number
                hanyupinyin += cl_chars[0];
            } else if (str.matches("[a-zA-Z]+")) {// If the character is a letter, take the letter

                hanyupinyin += cl_chars[0];
            } else {// Otherwise do not convert

            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            logger.warn("ChineseLanguage:{} The characters cannot be converted into Chinese Pinyin, {}", ChineseLanguage, e.getMessage());
        }
        return hanyupinyin;
    }
}
