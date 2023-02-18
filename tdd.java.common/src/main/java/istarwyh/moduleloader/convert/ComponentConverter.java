package istarwyh.moduleloader.convert;

import istarwyh.moduleloader.component.Board;
import istarwyh.moduleloader.component.Node;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import istarwyh.moduleloader.component.Module;

@Mapper
public interface ComponentConverter {

    ComponentConverter INSTANCE = Mappers.getMapper(ComponentConverter.class);

    @Mappings({
            @Mapping(target = "moduleTypeCode", ignore = true),
            @Mapping(target = "data", ignore = true)
    })
    Module convertModule(Node node);

}
