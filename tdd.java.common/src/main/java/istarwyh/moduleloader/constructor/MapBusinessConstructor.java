package istarwyh.moduleloader.constructor;

import com.google.common.collect.Maps;
import istarwyh.moduleloader.component.MapBusiness;
import istarwyh.moduleloader.display.SubjectCodeEnum;

import java.util.HashMap;

import static istarwyh.moduleloader.component.MapBusiness.createMapBusiness;

public class MapBusinessConstructor implements ComponentConstructor<MapBusiness> {

    private final ViewStructure viewStructure;
    private final Object queryDTO;

    private MapBusinessConstructor(ViewStructure viewStructure, Object queryDTO) {
        this.viewStructure = viewStructure;
        this.queryDTO = queryDTO;
    }

    public static ComponentConstructor<?> createMapBusinessConstructor(ViewStructure viewStructure, Object queryDTO ) {
        return new MapBusinessConstructor(viewStructure, queryDTO);
    }

    public MapBusiness build(ViewStructure viewStructure, Object queryDTO){
        if(SubjectCodeEnum.ABusiness.name().equals (queryDTO.toString())){
            HashMap<String, String> data = Maps.newHashMap();
            data.put("key1", "value1");
            data.put("key2", "value2");
            return createMapBusiness(SubjectCodeEnum.ABusiness.name(), data);
        }
        return null;
    }


}
