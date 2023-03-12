package istarwyh.moduleloader.component;

import istarwyh.moduleloader.AbstractElement;

import java.util.Map;


public class MapBusiness extends AbstractElement<Map<String,String>> {

    private Map<String,String> data;

    private MapBusiness(String subjectCode, Map<String, String> data) {
        super();
        super.setSubjectCode(subjectCode);
        this.data = data;
    }

    public static MapBusiness createMapBusiness(String subjectCode, Map<String, String> data) {
        return new MapBusiness(subjectCode, data);
    }

    @Override
    public Map<String,String> getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        if (data instanceof Map) {
            this.setData((Map<String, String>) data);
        }
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }


}
