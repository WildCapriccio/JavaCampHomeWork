package com.lagou.edu.dao;

import com.lagou.edu.pojo.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IResumeDao extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {
}
