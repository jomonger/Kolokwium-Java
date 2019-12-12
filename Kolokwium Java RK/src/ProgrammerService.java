import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.kolokwium.dc.*;
import pl.kolokwium.org.User;

public class ProgrammerService {
	public void execute(DocumentDao documentDao) {
		
	////Zadanie 0.
		Document tempDoc;
		
		List<Questionnaire> questionnaires = new ArrayList<>();
		List<ApplicationForHolidays> applications = new ArrayList<>();
		List<User> users = new ArrayList<User>();
		
		List<Document> list = documentDao.getAllDocumentsInDatabase();
		
		for(int i = 0; i < list.size(); i++) {// Rozdzielenie pobranych dokumentów.
			tempDoc = list.get(i);
			if (tempDoc.getClass() == ApplicationForHolidays.class) 
				applications.add((ApplicationForHolidays) tempDoc);	
			else if (tempDoc.getClass() == Questionnaire.class)
				questionnaires.add((Questionnaire) tempDoc);
		}
		
		long time = System.currentTimeMillis();
	/////Zadanie 1
		avgAnswersPerQuestion(questionnaires);
		
	/////Zadanie 2
		hasLoginPolSign(applications, users);
		
	/////Zadanie 3	
		isDateMistaken(applications);
		
	/////Zadanie 4 
		questionnaires.forEach(k -> Helper.saveQuestionnaireToTxt(k)); 
		
	/////Zadanie 5
		salaryChanger(users.get(0), 3500);
		
	/////Zadanie 6
		doubleExecution(questionnaires, applications, users);
		time = System.currentTimeMillis() - time;
		
	/////Zadanie 7
		executeAll(questionnaires, applications, users);	
		
		System.out.println("Czas wykonywania zadań szeregowo: "  + time + "ms");
	}
	////////////////////////////////////////////////////////////////////////////////////////////////
	/////Zadanie 1
	
	private double avgAnswersPerQuestion(List<Questionnaire> qs) {
		double questions = 0, answers = 0;
		for(int i = 0; i < qs.size(); i++) {  
			for(int j = 0; j < qs.get(i).getQuestions().size(); j++) {
				questions++;
				for(int k = 0; k < qs.get(i).getQuestions().get(j).getPossibleAnswers().size(); k++) {
					answers++;
				}
			}
		}
		double avg = answers / questions;
		System.out.println("\nZadanie 1: Średnia ilość odpowiedzi na pytanie wynosi: " + avg + "\n");
		return avg;
	}
	/////////////////////////////////////////////////////////////////////////////////////////////
	/////Zadanie 2
	
	private boolean hasLoginPolSign (List<ApplicationForHolidays> apps, List<User> users) {
		byte[] login_bytes;
		boolean sign_found = false;
		System.out.print("\nZadanie 2 ");
		for(int i = 0; i < apps.size(); i++) {
			users.add(apps.get(i).getUserWhoRequestAboutHolidays());//Wypełnienie listy userów.
			login_bytes = users.get(i).getLogin().getBytes(); // pobranie tekstu w postaci tablicy bajtów.
			for(int j = 0; j < login_bytes.length; j++) {
				if(login_bytes[j] < 0) {							// Polskie znaki przyjmują wartość ujemną w zapisie bajtowym. 
					System.out.print("Login z Polskim znakiem to ");
					System.out.println(users.get(i).getLogin());
					sign_found = true;
				}
			}
		}  
		return sign_found;// Metoda zwraca True jeśli jakakilwiek polski znak jest wykryty.	
	}
	///////////////////////////////////////////////////////////////////////////////////////////
	//////Zadanie 3
	
	private boolean isDateMistaken(List<ApplicationForHolidays> apps) {	
		boolean isMistaken = false;
		System.out.println("\n");
		for(int i = 0; i < apps.size(); i++) {
			if (apps.get(i).getSince().compareTo(apps.get(i).getTo()) >= 0) {//Ponieważ w dacie występuje godzina, przyjąłem, że ta sama data to błąd. 
				isMistaken = true;
				System.out.println("Zadanie 3: Użytkownik o loginie: " + apps.get(i).getUserWhoRequestAboutHolidays().getLogin()+ " ma złą datę");
			}
		}
		return isMistaken; // Metoda zwraca True jeśli jakakolwiek data jest żle wpisana.	
	}
	//////////////////////////////////////////////////////////////////////////////////////////
	//////Zadanie 4/////////////
	
