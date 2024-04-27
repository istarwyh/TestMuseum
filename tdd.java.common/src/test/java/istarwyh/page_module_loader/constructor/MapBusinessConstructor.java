package istarwyh.page_module_loader.constructor;

import com.google.common.collect.Maps;
import istarwyh.page_module_loader.*;
import istarwyh.page_module_loader.component.MapBusiness;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

import static istarwyh.page_module_loader.component.MapBusiness.createMapBusiness;

public class MapBusinessConstructor implements PageModuleConstructor<MapBusiness, MapBusinessConstructor.QueryDTO> {

    public MapBusiness construct(PageModuleRawStructure pageModuleRawStructure, DataContext<QueryDTO> dataContext){
        if("ABusiness".equals(dataContext.getQueryDTO().getBizCode())) {
            Map<String, String> data = Maps.newHashMap();
            data.put("key1", "value1");
            data.put("key2", "value2");
            return createMapBusiness("ABusiness", data);
        }
        throw new IllegalStateException("Invalid QueryDTO: " + dataContext);
    }

    @Getter
    @Builder
    public static class QueryDTO {

        private String bizCode;
    }

}
