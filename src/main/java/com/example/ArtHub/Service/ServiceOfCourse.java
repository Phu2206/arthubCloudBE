package com.example.ArtHub.Service;
import com.example.ArtHub.CourseNotFoundException;
import com.example.ArtHub.DTO.CreateCourseDTO;
import com.example.ArtHub.DTO.ResponeCourseDTO;
import com.example.ArtHub.DTO.ResponeStudentInfor;
import com.example.ArtHub.Entity.Account;
import com.example.ArtHub.Entity.Course;
import com.example.ArtHub.Repository.*;
import com.example.ArtHub.utils.ModelMapperObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOfCourse implements ICourseService {

    @Autowired
    CourseRateRepository courseRateRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    LearnerRepository learnerRepository;

    @Autowired
    private ModelMapperObject modelMapper;

    @Autowired
    ServiceOfCategory serviceOfCategory;

    @Autowired
    ServiceOfSection sectionService;

    @Autowired
    ServiceOfAccount serviceOfAccount;

    @Autowired
    ServiceOfCategoryCourse serviceOfCategoryCourse;

    @Autowired
    ServiceOfLearningObjective serviceOfLearningObjective;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ServiceOfImage serviceOfImage;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Course createCourse(CreateCourseDTO dto) {
        Course course = modelMapper.modelMapper().map(dto, Course.class);
        return courseRepository.save(course);
    }


    @Override
    public String getNameByID(int id)
    {
       return courseRepository.findById(id).getName();
    }

    @Override
    public int DeleteCourseByID(int courseId)
    {
        return courseRepository.deleteViolatedCourse(courseId);
    }


    @Override
    public List<ResponeCourseDTO> findCoursesByInstructorId(int id) throws CourseNotFoundException {
        return courseRepository.findCoursesByInstructorId(id).stream().map(course -> fromCourseToResponeCourseDTO2(course)).toList();
    }


    @Override
    public ResponeStudentInfor fromAccountToResponeStudentDTO(Account account)
    {
        ResponeStudentInfor student = new ResponeStudentInfor();
        student.setId(account.getId());
        student.setEmail(account.getEmail());
        student.setFacebook(account.getFacebook());
        student.setFirstname(account.getFirstname());
        student.setTwitter(account.getTwitter());
        student.setLastname(account.getLastname());
        return  student;
    }


   @Override
    public int updateCourseStatus(int courseId, int status)
    {
        return courseRepository.updateCourseStatus(courseId, status);
    }


    @Override
    public int updateMainImage(int id, String imageName)
    {
        return courseRepository.updateMainImage(id,imageName);
    }

    @Override
    public ResponeCourseDTO fromCourseToResponeCourseDTO2(Course course) {
        ResponeCourseDTO courseDTO = new ResponeCourseDTO();
        courseDTO.setName(course.getName());
        courseDTO.setPrice(course.getPrice());
        courseDTO.setInstructorName(accountRepository.findById(course.getAccountId()).get().getFirstname()+" "+accountRepository.findById(course.getAccountId()).get().getLastname());
        courseDTO.setImage(course.getImage());
        courseDTO.setStudents(learnerRepository.findLeanerOfCourse(course.getId()).stream().map(account -> fromAccountToResponeStudentDTO(account)).toList());
        return courseDTO;
    }


    @Override
    public  ResponeCourseDTO fromCourseToResponeCourseDTO(Course course) { //This function use to convert courseEntity to ResponeCourseDTO
        ResponeCourseDTO courseDTO = modelMapper.modelMapper().map(course, ResponeCourseDTO.class);
        courseDTO.setSections(sectionService.getSectionList(course.getId()).stream().map(section -> sectionService.fromSectionIntoResponeSectionDTO(section)).toList());
        courseDTO.setImages(serviceOfImage.getImageByCourseID(course.getId()));
        courseDTO.setBio(serviceOfAccount.getAccountByCourseID(course.getAccountId()).get().getBio());
        courseDTO.setInstructorName(serviceOfAccount.getAccountByCourseID(course.getAccountId()).get().getFirstname()+" "+serviceOfAccount.getAccountByCourseID(course.getAccountId()).get().getLastname());
        courseDTO.setInstructorImage(serviceOfAccount.getAccountByCourseID(course.getAccountId()).get().getImage());
        courseDTO.setInstructorAddress(serviceOfAccount.getAccountByCourseID(course.getAccountId()).get().getAddress());
        courseDTO.setInstructorEmail(serviceOfAccount.getAccountByCourseID(course.getAccountId()).get().getEmail());
        courseDTO.setInstructorFacebook(serviceOfAccount.getAccountByCourseID(course.getAccountId()).get().getFacebook());
        courseDTO.setInstructorPhone(serviceOfAccount.getAccountByCourseID(course.getAccountId()).get().getPhone());
        courseDTO.setInstructorTwitter(serviceOfAccount.getAccountByCourseID(course.getAccountId()).get().getTwitter());
        courseDTO.setCategories(serviceOfCategoryCourse.getCategoriesByCourseID(course.getId()).stream().map(categoryCourse -> serviceOfCategory.fromCategoryToCategotyResponeNameDTO(categoryCourse)).toList());
        courseDTO.setLearningObjective(serviceOfLearningObjective.getLearningObjectiveByCourseId(course.getId()));
        courseDTO.setCount(courseRateRepository.countCourseRateByCourseId(course.getId()));
        courseDTO.setAvg(courseRateRepository.avgCourseRateByCourseId(course.getId()));
        return courseDTO;
    }



@Override
    public  List<ResponeCourseDTO> fromCourseListToResponeCourseDTOList(List<Course> CourseList) { //This fucntion use to convert courseList into ResponeCourseDTO list
        List<ResponeCourseDTO> ResponeCourseDTOList = new ArrayList<>();
        for (Course course : CourseList) {

            ResponeCourseDTOList.add(fromCourseToResponeCourseDTO(course));
        }
        return ResponeCourseDTOList;
    }

    @Override
    public List<ResponeCourseDTO> getCourseList() {
        return courseRepository.findAll().stream().map(course -> fromCourseToResponeCourseDTO(course)).toList();
    }

}
