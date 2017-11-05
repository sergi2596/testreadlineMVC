import java.util.Map;
import java.util.*;
import java.util.TreeMap;

public class Model_Line extends Observable{
	/* VALUE: Aixo es tipus RowColumn, on tindrem totes les variables guardades i nomes les 
	*		  incrementarem o farem el que toqui amb elles, segons lo que ens digui el Controller.
	*/
	
	//VARIABLES
	Map<Integer, Integer> map = new TreeMap<Integer, Integer>();
	int currentcol, currentrow, maxcol, maxfil, FILAINICIAL, currentchar;
	boolean sobreescrivint, subratllant, enter;
	
	
	Model_Line(){
		subratllant = false;
		sobreescrivint = false;
		enter = false;
		currentcol = 0;
		currentrow = 0;
		maxcol = 0;
		maxfil = 0;
		FILAINICIAL = 0;
		currentchar = ' ';
		
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
	 * Estableix caràcter a printar.
	 * 
	 * @param Integer a printar.
	 */
	public void setChar(int currentchar) {
		this.currentchar = currentchar;
	}
	/**
	 * Retorna caràcter a printar
	 * 
	 * @return int caràcter actual
	 */
	public int getChar() {
		return currentchar;
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
		if (enter) {
			setCurrentRow(currentrow +1);
			map.put(currentrow, maxcol + ncols);
			enter = false;
		}else {
			map.put(currentrow, maxcol + ncols);
		}
		
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
	
	
	//////////////////////////////////////////////////////////////////////////////////
	//FUNCIONS QUE CRIDEN AL UPDATE DEL OBSERVER
	
	
	public void currentchar() {
		setChanged();
		AddColumn(1);
		notifyObservers(new View_Console.Command(View_Console.Opcode.CHAR));
	}
	
	public void space() {
		setChanged();
		AddColumn(1);
		notifyObservers(new View_Console.Command(View_Console.Opcode.SPACE));
	}
	
	public void tab() {
		setChanged();
		AddColumn(4);
		notifyObservers(new View_Console.Command(View_Console.Opcode.TAB));
	}
	
	public void ctrls() {
		setChanged();
		if (subratllant) {
			notifyObservers(new View_Console.Command(View_Console.Opcode.CTRLSOFF));
		}else {
			notifyObservers(new View_Console.Command(View_Console.Opcode.CTRLS));
		}
	}
	
	/**
	 * Per la UP_ARROW, primer mirem que la fila actual no sigui la
	 * primera. En tal cas, si estem a una columna més gran que el
	 * numero màxim de columnes de la fila de dalt, enviem el cursor a
	 * la columna màxima de la fila de dalt (així evitem que es pugui
	 * fer "trampa" i enviar el cursor a una columna de la fila de dalt
	 * que encara no existeix. Ho fem en dos passos, primer movem cursor
	 * a dalt i despres a la dreta (hi ha una comanda per fer-ho de cop
	 * però no m'ha funcionat bé).
	 */
	public void up_arrow() {
		setChanged();
		if (getMap().containsKey(currentrow-1)) {
			if (currentrow > getFirstRow()) {
				notifyObservers(new View_Console.Command(View_Console.Opcode.UP_ARROW));
				if (currentcol > getMaxColumn((currentrow - 1))) {
					notifyObservers(new View_Console.Command(View_Console.Opcode.UP_ARROWL));
				}
			}
		}
	}
	
	/**
	 * Igual que UP_ARROW però mirant que no ens passem de la fila
	 * màxima
	 */
	public void down_arrow() {
		setChanged();
		if (getMap().containsKey(currentrow+1)) {
			if (currentrow < getLastRow()) {
				notifyObservers(new View_Console.Command(View_Console.Opcode.DOWN_ARROW));
				if (currentcol > getMaxColumn((currentrow + 1))) {
					notifyObservers(new View_Console.Command(View_Console.Opcode.DOWN_ARROWL));
				}
			}
		}
		
	}
	
	/**
	 * Si no estem a la columna maxima de la fila, movem cursor a la
	 * dreta i punto. Si estem a la ultima columna (però NO a la
	 * ultima fila), movem cursor a la fila següent, columna 1
	 */
	public void right_arrow() {
		setChanged();
		if (currentcol <= getMaxColumn(currentrow) && getMaxColumn(currentrow) > 0) {
			notifyObservers(new View_Console.Command(View_Console.Opcode.RIGHT_ARROW));
		} else if (currentrow != getLastRow() && getMap().containsKey(currentrow+1)) {
			notifyObservers(new View_Console.Command(View_Console.Opcode.RIGHT_ARROWD));
		}
	}
	
	/**
	 * Igual que abans, però per canviar de fila ho fem en dos
	 * passos (també es pot fer en un però no funcionava). Primer
	 * movem cursor a la fila de dalt i despres el movem a la dreta
	 * fins la columna màxima.
	 */
	public void left_arrow() {
		setChanged();
		if (currentcol > 1) {
			notifyObservers(new View_Console.Command(View_Console.Opcode.LEFT_ARROW));
		} else if (currentrow > getFirstRow() && getMap().containsKey(currentrow-1)) {
			notifyObservers(new View_Console.Command(View_Console.Opcode.LEFT_ARROWU));
		}
	}
	
	/**
	 * Si estem borrant la primera lletra d'una fila (que no sigui
	 * la primera fila), movem cursor a la fila de dalt fins la
	 * ultima columna i borrem el caracter que quedava. També
	 * decrementem el numero de columnes de la fila (en cas que no
	 * en quedin, el metode DownCol() s'encarrega d'eliminar la fila
	 * del TreeMap)
	 */
	public void delete() {
		setChanged();
		if (currentcol == 1 && currentrow > getFirstRow()) {
			notifyObservers(new View_Console.Command(View_Console.Opcode.LEFT_ARROWU));
			if (getMaxColumn(currentrow) == 0 && currentrow == getLastRow()) {
				DownColumn();
			}
		}
		else {
			notifyObservers(new View_Console.Command(View_Console.Opcode.DELETE));
			DownColumn();
		}	
	}
	
	/**
	 * Borra caràcters de la dreta i els va movent. Decrementa número
	 * de columnes només si borra un caràcter
	 */
	public void suprimir() {
		setChanged();
		notifyObservers(new View_Console.Command(View_Console.Opcode.SUPRIMIR));
		if (getMaxColumn(currentrow) > 0 && currentcol <= getMaxColumn(currentrow)) {
			DownColumn();
		}
	}
	
	/**
	 * HOME i END són iguals que sempre, molt bàsic 
	 */
	public void home() {
		setChanged();
		notifyObservers(new View_Console.Command(View_Console.Opcode.HOME));
	}
	public void end() {
		setChanged();
		notifyObservers(new View_Console.Command(View_Console.Opcode.END));
	}
	
	/**
	 * ENTER només funciona si en fem un i escrivim algo. Si fem dos
	 * ENTERS (deixant una fila en blanc) NO està implementat
	 */
	public void enter() {
		setChanged();
		notifyObservers(new View_Console.Command(View_Console.Opcode.ENTER));
		
		if (!getMap().containsKey(getRow()+1)) {
			enter = true;
			AddColumn(1);
		}
	}
	
	public void sobreescrivint() {
		setChanged();
		if (sobreescrivint) {
			notifyObservers(new View_Console.Command(View_Console.Opcode.INSERT));
			sobreescrivint = false;
		}else {
			notifyObservers(new View_Console.Command(View_Console.Opcode.SOBREESCRIURE));											
			sobreescrivint = true;
		}
	}
	
	public void firstrow() {
		setChanged();
		notifyObservers(new View_Console.Command(View_Console.Opcode.FIRSTROW));
	}
	
	public void lastrow() {
		setChanged();
		notifyObservers(new View_Console.Command(View_Console.Opcode.LASTROW));
	}
	
	public void delrow() {
		setChanged();
		notifyObservers(new View_Console.Command(View_Console.Opcode.DELROW));
		if (currentrow == getLastRow() && currentrow != getFirstRow()) {
			notifyObservers(new View_Console.Command(View_Console.Opcode.UP_ARROWL));
			removeRow();
		}
		
		else {
			notifyObservers(new View_Console.Command(View_Console.Opcode.HOME));
			setRowtoZero();
		}
	}
	

}
