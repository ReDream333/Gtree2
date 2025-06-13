package ru.kpfu.itis.kononenko.gtree2.utils;


import java.util.HashMap;
import java.util.Map;

public final class ZodiacCompatibility {


    private enum Sign {
        ОВЕН    (Element.FIRE),
        ТЕЛЕЦ   (Element.EARTH),
        БЛИЗНЕЦЫ(Element.AIR),
        РАК     (Element.WATER),
        ЛЕВ     (Element.FIRE),
        ДЕВА    (Element.EARTH),
        ВЕСЫ    (Element.AIR),
        СКОРПИОН(Element.WATER),
        СТРЕЛЕЦ (Element.FIRE),
        КОЗЕРОГ (Element.EARTH),
        ВОДОЛЕЙ(Element.AIR),
        РЫБЫ    (Element.WATER);

        final Element element;
        Sign(Element element) {
            this.element = element;
        }
    }

    private enum Element {
        FIRE,
        EARTH,
        AIR,
        WATER
    }

    private static final int VERY_HIGH = 100;
    private static final int HIGH      =  75;
    private static final int MEDIUM    =  50;
    private static final int LOW       =  25;
    private static final int VERY_LOW  =   0;

    /**
     * Если вдруг нужно отдельно «подкрутить» пару,
     * можно добавить сюда sign1+":"+sign2 → свой процент.
     */
    private static final Map<String,Integer> OVERRIDES = new HashMap<>(0);
    static {
        // Пример «тонкой подстройки»:
//         OVERRIDES.put("ОВЕН:РИБЫ", 10);
//         OVERRIDES.put("РЫБЫ:ОВЕН", 10);
    }


    private static final Map<String, Sign> NAME_TO_SIGN = new HashMap<>(12);
    static {
        for (Sign s : Sign.values()) {
            NAME_TO_SIGN.put(s.name(), s);
        }
    }


    public static Integer computeCompatibility(String first, String second) {
        if (first == null || second == null) return null;
        Sign s1 = NAME_TO_SIGN.get(first.trim().toUpperCase());
        Sign s2 = NAME_TO_SIGN.get(second.trim().toUpperCase());
        if (s1 == null || s2 == null) return null;

        // Сначала проверяем, нет ли «тонкого» оверрайда
        String key = s1.name() + ":" + s2.name();
        if (OVERRIDES.containsKey(key)) {
            return OVERRIDES.get(key);
        }

        // Если знаки совпадают — полумера, 50%
        if (s1 == s2) {
            return MEDIUM;
        }

        Element e1 = s1.element;
        Element e2 = s2.element;

        // Разные, но одна стихия — супер-совместимость
        if (e1 == e2) {
            return VERY_HIGH;
        }

        // «Природные» пары: огонь ↔ воздух и земля ↔ вода — хорошо
        if ((e1 == Element.FIRE  && e2 == Element.AIR)   ||
                (e1 == Element.AIR   && e2 == Element.FIRE)  ||
                (e1 == Element.EARTH && e2 == Element.WATER) ||
                (e1 == Element.WATER && e2 == Element.EARTH)) {
            return HIGH;
        }

        // «Противоположные» по качествам: огонь ↔ вода и земля ↔ воздух — плохо
        if ((e1 == Element.FIRE  && e2 == Element.WATER) ||
                (e1 == Element.WATER && e2 == Element.FIRE)  ||
                (e1 == Element.EARTH && e2 == Element.AIR)   ||
                (e1 == Element.AIR   && e2 == Element.EARTH)) {
            return LOW;
        }

        // На всякий случай — средний уровень
        return VERY_LOW;
    }
}