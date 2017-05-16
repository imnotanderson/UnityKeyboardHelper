package unitykeyboardhelper;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.unity3d.player.UnityPlayer;

public class Helper {
	static Activity unityActivity;
	static MyEdit editText;
	static UnityPlayer mUnityPlayer;

	public static void Init() {

		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ViewInit();
			}
		});
	}

	static void ViewInit() {
		Log.d("xxxx", "kbinit");
		unityActivity = com.unity3d.player.UnityPlayer.currentActivity;
		Context context = unityActivity.getApplicationContext();
		Field field;
		mUnityPlayer = null;
		try {
			field = unityActivity.getClass().getDeclaredField("mUnityPlayer");
			field.setAccessible(true);
			mUnityPlayer = (UnityPlayer) field.get(unityActivity);
			ViewGroup vg = (ViewGroup) mUnityPlayer.getParent();
			((ViewGroup) mUnityPlayer.getParent()).removeView(mUnityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		//LinearLayout linearLayout = new LinearLayout(context);
		
		RelativeLayout linearLayout = new RelativeLayout(context);
		editText = new MyEdit(context);

		editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI
				| EditorInfo.IME_ACTION_DONE);

		editText.addTextChangedListener(new MyTextChangeListener());

		linearLayout.addView(editText);
		linearLayout.addView(mUnityPlayer.getView());

		unityActivity.setContentView(linearLayout);
		mUnityPlayer.requestFocus();

		unityActivity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		// /
		linearLayout.getRootView().getViewTreeObserver()
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						// TODO Auto-generated method stub

						View root = mUnityPlayer.getRootView();
						int heightDiff = root.getRootView().getHeight()
								- root.getHeight();
						Rect r = new Rect();
						View view = unityActivity.getWindow().getDecorView();
						view.getWindowVisibleDisplayFrame(r);
						float perc = 1 - r.height() * 1.00f
								/ root.getRootView().getHeight();
						SendToUnityPerc(perc + "");
					}
				});
	}

	static void SendToUnityPerc(String args) {
		UnityPlayer.UnitySendMessage("[UnityKeyboardHelper]", "Recv", args);
	}

	static void SendToUnityChar(String args) {
		UnityPlayer.UnitySendMessage("[UnityKeyboardHelper]", "RecvTextChange",
				args);
	}

	public static void Show(final String str) {

		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				editText.setText(str);
				editText.setSelection(editText.length());
				// TODO Auto-generated method stub
				ShowKeyboard();
			}
		});
	}

	static void ShowKeyboard() {
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) unityActivity
						.getApplicationContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

			}
		}, 0);
	}

	public static void HideKeyboard(){
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
		    InputMethodManager imm = (InputMethodManager) unityActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(unityActivity.getCurrentFocus().getWindowToken(), 0);
			}
		});
	}
	public static void SetTextValue(final String s) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				editText.setText(s);
				editText.setSelection(start, end);
			}
		});
	}

	static int start = 0, end = 0;

	public static void SetSelect(final int start, final int end) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Helper.start = start;
				Helper.end = end;
				editText.setSelection(start, end);
			}
		});
	}

	static class MyTextChangeListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			SendToUnityChar(s + "");
		}

	}

}
