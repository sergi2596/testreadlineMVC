import java.util.Map;
import java.util.*;
import java.util.TreeMap;

public class Model_Line extends Observable{
	/* VALUE: Aixo es tipus RowColumn, on tindrem totes les variables guardades i nomes les 
	*		  incrementarem o farem el que toqui amb elles, segons lo que ens digui el Controller.
	*/
	
	//VARIABLES
	Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
	int currentcol, currentrow, maxcol, maxfil, FILAINICIAL;
	
	
	Model_Line(){
		
	}


	/**
	 * Retorna columna actual
	 * 
	 * @return int columa_actual
	 */
	public int getColumn() {
		return this.currentcol;
	}

	/**
	 * Retorna fila actual
	 * 
	 * @return int fila_actual
	 */

	public int getRow() {
		return this.currentrow;
	}
	
	/**
	 * Estableix columna actual
	 */
	
	public void setCurrentColumn(int col) {
		this.currentcol = col;
	}
	
	/**
	 * Estableix fila actual
	 */
	
	public void setCurrentRow(int row) {
		this.currentrow = row;
	}

	/**
	 * Fixa la primera fila de l'editor. L'invoquem només un cop a l'inici del
	 * programa per marcar quina és la primera fila.
	 */

	public void setFirstRow() {
		map.put(currentrow, 0);
	}

	/**
	 * Incrementa la columna maxima de la fila actual del TreeMap. Així, cada
	 * cop que escrivim un caràcter, incrementem el número de columnes de la
	 * fila en qüestió. Abans d'això, mirem si la fila existeix dins del TreeMap
	 * (pot ser que estiguem escrivint la primera lletra d'aquella fila, i per
	 * tant hem de crear una nova clau amb la fila)
	 */

	public void AddColumn(int ncols) {
		int maxcol = -1;
		if (map.containsKey(currentrow)) {
			maxcol = getMaxColumn(currentrow);
		}
		map.put(currentrow, maxcol + ncols);
	}

	/**
	 * Idem però eliminant una columna. Es fa servir per quan apretem tecla
	 * DELETE. El primer if de tots és per mirar que no estiguem a l'inici de
	 * l'editor (1a fila, 1a columna) Si estem a la primera columna d'una fila
	 * (que no sigui la primera) i apretem DELETE, es borra aquella fila del
	 * TreeMap. Si no, simplement decrementem el numero de columnes de la fila.
	 */

	public void DownColumn() {
		if (currentrow != getFirstRow() || currentcol != 1) {
			int maxcol = getMaxColumn(currentrow);
			if (maxcol == 0) {
				removeRow();
			} else
				map.put(currentrow, maxcol - 1);
		}
	}

	public void removeRow() {
		map.remove(currentrow);
	}
	
	public void setRowtoZero() {
		map.put(currentrow, 0);
	}
	/**
	 * Retorna el numero maxim de columnes d'una fila
	 * 
	 * @param row
	 *            : fila de la que volem consultar el numero de columnes
	 * @return numero de columnes
	 */

	public int getMaxColumn(int row) {
		return map.get(row);
	}

	/**
	 * Retorna quina es la primera fila del programa. Util per marcar el limit
	 * de la UP_ARROW
	 * 
	 * @return
	 */

	public int getFirstRow() {
		return ((TreeMap<Integer, Integer>) map).firstKey();
	}

	/**
	 * Retorna la ultima fila de programa. Util per marcar el limit de la
	 * DOWN_ARROW
	 * 
	 * @return
	 */

	public int getLastRow() {
		int lastrow = ((TreeMap<Integer, Integer>) map).lastKey();
		return lastrow;
	}
	
	public Map<Integer, Integer> getMap() {
		return map;
	}

	

}
