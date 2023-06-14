package com.unpassant.unforum.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author UnPassant
 * @since 2023-06-13
 */
@Data
public class Notification implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 通知者
     */
    private Integer notifier;
    private String notifierName;
    /**
     * 接收者
     */
    private Integer receiver;
    /**
     * 外键ID
     */
    private Integer outerId;
    private String outerTitle;
    /**
     * 通知类型
     */
    private Integer type;
    private Long gmtCreate;
    /**
     * 0未读 1已读
     */
    private Integer status;
}
