package com.unpassant.unforum.dto;

import lombok.Data;

@Data
public class NotificationDTO {

    private Integer id;
    private Long gmt_create;
    private Integer status;
    private Integer notifier;
    private String notifierName;
    private Integer outerId;
    private String outerTitle;
    private String typeName;
    private Integer type;
}
