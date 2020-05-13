import com.lagou.edu.dao.IResumeDao;
import com.lagou.edu.pojo.Resume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:applicationContext.xml"})
public class ResumeDaoTest {

    @Autowired
    private IResumeDao resumeDao;

    @Test
    public void testAddNewRecord() {
        Resume resume = new Resume();
        resume.setName("李四");
        resume.setAddress("成都");
        resume.setPhone("123000");

        Resume result = resumeDao.save(resume);
        System.out.println(result);
    }

    // Try to update a non-existed record -> add a new record with an auto incremental ID at the end
    @Test
    public void testUpdateNonExistedRecord() {
        Resume resume = new Resume();
        resume.setId(4l);
        resume.setName("赵六liu");
        resume.setAddress("成都");
        resume.setPhone("893000");

        Resume updateResult = resumeDao.save(resume);
        System.out.println(updateResult);
    }

    // Try to update an existed record -> just update it
    @Test
    public void testUpdateRecord() {
        Resume resume = new Resume();
        resume.setId(6l);
        resume.setName("更新");
        resume.setAddress("湖南");
        resume.setPhone("678540");

        Resume result = resumeDao.save(resume);
        System.out.println(result);
    }

    // Deletion can only happen when id is given.
    // Hence, searching the record you want to delete at first.
    // E.g. use Dynamic Query to find one given multiple conditions.
    @Test
    public void testDeleteGivenFullInfo() {
        Resume resume = new Resume();
        resume.setName("更新");
        resume.setAddress("湖南");
        resume.setPhone("678540");

        resumeDao.delete(resume); // Cannot delete the record.
    }
    /*
     * Hibernate: insert into tb_resume (address, name, phone) values (?, ?, ?)
     * Hibernate: delete from tb_resume where id=?
     * */

    @Test
    public void testDeleteGivenIncompleteInfo() {
        Resume resume = new Resume();
        resume.setName("赵六liu");
        resume.setAddress("成都");

        resumeDao.delete(resume); // Cannot delete the record neither
    }
    /*
    * Hibernate: insert into tb_resume (address, name, phone) values (?, ?, ?)
    * Hibernate: delete from tb_resume where id=?
    * */

    @Test
    public void testFindOneAndDelete() {
        // Find one
        Specification<Resume> specification = new Specification<Resume>() {
            @Override
            public Predicate toPredicate(Root<Resume> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<Object> attribute1 = root.get("name");
                Path<Object> attribute2 = root.get("address");

                Predicate predicate1 = criteriaBuilder.equal(attribute1, "赵六liu");
                Predicate predicate2 = criteriaBuilder.equal(attribute2, "成都");

                Predicate conditions = criteriaBuilder.and(predicate1, predicate2);
                return conditions;
            }
        };
        Optional<Resume> optional = resumeDao.findOne(specification);
        Resume result = optional.get();
        System.out.println(result);

        // Delete the "result" record
        resumeDao.deleteById(result.getId());
    }

    @Test
    public void testFindAll() {
        List<Resume> list = resumeDao.findAll();
        for (int i = 0; i < list.size(); i++) {
            Resume resume =  list.get(i);
            System.out.println(resume);
        }
    }
}
