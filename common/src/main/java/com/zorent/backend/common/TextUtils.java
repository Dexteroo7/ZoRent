package com.zorent.backend.common;

/**
 * Created by dexter on 26/10/15.
 */
public enum TextUtils {;

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param sequence the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence sequence) {
        return sequence == null || sequence.length() == 0;
    }

    //I don't like putting '!' everywhere
    public static boolean isNotEmpty(CharSequence sequence) {
        return !isEmpty(sequence);
    }

    public static String replace(final String text,
                                 final String searchString,
                                 final String replacement,
                                 int max,
                                 final StringBuilder stringBuilder) {

        stringBuilder.setLength(0);
        if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }

        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end < 0) {
            return text;
        }
        final int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;

        stringBuilder.ensureCapacity(text.length() + increase);
        while (end > 0) {
            stringBuilder.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        stringBuilder.append(text.substring(start));
        return stringBuilder.toString();
    }
}