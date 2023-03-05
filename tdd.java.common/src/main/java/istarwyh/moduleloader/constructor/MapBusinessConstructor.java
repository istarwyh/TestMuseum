package istarwyh.moduleloader.constructor;

import com.google.common.collect.Maps;
import istarwyh.moduleloader.component.MapBusiness;
import istarwyh.moduleloader.display.ModuleLoader;
import istarwyh.moduleloader.display.SubjectCodeEnum;

import java.util.HashMap;

import static istarwyh.moduleloader.component.MapBusiness.createMapBusiness;

public class MapBusinessConstructor implements ComponentConstructor<MapBusiness> {

    public static ComponentConstructor<?> empty() {
        return new MapBusinessConstructor();
    }

    public MapBusiness build(ViewStructure viewStructure, ModuleLoader.DataContext queryDTO){
        if(SubjectCodeEnum.ABusiness.name().equals(queryDTO.getBizCode())){
            HashMap<String, String> data = Maps.newHashMap();
            data.put("key1", "value1");
            data.put("key2", "value2");
            return createMapBusiness(SubjectCodeEnum.ABusiness.name(), data);
        }
        return null;
    }


}
