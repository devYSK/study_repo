package com.example.performance.service;

import com.example.performance.dto.Notice;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface NoticeService {
    List<Notice> getAllNotices();
    List<Notice> findByPage(HttpServletRequest request, int pageNumber);

    List<Notice> findNoticesByDates(LocalDateTime startDate, LocalDateTime endDate);

}
