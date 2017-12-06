package com.javabrown.test.jna;

import com.sun.jna.platform.win32.OpenGL32Util;
import static com.sun.jna.platform.
import com.sun.jna.platform.win32.Kernel32Util;

public class Test1 {
   public static void main(String[] args){
     System.out.printf("Computer Name = %s\n", Kernel32Util.getComputerName());
     System.out.printf("Count GPUSNV = %s\n",  OpenGL32Util.countGpusNV()); 
   }
}

