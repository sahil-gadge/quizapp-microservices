package com.telusko.question_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telusko.question_service.model.Question;
import com.telusko.question_service.model.QuestionWrapper;
import com.telusko.question_service.model.Response;
import com.telusko.question_service.service.QuestionService;

@RestController
@RequestMapping("question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	Environment environment;

	@GetMapping("allQuestions")
	public ResponseEntity<List<Question>> getAllQuestions() {
		return questionService.getAllQuestions();
	}

	@GetMapping("category/{category}")
	public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable("category") String category) {
		return questionService.getQuestionsByCategory(category);
	}

	@PostMapping("add")
	public ResponseEntity<String> addQuestion(@RequestBody Question question) {

		return questionService.addQuestion(question);
	}

	@DeleteMapping("delete/{id}")
	public ResponseEntity<String> deleteQuestion(@PathVariable("id") int id) {
		return questionService.deleteQuestion(id);
	}

	@PutMapping("update/{id}")
	public ResponseEntity<Question> updateQuestion(@PathVariable int id, @RequestBody Question question) {

		return questionService.updateQuestion(id, question);
	}

	@PatchMapping("patch/{id}")
	public ResponseEntity<Question> patchQuestion(@PathVariable int id, @RequestBody Question question) {

		return questionService.patchQuestion(id, question);
	}

	@GetMapping("generate")
	public ResponseEntity<List<Integer>> getQuestionsForQuiz(@RequestParam("category") String category,
			@RequestParam("numQuestions") Integer numQuestions) {
		return questionService.getQuestionsForQuiz(category, numQuestions);
	}

	@PostMapping("getQuestions")
	public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds) {
//		System.out.println("%%%%%%%%%%%%%%%%%%% " + environment.getProperty("local.server.port"));
		return questionService.getQuestionsFromId(questionIds);
	}

	@PostMapping("getScore")
	public ResponseEntity<Integer> getScore(@RequestBody List<Response> responses) {
		return questionService.getScore(responses);
	}

}
