package com.telusko.quiz_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.telusko.quiz_service.dao.QuizDao;
import com.telusko.quiz_service.feign.QuizInterface;
import com.telusko.quiz_service.model.QuestionWrapper;
import com.telusko.quiz_service.model.Quiz;
import com.telusko.quiz_service.model.Response;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class QuizService {
	@Autowired
	private QuizDao quizDao;

	@Autowired
	QuizInterface quizInterface;

	@CircuitBreaker(name = "questionService", fallbackMethod = "createQuizFallback")
	public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

		List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();
		Quiz quiz = new Quiz();
		quiz.setTitle(title);
		quiz.setQuestionIds(questions);
		quizDao.save(quiz);
		return new ResponseEntity<>("Success", HttpStatus.CREATED);
	}

	@CircuitBreaker(name = "questionService", fallbackMethod = "getQuizQuestionsFallback")
	public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(int id) {
		Quiz quiz = quizDao.findById(id).get();

		List<Integer> questionIds = quiz.getQuestionIds();
		ResponseEntity<List<QuestionWrapper>> questionsForUser = quizInterface.getQuestionsFromId(questionIds);

		return questionsForUser;
	}

	@CircuitBreaker(name = "questionService", fallbackMethod = "calculateResultFallback")
	public ResponseEntity<Integer> calculateResult(int id, List<Response> responses) {
		ResponseEntity<Integer> score = quizInterface.getScore(responses);
		return score;
	}

	// ---------------- FALLBACK METHODS ----------------

	public ResponseEntity<String> createQuizFallback(String category, int numQ, String title, Exception ex) {

		return new ResponseEntity<>("Question Service is down. Quiz creation failed.", HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuizQuestionsFallback(int id, Exception ex) {

		List<QuestionWrapper> fallbackQuestions = new ArrayList<>();

		QuestionWrapper q = new QuestionWrapper();

		q.setQuestionTitle("Question Service temporarily unavailable");

		fallbackQuestions.add(q);

		return new ResponseEntity<>(fallbackQuestions, HttpStatus.SERVICE_UNAVAILABLE);
	}

	public ResponseEntity<Integer> calculateResultFallback(int id, List<Response> responses, Exception ex) {

		return new ResponseEntity<>(-1, HttpStatus.SERVICE_UNAVAILABLE);
	}

}
