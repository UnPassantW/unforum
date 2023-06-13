package com.unpassant.unforum.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * @author UnPassant
 * @since 2023-06-08
 */
@Data
public class Comment{

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;

    @Version
    private Integer version;
}
