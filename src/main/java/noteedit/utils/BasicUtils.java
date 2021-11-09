package noteedit.utils;

import java.util.ArrayList;
import java.util.List;

public class BasicUtils {
    public static List<Long> intToLongBatch(List<Integer> list) {
        List<Long> result = new ArrayList<>();
        for(Integer x: list) {
            result.add(x.longValue());
        }
        return result;
    }
}
