import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Controller_EditableBufferedReader extends BufferedReader{
	
	Model_Line model;
	View_Console view;
	
	int currentcol, currentrow;
	boolean aux = false,sobreescrivint = false;
	
	final int UP_ARROW = 300, DOWN_ARROW = 301, RIGHT_ARROW = 302,
			LEFT_ARROW = 303, SPACE = 32, CTRLD = 4, CTRLS = 19,
			SUPRIMIR = 295, ESC = 27, CORXET = 91, DELETE = 127, HOME = 305,
			END = 304, ENTER = 13, INSERT = 294, FIRSTROW = 6, LASTROW = 12, 
			TAB = 9, DELROW = 8, PUNTICOMA = 11;
	public Controller_EditableBufferedReader(Reader in) {
		super(in);
	}
	
	
	//AFEGIM MODEL I VIEW PER CONTROLAR-LOS
	public void addModel(Model_Line model) {
		this.model = model;
	}
	public void addView(View_Console view) {
		this.view = view;
	}
	
	
	public void filacol() {
		List<Integer> filacols = new ArrayList<Integer>();
		int aux, i, indice, longitud;
		aux = 0;
		i = 0;
		indice = 0;
		longitud = 0;
		char escCode = 0x1B;
		BufferedReader buf = new BufferedReader(
				new InputStreamReader(System.in));

		try {
			System.out.print(String.format("%c[%d%s", escCode, 6, "n"));
			while (aux != 82) {
				aux = buf.read();
				if ((aux != 27) && (aux != 91) && (aux != 82)) {
					filacols.add(aux);
				}
			}
			while (i < filacols.size()) {
				filacols.set(i, filacols.get(i) - 48);
				i++;
			}
			indice = filacols.indexOf(PUNTICOMA);
			longitud = filacols.size();
			switch (indice) {
			case 1:
				model.setCurrentRow(filacols.get(0));
				if (longitud == 3) {
					
					currentcol = filacols.get(2);
				} else {
					currentcol = Integer.parseInt(filacols.get(2) + ""
							+ filacols.get(3));
				}
				break;
			case 2:
				currentrow = Integer.parseInt(filacols.get(0) + ""
						+ filacols.get(1));
				if (longitud == 4) {
					currentcol = filacols.get(3);
				} else if (longitud == 5) {
					currentcol = Integer.parseInt(filacols.get(3) + ""
							+ filacols.get(4));
				}
				break;
			default:
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public String readLine() throws IOException {
		int cr = 0;
		String str = "";
		char escCode = 0x1B;
		System.out.print(String.format("%c[%d;%d%s", escCode, 37, 49, "m")); // colors
		System.out.print(String.format("%c[%d%s", escCode, 4, "h")); // insert

		/**
		 * Quan inciem el programa, marquem amb setFirstRow() quina es la
		 * primera fila de l'editor. A continuació la guardem amb getFirstRow()
		 */
		filacol();
		currentrow = model.getRow();
		currentcol = model.getColumn();
		model.setFirstRow();
		int firstrow = model.getFirstRow();

		while (cr != CTRLD) {

			/**
			 * Cada cop que escrivim, guardem la posició del cursor amb getRow()
			 * i getCol()
			 */
			filacol();
			currentrow = model.getRow();
			currentcol = model.getColumn();
			cr = read();
			if (32 < cr && cr < 255 && cr != 127) {
				
				model.AddColumn(1); 
				str = Character.toString((char) cr);
				System.out.print(str);
			} else if (cr == CTRLS) {
				if (aux) {
					String[] cmd = { "bash", "-c", "tput rmul > /dev/tty" };
					executarComanda(cmd);
					aux = false;
				} else {
					String[] cmd = { "bash", "-c", "tput smul > /dev/tty" };
					executarComanda(cmd);
					aux = true;
				}
			}

			else if (cr == SPACE) {
				model.AddColumn(1);
				System.out.print(String.format("%c[%d%s", escCode, 1, "@"));
				System.out.print(String.format("%c[%d%s", escCode, 1, "C"));

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

			else if (cr == UP_ARROW && model.getMap().containsKey(currentrow-1)) {
				if (currentrow > firstrow) {
					System.out.print(String.format("%c[%d%s", escCode, 1, "A"));
					if (currentcol > model.getMaxColumn((currentrow - 1))) {
						System.out.print(String.format("%c[%d%s", escCode,
								model.getMaxColumn(currentrow - 1), "G"));
					}
				}

				/**
				 * Igual que UP_ARROW però mirant que no ens passem de la fila
				 * màxima
				 */

			} else if (cr == DOWN_ARROW && model.getMap().containsKey(currentrow+1)) {
				if (currentrow < model.getLastRow()) {
					System.out.print(String.format("%c[%d%s", escCode, 1, "B"));
					if (currentcol > model.getMaxColumn((currentrow + 1))) {
						System.out.print(String.format("%c[%d%s", escCode,
								model.getMaxColumn(currentrow + 1), "G"));
					}
				}

				/**
				 * Si no estem a la columna maxima de la fila, movem cursor a la
				 * dreta i punto. Si estem a la ultima columna (però NO a la
				 * ultima fila), movem cursor a la fila següent, columna 1
				 */

			} else if (cr == RIGHT_ARROW) {
				if (currentcol <= model.getMaxColumn(currentrow) && model.getMaxColumn(currentrow) > 0) {
					System.out.print(String.format("%c[%d%s", escCode, 1, "C"));
				} else if (currentrow != model.getLastRow() && model.getMap().containsKey(currentrow+1)) {
					System.out.print(String.format("%c[%d%s", escCode, 1, "E"));
				}

				/**
				 * Igual que abans, però per canviar de fila ho fem en dos
				 * passos (també es pot fer en un però no funcionava). Primer
				 * movem cursor a la fila de dalt i despres el movem a la dreta
				 * fins la columna màxima.
				 */

			} else if (cr == LEFT_ARROW) {
				if (currentcol > 1) {
					System.out.print(String.format("%c[%d%s", escCode, 1, "D"));
				} else if (currentrow > firstrow && model.getMap().containsKey(currentrow-1)) {
					System.out.print(String.format("%c[%d%s", escCode, 1, "A"));
					if (model.getMaxColumn(currentrow-1) > 0) {
						System.out.print(String.format("%c[%d%s", escCode, model.getMaxColumn(model.getRow()-1), "C"));
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

			} else if (cr == DELETE) {
				

				if (currentcol == 1 && currentrow > firstrow) {
					System.out.print(String.format("%c[%d%s", escCode, 1, "A"));
					System.out.print(String.format("%c[%d%s", escCode, model.getMaxColumn(currentrow-1)+1, "G"));
					if (model.getMaxColumn(currentrow) == 0 && currentrow == model.getLastRow()) {
						model.DownColumn();
					}
				}
				else {
					System.out.print(String.format("%c[%d%s", escCode, 1, "D"));
					System.out.print(String.format("%c[%d%s", escCode, 1, "P"));
					model.DownColumn();
				}		

				/**
				 * Borra caràcters de la dreta i els va movent. Decrementa número
				 * de columnes només si borra un caràcter
				 */

			} else if (cr == SUPRIMIR) {
				System.out.print(String.format("%c[%d%s", escCode, 1, "P"));
				if (model.getMaxColumn(currentrow) > 0 && currentcol <= model.getMaxColumn(currentrow)) {
					model.DownColumn();
				}
				
				/**
				 * HOME i END són iguals que sempre, molt bàsic 
				 */
				
			} else if (cr == HOME) {
				System.out.print(String.format("%c[%d%s", escCode, 1, "G"));
				
			} else if (cr == END) {
				System.out.print(String.format("%c[%d%s", escCode,
						model.getMaxColumn(currentrow) + 1, "G"));

				/**
				 * ENTER només funciona si en fem un i escrivim algo. Si fem dos
				 * ENTERS (deixant una fila en blanc) NO està implementat
				 */
			} else if (cr == ENTER) {
				System.out.print(String.format("%c[%d%s", escCode, 1, "E"));
				rowcol.filacol();
				if (!model.getMap().containsKey(model.getRow())) {
					model.AddColumn(1);
				}
				
			} else if (cr == INSERT) {
				if (sobreescrivint) {
					System.out.print(String.format("%c[%d%s", escCode, 4, "h")); // insert
					sobreescrivint = false;
				}else {
					System.out.print(String.format("%c[%d%s", escCode, 4, "l")); // sobrescriure
																				//amb la l es veu que es resetegen parametres
					sobreescrivint = true;
				}
				
			}
			else if (cr == FIRSTROW) {
				System.out.print(String.format("%c[%d%s", escCode, model.getFirstRow(), "H"));
			}
			
			else if (cr == LASTROW) {
				System.out.print(String.format("%c[%d%s", escCode, model.getLastRow(), "H"));				
				System.out.print(String.format("%c[%d%s", escCode, model.getMaxColumn(model.getLastRow())+1, "G"));
			}
			
			else if (cr == TAB) {
				System.out.print(String.format("%c[%d%s", escCode, 4, "@"));
				System.out.print(String.format("%c[%d%s", escCode, 4, "C"));
				model.AddColumn(4);			
			}
			
			else if (cr == DELROW) {
				
				System.out.print(String.format("%c[%d%s", escCode, 2, "K"));
				if (currentrow == model.getLastRow() && currentrow != model.getFirstRow()) {
					System.out.print(String.format("%c[%d%s", escCode, 1, "A"));
					System.out.print(String.format("%c[%d%s", escCode, model.getMaxColumn(currentrow-1)+1, "G"));
					model.removeRow();
				}
				
				else {
					System.out.print(String.format("%c[%d%s", escCode, 1, "G"));
					model.setRowtoZero();
				}			
			}

		}
		return str;
	}
	
	public int read() throws IOException {
		int cr = 0;
		int valor_final;
		int aux, aux2, aux3;
		cr = super.read();
		if (cr == ESC) {
			aux = super.read();
			if (aux == CORXET) {
				aux2 = super.read();
				if (aux2 == 65 || aux2 == 66 || aux2 == 67 || aux2 == 68) {
					cr = cr + aux + aux2 + 117;
				}

				else if (aux2 == 51) {
					aux3 = super.read();
					if (aux3 == 126) {
						cr = SUPRIMIR;
					}

				} else if (aux2 == 70) {
					cr = END;
				} else if (aux2 == 72) {
					cr = HOME;
				} else if (aux2 == 50) {
					aux3 = super.read();
					if (aux3 == 126) {
						cr = INSERT;
					}
				}
			}
		}
		valor_final = cr;
		return valor_final;
	}

	public void setRaw() {
		String[] cmd = { "/bin/sh", "-c", "stty -echo raw </dev/tty" };
		executarComanda(cmd);
	}

	public void unsetRaw() {
		String[] cmd = { "/bin/sh", "-c", "stty echo cooked </dev/tty" };
		executarComanda(cmd);
	}

	private void executarComanda(String[] command) {
		try {
			Runtime.getRuntime().exec(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
