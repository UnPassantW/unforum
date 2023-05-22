package com.unpassant.unforum.model;

import lombok.Data;

@Data
public class Post {
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


}
