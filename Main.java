package cinema;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		boolean is_wednesday = false; //Variabile booleana che valorizzerò a true se il giorno attuale è mercoledì
		boolean valid_date = false; //Variabile booleana che mi servirà a ripetere l'input della data finchè non sarà corretta
		Instant today_date = Instant.now(); //Oggetto Instant che contiene un valore assoluto riferito alla data di sistema
		/*
		 * Oggetto DayOfWeek che contiene la conversione della data di sistema nel nome del giorno della settimana
		 * Viene indicata la Time Zone di sistema con ZoneId.systemDefault(), per consentire una conversione corretta
		 */
		DayOfWeek day_of_week = today_date.atZone(ZoneId.systemDefault()).getDayOfWeek(); 
		Date user_birth_date = new Date(); //Oggetto Date inizializzato vuoto, conterrà la data di nascita inserita dall'utente
		Duration date_difference; //Oggetto Duration, che sarà utilizzato per il calcolo dell'età dell'utente
		/*
		 * Variabile di tipo long che conterrà l'età dell'utente
		 * Uso long, perchè, con date molto distanti tra di loro (possibili, ad esempio, con un utente molto anziano)
		 * potrei andare in overflow di una variabile int, ed ottenere dati errati.
		 * Tra l'altro, anche la classe Duration, quando si calcola un intervallo tra date, ritorna un valore di tipo long
		 */
		long user_age; 
		int prezzo; //Variabile intera che conterrà il prezzo del biglietto
		Scanner keyboard_input = new Scanner(System.in); //Oggetto Scanner per gestire l'input da tastiera
		String user_input = ""; //Stringa in cui inserirò i valori inseriti da tastiera dall'utente
		
		if(day_of_week.toString().equals("WEDNESDAY")) is_wednesday = true; //Se il giorno è mercoledì, imposto is_wednesday a true
		
		System.out.print("Ciao, e benvenuto al cinema!\n"
					   + "Per acquistare i biglietti, ci serve conoscere la tua data di nascita: "); //Stampo questa stringa a video
		
		if(keyboard_input.hasNext()) { //Se l'utente ha digitato qualcosa
			user_input = keyboard_input.nextLine(); //Salvo ciò che è stato digitato dall'utente nella variabile user_input
		}
		
		//Ciclo while: dovrò continuare a ripetere l'inserimento da tastiera finchè la data inserita non sarà valida
		while(!valid_date) {
			/* 
			 * Blocco try/catch: serve per la gestione degli errori. 
			 * Vengono eseguite le istruzioni contenute nel blocco try, e poi, in caso di errore, quelle contenute nel blocco catch
			 * Il blocco catch riceve come parametro l'eccezione sollevata dal programma, nel caso in cui la si volesse gestire in modi
			 * particolari, ad esempio, stampando a video l'errore completo utilizzando l'istruzione e.printStackTrace(), 
			 * oppure scrivendo messaggi di errore personalizzati
			 */
			try { 
				//Salvo la data di nascita dell'utente e la converto da stringa a data
				user_birth_date = new SimpleDateFormat("dd/MM/yyyy").parse(user_input); //Indico in che modo deve essere formattata la data
				
				//Controllo che la data inserita non sia successiva alla data di sistema
				if(user_birth_date.after(Date.from(today_date))) {
					//Se la data inserita è successiva alla data di sistema, avverto l'utente che me ne sono accorto
					System.out.print("Vorresti farmi credere di essere nato nel futuro? Inserisci la tua vera data di nascita: ");
					throw new Exception("BIRTH_DATE_AFTER_SYSTEM_DATE"); //Sollevo un'eccezione per finire nel blocco catch
				} else {
					/*
					 * Siamo nel blocco in cui la conversione della data è andata a buone fine: imposto la variabile valid_date a true
					 * così da poter uscire dal ciclo while
					 */
					valid_date = true; 
				}
			} catch (Exception e) { //Siamo nel blocco in cui la conversione della data è fallita
				/*
				 * Visto che la data non è corretta, chiedo di reinserirla.
				 * Controllo il messaggio dell'eccezione: se non è quella sollevata volutamente
				 * quando la data di nascita è successiva alla data di sistema, stampo a video un messaggio differente
				 */
				if(!e.getMessage().equals("BIRTH_DATE_AFTER_SYSTEM_DATE")) {
					System.out.print("Attenzione: la data inserita non è corretta! Riprova: "); 
				}
				
				if(keyboard_input.hasNext()) {
					user_input = keyboard_input.nextLine(); //Salvo la nuova data inserita dall'utente
				}
			}
		}
		
		keyboard_input.close(); //Chiudo lo stream di input da tastiera
		
		/*
		 * Calcolo la differenza di date tra quella inserita dall'utente, convertita in tipo Instant (user_birth_date.toInstant())
		 * e la data attuale (today_date)
		 * 
		 */
		date_difference = Duration.between(user_birth_date.toInstant(), today_date); 
	
		/* 
		 * Calcolo l'età dell'utente, dividendo il numero di secondi ottenuto prima nel seguente modo:
		 * 3600 (numero di secondi in un'ora)
		 * 24 (numero di ore in un giorno)
		 * 365 (numero di giorni in un anno), ignoro volutamente gli anni bisestili con 366 giorni
		 * poichè, in questo caso, sarebbero una inutile complicazione.
		 * 
		 */
		user_age = date_difference.toSeconds() / 3600 / 24 / 365;
		
		/*
		 * Calcolo il prezzo, nei seguenti modi:
		 * se l'utente ha da 0 a 6 anni, il prezzo sarà pari a 10 euro
		 * se l'utente ha da 7 a 15 anni, il prezzo sarà pari a 12 euro
		 * se l'utente ha più di 15 anni, il prezzo sarà pari a 15 euro
		 * 
		 */
		if(user_age <= 6) { //Se l'età è <= 6
			prezzo = 10; //Assegno il valore 10 alla variabile prezzo
		} else if (user_age > 6 && user_age <= 15) { //Se l'età è > 6 e <= 15
			prezzo = 12; //Assegno il valore 10 alla variabile prezzo
		} else { //Se l'età è > 15
			prezzo = 15; //Assegno il valore 10 alla variabile prezzo
		}
		
		/*
		 * Controllo se è mercoledì: in tal caso applicherò uno sconto!
		 * Non metto is_wednesday == true nella condizione dell'if, poichè non è necessario
		 * Allo stesso modo, per controllare che non sia mercoledì, potrei scrivere !is_wednesday al posto di is_wednesday == false
		 */
		if(is_wednesday) {
			//Se è mercoledì, avviso l'utente della bella notizia...
			System.out.println("Che fortuna! Oggi è mercoledì, e regaliamo 2 euro di sconto a tutti i nostri clienti!");
			/*
			 * E poi, decremento il prezzo
			 * Avrei potuto scrivere prezzo = prezzo - 2, l'espressione prezzo -= 2 è equivalente e più veloce da scrivere
			 * Allo stesso modo, sono equivalenti prezzo = prezzo + 2 e prezzo += 2
			 */
			prezzo -= 2; 
		}
		
		//Avviso l'utente che abbiamo finito di fare i nostri calcoli, e gli dico quanto deve pagare
		System.out.print("Dai nostri calcoli, sembra che tu abbia " + user_age + " anni!\n"
					   + "Il prezzo del tuo biglietto, quindi, è di " + prezzo + "€.\nGrazie, e buona visione!");

	}
}

/*
 * Ci sarebbe stato un modo più semplice per gestire le date, facendo però due chiamate alle API di sistema
 * (cosa che, in questo caso, avrebbe avuto un impatto inesistente sulle prestazioni del programma).
 * 
 * Si sarebbe potuto ricavare il nome del giorno della settimana utilizzando la classe LocalDate, utilizzando la seguente istruzione:
 * DayOfWeek day_of_week = LocalDate.now().getDayOfWeek();
 *  
 * E poi, avrei utilizzato la classe Instant, con l'istruzione Instant.now(), nel momento del calcolo degli anni dell'utente.
 * date_difference = Duration.between(user_birth_date.toInstant(), Instant.now());
 *  
 * Avrei, quindi, interrogato il sistema chiedendogli la data due volte: con LocalDate.now() e con Instant.now()
 * 
 * Ho preferito, quindi, utilizzare solamente la classe Instant ed i suoi metodi, salvandone il valore nella variabile today_date
 * 
 */