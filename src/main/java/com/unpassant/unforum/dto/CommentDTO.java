package com.unpassant.unforum.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.unpassant.unforum.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long like_count;
    private Integer commentCount;
    private String content;
    private User user;
}
