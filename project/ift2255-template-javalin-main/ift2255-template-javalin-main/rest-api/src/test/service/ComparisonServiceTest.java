package com.diro.ift2255.service;

import com.diro.ift2255.model.ComparaisonResult;
import com.diro.ift2255.model.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests pour la classe ComparaisonService.
 */
@ExtendWith(MockitoExtension.class)
class ComparaisonServiceTest {

    @Mock
    private CourseService courseService;

    private ComparaisonService comparaisonService;

    @BeforeEach
    void setUp() {
        comparaisonService = new ComparaisonService(courseService);
    }

    @Test
    void compareCourses_whenBothCoursesExist_returnsResult() {
        // ARRANGE
        // On define 2 courses differents
        String idA = "IFT1015";
        String idB = "IFT1025";

        Course courseA = mock(Course.class);
        Course courseB = mock(Course.class);

        when(courseService.getCourseById(idA)).thenReturn(Optional.of(courseA));
        when(courseService.getCourseById(idB)).thenReturn(Optional.of(courseB));

        // ACT
        // Appel de la methode a tester
        ComparaisonResult result = comparaisonService.compareCourses(idA, idB);

        // ASSERT
        // Le resultat de la comparaison ne devrait pas etre null
        assertNotNull(result, "Le résultat de comparaison ne devrait pas être null");
        // Verifie que les bonnes methodes ont ete appelees
        verify(courseService).getCourseById(idA);
        verify(courseService).getCourseById(idB);
    }







}
