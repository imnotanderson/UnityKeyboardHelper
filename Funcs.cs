using UnityEngine;
using System.Collections;

public class Funcs : MonoBehaviour {

    void Awake() {
        Application.RegisterLogCallback((a, b, c) => {
            str += a + b + c + "\n";
        });
        UnityKeyboardHelper.Init(UnityKeyboardHelper.MODE.RECV_PERC,null);

        UnityKeyboardHelper.SetTextChangeListener(OnEditChange);
    }

    void OnEditChange(string str) {
        if (MyUIInput.current!=null)
        {
            if (MyUIInput.current.value != str)
            {
                MyUIInput.current.value = str;
            }
        }
    }
 
    string str = "";
    void OnGUI() { 
        GUILayout.Label(str);
    }

    public void Clear() {
        str = "";
    }

    public void Hide() {
        UnityKeyboardHelper.Hide();
    }
}
