package istarwyh.moduleloader.constructor;


import istarwyh.moduleloader.component.AnnularChart;

public class AnnularChartConstructor implements ComponentConstructor<AnnularChart> {

    public static AnnularChartConstructor empty() {
        return new AnnularChartConstructor();
    }
}
