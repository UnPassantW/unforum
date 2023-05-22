package com.unpassant.unforum.dto;

import com.unpassant.unforum.model.User;
import lombok.Data;

@Data
public class PostDTO {
    private int id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private int creator;
    private int commentCount;
    private int viewCount;
    private int likeCount;
    private String tags;
    private User user;
}
