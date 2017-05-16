 //#define DEBUG

using UnityEngine;
using System.Collections;
using System.Runtime.InteropServices;
using System;


public class UnityKeyboardHelper :MonoBehaviour{

#if UNITY_IOS
    [DllImport("__Internal")]
	static extern void _Init (int mode);
#elif UNITY_ANDROID && !UNITY_EDITOR || DEBUG
    static void _Init(int mode) {
        Debug.Log("android_init");
        new AndroidJavaClass("unitykeyboardhelper.Helper").CallStatic("Init");
        Debug.Log("android_init_ok");
    }
#else
    static void _Init(int mode) { }
#endif


    public enum MODE{
		PUSH_SCREEN = 0,
		RECV_PERC = 1,
	}
	static GameObject recvGo = null;
	Action<float>recvPerc = null;

	public static void Init(MODE mode,Action<float>recvPerc){
		if (recvGo != null)
			return;
		_Init ((int)mode);
		recvGo = new GameObject ("[UnityKeyboardHelper]");
        recvGo.hideFlags = HideFlags.HideInHierarchy;
		recvGo.AddComponent<UnityKeyboardHelper> ().recvPerc = recvPerc;
		DontDestroyOnLoad (recvGo);
	}
    //android
    public static void SetTextChangeListener(Action<string> textChangeListener)
    {
        UnityKeyboardHelper.textChangeListener = textChangeListener;
    }

    public static void Hide() {
#if UNITY_ANDROID && !UNITY_EDITOR
        new AndroidJavaClass("unitykeyboardhelper.Helper").CallStatic("HideKeyboard");
#endif
    }

    public static void Show(string str)
    {
#if UNITY_ANDROID && !UNITY_EDITOR
        new AndroidJavaClass("unitykeyboardhelper.Helper").CallStatic("Show",str);
#endif
    }

    public void Recv(string str){
		Debug.Log ("Recv perc:"+str);
		try {
			var prec = float.Parse (str);
			recvPerc (prec);
		} catch {
		}
	}
    
    static Action<string> textChangeListener = null;
    public void RecvTextChange(string str)
    {
        Debug.Log("RecvTextChange:" + str);
        if (textChangeListener != null)
        {
            textChangeListener(str);
        }
    }

    public static void SetTextValue(string str)
    {
#if UNITY_ANDROID && !UNITY_EDITOR || DEBUG
        new AndroidJavaClass("unitykeyboardhelper.Helper").CallStatic("SetTextValue", str);
#endif
    }


    public static void SetSelect(int start,int end)
    {
#if UNITY_ANDROID && !UNITY_EDITOR || DEBUG
    //    Debug.Log("SetSelect:" + start + ":" + end);
     //   new AndroidJavaClass("unitykeyboardhelper.Helper").CallStatic("SetSelect", start,end);
#endif
    }


}
