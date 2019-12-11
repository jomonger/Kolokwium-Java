package pl.kolokwium.dc;

import java.util.ArrayList;
import java.util.List;

public class Question {

	private String questionText;
	private List<String> possibleAnswers = new ArrayList<>();
	
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	
	public List<String> getPossibleAnswers() {
		return possibleAnswers;
	}
	
	public void setPossibleAnswers(List<String> possibleAnswers) {
		this.possibleAnswers = possibleAnswers;
	}
	
	
}
