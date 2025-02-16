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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class Logic implements IGameHandler {
	private static final Logger log = LoggerFactory.getLogger(Logic.class);

	private GameState gameState; // AktuellerSpielstatus
	
    private long totalCalculationTime = 0;

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

	// Gibt die Weiten der Züge in einem Array zurück. Die Züge werden dabei am
	// Anfang als Liste in die Methode übergeben.
	public static int[] Zugweite(List<Move> Listmoves, GameState game) {
		int arr[] = new int[Listmoves.size()];
		int i = 0;
		for (Move move : Listmoves) {
			GameState clone = game.clone();
			clone.performMoveDirectly(move);
			arr[i] = clone.getOtherPlayer().getPosition() - game.getCurrentPlayer().getPosition();
			i++;
		}
		return arr;
	}

	public int[] Karten(List<Card> PlayerCards) {
		int[] Karten_Typ = new int[4];
		for (Object karte : PlayerCards) {
			switch (karte.toString()) {
			case "EAT_SALAD" -> Karten_Typ[0]++;
			case "SWAP_CARROTS" -> Karten_Typ[1]++;
			case "HURRY_AHEAD" -> Karten_Typ[2]++;
			case "FALL_BACK" -> Karten_Typ[3]++;
			}
		}
		return Karten_Typ;
	}
	
	public String[] VorherigeZüge(String[] Züge) {
		
		String[] VorherigeZüge = new String[Züge.length];
		 for (int i = 0; i < VorherigeZüge.length; i++) {	 
			 Züge[i] = VorherigeZüge[i];
		 }
		return VorherigeZüge;
	}

	public boolean[] Condition(int Eigene_Position, int Eigene_Salate, int Eigene_Karrotten, int SWAP_CARROTS, int EAT_SALAD ,int[] GegnerKarten, String VorherigerZug) {

		int Gegner_Position = gameState.getOtherPlayer().getPosition();
		int Gengner_Karroten = gameState.getOtherPlayer().getCarrots();

	    int Nächter_Markt = gameState.getBoard().getNextField(Field.MARKET, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.MARKET, Eigene_Position) : 0;
	    int Nächter_Salat = gameState.getBoard().getNextField(Field.SALAD, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.SALAD, Eigene_Position) : 0;
	    int Nächter_Hase = gameState.getBoard().getNextField(Field.HARE, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.HARE, Eigene_Position) : 0;
	    int Nächter_Igel = gameState.getBoard().getNextField(Field.HEDGEHOG, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.HEDGEHOG, Eigene_Position) : 0;
	    int Vorheriger_Igel = gameState.getBoard().getPreviousField(Field.HEDGEHOG, Eigene_Position) != null ? gameState.getBoard().getPreviousField(Field.HEDGEHOG, Eigene_Position) : 0;
	    int Nächstes_zwei_Feld = gameState.getBoard().getNextField(Field.POSITION_2, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.POSITION_2, Eigene_Position) : 0;
	    int Nächstes_eins_Feld = gameState.getBoard().getNextField(Field.POSITION_1, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.POSITION_1, Eigene_Position) : 0;
	    int Nächste_Karrote = gameState.getBoard().getNextField(Field.CARROTS, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.CARROTS, Eigene_Position) : 0;
	
	    int Dist_eins_Feld = Nächstes_eins_Feld - Eigene_Position;
	    int Dist_zwei_Feld = Nächstes_zwei_Feld - Eigene_Position;
	    int Dist_Hase = Nächter_Hase - Eigene_Position;
	    int Dist_Salat = Nächter_Salat - Eigene_Position;
	    int Dist_Markt = Nächter_Markt - Eigene_Position;
	    int Dist_Ziel = 64 - Eigene_Position;
	    int Dist_vor_Ziel = 63 - Eigene_Position;
	    int Dist_Karrote = Nächste_Karrote - Eigene_Position;
	    int Dist_Igel = Vorheriger_Igel - Eigene_Position ;
	    int Eigene_Dist = GameRuleLogic.INSTANCE.calculateMoveableFields(Eigene_Karrotten); 
	    
	    Field FeldTyp = gameState.getBoard().getField(Eigene_Position);
	   
		boolean[] Condition = new boolean[13];
	    Condition[0] = FeldTyp == Field.SALAD && VorherigerZug != "Salatfressen Funktion " ;
	    if ( VorherigerZug == "nichts") {
	    	Condition[0]= gameState.mustEatSalad(gameState.getCurrentPlayer());
	    }
	    if (Condition[0] == false) {
	    Condition[1] = Dist_Ziel <= Eigene_Dist && Gegner_Position != 64 && (Eigene_Karrotten - GameRuleLogic.INSTANCE.calculateCarrots(Dist_Ziel)) <= 10 && Eigene_Salate == 0;
	    Condition[2] = Eigene_Position == 63 && Eigene_Salate == 0 && (Eigene_Karrotten - GameRuleLogic.INSTANCE.calculateCarrots(Dist_Ziel)) > 10;
	    Condition[3] = Eigene_Position != 63 && Dist_vor_Ziel <= Eigene_Dist && Gegner_Position != 63 && (Eigene_Karrotten - GameRuleLogic.INSTANCE.calculateCarrots(Dist_vor_Ziel)) > 10 && Eigene_Salate == 0;
	    Condition[4] = Dist_Salat < Eigene_Dist && Gegner_Position != Nächter_Salat && Eigene_Salate != 0 && Nächter_Salat > 0;
	    Condition[5] = Dist_Markt < Eigene_Dist && Gegner_Position != Nächter_Markt && Nächter_Markt > 0 && (Eigene_Karrotten - GameRuleLogic.INSTANCE.calculateCarrots(Dist_Markt)) >= 10 &&
	                   ((GegnerKarten[1] >= SWAP_CARROTS || SWAP_CARROTS == 0 || GegnerKarten[1] <SWAP_CARROTS && SWAP_CARROTS != 0 && Eigene_Salate != 0 && SWAP_CARROTS + 1 <= Eigene_Salate));
	    Condition[6] = Dist_Hase < Eigene_Dist && Gegner_Position != Nächter_Hase  && Nächter_Hase > 0 &&
	    		       ((GegnerKarten[1] >= SWAP_CARROTS && SWAP_CARROTS != 0 && Gegner_Position > 55 && Gengner_Karroten + 100  > Eigene_Karrotten) || (Eigene_Salate != 0 && EAT_SALAD != 0));
	    Condition[7] = Dist_eins_Feld < Eigene_Dist && Gegner_Position != Nächstes_eins_Feld && Eigene_Position > Gegner_Position &&
	    			   Nächstes_eins_Feld > Gegner_Position && Nächstes_eins_Feld > 0;
	    Condition[8] = Dist_zwei_Feld < Eigene_Dist && Gegner_Position != Nächstes_zwei_Feld && Eigene_Position < Gegner_Position && Nächstes_zwei_Feld < Gegner_Position && Nächstes_zwei_Feld > 0;
	    Condition[9] = Eigene_Salate != 0 && Eigene_Position < 57 && Eigene_Karrotten <= 20 && gameState.getBoard().getPreviousField(Field.HEDGEHOG, Eigene_Position) != null;
	    Condition[10] = Dist_Karrote <= Eigene_Dist &&  Gegner_Position != Nächste_Karrote && Eigene_Position != 63;
	    }
	    return Condition;
	   }
	
   public int [] Dist(int Eigene_Position) {
	   
	    int Nächter_Markt = gameState.getBoard().getNextField(Field.MARKET, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.MARKET, Eigene_Position) : 0;
	    int Nächter_Salat = gameState.getBoard().getNextField(Field.SALAD, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.SALAD, Eigene_Position) : 0;
	    int Nächter_Hase = gameState.getBoard().getNextField(Field.HARE, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.HARE, Eigene_Position) : 0;
	    int Nächter_Igel = gameState.getBoard().getNextField(Field.HEDGEHOG, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.HEDGEHOG, Eigene_Position) : 0;
	    int Vorheriger_Igel = gameState.getBoard().getPreviousField(Field.HEDGEHOG, Eigene_Position) != null ? gameState.getBoard().getPreviousField(Field.HEDGEHOG, Eigene_Position) : 0;
	    int Nächstes_zwei_Feld = gameState.getBoard().getNextField(Field.POSITION_2, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.POSITION_2, Eigene_Position) : 0;
	    int Nächstes_eins_Feld = gameState.getBoard().getNextField(Field.POSITION_1, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.POSITION_1, Eigene_Position) : 0;
	    int Nächste_Karrote = gameState.getBoard().getNextField(Field.CARROTS, Eigene_Position) != null ? gameState.getBoard().getNextField(Field.CARROTS, Eigene_Position) : 0;
	
	    int Dist_eins_Feld = Nächstes_eins_Feld - Eigene_Position;
	    int Dist_zwei_Feld = Nächstes_zwei_Feld - Eigene_Position;
	    int Dist_Hase = Nächter_Hase - Eigene_Position;
	    int Dist_Salat = Nächter_Salat - Eigene_Position;
	    int Dist_Markt = Nächter_Markt - Eigene_Position;
	    int Dist_Ziel = 64 - Eigene_Position;
	    int Dist_vor_Ziel = 63 - Eigene_Position;
	    int Dist_Karrote = Nächste_Karrote - Eigene_Position;
	    int Dist_Igel = Vorheriger_Igel - Eigene_Position ;
	    
	    int[] Dist = {0, Dist_Ziel, 0 , Dist_vor_Ziel, Dist_Salat, Dist_Markt, Dist_Hase, Dist_eins_Feld, Dist_zwei_Feld, Dist_Igel , Dist_Karrote} ;
		
	    return Dist;
   }
   public String[] Schleife = {"Salatfressen Funktion ", "Ziel Funktion", "Karrotten abgeben Funktion", "Vor Ziel Funktion", "Salat Funktion", "Markt Funktion", "Hasen Funktion", 
                              "Erste Position Funktion", "Zweite Position Funktion", "Igel Funktion", "Karrotten Funktion"};
   
   public int[] Werteveränderung(int Eigene_Position, int Eigene_Salate, int Eigene_Karrotten, int SWAP_CARROTS, int EAT_SALAD, String Zug ){
	   
	  int Gegner_Position = gameState.getOtherPlayer().getPosition();
	  int Gengner_Karroten = gameState.getOtherPlayer().getCarrots();
	  int[] GegnerKarten  = Karten(gameState.getOtherPlayer().getCards());
	  int [] Dist = Dist(Eigene_Position);
	  int Dist_Feld =0;
	  for (int i = 0; i < Schleife.length; i++) {
	    	if (Schleife[i] == Zug ) {
	    	Dist_Feld = Dist[i];
			break;
	    	}
	    	else {
	    	continue;
		    }	
	      }
	  
	  switch(Zug)  {
	  
	      case "Ziel Funktion", "Vor Ziel Funktion", "Salat Funktion", "Erste Position Funktion", "Zweite Position Funktion", "Karrotten Funktion":{
	          Eigene_Position = Eigene_Position + Dist_Feld;
	          Eigene_Karrotten = Eigene_Karrotten - GameRuleLogic.INSTANCE.calculateCarrots(Dist_Feld);
	          break;
	      }
	      case "Markt Funktion" :{
	          Eigene_Position = Eigene_Position + Dist_Feld;
	          Eigene_Karrotten = Eigene_Karrotten - GameRuleLogic.INSTANCE.calculateCarrots(Dist_Feld) - 10;
	          if (GegnerKarten[1] >= SWAP_CARROTS || SWAP_CARROTS == 0 ){
	        	  SWAP_CARROTS++;
	          }

	          if (GegnerKarten[1] < EAT_SALAD && EAT_SALAD != 0 && Eigene_Salate != 0) {
	        	  EAT_SALAD++; 
	          }
	          break;
	      }
	      case "Hasen Funktion" :{
	          Eigene_Position = Eigene_Position + Dist_Feld;
	          Eigene_Karrotten = Eigene_Karrotten - GameRuleLogic.INSTANCE.calculateCarrots(Dist_Feld) - 10;
	          if ( GegnerKarten[1] != SWAP_CARROTS && SWAP_CARROTS != 0 && Gegner_Position > 57 && Gengner_Karroten + 100  > Eigene_Karrotten){
	        	  SWAP_CARROTS--;
	              Eigene_Karrotten = Gengner_Karroten;
	          }

	          if (Eigene_Salate != 0 && EAT_SALAD != 0) {
	        	  EAT_SALAD--; 
	              Eigene_Salate--;
	              Eigene_Salate--;
		          if(Eigene_Position > Gegner_Position ) {
	              Eigene_Karrotten = Eigene_Karrotten + 10;
		          }
		          if(Eigene_Position > Gegner_Position ) {
	              Eigene_Karrotten = Eigene_Karrotten + 30;
		          }
	          }
	          break;
	      }

	      case "Salatfressen Funktion " :{
	          Eigene_Salate--;
	          if(Eigene_Position > Gegner_Position ) {
              Eigene_Karrotten = Eigene_Karrotten + 10;
	          }
	          if(Eigene_Position > Gegner_Position ) {
              Eigene_Karrotten = Eigene_Karrotten + 30;
	          }
	          break;
	      }
	      case "Igel Funktion" :{
	          Eigene_Position = Eigene_Position - Dist_Feld;
	          Eigene_Karrotten = Eigene_Karrotten + 10 * Dist_Feld;
	          break;
	      }
	      case "Karrotten abgeben Funktion":{
	          Eigene_Karrotten = Eigene_Karrotten - 10 ;
	          break;
	      }
	     }
	  
	  int[] Werteveränderung =  new int[5];
	  Werteveränderung[0] = Eigene_Position;
	  Werteveränderung[1] = Eigene_Salate;
	  Werteveränderung[2] = Eigene_Karrotten;
      Werteveränderung[3] = SWAP_CARROTS;
      Werteveränderung[4] = EAT_SALAD;
	  
	return Werteveränderung;
	 }
   
   public String[] ZugZuortnung(boolean[] Condition) {
	   
	   String[] Platzhalter = new  String[Condition.length];
	    int Mögliche_Züge_int = 0;
	    for (int i = 0; i < Condition.length; i++) {
	        if (Condition[i] == true) {
	        Platzhalter[Mögliche_Züge_int] = Schleife[i];
	        Mögliche_Züge_int++;
	        }
	        else {
	        continue;
	        }
	     }
	    String[] Mögliche_Züge = new  String[Mögliche_Züge_int];
	    for (int i = 0; i < Mögliche_Züge_int; i++) {
	         Mögliche_Züge[i] = Platzhalter[i];
	     }
	   return Mögliche_Züge;
   }
   
	  
   
   public String[][] ZugMöglichkeiten(int MengeAnWiederholungen){

	    int Eigene_Karrotten = gameState.getCurrentPlayer().getCarrots();
	    int Gengner_Karroten = gameState.getOtherPlayer().getCarrots();
	    int Eigene_Salate = gameState.getCurrentPlayer().getSalads();
	    int Gegner_Position = gameState.getOtherPlayer().getPosition(); 
	    int Eigene_Position = gameState.getCurrentPlayer().getPosition();
	    int[] Dist = Dist(Eigene_Position);
	    int Dist_Feld = 0;
	    int[] Karten  = Karten(gameState.getCurrentPlayer().getCards());
	    int[] GegnerKarten  = Karten(gameState.getOtherPlayer().getCards());
	    boolean[]Condition = Condition(Eigene_Position, Eigene_Salate, Eigene_Karrotten, Karten[1], Karten[0] , GegnerKarten, "nichts");
	    String[][] Platzhalter2 = new String[1000][MengeAnWiederholungen];
	    String[] Mögliche_Züge2 = null;
	    int Mögliche_Züge_int1 = 0;
	    String[] Mögliche_Züge1 = null;
	    int Mögliche_Züge_int2 = 0;
	    String[] Mögliche_Züge3 = null;
	    String[] Mögliche_Züge4 = null;
	    String[]  Mögliche_Züge = ZugZuortnung(Condition);
	    int e = 0;
	    
	    for(int i = 0 ; i < Mögliche_Züge.length ; i++){  
	    	
        int[] Werte = Werteveränderung(Eigene_Position,  Eigene_Salate,  Eigene_Karrotten, Karten[1], Karten[2], Mögliche_Züge[i] );
	    boolean[]Condition1 = Condition(Werte[0], Werte[1], Werte[2], Werte[3], Werte[4], GegnerKarten, Mögliche_Züge[i]);
	    Mögliche_Züge1 = ZugZuortnung(Condition1);

		    for (int f = 0; f <  Mögliche_Züge1.length; f++) {
		    	
		    int[] Werte1 = Werteveränderung(Werte[0],  Werte[1],  Werte[2], Werte[3], Werte[4], Mögliche_Züge1[f]);
			boolean[]Condition2 = Condition(Werte1[0], Werte1[1], Werte1[2], Werte1[3], Werte1[4], GegnerKarten, Mögliche_Züge1[f] );
			Mögliche_Züge2 = ZugZuortnung(Condition2);
			
			for (int b = 0; b < Mögliche_Züge2.length; b ++) {
				
			    int[] Werte2 = Werteveränderung(Werte1[0],  Werte1[1],  Werte1[2], Werte1[3], Werte1[4], Mögliche_Züge2[b]);
				boolean[]Condition3 = Condition(Werte2[0], Werte2[1], Werte2[2], Werte2[3], Werte2[4], GegnerKarten,  Mögliche_Züge2[b]);
				Mögliche_Züge3 = ZugZuortnung(Condition3);
				
				for (int k = 0; k < Mögliche_Züge3.length; k ++ ) {
						
				    int[] Werte3 = Werteveränderung(Werte2[0],  Werte2[1],  Werte2[2], Werte2[3], Werte2[4], Mögliche_Züge3[k]);
					boolean[]Condition4 = Condition(Werte3[0], Werte3[1], Werte3[2], Werte3[3], Werte3[4], GegnerKarten, Mögliche_Züge3[k]);
					Mögliche_Züge4 = ZugZuortnung(Condition4);
						
					for (int h = 0; h < Mögliche_Züge4.length; h ++ ,e++) {
			            Platzhalter2 [e][0] = Mögliche_Züge[i];
			            Platzhalter2 [e][1] = Mögliche_Züge1[f] ;
			            Platzhalter2 [e][2] = Mögliche_Züge2[b];
			            Platzhalter2 [e][3] = Mögliche_Züge3[k];
			            Platzhalter2 [e][4] = Mögliche_Züge4[h];
			  }	
		     }	
			}
           }
	      }
	    
	    String[][] Alle_Möglichkeiten = new String [e][5];
	    for (int i = 0; i < e; i++){
	       for (int j = 0; j < 5; j++){
	       Alle_Möglichkeiten [i][j] = Platzhalter2 [i][j];
	   }
	  }
	    
		return Alle_Möglichkeiten;
	    
	  }	
   
   public float Floatwerte_Anpassung (int[] VorherigeWerte, int [] AktuelleWerte, float Floatwert, String Zug){
	
	   float Postion = 0.0f;
	   
	  switch(Zug){ 
	  
	  case "Ziel Funktion","Vor Ziel Funktion", "Salat Funktion", "Markt Funktion", "Hasen Funktion", "Erste Position Funktion", "Zweite Position Funktion", "Karrotten Funktion":{
	  
	   }
	  case "Karrotten abgeben Funktion":{
	 
	   }
	  case "Salatfressen Funktion ":{
			 
	   }
	  case "Igel Funktion":{
			 
	   }
	}

	  Floatwert = Floatwert + Postion;
	   return Floatwert; 
   }
   
   public float[][] Floatwerte( String[][] Züge ) {
	    int Eigene_Karrotten = gameState.getCurrentPlayer().getCarrots();
	    int Eigene_Salate = gameState.getCurrentPlayer().getSalads(); 
	    int Eigene_Position = gameState.getCurrentPlayer().getPosition();
	    int[] Karten  = Karten(gameState.getCurrentPlayer().getCards());
	    
	   int[] Werte0 = {Eigene_Position,  Eigene_Salate,  Eigene_Karrotten, Karten[1], Karten[2]};

	   float[][]Floatwerte = new float[Züge.length][6];
	   float Floatwert = 0.0f;    
	   String Zug = "nichts";
	   
	   
	   for (int i = 0; i < Züge.length; i++){
	       for (int j = 0; j < Züge[i].length ; j++){
	    	   Zug = Züge [i][j];
	    	   switch(Zug)  {
	    	   case "Salatfressen Funktion " : Floatwert = 0.1f;
	    	   case "Ziel Funktion": Floatwert = 0.1f;
	    	   case "Karrotten abgeben Funktion": Floatwert = 0.1f;
	    	   case "Vor Ziel Funktion": Floatwert = 0.1f;
	    	   case "Salat Funktion": Floatwert = 0.1f;
	    	   case "Markt Funktion": Floatwert = 0.1f;
	    	   case "Hasen Funktion": Floatwert = 0.1f;
	    	   case "Erste Position Funktion": Floatwert = 0.1f;
	    	   case "Zweite Position Funktion": Floatwert = 0.1f;
	    	   case "Igel Funktion": Floatwert = 0.1f;
	    	   case "Karrotten Funktion": Floatwert = 0.1f;  
	    	   
	 	     }
	    	 Floatwerte [i][j] = Floatwert;
	    	 
	       }
	       int[] Werte = Werteveränderung(Eigene_Position,  Eigene_Salate,  Eigene_Karrotten, Karten[1], Karten[2],Züge [i][0]);
	       Floatwerte [i][0] = Floatwerte_Anpassung (Werte0, Werteveränderung(Eigene_Position,  Eigene_Salate,  Eigene_Karrotten, Karten[1], Karten[2],Züge [i][0]),
	                                         Floatwerte [i][0],Züge [i][0]);

	       int[] Werte1 = Werteveränderung(Werte[0],  Werte[1],  Werte[2], Werte[3], Werte[4], Züge [i][1]);
	       Floatwerte [i][1] = Floatwerte_Anpassung (Werteveränderung(Eigene_Position,  Eigene_Salate,  Eigene_Karrotten, Karten[1], Karten[2],Züge [i][0]),
	                                         Werteveränderung(Werte[0],  Werte[1],  Werte[2], Werte[3], Werte[4], Züge [i][1]), Floatwerte [i][1],Züge [i][1]);

	       int[] Werte2 = Werteveränderung(Werte1[0],  Werte1[1],  Werte1[2], Werte1[3], Werte1[4], Züge [i][2]);
	       Floatwerte [i][2] = Floatwerte_Anpassung (Werteveränderung(Werte[0],  Werte[1],  Werte[2], Werte[3], Werte[4], Züge [i][1]),
	                                         Werteveränderung(Werte1[0],  Werte1[1],  Werte1[2], Werte1[3], Werte1[4], Züge [i][2]), Floatwerte [i][2],Züge [i][2]);

	       int[] Werte3 = Werteveränderung(Werte2[0],  Werte2[1],  Werte2[2], Werte2[3], Werte2[4], Züge [i][3]);
	       Floatwerte [i][3] = Floatwerte_Anpassung (Werteveränderung(Werte1[0],  Werte1[1],  Werte1[2], Werte1[3], Werte1[4], Züge [i][2]),
	                                         Werteveränderung(Werte2[0],  Werte2[1],  Werte2[2], Werte2[3], Werte2[4], Züge [i][3]), Floatwerte [i][3],Züge [i][3]);
	    
	       int[] Werte4 = Werteveränderung(Werte3[0],  Werte3[1],  Werte3[2], Werte3[3], Werte3[4], Züge [i][4]);
	       Floatwerte [i][4] = Floatwerte_Anpassung (Werteveränderung(Werte2[0],  Werte2[1],  Werte2[2], Werte2[3], Werte2[4], Züge [i][3]),
	                                         Werteveränderung(Werte3[0],  Werte3[1],  Werte3[2], Werte3[3], Werte3[4], Züge [i][4]), Floatwerte [i][4],Züge [i][4]);
	       
	       Floatwerte [i][5] = Floatwerte [i][0] + Floatwerte [i][1] + Floatwerte [i][2] + Floatwerte [i][3] + Floatwerte [i][4];
	     } 
	   return  Floatwerte; 
   }
   

    public Move calculateMove() {  

    long startTime = System.currentTimeMillis(); // zum messen der Zeit 

    log.info("Es wurde ein Zug von {} angefordert.", gameState.getCurrentTeam());
    
    int Eigene_Karrotten = gameState.getCurrentPlayer().getCarrots();
    int Gengner_Karroten = gameState.getOtherPlayer().getCarrots();
    int Eigene_Salate = gameState.getCurrentPlayer().getSalads();
    int Gegner_Position = gameState.getOtherPlayer().getPosition(); 
    int Eigene_Position = gameState.getCurrentPlayer().getPosition();
    Field Eigne_FeldTyp = gameState.getBoard().getField(gameState.getCurrentPlayer().getPosition());
    Field Gegner_FeldTyp = gameState.getBoard().getField(gameState.getOtherPlayer().getPosition());
    String Gespielte_Schleife = "nichts";
    int[] Karten  = Karten(gameState.getCurrentPlayer().getCards());
    int[] GegnerKarten  = Karten(gameState.getOtherPlayer().getCards());
    List < Move > Alle_Züge = gameState.getSensibleMoves();
    int Move = 0;
    int Anzahl_Mögliche_Züge = Alle_Züge.size();   
    
    gameState.getCurrentPlayer().getLastAction();
  
    int Feld_Dist = 0;
    int[] Zugweite = Zugweite(Alle_Züge, gameState);
    boolean[]Condition = Condition(Eigene_Position, Eigene_Salate, Eigene_Karrotten, Karten[1], Karten[0] , GegnerKarten, "nichts");
    int[] Dist = Dist(Eigene_Position);
    
    
    String[][] Alle_Möglichkeiten = ZugMöglichkeiten(5);
    float[][] Alle_Möglichkeiten_Floatwerte = Floatwerte(ZugMöglichkeiten(5));
    
    
    for (int i = 0; i < Condition.length; i++) {
    
    	if (Condition[i] == true ) {

		Feld_Dist = Dist[i];
		Gespielte_Schleife = Schleife[i];
		break;
    	}
    	else {
    	continue;
	    }	
      }
  
    switch (Gespielte_Schleife) {
     
    case "Ziel Funktion","Vor Ziel Funktion", "Salat Funktion", "Markt Funktion", "Hasen Funktion", "Erste Position Funktion", "Zweite Position Funktion", "Karrotten Funktion","Igel Funktion", "Salatfressen Funktion ": {
    	int Zugindex = 0;
    	for (int i = 0; i < Zugweite.length; i++) {
    	   if (Feld_Dist == Zugweite[i] ) {
               Zugindex = i;
               break;
    	   }
          } 	
    	
      int Karte = 0; 
        if (Gespielte_Schleife == "Hasen Funktion" && GegnerKarten[1] != Karten[1] && Karten[1] != 0 && Gegner_Position > 57 && Gengner_Karroten + 100  > Eigene_Karrotten
          ||Gespielte_Schleife ==  "Markt Funktion" && (GegnerKarten[1] >= Karten[1] || Karten[1] == 0 )) {
          Karte = 1; // Karroten tauschen
        }

        if (Gespielte_Schleife == "Hasen Funktion" && Eigene_Salate != 0 && Karten[0] != 0 
          ||Gespielte_Schleife ==  "Markt Funktion" &&  GegnerKarten[1] < Karten[1] && Karten[1] != 0 && Eigene_Salate != 0) {
          Karte = 0; //Saladfress 	 
        }

      Move = Zugindex + Karte;
      
    break;
    }
      
    case "Karrotten abgeben Funktion" : Move = 2;
    break;

    case "nichts": Move = 0;
    break;
    }
    
    if  (Anzahl_Mögliche_Züge <= Move ||  Move < 0) {
        Move = Anzahl_Mögliche_Züge - 1;
        Gespielte_Schleife = "Notfall Funktion";
    }
 
    Move move = Alle_Züge.get(Move); // hier wird für die berechnung des Zuges "Move" eingesetzt und dann aus der Liste an möglichen Zügen ausgweählt 

    // Der folgende Abschnnit dient dazu um in der Konsole zu sehen was durchgeführt wird:

    /*--------------------------------------------------------------------------------------------------------*/
    
    

    log.info("Sende {} nach {}ms.", move, System.currentTimeMillis() - startTime);
    
    long moveCalculationTime = System.currentTimeMillis() - startTime;
    totalCalculationTime += moveCalculationTime;
    
    int columnWidth = 26;
    int columnCount = 5;

    String separator = new String(new char[columnCount * (columnWidth + 3) + 1]).replace("\0", "_");
    
    System.out.println(separator);
    System.out.println("\n\033[1;36mSpielrunde: " + gameState.getTurn() + "\u001B[0m | Menge an Karrotten: " + Eigene_Karrotten + " | \033[1;31mBerechnungszeit: " + (System.currentTimeMillis() - startTime) + "ms\u001B[0m");        
    System.out.println("\n");
    System.out.println("Mögliche Züge: (gesammt: " + Anzahl_Mögliche_Züge + ") " + Alle_Züge + "\n");
    System.out.println("Länge der möglichen Züge:" + Arrays.toString(Zugweite) + "\n");
    System.out.println("  Eigendes Feld  : " + Eigne_FeldTyp + " auf Position: " + Eigene_Position);
    System.out.println("Gegnerisches Feld: " + Gegner_FeldTyp + " auf Position: " + Gegner_Position + "\n");
    
    System.out.println("    Karten    = " + "Saladfressen: " + Karten(gameState.getCurrentPlayer().getCards())[0]
                       + " | Karotten tauschen: " + Karten(gameState.getCurrentPlayer().getCards())[1]
                       + " | Zurückfallen: " + Karten(gameState.getCurrentPlayer().getCards())[2]
                       + " | Vorrücken: " + Karten(gameState.getCurrentPlayer().getCards())[3]);
    System.out.println("Karten Gegner = " + "Saladfressen: " + Karten(gameState.getOtherPlayer().getCards())[0]
                       + " | Karotten tauschen: " + Karten(gameState.getOtherPlayer().getCards())[1]
                       + " | Zurückfallen: " + Karten(gameState.getOtherPlayer().getCards())[2]
                       + " | Vorrücken: " + Karten(gameState.getOtherPlayer().getCards())[3] + "\n");

    // Kopfzeile mit Spaltentiteln
    System.out.println(separator);
    System.out.printf("| %-" + columnWidth + "s | %-" + columnWidth + "s | %-" + columnWidth + "s | %-" + columnWidth + "s | %-" + columnWidth + "s |\n",
            "Zug1", "Zug2", "Zug3", "Zug4", "Zug5");
    System.out.println(separator);


    for (int i = 0; i < Alle_Möglichkeiten.length; i++) {
        System.out.printf("| %-" + columnWidth + "s | %-" + columnWidth + "s | %-" + columnWidth + "s | %-" + columnWidth + "s | %-" + columnWidth + "s |\n",
        		Alle_Möglichkeiten[i][0],
        		Alle_Möglichkeiten[i][1],
        		Alle_Möglichkeiten[i][2],
                Alle_Möglichkeiten[i][3],
                Alle_Möglichkeiten[i][4]);
    }
    System.out.println(separator);


    /*---------------------------------------------------------------------------------------------------------*/
    if (gameState.getTurn() >= 60) {
        System.out.println("\033[1;35mGesamte Berechnungszeit: " + totalCalculationTime + "ms\033[0m");
    }

    return move;
    
  }

  @Override

  public void onUpdate(IGameState gameState) {

    this.gameState = (GameState) gameState;
    log.info("Zug: {} Dran: {}", gameState.getTurn(), gameState.getCurrentTeam());

  }

  public void onGameOver(GameResult data) { //Wird aufgerufen, wenn das Spiel beendet ist.

    log.info("Das Spiel ist beendet, Ergebnis: {}", data);
    System.out.println("\033[1;35mGesamte Berechnungszeit: " + totalCalculationTime + "ms\033[0m");
 
  }

  @Override

  public void onError(String error) { //Wird aufgerufen, wenn der Server einen Fehler meldet. Bedeutet auch den Abbruch des Spiels. 

    log.warn("Fehler: {}", error);

  }

}
