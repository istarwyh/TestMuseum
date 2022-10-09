package istarwyh;

import java.util.List;

/**
 * @Description: OptionParser
 * @Author: wx:istarwyh
 * @Date: 2022-05-22 22:41
 * @Version: ing
 */
interface OptionParser<T> {
    T parse(List<String> arguments, Option option);
}
