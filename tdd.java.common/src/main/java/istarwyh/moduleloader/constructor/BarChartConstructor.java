package istarwyh.moduleloader.constructor;



import istarwyh.moduleloader.component.BarChart;

public class BarChartConstructor implements PageModuleConstructor<BarChart> {

    public static BarChartConstructor empty() {
        return new BarChartConstructor();
    }
}
