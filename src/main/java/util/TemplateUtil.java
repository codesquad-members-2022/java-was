package util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateUtil {

    private static final String TEMPLATE_TAG_OPEN_REGEX = "\\{\\{";
    private static final String TEMPLATE_TAG_CLOSE_REGEX = "}}";
    private static final String TEMPLATE_TAG_BRACES_REGEX = TEMPLATE_TAG_OPEN_REGEX + "|" + TEMPLATE_TAG_CLOSE_REGEX;
    private static final Pattern TEMPLATE_TAG = Pattern.compile(TEMPLATE_TAG_OPEN_REGEX + ".+?" + TEMPLATE_TAG_CLOSE_REGEX);
    private static final String TEMPLATE_SECTION_START_PREFIX = "#";
    private static final String TEMPLATE_SECTION_END_PREFIX = "/";

    public static String render(String template, Map<String, Object> model) {
        StringBuilder view = new StringBuilder();
        Matcher matcher = TEMPLATE_TAG.matcher(template);

        while (matcher.find()) {
            view.append(template, matcher.regionStart(), matcher.start());
            String tagName = getTagName(matcher);

            matcher.region(matcher.end(), matcher.regionEnd());

            if (!isSection(tagName)) {
                view.append(model.get(tagName).toString());
                continue;
            }

            String key = tagName.replaceFirst(TEMPLATE_SECTION_START_PREFIX, "");

            // 섹션을 닫는 태그 위치를 찾는다.
            matcher.usePattern(Pattern.compile(TEMPLATE_TAG_OPEN_REGEX +
                    TEMPLATE_SECTION_END_PREFIX + key + TEMPLATE_TAG_CLOSE_REGEX));

            if (!matcher.find()) {
                matcher.usePattern(TEMPLATE_TAG);
                continue;
            }

            String section = template.substring(matcher.regionStart(), matcher.start());

            List<Object> objects = (List<Object>) model.get(key);

            view.append(renderSection(section, objects));

            matcher.region(matcher.end(), matcher.regionEnd());
            matcher.usePattern(TEMPLATE_TAG);
        }

        view.append(template, matcher.regionStart(), matcher.regionEnd());

        return view.toString();

    }

    private static StringBuilder renderSection(String section, List<Object> objects) {
        StringBuilder sectionView = new StringBuilder();
        Matcher matcher = TEMPLATE_TAG.matcher(section);

        for (Object object : objects) {
            while (matcher.find()) {
                sectionView.append(section, matcher.regionStart(), matcher.start());
                String tagName = getTagName(matcher);

                matcher.region(matcher.end(), matcher.regionEnd());

                try {
                    Field field = object.getClass().getDeclaredField(tagName);
                    field.setAccessible(true);
                    sectionView.append(field.get(object));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            sectionView.append(section, matcher.regionStart(), matcher.regionEnd());
            matcher.reset();
        }

        return sectionView;
    }

    private static String getTagName(Matcher matcher) {
        return matcher.group()
                .replaceAll(TEMPLATE_TAG_BRACES_REGEX, "")
                .trim();
    }

    private static boolean isSection(String tag) {
        return tag.startsWith(TEMPLATE_SECTION_START_PREFIX);
    }

}
