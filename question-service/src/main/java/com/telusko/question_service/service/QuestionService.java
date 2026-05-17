package com.telusko.question_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.telusko.question_service.dao.QuestionDao;
import com.telusko.question_service.model.Question;
import com.telusko.question_service.model.QuestionWrapper;
import com.telusko.question_service.model.Response;

@Service
public class QuestionService {

	@Autowired
	private QuestionDao questionDao;

	public ResponseEntity<List<Question>> getAllQuestions() {
		try {
			return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {

		try {
			return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> addQuestion(Question question) {
		try {
			questionDao.save(question);
			return new ResponseEntity<>("Success", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<String> deleteQuestion(int id) {
		try {
			questionDao.deleteById(id);
			return new ResponseEntity<>("deleted successfully", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}

	public ResponseEntity<Question> updateQuestion(int id, Question updatedQuestion) {

		Optional<Question> optionalQuestion = questionDao.findById(id);

		if (optionalQuestion.isPresent()) {

			Question existingQuestion = optionalQuestion.get();

			existingQuestion.setQuestionTitle(updatedQuestion.getQuestionTitle());
			existingQuestion.setOption1(updatedQuestion.getOption1());
			existingQuestion.setOption2(updatedQuestion.getOption2());
			existingQuestion.setOption3(updatedQuestion.getOption3());
			existingQuestion.setOption4(updatedQuestion.getOption4());
			existingQuestion.setRightAnswer(updatedQuestion.getRightAnswer());
			existingQuestion.setDifficultyLevel(updatedQuestion.getDifficultyLevel());
			existingQuestion.setCategory(updatedQuestion.getCategory());

			questionDao.save(existingQuestion);

			return new ResponseEntity<>(existingQuestion, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	public ResponseEntity<Question> patchQuestion(int id, Question updatedQuestion) {

		Optional<Question> optionalQuestion = questionDao.findById(id);

		if (optionalQuestion.isPresent()) {

			Question existingQuestion = optionalQuestion.get();

			if (updatedQuestion.getQuestionTitle() != null) {
				existingQuestion.setQuestionTitle(updatedQuestion.getQuestionTitle());
			}

			if (updatedQuestion.getOption1() != null) {
				existingQuestion.setOption1(updatedQuestion.getOption1());
			}

			if (updatedQuestion.getOption2() != null) {
				existingQuestion.setOption2(updatedQuestion.getOption2());
			}

			if (updatedQuestion.getOption3() != null) {
				existingQuestion.setOption3(updatedQuestion.getOption3());
			}

			if (updatedQuestion.getOption4() != null) {
				existingQuestion.setOption4(updatedQuestion.getOption4());
			}

			if (updatedQuestion.getRightAnswer() != null) {
				existingQuestion.setRightAnswer(updatedQuestion.getRightAnswer());
			}

			if (updatedQuestion.getDifficultyLevel() != null) {
				existingQuestion.setDifficultyLevel(updatedQuestion.getDifficultyLevel());
			}

			if (updatedQuestion.getCategory() != null) {
				existingQuestion.setCategory(updatedQuestion.getCategory());
			}

			questionDao.save(existingQuestion);

			return new ResponseEntity<>(existingQuestion, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	public ResponseEntity<List<Integer>> getQuestionsForQuiz(String category, Integer numQuestions) {
		List<Integer> questions = questionDao.findRandomQuestionsByCategory(category, numQuestions);

		return new ResponseEntity<>(questions, HttpStatus.OK);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {

		List<QuestionWrapper> wrappers = new ArrayList<>();
		List<Question> questions = questionDao.findAllById(questionIds);

		for (Question q : questions) {
			QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(),
					q.getOption3(), q.getOption4());
			wrappers.add(qw);
		}

		return new ResponseEntity<>(wrappers, HttpStatus.OK);
	}

	public ResponseEntity<Integer> getScore(List<Response> responses) {

		int right = 0;

		// Extract all question IDs
		List<Integer> ids = responses.stream().map(Response::getId).toList();

		// Single DB call
		List<Question> questions = questionDao.findAllById(ids);

		// Convert to Map<Id, RightAnswer>
		Map<Integer, String> answerMap = new HashMap<>();

		for (Question question : questions) {
			answerMap.put(question.getId(), question.getRightAnswer());
		}

		// Compare answers
		for (Response response : responses) {

			String correctAnswer = answerMap.get(response.getId());

			if (response.getResponse().equals(correctAnswer)) {

				right++;
			}
		}

		return new ResponseEntity<>(right, HttpStatus.OK);
	}

}
