package istarwyh.moduleloader.component;

import istarwyh.moduleloader.AbstractElement;

import java.util.Map;


public class MapBusiness extends AbstractElement<Map<String,String>> {

    private MapBusiness(String subjectCode, Map<String, String> data) {
        super();
        super.setSubjectCode(subjectCode);
        super.setData(data);
    }

    public static MapBusiness createMapBusiness(String subjectCode, Map<String, String> data) {
        return new MapBusiness(subjectCode, data);
    }

}
