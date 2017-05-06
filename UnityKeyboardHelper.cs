using UnityEngine;
using System.Collections;
using System.Runtime.InteropServices;
using System;

public class UnityKeyboardHelper :MonoBehaviour{

#if UNITY_IOS
	[DllImport("__Internal")]
	static extern void _Init (int mode);
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

	public void Recv(string str){
		Debug.Log (str);
		try {
			var prec = float.Parse (str);
			recvPerc (prec);
		} catch {
		}
	}
}
