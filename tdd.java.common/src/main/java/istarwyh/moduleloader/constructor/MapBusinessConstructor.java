package istarwyh.moduleloader.constructor;

import com.google.common.collect.Maps;
import istarwyh.moduleloader.*;
import istarwyh.moduleloader.component.MapBusiness;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

import static istarwyh.moduleloader.component.MapBusiness.createMapBusiness;

public class MapBusinessConstructor implements PageModuleConstructor<MapBusiness, MapBusinessConstructor.QueryDTO> {

    public MapBusiness construct(ViewStructure viewStructure, DataContext<QueryDTO> dataContext){
        if("ABusiness".equals(dataContext.getQueryDTO().getBizCode())) {
            Map<String, String> data = Maps.newHashMap();
            data.put("key1", "value1");
            data.put("key2", "value2");
            return createMapBusiness("ABusiness", data);
        }
        throw new IllegalStateException("Invalid QueryDTO: " + dataContext);
    }

    @Builder
    public static class QueryDTO {

        @Getter
        private String bizCode;
    }

}
