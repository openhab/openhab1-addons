package org.openhab.binding.wr3223;

public enum WR3223Commands {
	/** Abtau ein Temperatur lesen/schreiben */
	AE,
	
	/** Abtau aus Temperatur lesen/schreiben */
	AA,
	
	/** Abtau Zuluft lesen/schreiben */
	Az,
	
	/** Abtauluft lesen/schreiben */
	Aa,
	
	/** Frost aus lesen/schreiben */
	AR,
	
	/** Frost an Schnittstelle lesen/schreiben */
	AZ,
	
	/** Abtaupause lesen/schreiben */
	AP,
	
	/** Abtaunachlauf lesen/schreiben */
	AN,
	
	/** Frost Verzögerung lesen */
	AV, 
	
	
	/** Verdampfertemperatur istwert auslesen */
	T1,
	
	/** Kondensatortemperatur auslesen */
	T2,
	
	/** Aussentemperatur auslesen */
	T3,
	
	/** Temperatur nach Wärmetauscher (Fortluft) lesen */
	T5,
	
	/** Zulufttemperatur auslesen */
	T6,
	
	/** Temperatur nach Solevorerwärmung lesen */
	T7,
	
	/** Temperatur nach Vorheizregister lesen */
	T8, 
	
	
	/** aktuelle Luftstufe lesen/schreiben */
	LS, 
	
	/** Luftstufe 1 lesen/schreiben */
	L1, 
	
	/** Luftstufe 2 lesen/schreiben */
	L2, 
	
	/** Luftstufe 3 lesen/schreiben */
	L3, 
	
	/** Zuluft +/- lesen/schreiben */
	LD, 
	
	/** Abluft +/- lesen/schreiben */
	Ld, 
	
	
	/** Erdwärmetauscher vorhanden lesen/schreiben */
	EC,

	/** Schaltpunkt Sommer stopp lesen/schreiben */
	Es,

	/** Schaltpunkt Erdwärmetauscher Sommer lesen/schreiben */
	ES,

	/** Schaltpunkt Erdwärmetauscher Winter lesen/schreiben */
	EW,

	/** Schaltpunkt Solepumpe Ein lesen/schreiben */
	EE,

	/** Schaltpunkt Solepumpe aus lesen/schreiben */
	EA,

	/** Fehler lesen */
	ER,
	
	
	/** Status auslesen */
	ST,

	/** Status schreib byte auslesen/schreiben (nur bei PC Steuerung) */
	SW,
	
	/** Zuluftsoll Temperatur auslesen/schreiben (nur bei PC Steuerung) */
	SP,
	
	/** Mode auslesen/schreiben (nur bei PC Steuerung)*/
	MD, 	
	
	/** Relais lesen */
	RL,
	
	/** Steuerspannnung Zuluft lesen */
	UZ, 
	
	/** Steuerspannnung Abluft lesen */
	UA, 
	
	/** Drehzahl Zuluftmotor lesen */
	NZ, 
	
	/** Drehzahl Abluftmotor lesen */
	NA, 
	
	/** Grenzdrehzahl lesen/schreiben (*50) */
	NM, 
		
	/** Configuration auslesen */
	CN, 
	
	/** Maximale Kondensationstemperatur lesen/schreiben */
	KM, 
	
	/** Zusatzheizung frei lesen/schreiben */
	ZH, 
	
	/** Zusatzheizung Ein lesen/schreiben */
	ZE, 
	
	/** Wärmepumpe frei lesen/schreiben */
	WP, 
	
	/** Ausgleichszeit lesen/schreiben */
	PA, 
	
	/** Identifikation lesen */
	II	

}
