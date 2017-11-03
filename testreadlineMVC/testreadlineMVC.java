import java.io.IOException;
import java.io.InputStreamReader;

public class testreadlineMVC {

	/**
	 * @param args
	 */

	
	public static void main(String[] args) {
		
		Model_Line model = new Model_Line();
		Controller_EditableBufferedReader controller = new Controller_EditableBufferedReader(new InputStreamReader(System.in));
		View_Console view = new View_Console(model);
		model.addObserver(view);
		
		controller.addModel(model);
		controller.addView(view);
		
		controller.setRaw();
        String str= null;
        try {
                str = controller.readLine();
            } catch(IOException e) {
                e.printStackTrace();
            }
        System.out.print(str);
        controller.unsetRaw();     

	}

}
