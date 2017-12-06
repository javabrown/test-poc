package com.javabrown.test.jna;
 
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.sun.jna.win32.StdCallLibrary;
 
public class KillAllNotePad {
  public static void main(String[] args) {
    getIterateAllWindow();
    
    /*for (;;) {
        //HWND hwnd = User32.INSTANCE.FindWindow("Notepad", null);
        HWND hwnd = StdLibrary.INSTANCE.FindWindow("Notepad", null);
        if (hwnd == null) {
           System.out.println("No Notepad instance detected");
           break;
        }
        else{
           System.out.println("Notepad instance found.");
           StdLibrary.INSTANCE.PostMessage(hwnd, WinUser.WM_QUIT, null, null);
        }
     }*/
  }
  

  
  public static final String getTitle(User32 user32, HWND hWnd){
    char[] windowText = new char[512];
    user32.GetWindowText(hWnd, windowText, 512);
    String wText = Native.toString(windowText);
    
    return wText;
  }

  public static void getIterateAllWindow(){
    final User32 user32 = StdLibrary.INSTANCE;
   
    user32.EnumWindows(new WNDENUMPROC() {
      int count = 0;
      
      @Override
      public boolean callback(HWND hWnd, Pointer arg1) {
         byte[] windowText = new byte[512];
          
         String wText = getTitle(user32, hWnd);
         System.out.println("Found window with text " + hWnd + ", total "
            + ++count + " Text: " + wText);
         return true;
      }
   }, null);    
  }
  
   
  public interface StdLibrary extends StdCallLibrary {
    User32 INSTANCE = User32.INSTANCE;

    HWND FindWindow(java.lang.String arg0, java.lang.String arg1);

    boolean EnumWindows(WinUser.WNDENUMPROC lpEnumFunc, Pointer arg);

    int GetWindowTextA(HWND hWnd, byte[] lpString, int nMaxCount);

    //void PostMessage(com.sun.jna.platform.win32.WinDef.HWND arg0, int arg1,
    //    com.sun.jna.platform.win32.WinDef.WPARAM arg2,
    //    com.sun.jna.platform.win32.WinDef.LPARAM arg3);
  } 
  
  
}