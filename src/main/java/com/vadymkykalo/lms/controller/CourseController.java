package com.vadymkykalo.lms.controller;

import com.vadymkykalo.lms.entity.Course;
import com.vadymkykalo.lms.repository.CourseRepository;
import com.vadymkykalo.lms.service.markdown.MarkdownService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final MarkdownService markdownService;
    private final CourseRepository courseRepository;

    @Value("${markdown.courses.path}")
    private String markdownCoursesPath;

    public CourseController(MarkdownService markdownService, CourseRepository courseRepository) {
        this.markdownService = markdownService;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseRepository.findByIsActiveTrue();
        model.addAttribute("courses", courses);
        return "courses";
    }

    @GetMapping("/{courseName}")
    public String listCourseLessons(@PathVariable String courseName, Model model) {
        File directory = new File(markdownCoursesPath + "/" + courseName);
        List<String> lessons = Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .filter(File::isFile)
                .map(file -> file.getName().replace(".md", ""))
                .collect(Collectors.toList());

        model.addAttribute("courseName", courseName);
        model.addAttribute("lessons", lessons);
        return "course_lessons";
    }

    @GetMapping("/{courseName}/{lessonName}")
    public String showLesson(@PathVariable String courseName, @PathVariable String lessonName, Model model) {
        try {
            String markdownPath = markdownCoursesPath + "/" + courseName + "/" + lessonName + ".md";
            String htmlContent = markdownService.renderMarkdown(markdownPath);
            model.addAttribute("content", htmlContent);
            model.addAttribute("lessonName", lessonName);
            model.addAttribute("courseName", courseName);
        } catch (Exception e) {
            model.addAttribute("content", "Упс! Не вдалося завантажити урок.");
        }

        return "lesson";
    }
}