	///// ->Helper.java//////////
	
	/////////////////////////////////////////////////////////////////////////////////////
	///// Zadanie 5
	
	private void salaryChanger(User u, double new_salary) {
		System.out.println("\nZadanie 5:");
		try {
			Field f = User.class.getDeclaredField("salary");
			f.setAccessible(true);// Wymuszenie dostępu do zmiennej prywatnej.
			System.out.println("\nWynagrodzenie przed zmianą wynosi : " + f.getDouble(u) + ".");   
			f.setDouble(u, new_salary);
			System.out.println("Wynagrodzenie po zmianie wynosi : " + f.getDouble(u) + ".");  
			f.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		} 	
	}
	////////////////////////////////////////////////////////////////////////////// 
	///// Zadanie 6
	
	private void doubleExecution(List<Questionnaire> qs, List<ApplicationForHolidays> apps, List<User> users) { 
		System.out.println("\nZadanie 6: Zadanie 2 i 5 uruchomione w 2 watkach równlolegle\n");
		ExecutorService _EXSE = Executors.newFixedThreadPool(2);//Ilosc wątków wykonywanych równolegle dla tego executora.
		
		_EXSE.execute(new Runnable() {
			@Override
			public void run() {
			/////Zadanie 2
				hasLoginPolSign(apps, users);
			}
		});
		_EXSE.execute(new Runnable() {
			@Override
			public void run() {
			/////Zadanie 5	
				salaryChanger(users.get(0), 4000);
			}
		});	
		_EXSE.shutdown();// Wyłączenie executora po wykonaniu wątków (inaczej wpadnie w nieskończoną pętle).
		while (!_EXSE.isTerminated());// Zawieszenie głównego wątku aż zakończą się wszystkie wątki, aby poprawnie zmierzyć czas wykonywania.
	}
	/////////////////////////////////////////////////////////////////////////////
	///// Zadanie 7
	//// Uruchamianie zadań 1 - 6 w równoległych wątkach.
	private void executeAll(List<Questionnaire> qs, List<ApplicationForHolidays> apps, List<User> users) {
		System.out.println("\n\n\nZADANIE 7:");
		long time = System.currentTimeMillis();
		ExecutorService _EXSE = Executors.newFixedThreadPool(6);//Ilosc wątków wykonywanych równolegle dla tego executora.
		
		_EXSE.execute(new Runnable() {
			@Override
			public void run() {
				avgAnswersPerQuestion(qs);
			}
		});
		_EXSE.execute(new Runnable() {
			@Override
			public void run() {	
				hasLoginPolSign(apps, users);
			}
		});	
		
		_EXSE.execute(new Runnable() {
			@Override
			public void run() {
				isDateMistaken(apps);
			}
		});	
		_EXSE.execute(new Runnable() {
			@Override
			public void run() {
				qs.forEach(k -> Helper.saveQuestionnaireToTxt(k)); 
			}
		});	
		_EXSE.execute(new Runnable() {
			@Override
			public void run() {
				salaryChanger(users.get(0), 3500);
			}
		});	
		_EXSE.execute(new Runnable() {
			@Override
			public void run() {
				doubleExecution(qs, apps, users);
			}
		});	
		_EXSE.shutdown();// Wyłączenie executora po wykonaniu wątków (inaczej wpadnie w nieskończoną pętle).
		while (!_EXSE.isTerminated());// Zawieszenie głównego wątku aż zakończą się wszystkie wątki, aby poprawnie zmierzyć czas wykonywania.
		System.out.println("\nCzas wykonywania zadań równolegle: " + (System.currentTimeMillis() - time) + " ms");
	}
}
