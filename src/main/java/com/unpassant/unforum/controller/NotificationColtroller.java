package com.unpassant.unforum.controller;

import com.unpassant.unforum.dto.NotificationDTO;
import com.unpassant.unforum.dto.PaginationDTO;
import com.unpassant.unforum.enums.NotificationTypeEnum;
import com.unpassant.unforum.model.User;
import com.unpassant.unforum.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationColtroller {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "id") Integer id) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id, user);

        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType() ||
            NotificationTypeEnum.REPLY_POST.getType() == notificationDTO.getType()) {

            return "redirect:/post/" + notificationDTO.getOuterId();
        }else {
            return "redirect:/";
        }
    }
}
