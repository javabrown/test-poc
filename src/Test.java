
public class Test {
  public static void main(String[] args){
    String userLocalDirectory = "c:/test/";
    
    System.out.println(trimLastSlashChar(userLocalDirectory));
  }
  
  public static String trimLastSlashChar(String str) {
    if(str.endsWith("/")){
      str = trimLastChar(str, '/');
    }
    
    if(str.endsWith("\\")){
      str = trimLastChar(str, '\\');
    }
    
    return str;
  }
  
  public static String trimLastChar(String str, char c) {
    if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == c) {
        str = str.substring(0, str.length() - 1);
    }
    return str;
  }
  
}
