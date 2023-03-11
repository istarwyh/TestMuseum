package istarwyh.moduleloader.constructor;



import istarwyh.moduleloader.PageModuleConstructor;
import istarwyh.moduleloader.component.BarChart;

public class BarChartConstructor implements PageModuleConstructor<BarChart,Void> {

    public static BarChartConstructor empty() {
        return new BarChartConstructor();
    }
}
