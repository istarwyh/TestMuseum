package istarwyh.page_module_loader.component;

import istarwyh.page_module_loader.ElementDTO;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * @author xiaohui
 */
@NoArgsConstructor
public class GraphLevel<T extends ElementDTO> extends AbstractElement<List<T>> {

    /**
     * 演示组件内的值会随着子元素的不同而动态变化,这里为子元素之和
     * @return amount
     */
    @Override
    public String getAmount() {
        String amount = super.getAmount();
        if(amount != null || super.dataEmpty()){
            return amount;
        }
        return super.getData().stream()
                .map(ElementDTO::getAmount)
                .filter(Objects::nonNull)
                .map(BigDecimal::new)
                .toList()
                .stream()
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_EVEN)
                .toString();
    }

    /**
     * 演示组件内的值会随着子元素的不同而动态变化,这里为子元素之和
     * @return number
     */
    @Override
    public String getNumber() {
        String number = super.getNumber();
        if(number != null || super.dataEmpty()){
            return number;
        }
        return super.getData().stream()
                .map(ElementDTO::getNumber)
                .filter(Objects::nonNull)
                .map(Long::valueOf)
                .toList()
                .stream()
                .reduce(Long::sum)
                .orElse(0L)
                .toString();
    }
}
