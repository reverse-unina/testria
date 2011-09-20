package testsuitereducer;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * Classe che fornisce funzioni di utility per implementare la riduzione della
 * test suite basata sull'algoritmo delle righe e delle colonne dominanti.
 * 
 * @author Carmen Naimoli
 * 
 */
public class ReductionUtility
{
	private ArrayList tcEssentialIndex = null;

	public ReductionUtility()
	{
		this.tcEssentialIndex = new ArrayList();

	}

	/**
	 * Data una matrice TC/UC si occupa di calcolare i TC essenziali cio� quelli
	 * che hanno la caratteristica di essere gli unici a coprire un certo use
	 * case.
	 * 
	 * @param matrix
	 *            ---> matrice TC/UC di ingresso
	 * 
	 * 
	 * 
	 */
	public void essentialTC(Matrice matrix)
	{
		this.tcEssentialIndex = new ArrayList();
		for (int colonna = 0; colonna < matrix.getColonna(); colonna++)
		{
			int contaUno = 0;
			int rigaTc = 0;
			for (int riga = 0; riga < matrix.getRiga(); riga++)
			{
				if (matrix.elemento(riga, colonna) == 1)
				{
					contaUno++;
					rigaTc = riga;
				}
			}
			if (contaUno == 1)
			{
				if (!this.tcEssentialIndex.contains(new Integer(rigaTc)))
				{
					this.tcEssentialIndex.add(new Integer(rigaTc));
				}
			}
			Collections.sort(this.tcEssentialIndex);
//			System.out.println("I tc essenziali sono:" + this.tcEssentialIndex);

		}
	}

	/**
	 * Riduce la matrice in ingresso eliminando la riga che individua un test
	 * case essenziale e tutte le colonne che il test case copre.
	 * 
	 * @param matrix ->
	 *            matrice in ingresso
	 * @return matrice ridotta
	 */
	public Matrice reduceEssential(Matrice matrix)
	{
		int colon = 0;
		int row = 0;
				

		for (int k = 0; k < this.tcEssentialIndex.size(); k++)
		{
			int col = 0;
			// prelevo il k-simo test case essenziale
			Integer rigaTc = (Integer) this.tcEssentialIndex.get(k);

			// scorro sulle colonne
			while (col < matrix.getColonna())
			{
				if (matrix.elemento(rigaTc.intValue(), col) == 1)
				{
					// elimina la colonna di indice col
					matrix.eliminacolonna(col);

				}
				else
				{
					col++;
				}
			}

			// elimino la riga che individua il tc essenziale

			matrix.eliminariga(rigaTc.intValue() );
			row = matrix.getRiga();
		

		}
		if ((colon == 0) || (row == 0))
		{
			System.out.println("Matrice vuota!");
		}
		else
		{
			System.out.println("Matrice senza TC essenziali: ");
			matrix.printM();
		}
		return matrix;

	}

	/**
	 * Calcola le righe dominate e le elimina.
	 * 
	 * @param matrix =
	 *            matrice senza righe dominate
	 */
	public Matrice righeDominate(Matrice matrix)
	{

		for (int i = 0; i < matrix.getRiga(); i++)
		{
			for (int j = 0; j < matrix.getRiga(); j++)
			{
				if (i != j)
				{
					// Prelevo la riga i-sima
					ArrayList rigaI = matrix.prelevaRiga(i);
					// Prelevo la riga j-sima
					ArrayList rigaJ = matrix.prelevaRiga(j);
					// Confronto riga I e riga J
					boolean jDominata = matrix.confronta(matrix.trovaIndex(rigaI), matrix.trovaIndex(rigaJ));

					if (jDominata)
					{

						matrix.eliminariga(j);

					}
				}

			}

		}

		return matrix;
	}

	/**
	 * Calcola le colonne dominanti sulla matrice senza i tc essenziali (copia)
	 * e le elimina dalla matrice gi� priva delle righe dominate (matrix).
	 * 
	 * @param matrix
	 * @param copia
	 * @return -> restiruisce la matrice senza colonne dominanti
	 */
	public Matrice colonneDominanti(Matrice copia)
	{
		// Hashtable colonneDaCancellare = new Hashtable();
		for (int i = 0; i < copia.getColonna(); i++)
		{
			for (int j = 0; j < copia.getColonna(); j++)
			{
				if (i != j)
				{
					// Prelevo la colonna i-sima
					ArrayList colonnaI = copia.prelevaColonna(i);
					// Prelevo la colonna j-sima
					ArrayList colonnaJ = copia.prelevaColonna(j);
					// Confronto colonnaI e colonnaJ
					boolean jDominata = copia.confronta(copia.trovaIndex(colonnaI), copia.trovaIndex(colonnaJ));

					if (jDominata)
					{
						// System.out.println("Colonna Dominante -->" + i);
						copia.eliminacolonna(i);
					}

				}
			}

		}
		return copia;
	}

	

	public ArrayList getTcEssentialIndex()
	{
		return tcEssentialIndex;
	}

}
