package com.example.performance.service;

import com.example.performance.dto.Notice;
import com.example.performance.mapper.NoticeReadMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class NoticeServiceImpl implements NoticeService{

    private NoticeReadMapper noticeReadMapper;

    public NoticeServiceImpl(NoticeReadMapper noticeReadMapper){
        this.noticeReadMapper = noticeReadMapper;
    }

    @Override
    @Cacheable(value = "NoticeReadMapper.findAll")
    public List<Notice> getAllNotices() {
        return noticeReadMapper.findAll();
    }

    @Override
    // @Cacheable(value = "NoticeReadMapper.findByPage", key = "#request.requestURI + '-' + #pageNumber", condition = "#pageNumber <= 5")
    public List<Notice> findByPage(HttpServletRequest request, int pageNumber) {
        int startIdx = (pageNumber - 1) * 10;
        return noticeReadMapper.findByPage(startIdx);
    }

    @Override
    public List<Notice> findNoticesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return noticeReadMapper.findNoticesByDates(startDate, endDate);
    }


}
