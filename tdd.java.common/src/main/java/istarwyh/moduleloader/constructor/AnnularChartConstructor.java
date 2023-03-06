package istarwyh.moduleloader.constructor;


import istarwyh.moduleloader.component.AnnularChart;

public class AnnularChartConstructor implements PageModuleConstructor<AnnularChart> {

    public static AnnularChartConstructor empty() {
        return new AnnularChartConstructor();
    }
}
