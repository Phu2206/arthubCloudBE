package com.example.ArtHub.Service;

import com.example.ArtHub.AppServiceExeption;
import com.example.ArtHub.DTO.CreateLearningObjectiveDTO;
import com.example.ArtHub.Entity.LearningObjective;

import java.util.List;

public interface ILearningObjectiveService {
    LearningObjective createLearningObjective(CreateLearningObjectiveDTO dto,int ID) throws AppServiceExeption;

    List<LearningObjective> getLearningObjectiveList();

    LearningObjective getLearningObjectiveByCourseId(int ID);
}
