package com.unpassant.unforum.cache;

import com.unpassant.unforum.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();

        TagDTO program = new TagDTO();
        program.setCategoryName("编程相关");
        program.setTags(Arrays.asList("Java","Spring Boot","Spring","js"));
        tagDTOS.add(program);

        TagDTO game = new TagDTO();
        game.setCategoryName("游戏相关");
        game.setTags(Arrays.asList("Blue Archive","Minecraft","Darkest Dungeon"));
        tagDTOS.add(game);

        TagDTO thing = new TagDTO();
        thing.setCategoryName("杂项");
        thing.setTags(Arrays.asList("贴吧","微博","B站","Test"));
        tagDTOS.add(thing);

        return  tagDTOS;
    }

    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));

        return invalid;
    }
}
