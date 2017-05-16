package unitykeyboardhelper;

import android.content.Context;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

class MyEdit extends EditText{
	
	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		// TODO Auto-generated method stub
		return  new ZanyInputConnection(super.onCreateInputConnection(outAttrs),true);
		//return super.onCreateInputConnection(outAttrs);
	}

	public MyEdit(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

	}
	
	
	  private class ZanyInputConnection extends InputConnectionWrapper {  
	        public ZanyInputConnection(InputConnection target, boolean mutable) {  
	            super(target, mutable);  
	        }  
	        @Override  
	        public boolean sendKeyEvent(KeyEvent event) {  
	            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {  
	            	MyEdit edit = Helper.editText;  
	                String text = edit.getText().toString();  
	                if(text.length() > 0){  
	                	String newText =text.substring(0, text.length()-1);
//	                	String newText = text;
//	                	if(Helper.start!=Helper.end){
//	                		newText = text.substring(0,Helper.start) + text.substring(Helper.end,  text.length() );
//	                	}else{
//	                		newText = text.substring(0,Helper.start-1) + text.substring(Helper.end,  text.length() );
//	                		Helper.start =Helper.start -1;
//	                		Helper.end = Helper.start;
//	                	} 
	                    edit.setText(newText);  
	                    Selection.setSelection(edit.getText(), newText.length());  
	                }  
	                return false;  
	            }  
	            return super.sendKeyEvent(event);  
	        }  
	    }  
}
