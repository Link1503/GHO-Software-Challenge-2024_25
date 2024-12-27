long startTime = System.currentTimeMillis(); // Zeitmessung
log.info("Es wurde ein Zug von {} angefordert.", gameState.getCurrentTeam());

// Variablen initialisieren
int eigeneKarotten = gameState.getCurrentPlayer().getCarrots();
int eigenePosition = gameState.getCurrentPlayer().getPosition();
int gegnerPosition = gameState.getOtherPlayer().getPosition();
int distanzZiel = 64 - eigenePosition;
Field eigeneFeldTyp = gameState.getBoard().getField(eigenePosition);
Field gegnerFeldTyp = gameState.getBoard().getField(gegnerPosition);
List<Move> möglicheZüge = gameState.getSensibleMoves();
int move = 0;
String gespielteSchleife = "nichts";

// Helper-Methode zur Vereinfachung
int getNextFieldOrDefault(Field fieldType, int position, int defaultValue) {
    Integer nextField = gameState.getBoard().getNextField(fieldType, position);
    return nextField != null ? nextField : defaultValue;
}

// Nächste Felder berechnen
int nächsterMarkt = getNextFieldOrDefault(Field.MARKET, eigenePosition, 0);
int nächsterSalat = getNextFieldOrDefault(Field.SALAD, eigenePosition, 0);
int nächsterHase = getNextFieldOrDefault(Field.HARE, eigenePosition, 0);
int nächsterIgel = getNextFieldOrDefault(Field.HEDGEHOG, eigenePosition, 0);

// Distanzen berechnen
int distanzMarkt = nächsterMarkt - eigenePosition;
int distanzSalat = nächsterSalat - eigenePosition;
int distanzHase = nächsterHase - eigenePosition;
int distanzIgel = nächsterIgel - eigenePosition;

// Karten-Typen berechnen
int[] kartenTyp = new int[4];
for (Object karte : gameState.getCurrentPlayer().getCards()) {
    switch (karte.toString()) {
        case "EAT_SALAD" -> kartenTyp[0]++;
        case "SWAP_CARROTS" -> kartenTyp[1]++;
        case "HURRY_AHEAD" -> kartenTyp[2]++;
        case "FALL_BACK" -> kartenTyp[3]++;
    }
}
int kartenGesamt = Arrays.stream(kartenTyp).sum();

// Gegner-Karten-Typen berechnen
int[] gegnerKartenTyp = new int[4];
for (Object karte : gameState.getOtherPlayer().getCards()) {
    switch (karte.toString()) {
        case "EAT_SALAD" -> gegnerKartenTyp[0]++;
        case "SWAP_CARROTS" -> gegnerKartenTyp[1]++;
        case "HURRY_AHEAD" -> gegnerKartenTyp[2]++;
        case "FALL_BACK" -> gegnerKartenTyp[3]++;
    }
}
int gegnerKartenGesamt = Arrays.stream(gegnerKartenTyp).sum();

// Letzte Felder berechnen
int letzterMarkt = getLastField(Field.MARKET);
int letzterHase = getLastField(Field.HARE);
int letzterIgel = getLastField(Field.HEDGEHOG);

// Funktion zur Berechnung letzter Felder
int getLastField(Field fieldType) {
    int lastField = 0;
    while (gameState.getBoard().getNextField(fieldType, lastField) != null) {
        lastField = gameState.getBoard().getNextField(fieldType, lastField);
    }
    return lastField;
}

// Züge berechnen
if (distanzHase < distanzMarkt && distanzHase > 0) {
    gespielteSchleife = "Hasen Funktion";
    move = berechneMove(nächsterHase, eigenePosition, gegnerPosition, eigeneFeldTyp, gegnerFeldTyp);
} else if (distanzMarkt > 0) {
    gespielteSchleife = "Markt Funktion";
    e = "Standard Zug";
    move = distanzZimove = berechneMove(nächsterMarkt, eigenePosition, gegnerPosition, eigeneFeldTyp, gegnerFeldTyp);
} else {
    gespielteSchleifel;
}

// Funktion zur Move-Berechnung
int berechneMove(int zielPosition, int eigenePos, int gegnerPos, Field eigeneTyp, Field gegnerTyp) {
    int nichtBegehbareFelder = 0;
    int nächsteIgel = eigenePos;

    while (nächsteIgel < zielPosition) {
        nichtBegehbareFelder++;
        nächsteIgel = getNextFieldOrDefault(Field.HEDGEHOG, nächsteIgel, zielPosition);
    }
    return zielPosition - eigenePos - nichtBegehbareFelder;
}
