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
    public void addNewResume(Resume resume){
        // input doesn't have id
        resumeDao.save(resume);
    }

    @Override
    public Resume findResumeById(Long id) throws Exception {
        return resumeDao.findById(id).get();
    }

    @Override
    public List<Resume> queryAllResumes() throws Exception {
        return resumeDao.findAll();
    }

    @Override
    public void deleteResumeById(Long id) throws Exception {
        resumeDao.deleteById(id);
    }

    @Override
    public void updateResume(Resume resume) throws Exception {
        // input has id
        resumeDao.save(resume);
    }
}
