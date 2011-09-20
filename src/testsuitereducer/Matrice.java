package testsuitereducer;

import java.util.ArrayList;

public class Matrice
{

	public int[][] matrix = null;

	private int riga = 0;

	private int colonna = 0;

	private int el;
	
	private boolean righeUguali = false;

        public Matrice(int[][] matrice)
        {
            this.matrix = matrice;
            this.riga = matrice.length;
            this.colonna = matrice[0].length;
            this.el = 0;
        }

	public Matrice(int row, int column)
	{
		this.riga = row;
		this.colonna = column;
		this.matrix = new int[this.riga][this.colonna];
		this.el = 0;

	}

	public Matrice()
	{
		this.riga = 0;
		this.colonna = 0;
	}

	public void initUno()
	{
		//ESEMPIO 1
//		matrix[0][3] = 1;
//		matrix[0][4] = 1;
//		matrix[0][8] = 1;
//		matrix[0][9] = 1;
//
//		matrix[1][1] = 1;
//		matrix[1][4] = 1;
//		matrix[1][6] = 1;
//		matrix[1][9] = 1;
//
//		matrix[2][0] = 1;
//		matrix[2][1] = 1;
//		matrix[2][3] = 1;
//		matrix[2][4] = 1;
//
//		matrix[3][12] = 1;
//		matrix[3][10] = 1;
//		
//
//		matrix[4][5] = 1;
//		matrix[4][12] = 1;
//		
//		matrix[5][10] = 1;
//		matrix[5][9] = 1;
//		matrix[6][6] = 1;
//		matrix[6][7] = 1;
//		matrix[7][8] = 1;
//		matrix[7][11] = 1;
//		matrix[8][0] = 1;
//		matrix[8][2] = 1;
//		
//		ESEMPIO 2
		
//		matrix[0][0] = 1;
//		matrix[1][4] = 1;
//		matrix[2][0] = 1;
//		matrix[3][1] = 1;
//		matrix[4][1] = 1;
//		matrix[5][2] = 1;
//		matrix[5][3] = 1;
//		matrix[5][5] = 1;
//		matrix[6][2] = 1;
//		matrix[6][4] = 1;
//		matrix[6][5] = 1;
		
		//ESEMPIO 3
		
		matrix[0][1] = 1;
		matrix[0][3] = 1;
		matrix[1][0] = 1;
		matrix[1][1] = 1;
		matrix[1][2] = 1;
		
		matrix[2][3] = 1;
		matrix[3][2] = 1;
		matrix[3][4] = 1;
		
	}
        
	/**
	 * Ricava l'emento sulla riga k e sulla colonna j
	 * 
	 * @param k
	 * @param j
	 * @return
	 */
	public int elemento(int k, int j)
	{
		this.el = this.matrix[k][j];
		return el;
	}

	/**
	 * Popola l'elemento di posto (k,j) con il valore dato in ingresso.
	 * 
	 * @param k
	 * @param j
	 * @param valore
	 */
	public void popola(int k, int j, int valore)
	{
		for (int riga = 0; riga < this.riga; riga++)
		{
			for (int colonna = 0; colonna < this.colonna; colonna++)
			{
				if ((riga == k) && (colonna == j))
				{
					matrix[riga][colonna] = valore;
				}
			}
		}
	}



	public void eliminariga(int index)
	{
		for (int j = 0; j < this.colonna; j++)
		{
			this.popola(index, j, 0);
		}

	}

	public void eliminacolonna(int index)
	{
		for (int j = 0; j < this.riga; j++)
		{
			this.popola(j, index, 0);
		}
	}

	/**
	 * Restituisce l'arraylist contenente la riga j-sima.
	 * 
	 * @param riga
	 * @return -> arraylist
	 */
	public ArrayList prelevaRiga(int riga)
	{
		ArrayList row = new ArrayList();
		for (int j = 0; j < this.colonna; j++)
		{
			Integer el = new Integer(this.elemento(riga, j));
			row.add(el);

		}
		return row;
	}

	/**
	 * Restituisce l'arraylist contenente la colonna j-sima.
	 * 
	 * @param colonna
	 * @return -> arraylist
	 */
	public ArrayList prelevaColonna(int colonna)
	{
		ArrayList column = new ArrayList();
		for (int j = 0; j < this.riga; j++)
		{
			Integer el = new Integer(this.elemento(j, colonna));
			column.add(el);

		}
		return column;
	}

	/**
	 * Restituisce un arraylist contenente la posizione degli uno all'interno
	 * della riga.
	 * 
	 * @param riga
	 * @return
	 */
	public ArrayList trovaIndex(ArrayList riga)
	{
		ArrayList index = new ArrayList();
		for (int k = 0; k < riga.size(); k++)
		{
			Integer el = (Integer) riga.get(k);
			if (el.intValue() == 1)
			{
				index.add(new Integer(k));
			}
		}
		return index;
	}

	/**
	 * Verifica se la riga J � dominata dalla riga I.
	 * 
	 * @param rigaI
	 * @param rigaJ
	 * @return -> restituisce vero se J � dominata da I, altrimenti falso.
	 */
	public boolean confronta(ArrayList rigaI, ArrayList rigaJ)
	{
		if(rigaI.size()==0 || rigaJ.size()==0)
			return false;
		
		boolean flag = false;
		int count = 0;
		for (int k = 0; k < rigaJ.size(); k++)
		{
			Integer el = (Integer) rigaJ.get(k);
			if (rigaI.contains(el))
			{
				count++;
			}
		}
		if (count == rigaJ.size())
		{
			flag = true;
		}
		if (count == rigaJ.size() && rigaI.size() == rigaJ.size())
		{
			flag = true;
			this.righeUguali = true;
		}
		return flag;
	}

	/**
	 * Fa una copia della matrice
	 * 
	 * @return -> matrice copia
	 */
	public Matrice copia()
	{
		Matrice copia = new Matrice(this.riga, this.colonna);
		for (int k = 0; k < this.riga; k++)
		{
			for (int j = 0; j < this.colonna; j++)
			{
				copia.popola(k, j, this.elemento(k, j));
			}
		}
		return copia;
	}

	/**
	 * Controlla se la matrice � vuota
	 * 
	 * @return
	 */
	public boolean vuota()
	{
		boolean flag = false;
		if ((this.riga == 0) || (this.colonna == 0))
		{
			flag = true;
		}
		return flag;
	}

	/**
	 * Stampa la matrice
	 * 
	 */
	public void printM()
	{
		System.out.print("   ");
		for (int j = 0; j < this.colonna; j++)
		System.out.print(j+"  ");
		
		System.out.println("\n");
		
		for (int i = 0; i < this.riga; i++)
		{
			System.out.print(i+". ");
			for (int j = 0; j < this.colonna; j++)
			{
				System.out.print(this.elemento(i, j) + "  ");

			}
			System.out.println("\n");
		}

	}

	public int getColonna()
	{
		return colonna;
	}

	public int getRiga()
	{
		return riga;
	}

	public boolean reducedToAVector()
	{
		if (this.getColonna() == 1 || this.getRiga() == 1)
			return true;
		else
			return false;
	}
	
	public boolean isZero(ArrayList al )
	{
		if (al.contains(new Integer(1)))
		return false;
		else return true;
	}

//	public static void main(String[] args)
//	{
//
//		Matrice m = new Matrice(5, 4);
//		ReductionUtility ru = new ReductionUtility();
//
//		m.initUno();
//		m.printM();
//		ru.essentialTC(m);
//
//		ru.reduceEssential(m);
//
//	}

	public boolean isRigheUguali()
	{
		return righeUguali;
	}

}
