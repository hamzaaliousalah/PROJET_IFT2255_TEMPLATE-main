

////////////////////////////////////
/*
package com.diro.ift2255.config;

import com.diro.ift2255.controller.CourseController;
import com.diro.ift2255.controller.StudentController;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.util.HttpClientApi;
import io.javalin.Javalin;

public class Routes {

    public static void register(Javalin app) {
        registerStudentRoutes(app);
        registerCourseRoutes(app);
    }

    private static void registerStudentRoutes(Javalin app) {
        StudentController studentController = new StudentController();

        app.get("/students", studentController::getAll);
        app.get("/students/{matricule}", studentController::getByMatricule);
        //app.get("/teachers/{numemploye}", teacherController::getOneTeacher)
    }

    private static void registerCourseRoutes(Javalin app) {
        CourseService courseService = new CourseService(new HttpClientApi());
        CourseController courseController = new CourseController(courseService);

        app.get("/courses", courseController::getAllCourses);
        app.get("/courses/{id}", courseController::getCourseById);
    }
}
*/
package com.diro.ift2255.config;

import com.diro.ift2255.controller.CourseController;
import com.diro.ift2255.controller.StudentController;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.util.HttpClientApi;
import io.javalin.Javalin;
import java.util.*;

public class Routes {
    // NOUVEAU: Stockage des commentaires en mémoire
    private static Map<String, List<Map<String, String>>> comments = new HashMap<>();

    public static void register(Javalin app) {
        registerStudentRoutes(app);
        registerCourseRoutes(app);
        registerCommentRoutes(app);  // NOUVEAU
    }

    private static void registerStudentRoutes(Javalin app) {
        StudentController studentController = new StudentController();
        app.get("/students", studentController::getAll);
        app.get("/students/{matricule}", studentController::getByMatricule);
    }

    private static void registerCourseRoutes(Javalin app) {
        CourseService courseService = new CourseService(new HttpClientApi());
        CourseController courseController = new CourseController(courseService);

        app.get("/courses", courseController::getAllCourses);


        app.get("/courses/{id}", ctx -> {
            String id = ctx.pathParam("id");
            var course = courseService.getCourseById(id);

            if (course.isPresent()) {
                var c = course.get();

                // Construire la réponse avec le cours + commentaires
                Map<String, Object> response = new HashMap<>();
                response.put("id", c.getId());
                response.put("name", c.getName());
                response.put("description", c.getDescription());
                response.put("prerequis", c.getPrerequisites());

                
                String courseId = id.toUpperCase();
                List<Map<String, String>> courseComments = comments.getOrDefault(courseId, new ArrayList<>());
                response.put("comments", courseComments);

                ctx.json(response);
            } else {
                ctx.status(404).json(Map.of("error", "Cours non trouvé"));
            }
        });
    }

    // NOUVEAU: Routes pour les commentaires
    private static void registerCommentRoutes(Javalin app) {
        // POST /comments - Ajouter un commentaire
        app.post("/comments", ctx -> {
            Map<String, String> comment = ctx.bodyAsClass(Map.class);
            String courseId = comment.get("courseId").toUpperCase();

            comments.putIfAbsent(courseId, new ArrayList<>());
            comments.get(courseId).add(comment);

            ctx.status(201).json(Map.of("status", "ok", "courseId", courseId));
        });


        app.get("/comments/{courseId}", ctx -> {
            String courseId = ctx.pathParam("courseId").toUpperCase();
            List<Map<String, String>> courseComments = comments.getOrDefault(courseId, new ArrayList<>());
            ctx.json(courseComments);
        });
    }
}