package sc.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.api.plugins.IGameState;
import sc.plugin2025.Move;
import sc.plugin2025.Board;
import sc.plugin2025.Card;
import sc.plugin2025.Field;
import sc.plugin2025.GameRuleLogic;
import sc.plugin2025.GameState;
import sc.shared.GameResult;

import java.util.Arrays;
import java.util.List;

public class Logic implements IGameHandler {
  private static final Logger log = LoggerFactory.getLogger(Logic.class);

  private GameState gameState; // AktuellerSpielstatus 

  public static int[] IndexArrFürFeldtyp(Board Board, Field Feld) {
    int arr[] = new int[64];
    int x = 0;

    for (int i = 0; i < arr.length; i++) {
      if (Board.getField(i) == Feld) {
        arr[x] = i;
        x++;
      } else {
        continue;
      }

    }
    int arr1[] = new int[x];
    for (int i = 0; i < x; i++) {
      arr1[i] = arr[i];
    }
    return arr1;
  }

  public Move calculateMove() {
	  
	  
	  

    long startTime = System.currentTimeMillis(); // zum messen der Zeit 

    log.info("Es wurde ein Zug von {} angefordert.", gameState.getCurrentTeam());

    //  Dieser Abschnit ist nur für die bennenung und berechnung von einigen Werten, damit es übersichtlicher wird

    /*-----------------------------------------------------------------------------------------------------------------------------*/
    
    int[] Alle_Hasen = IndexArrFürFeldtyp(gameState.getBoard(), Field.HARE);
    int[] Alle_Igel = IndexArrFürFeldtyp(gameState.getBoard(), Field.HEDGEHOG);
    int[] Alle_Salate = IndexArrFürFeldtyp(gameState.getBoard(), Field.SALAD);
    int[] Alle_Märkte = IndexArrFürFeldtyp(gameState.getBoard(), Field.MARKET);
    int[] Alle_zwei_Felder = IndexArrFürFeldtyp(gameState.getBoard(), Field.POSITION_2);
    int[] Alle_eins_Felder = IndexArrFürFeldtyp(gameState.getBoard(), Field.POSITION_1);
    int[] Alle_Karroten = IndexArrFürFeldtyp(gameState.getBoard(), Field.CARROTS);
    
    int Eigne_Karrotten = gameState.getCurrentPlayer().getCarrots();
    int Gengner_Karroten = gameState.getOtherPlayer().getCarrots();
    int Eigne_Salate = gameState.getCurrentPlayer().getSalads();
    int Eigne_Position = gameState.getCurrentPlayer().getPosition();
    int Gegner_Position = gameState.getOtherPlayer().getPosition();

    int Nächter_Markt = gameState.getBoard().getNextField(Field.MARKET, Eigne_Position) != null ? gameState.getBoard().getNextField(Field.MARKET, Eigne_Position) : 0;
    int Nächter_Salat = gameState.getBoard().getNextField(Field.SALAD, Eigne_Position) != null ? gameState.getBoard().getNextField(Field.SALAD, Eigne_Position) : 0;
    int Nächter_Hase = gameState.getBoard().getNextField(Field.HARE, Eigne_Position) != null ? gameState.getBoard().getNextField(Field.HARE, Eigne_Position) : 0;
    int Nächter_Igel = gameState.getBoard().getNextField(Field.HEDGEHOG, Eigne_Position) != null ? gameState.getBoard().getNextField(Field.HEDGEHOG, Eigne_Position) : 0;
    int Nächstes_zwei_Feld = gameState.getBoard().getNextField(Field.POSITION_2, Eigne_Position) != null ? gameState.getBoard().getNextField(Field.POSITION_2, Eigne_Position) : 0;
    int Nächstes_eins_Feld = gameState.getBoard().getNextField(Field.POSITION_1, Eigne_Position) != null ? gameState.getBoard().getNextField(Field.POSITION_1, Eigne_Position) : 0;
    int Nächste_Karrote = gameState.getBoard().getNextField(Field.CARROTS, Eigne_Position) != null ? gameState.getBoard().getNextField(Field.CARROTS, Eigne_Position) : 0;
    
    int Letzter_Markt = Alle_Märkte[Alle_Märkte.length - 1];
    int Letzter_Hase = Alle_Hasen[Alle_Hasen.length - 1];
    int Letzter_Igel = Alle_Igel[Alle_Igel.length - 1];
    int Letzter_Salat = Alle_Salate[Alle_Salate.length - 1];

    int Dist_eins_Feld = Nächstes_eins_Feld - Eigne_Position;
    int Dist_zwei_Feld = Nächstes_zwei_Feld - Eigne_Position;
    int Dist_Hase = Nächter_Hase - Eigne_Position;
    int Dist_Salat = Nächter_Salat - Eigne_Position;
    int Dist_Markt = Nächter_Markt - Eigne_Position;
    int Dist_Ziel = 64 - Eigne_Position;
    int Dist_vor_Ziel = 63 - Eigne_Position;
    int Dist_Karrote = Nächste_Karrote - Eigne_Position;
    int Dist = GameRuleLogic.INSTANCE.calculateMoveableFields(Eigne_Karrotten);

    Field Eigne_FeldTyp = gameState.getBoard().getField(gameState.getCurrentPlayer().getPosition());
    Field Gegner_FeldTyp = gameState.getBoard().getField(gameState.getOtherPlayer().getPosition());

    String Gespielte_Schleife = "nichts";

    // Eigene-Karten-Typen berechnen
    int[] Karten_Typ = new int[4];
    for (Object karte: gameState.getCurrentPlayer().getCards()) {
      switch (karte.toString()) {
      case "EAT_SALAD" -> Karten_Typ[0]++;
      case "SWAP_CARROTS" -> Karten_Typ[1]++;
      case "HURRY_AHEAD" -> Karten_Typ[2]++;
      case "FALL_BACK" -> Karten_Typ[3]++;
      }
    }
    // Gegner-Karten-Typen berechnen
    int[] Gegner_Karten_Typ = new int[4];
    for (Object karte: gameState.getOtherPlayer().getCards()) {
      switch (karte.toString()) {
      case "EAT_SALAD" -> Gegner_Karten_Typ[0]++;
      case "SWAP_CARROTS" -> Gegner_Karten_Typ[1]++;
      case "HURRY_AHEAD" -> Gegner_Karten_Typ[2]++;
      case "FALL_BACK" -> Gegner_Karten_Typ[3]++;
      }
    }

    int Karten_ings = Karten_Typ[0] + Karten_Typ[1] + Karten_Typ[2] + Karten_Typ[3];

    int Gegner_Karten_ings = Gegner_Karten_Typ[0] + Gegner_Karten_Typ[1] + Gegner_Karten_Typ[2] + Gegner_Karten_Typ[3];

    boolean Kartenspielbarkeit = false;

    int KartenZahl = 0;

    if (Karten_ings != 0) {

      if (Karten_Typ[0] != 0 && Eigne_Salate != 0) {
        Kartenspielbarkeit = true;
        KartenZahl = KartenZahl + 1;
      }
      if (Karten_Typ[1] != 0 && Gegner_Position < Letzter_Salat && Eigne_Position < Letzter_Salat && Nächter_Hase < Letzter_Salat) {
        Kartenspielbarkeit = true;
        KartenZahl = KartenZahl + 1;
      }
      if (Karten_Typ[2] != 0 && Eigne_Position < Gegner_Position) {
        Kartenspielbarkeit = true;
        KartenZahl = KartenZahl + 1;
      }
      if (Karten_Typ[3] != 0 && Eigne_Position > Gegner_Position) {
        Kartenspielbarkeit = true;
        KartenZahl = KartenZahl + 1;
      }
    }

    
    List < Move > Mögliche_Züge = gameState.getSensibleMoves();
    int Move = 0;
    int Anzahl_Mögliche_Züge = Mögliche_Züge.size();
   
    /*-------------------------------------------------------------------------------------------------------------------------------------*/

    // Ab hier beginnt die eigendlich berechnug des Zuges 

    int Nächstes_Feld = 0;
    int Feld_Dist = 0;
    if (Dist_Ziel <= Dist && Gegner_Position != 64 && (Eigne_Karrotten - GameRuleLogic.INSTANCE.calculateCarrots(Dist_Ziel)) <= 10 && Eigne_Salate == 0) {

        Gespielte_Schleife = "Ziel Funktion";

        Nächstes_Feld = 64;

        Feld_Dist = Dist_Ziel;
      }
    if (Gespielte_Schleife == "nichts" && Dist_vor_Ziel <= Dist && Gegner_Position != 63 && (Eigne_Karrotten - GameRuleLogic.INSTANCE.calculateCarrots(Dist_vor_Ziel)) > 10 && Eigne_Salate == 0) {

        Gespielte_Schleife = "Vor Ziel Funktion";

        Nächstes_Feld = 63;

        Feld_Dist = Dist_vor_Ziel;
      }
    if (Gespielte_Schleife == "nichts" && Dist_Salat < Dist && Gegner_Position != Nächter_Salat && Eigne_Salate != 0 && Nächter_Salat > 0) {

        Gespielte_Schleife = "Salat Funktion";

        Nächstes_Feld = Nächter_Salat;

        Feld_Dist = Dist_Salat;
    }
    if (Gespielte_Schleife == "nichts" && Dist_Markt < Dist && Gegner_Position != Nächter_Markt && Nächter_Markt < Nächter_Salat && Nächter_Markt > 0) {

        Gespielte_Schleife = "Markt Funktion";

        Nächstes_Feld = Nächter_Markt;

        Feld_Dist = Dist_Markt;
    }
    if (Gespielte_Schleife == "nichts"  && Dist_Hase < Dist && Gegner_Position != Nächter_Hase  && Nächter_Hase > 0 &&
        ((Gegner_Karten_Typ[1] != Karten_Typ[1] && Karten_Typ[1] != 0 && Gegner_Position > 55 && Gengner_Karroten + 100  > Eigne_Karrotten) || (Eigne_Salate != 0 && Karten_Typ[0] != 0))) {

        Gespielte_Schleife = "Hasen Funktion";

        Nächstes_Feld = Nächter_Hase;

        Feld_Dist = Dist_Hase;
      }
    if (Gespielte_Schleife == "nichts" && Dist_eins_Feld < Dist && Gegner_Position != Nächstes_eins_Feld && Eigne_Position > Gegner_Position && Nächstes_eins_Feld > Gegner_Position && Nächstes_eins_Feld > 0 && Nächstes_eins_Feld < Nächter_Salat) {

        Gespielte_Schleife = "Erste Position Funktion";

        Nächstes_Feld = Nächstes_eins_Feld;

        Feld_Dist = Dist_eins_Feld;
      }
      if (Gespielte_Schleife == "nichts"  && Dist_zwei_Feld < Dist && Gegner_Position != Nächstes_zwei_Feld && Eigne_Position < Gegner_Position && Nächstes_zwei_Feld < Gegner_Position && Nächstes_zwei_Feld > 0) {

        Gespielte_Schleife = "Zweite Position Funktion";

        Nächstes_Feld = Nächstes_zwei_Feld;

        Feld_Dist = Dist_zwei_Feld;
      }
      if (Gespielte_Schleife == "nichts" && Eigne_Salate != 0 && Eigne_Position < 57 && Eigne_Karrotten <= 20 && gameState.getBoard().getPreviousField(Field.HEDGEHOG, Eigne_Position) != null) {

          Gespielte_Schleife = "Igel Funktion";

      }
      if (Dist_Karrote <= Dist &&  Gegner_Position != Nächste_Karrote && Gespielte_Schleife == "nichts" && Eigne_Position != 63) {

         Gespielte_Schleife = "Karrotten Funktion";
         
         Nächstes_Feld = Nächste_Karrote;

         Feld_Dist = Dist_Karrote;
      }
      
      if ( Gespielte_Schleife == "Igel Funktion") {
      	
      	if (Eigne_FeldTyp != Field.CARROTS) {
      		Move = Anzahl_Mögliche_Züge - 1;
      	}
        if (Eigne_FeldTyp == Field.CARROTS) {
          	Move = Anzahl_Mögliche_Züge - 2;	
      	}
      	
      } 
      if (Eigne_Position == 63 && Gespielte_Schleife != "Ziel Funktion" && Eigne_Salate == 0) {
          Move = 2;
          Gespielte_Schleife = "Karrotten abgeben Funktion";
        }
     
    if (Gespielte_Schleife != "nichts" && Gespielte_Schleife != "Igel Funktion" && Gespielte_Schleife != "Karrotten abgeben Funktion") {
      int Nicht_begebare_Felder = 0;
      Move = 0;

      for (int i = 0; i < Alle_Igel.length; i++) {
          if (Alle_Igel[i] > Eigne_Position && Alle_Igel[i] < Nächstes_Feld) {
          Nicht_begebare_Felder++;
          }
          
          else {
          continue;
          }
        }
        for (int i = 0; i < Alle_Hasen.length; i++) {
          if (Alle_Hasen[i] > Eigne_Position && Alle_Hasen[i] < Nächstes_Feld && Kartenspielbarkeit == false && Nächstes_Feld != Nächter_Hase ) {
          Nicht_begebare_Felder++;
          }
          if (Alle_Hasen[i] > Eigne_Position && Alle_Hasen[i] < Nächstes_Feld && Kartenspielbarkeit == true && Nächstes_Feld != Nächter_Hase ) {
          Nicht_begebare_Felder = Nicht_begebare_Felder - (KartenZahl - 1) ;
          }

          else {
          continue;
          }
        }
        for (int i = 0; i < Alle_Salate.length; i++) { 
          if (Alle_Salate[i] > Eigne_Position && Alle_Salate[i] < Nächstes_Feld && Eigne_Salate == 0 && Nächstes_Feld != Nächter_Salat && Eigne_Position != Nächter_Salat) {
          Nicht_begebare_Felder++;
          }

          else {
          continue;
          }
        }
        for (int i = 0; i < Alle_Märkte.length; i++) { 
  	      if (Alle_Märkte[i] > Eigne_Position && Alle_Märkte[i] < Nächstes_Feld && Nächstes_Feld != Nächter_Markt) {
  	      Nicht_begebare_Felder = Nicht_begebare_Felder - 3;
  	      }
  	
  	      else {
  	      continue;
  	        }
  	      }
      int Karte = 0;

        if (Nächstes_Feld == Nächter_Hase && Gegner_Karten_Typ[1] != Karten_Typ[1] && Karten_Typ[1] != 0 && Gegner_Position > 57 && Gengner_Karroten + 100  > Eigne_Karrotten
          ||Nächstes_Feld == Nächter_Markt && (Gegner_Karten_Typ[1] >= Karten_Typ[1] || Karten_Typ[1] == 0 )) {
          Karte = 1; // Karroten tauschen
        }

        if (Nächstes_Feld == Nächter_Hase && Eigne_Salate != 0 && Karten_Typ[0] != 0 || Nächstes_Feld == Nächter_Markt &&  Gegner_Karten_Typ[1] < Karten_Typ[1] && Karten_Typ[1] != 0 && Eigne_Salate != 0) {
          Karte = 0; //Saladfress 	 
        }
        
      if (Gegner_Position < Nächstes_Feld && Eigne_Position < Gegner_Position && Gegner_FeldTyp != Field.HEDGEHOG && Gegner_FeldTyp != Field.HARE) {
          Nicht_begebare_Felder++;
      }

      Move = Feld_Dist - Nicht_begebare_Felder + Karte - 1;
    }
    
    while (Anzahl_Mögliche_Züge < Move) {

      Move = Anzahl_Mögliche_Züge - 1;
      Gespielte_Schleife = "Notfall Funktion";
    }
    if (gameState.mustEatSalad(gameState.getCurrentPlayer()) == true) {

      // Für den Fall das wir auf einem Salatfeld sind muss immer "Move = 0" gesetzt werde da es sonst zu einem Fehler kommt 

      Gespielte_Schleife = "Salatfressen Funktion ";

      Move = 0;

    }

   
    
    Move move = Mögliche_Züge.get(Move); // hier wird für die berechnung des Zuges "Move" eingesetzt und dann aus der Liste an möglichen Zügen ausgweählt 

    // Der folgende Abschnnit dient dazu um in der Konsole zu sehen was durchgeführt wird:

    /*--------------------------------------------------------------------------------------------------------*/

    log.info("Sende {} nach {}ms.", move, System.currentTimeMillis() - startTime);

    System.out.println("________________________________________________________________________" + "\n");
    System.out.println("\033[1;36mSpielrunde: " + gameState.getTurn() + "\u001B[0m | Menge an Karrotten: " + Eigne_Karrotten + "\n");
    System.out.println("Nächster Markt auf Position: " + Nächter_Markt + "\n");
    System.out.println("Mögliche Züge: (gesammt: " + Anzahl_Mögliche_Züge + ") " + Mögliche_Züge + "\n");
    System.out.println("Gespielter Zug: " + move + " ; " + Move + "\n");
    System.out.println("Eigendes Feld: " + Eigne_FeldTyp + " auf Position: " + Eigne_Position + "\n");
    System.out.println("Distans zum Markt: " + Nächter_Salat + " | " + "Begebare Felder: " + Dist + "\n");
    System.out.println("Gespielte Schleife: \033[1;91m" + Gespielte_Schleife + "\u001B[0m\n\n");
    System.out.println("Kartenspielbarkeit: " + Kartenspielbarkeit + "\n");
    System.out.println("Karten =  " + "Saladfressen: " + Karten_Typ[0] + " | " + "Karotten tauschen: " + Karten_Typ[1] + " | " + "Zurückfallen: " + Karten_Typ[2] + " | " + "Vorrücken: " + Karten_Typ[3] + "\n");
    System.out.println("Karten-Gegner =  " + "Saladfressen: " + Gegner_Karten_Typ[0] + " | " + "Karotten tauschen: " + Gegner_Karten_Typ[1] + " | " + "Zurückfallen: " + Gegner_Karten_Typ[2] + " | " + "Vorrücken: " + Gegner_Karten_Typ[3] + "\n");
    System.out.println("________________________________________________________________________");

    /*---------------------------------------------------------------------------------------------------------*/

    return move;

  }

  @Override

  public void onUpdate(IGameState gameState) {

    this.gameState = (GameState) gameState;
    log.info("Zug: {} Dran: {}", gameState.getTurn(), gameState.getCurrentTeam());

  }

  public void onGameOver(GameResult data) { //Wird aufgerufen, wenn das Spiel beendet ist.

    log.info("Das Spiel ist beendet, Ergebnis: {}", data);

  }

  @Override

  public void onError(String error) { //Wird aufgerufen, wenn der Server einen Fehler meldet. Bedeutet auch den Abbruch des Spiels. 

    log.warn("Fehler: {}", error);

  }

}
