package com.unpassant.unforum.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.unpassant.unforum.dto.NotificationDTO;
import com.unpassant.unforum.dto.PaginationDTO;
import com.unpassant.unforum.enums.NotificationStatusEnum;
import com.unpassant.unforum.enums.NotificationTypeEnum;
import com.unpassant.unforum.exception.CustomErrorCode;
import com.unpassant.unforum.exception.CustomException;
import com.unpassant.unforum.mapper.NotificationMapper;
import com.unpassant.unforum.model.Notification;
import com.unpassant.unforum.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    public PaginationDTO list(int id, Integer page, Integer size) {

        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();

        //定义查询条件
        QueryWrapper<Notification> qw = new QueryWrapper<>();
        qw.eq("receiver",id);

        Integer totalPage;
        //分页总数
        Integer totalCount = notificationMapper.selectCount(qw);

        //计算totalPage
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        //页码越界处理
        if (page > totalPage) {
            page = totalPage;
        }
        if (page < 1) {
            page = 1;
        }

        paginationDTO.setPagination(totalPage,page);

        //越界判断后查询
        //定义分页
        IPage ipage = new Page(page, size);

        //通知倒序排列且未查看的在上
        qw.orderByAsc("status");
        qw.orderByDesc("gmt_create");
        //分页查询
        notificationMapper.selectPage(ipage,qw);
        List<Notification> notifications = ipage.getRecords();
        if (notifications.size() == 0){
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }


        paginationDTO.setData(notificationDTOList);
        return paginationDTO;
    }

    public Integer unreadCount(int userId) {
        QueryWrapper<Notification> qw = new QueryWrapper<>();
        qw.eq("receiver",userId);
        qw.eq("status",NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.selectCount(qw);
    }

    public NotificationDTO read(Integer id, User user) {
        Notification notification = notificationMapper.selectById(id);

        //校验
        if(notification == null){
            throw new CustomException(CustomErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomException(CustomErrorCode.READ_NOTIFICATION_FAIL);
        }

        //更新已读状态
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateById(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return  notificationDTO;
    }
}
