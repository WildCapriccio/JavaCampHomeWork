package com.lagou.edu.service;

import com.lagou.edu.pojo.Resume;

import java.util.List;

public interface IResumeService {

    void addNewResume(Resume resume);

    Resume findResumeById(Long id) throws Exception;

    List<Resume> queryAllResumes() throws Exception;

    void deleteResumeById(Long id) throws Exception;

    void updateResume(Resume resume) throws Exception;
}
