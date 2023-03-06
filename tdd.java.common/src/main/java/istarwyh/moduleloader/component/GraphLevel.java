package istarwyh.moduleloader.component;

import istarwyh.moduleloader.display.SubjectCodeEnum;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
public class GraphLevel<T extends BaseElement> extends BaseElement implements PageModule<List<T>> {


    private List<T> data;

    public GraphLevel(String subjectCode, List<T> data) {
        super();
        super.setSubjectCode(subjectCode);
        this.data = data;
    }

    @SafeVarargs
    public static <T extends BaseElement> GraphLevel<T> createModule(SubjectCodeEnum subjectCode, T... data) {
        return new GraphLevel<>(subjectCode.name(), Arrays.stream(data).toList());
    }

    @Override
    public void setData(Object data) {
        if(data instanceof List) {
            this.setData((List<T>) data);
        }
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public String getAmount() {
        String amount = super.getAmount();
        if(amount != null || CollectionUtils.isEmpty(this.data)){
            return amount;
        }
        return this.data.stream()
                .map(T::getAmount)
                .filter(Objects::nonNull)
                .map(BigDecimal::new)
                .toList()
                .stream()
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .setScale(2, RoundingMode.HALF_EVEN)
                .toString();
    }

    @Override
    public String getNumber() {
        String number = super.getNumber();
        if(number != null || CollectionUtils.isEmpty(this.data)){
            return number;
        }
        return this.data.stream()
                .map(T::getNumber)
                .filter(Objects::nonNull)
                .map(Long::valueOf)
                .toList()
                .stream()
                .reduce(Long::sum)
                .orElse(0L)
                .toString();
    }
}
