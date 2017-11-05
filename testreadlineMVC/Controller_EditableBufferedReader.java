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
					model.setCurrentColumn(filacols.get(2));
				} else {
					model.setCurrentColumn(Integer.parseInt(filacols.get(2)+ "" + filacols.get(3)));
				}
				break;
			case 2:
				model.setCurrentRow(Integer.parseInt(filacols.get(0)+ "" + filacols.get(1)));
				if (longitud == 4) {
					model.setCurrentColumn(filacols.get(3));
				} else if (longitud == 5) {
					model.setCurrentColumn(Integer.parseInt(filacols.get(3)+ "" + filacols.get(4)));
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
				
				model.setChar(cr);
				model.currentchar();
				
			} else if (cr == CTRLS) {
				
				model.ctrls();
				
			}else if (cr == SPACE) {
				
				model.space();
				
			}else if (cr == UP_ARROW) {
				
				model.up_arrow();

			} else if (cr == DOWN_ARROW) {
				
				model.down_arrow();

			} else if (cr == RIGHT_ARROW) {
				
				model.right_arrow();
				
			} else if (cr == LEFT_ARROW) {
				
				model.left_arrow();

			} else if (cr == DELETE) {
				
				model.delete();	

			} else if (cr == SUPRIMIR) {
				
				model.suprimir();
				
			} else if (cr == HOME) {
				model.home();
				
			} else if (cr == END) {
				model.end();

			} else if (cr == ENTER) {
				
				model.enter();
				
			} else if (cr == INSERT) {
				
				model.sobreescrivint();
				
			}else if (cr == FIRSTROW) {
				
				model.firstrow();
				
			}else if (cr == LASTROW) {
				
				model.lastrow();
				
			}else if (cr == TAB) {
				
				model.tab();		
				
			}else if (cr == DELROW) {
				
				model.delrow();
				
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
