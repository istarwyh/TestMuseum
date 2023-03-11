package istarwyh.moduleloader.constructor;

import com.google.common.collect.Maps;
import istarwyh.moduleloader.PageModuleConstructor;
import istarwyh.moduleloader.component.MapBusiness;
import istarwyh.moduleloader.ModuleLoader;
import istarwyh.moduleloader.SubjectCodeEnum;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

import static istarwyh.moduleloader.component.MapBusiness.createMapBusiness;

public class MapBusinessConstructor implements PageModuleConstructor<MapBusiness, MapBusinessConstructor.QueryDTO> {

    public static PageModuleConstructor<?,QueryDTO> empty() {
        return new MapBusinessConstructor();
    }

    public MapBusiness build(ViewStructure viewStructure, ModuleLoader.DataContext<QueryDTO> dataContext){
        if(SubjectCodeEnum.ABusiness.name().equals(dataContext.getQueryDTO().getBizCode())) {
            Map<String, String> data = Maps.newHashMap();
            data.put("key1", "value1");
            data.put("key2", "value2");
            return createMapBusiness(SubjectCodeEnum.ABusiness.name(), data);
        }
        throw new IllegalStateException("Invalid QueryDTO: " + dataContext);
    }

    @Builder
    public static class QueryDTO {

        @Getter
        private String bizCode;
    }

}
