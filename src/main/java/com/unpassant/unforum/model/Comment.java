package com.unpassant.unforum.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @author UnPassant
 * @since 2023-06-08
 */
@Data
public class Comment{

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父类ID
     */
    private Integer parent_id;

    /**
     * 父类类型
     */
    private Integer type;

    /**
     * 评论者ID
     */
    private Integer commentator;

    private Long gmt_create;

    private Long gmt_modified;

    /**
     * 被点赞数
     */
    private Long like_count;

    /**
     * 评论内容
     */
    private String content;


}
