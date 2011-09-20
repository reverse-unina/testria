package testsuitereducer;

import java.util.ArrayList;

/**
 * La classe si occupa di implementare l'algoritmo delle righe e delle colonne
 * dominanti con l'obiettivo di minimizzare la matrice TC/UC e selezionare il
 * numero minimo di test case (le righe) che copra tutti i casi d'uso (le
 * colonne).
 * 
 * @author Carmen Naimoli
 * 
 */
public class RigheColonne
{

	private boolean isFinish = false;

	private boolean isFirstTime = true;

	private ReductionUtility ru = null;

	private ArrayList TCexential = null;

	public RigheColonne()
	{
		this.ru = new ReductionUtility();
		this.TCexential = new ArrayList();
	}

	/**
	 * Algoritmo delle righe e delle colonne dominanti
	 * 
	 * @param matrix ->
	 *            matrice da ridurre
	 */
	public Matrice algoritmo(Matrice reduced)
	{
		// Individuazione dei TC essenziali ed eliminazione di tutte i
		// casi d'uso coperti dai TC e della riga corrispondente al TC

		ru.essentialTC(reduced);

		boolean existTC = false;

		if (ru.getTcEssentialIndex().size() != 0)
		{
			existTC = true;
			//System.out.println("I tc essenziali sono:" + this.ru.getTcEssentialIndex());
			this.TCexential.add(ru.getTcEssentialIndex());
			reduced = ru.reduceEssential(reduced);
			//System.out.println("Matrice senza tc essenziali;");
			//reduced.printM();
		}

		if ((existTC || this.isFirstTime) && !reduced.reducedToAVector())
		{
			// calcola tutte le righe dominate e le elimina
			//System.out.println("*** Senza Righe Dominate *** \n");
			reduced = ru.righeDominate(reduced);
			//reduced.printM();
			//System.out.println("********************** \n");

			// calcola tutte le colonne dominanti e le elimina
			//System.out.println("***Senza Colonne Dominanti *** \n");
			reduced = ru.colonneDominanti(reduced);
			//reduced.printM();
//			System.out.println("************************* \n");

		}
		else
			this.isFinish = true;

		return reduced;

	}

	public void reduction(Matrice copia)
	{
		while (!this.isFinish && !copia.reducedToAVector())
		{
			copia = algoritmo(copia);

			this.isFirstTime = false;

			//System.out.println("******STAMPA******");
			//copia.printM();
			//System.out.println("******************");

		}
	}

        public ArrayList getTCexential(){
            ArrayList temp = null;
            ArrayList res = new ArrayList();
            for(int i = 0; i < this.TCexential.size(); i++){
                temp = new ArrayList();
                temp = (ArrayList) this.TCexential.get(i);
                for(int j = 0; j < temp.size(); j++){
                    res.add(temp.get(j));
                }
            }
            return res;
        }

	public void stampaTC()
	{

		System.out.println("Tutti i tc sono: " + this.TCexential);
		System.out.println("Size :" + this.TCexential.size());
	}

//	public static void main(String[] args)
//	{
//		Matrice m = new Matrice(4, 5);
//
//		m.initUno();
//		m.printM();
//		RigheColonne rc = new RigheColonne();
//		rc.reduction(m);
//		rc.stampaTC();
//	}
}
