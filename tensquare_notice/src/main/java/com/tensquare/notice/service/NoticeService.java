package com.tensquare.notice.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.tensquare.notice.client.ArticleClient;
import com.tensquare.notice.client.UserClient;
import com.tensquare.notice.dao.NoticeDao;
import com.tensquare.notice.dao.NoticeFreshDao;
import com.tensquare.notice.pojo.Notice;
import com.tensquare.notice.pojo.NoticeFresh;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private NoticeFreshDao noticeFreshDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ArticleClient articleClient;

    public Notice selectById(String id) {
        Notice notice = noticeDao.selectById(id);
        getNoticeInfo(notice);
        return notice;
    }

    public Page<Notice> selectByPage(Notice notice,Integer page,Integer size) {
        //封装分页对象
        Page<Notice> pageData = new Page<>(page, size);

        //执行分页查询
        List<Notice> noticeList = noticeDao.selectPage(pageData, new EntityWrapper<>(notice));

        for (Notice notice1 : noticeList) {
            getNoticeInfo(notice1);
        }

        //设置分页结果集到分页对象
        pageData.setRecords(noticeList);

        return pageData;
    }

    public void save(Notice notice) {

        notice.setState("0");
        notice.setCreatetime(new Date());

        String id = idWorker.nextId() + "";
        notice.setId(id);

        noticeDao.insert(notice);

        //推送消息入库，新消息提醒
        NoticeFresh noticeFresh = new NoticeFresh();
        noticeFresh.setNoticeId(id);
        noticeFresh.setUserId(notice.getReceiverId());
        noticeFreshDao.insert(noticeFresh);
    }

    public void updateById(Notice notice) {
        noticeDao.updateById(notice);
    }

    public Page<NoticeFresh> freshPage(String userId, Integer page, Integer size) {
        //封装查询条件
        NoticeFresh noticeFresh = new NoticeFresh();
        noticeFresh.setUserId(userId);

        //创建分页对象
        Page<NoticeFresh> pageData = new Page<>(page, size);

        //执行查询
        List<NoticeFresh> list = noticeFreshDao.selectPage(pageData, new EntityWrapper<>(noticeFresh));

        //设置查询结果到分页对象中
        pageData.setRecords(list);

        return pageData;


    }

    public void freshDelete(NoticeFresh noticeFresh) {
        noticeFreshDao.delete(new EntityWrapper<>(noticeFresh));
    }

    /**
     * 查询相关数据
     * @param notice
     */
    private void getNoticeInfo(Notice notice) {
        //获取用户信息
        Result userResult = userClient.findById(notice.getOperatorId());
        HashMap userMap = (HashMap) userResult.getData();
        notice.setOperatorName(userMap.get("nickname").toString());

        //获取文章信息
        if ("article".equals(notice.getTargetType())) {
            Result articleResult = articleClient.findById(notice.getTargetId());
            HashMap articleMap = (HashMap) articleResult.getData();
            notice.setTargetName(articleMap.get("title").toString());
        }
    }
}
