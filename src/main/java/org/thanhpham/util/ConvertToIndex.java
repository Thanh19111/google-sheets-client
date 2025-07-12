package org.thanhpham.util;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConvertToIndex {
    public static final Map<Character, Integer> map =
            IntStream.rangeClosed('A', 'Z')
                    .boxed()
                    .collect(Collectors.toMap(
                            i -> (char) i.intValue(),
                            i -> i - 'A'
                    ));

    public static final Map<Integer, Character> reverseMap =
            IntStream.range(0, 26)
                    .boxed()
                    .collect(Collectors.toMap(
                            i -> i,
                            i -> (char) ('A' + i)
                    ));

    public static Character getCharacter(Integer index){
        return reverseMap.get(index);
    }

    public static Integer getIndex(Character character){
        return map.get(character);
    }
}
