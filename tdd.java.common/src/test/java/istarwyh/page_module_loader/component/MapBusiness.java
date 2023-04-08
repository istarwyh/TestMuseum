package istarwyh.page_module_loader.component;

import istarwyh.page_module_loader.bill.AbstractBillElement;

import java.util.Map;


public class MapBusiness extends AbstractBillElement<Map<String,String>> {

    public static MapBusiness createMapBusiness(String subjectCode, Map<String, String> data) {
        MapBusiness mapBusiness = new MapBusiness();
        mapBusiness.setSubjectCode(subjectCode);
        mapBusiness.setData(data);
        return mapBusiness;
    }

}
