package com.lagou.edu.service.impl;

import com.lagou.edu.dao.IResumeDao;
import com.lagou.edu.pojo.Resume;
import com.lagou.edu.service.IResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResumeService implements IResumeService {

    @Autowired
    private IResumeDao resumeDao;

    @Override
    public List<Resume> queryAllResumes() throws Exception {
        return resumeDao.findAll();
    }
}
