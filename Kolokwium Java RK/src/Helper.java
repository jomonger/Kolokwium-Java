import java.io.File;
import java.io.PrintWriter;

import pl.kolokwium.dc.Questionnaire;

public class Helper {
	
	public static void saveQuestionnaireToTxt(Questionnaire q) {
		System.out.println("\nZadanie 4:");
		int temp = 1;
		String name = "Kwestionariusz";
		
		try {// Sprawdzenie dostêpnej nazwy, i utworzenie nowego pliku. Mo¿na stworzyc wiele plików w wielu sesjach.
			File file = new File (name + temp + ".txt");
			while(file.exists()) {
				temp++;
				file = new File (name + temp + ".txt");
			}
			file.createNewFile();
			System.out.println("\nStworzono plik " + name + temp + ".txt.");
			
			PrintWriter wr = new PrintWriter(name + temp + ".txt");
			wr.append(q.getTitle());
			wr.append("\n\n");
			for(int j = 0; j < q.getQuestions().size(); j++) {
				wr.append(q.getQuestions().get(j).getQuestionText());
				wr.append("\n\n");
				for(int k = 0; k < q.getQuestions().get(j).getPossibleAnswers().size(); k++) {
					wr.append((k + 1) + ". ");
					wr.append(q.getQuestions().get(j).getPossibleAnswers().get(k));
					wr.append("\n");
				}
				wr.append("\n");
			}
			wr.append("------------------------------------------------------------------\n");
			wr.close();
			
			System.out.println("Zapisano kwestionariusz do pliku " + name + temp + ".txt.");
			
		} catch (Exception e) {
			System.out.println("Nie uda³o siê wraæ/utworzyæ pliku.");
		} 
	}
}
