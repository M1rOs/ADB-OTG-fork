package com.htetznaing.adbotg;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnsiParser {

    private static final Pattern pattern = Pattern.compile("\\u001B\\[(\\d+)m");

    public static SpannableStringBuilder parse(String input) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        Matcher matcher = pattern.matcher(input);
        int lastEnd = 0;
        int currentColor = Color.WHITE;

        while (matcher.find()) {
            String text = input.substring(lastEnd, matcher.start());
            SpannableString span = new SpannableString(text);
            span.setSpan(new ForegroundColorSpan(currentColor), 0, text.length(), 0);
            builder.append(span);

            int code = Integer.parseInt(matcher.group(1));
            currentColor = getColor(code);

            lastEnd = matcher.end();
        }

        String remaining = input.substring(lastEnd);
        SpannableString span = new SpannableString(remaining);
        span.setSpan(new ForegroundColorSpan(currentColor), 0, remaining.length(), 0);
        builder.append(span);

        return builder;
    }

    private static int getColor(int code) {
        switch (code) {
            case 30: return Color.BLACK;
            case 31: return Color.RED;
            case 32: return Color.GREEN;
            case 33: return Color.YELLOW;
            case 34: return Color.BLUE;
            case 35: return Color.MAGENTA;
            case 36: return Color.CYAN;
            case 37: return Color.WHITE;
            default: return Color.WHITE;
        }
    }
}