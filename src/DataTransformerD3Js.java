import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class DataTransformerD3Js {
  public static void main(String[] args) throws IOException {
    List<Data> list = readData(new File("data/data.txt"));
    MultiMap<Data, Data> multiMap = new MultiMap<Data, Data>();
    
    Data previousRow = list.get(0);
    
    for(Data data : list) {
      for(Data data1 : list) {
        if(data1.isParent(data.key)){
          multiMap.insertValue(data, data1);
        }
      }
    }
 
    for(Data data : multiMap.keySet()){
      List<Data> child = multiMap.getValue(data);
      
      if(child != null && child.size() > 0){
        data.addChild(child);
      }
    }
    
    for(Data data : list) {
      if(data.emptyChild()) continue;
     // System.out.printf("%s ==> < %s >\n", data.key, Arrays.asList(data.child));
    }
     
    System.out.println(list.get(0).child.toString());
  }
  
  

  private static List<Data> readData(File fin) throws IOException {
    FileInputStream fis = new FileInputStream(fin);

    // Construct BufferedReader from InputStreamReader
    BufferedReader br = new BufferedReader(new InputStreamReader(fis));

    String line = null;
    List<Data> list = new ArrayList<Data>();
    
    while ((line = br.readLine()) != null) {
      //System.out.printf("\n-----%s-----\n", line );
      list.add(new Data(getNumberAndName(line)));
    }

    br.close();  
    
    return list;
  }
  
 
  
  public static String getNumber(String str){
    Pattern p = Pattern.compile ("[+-]?([0-9]*[.])?[0-9]+");
    //Pattern p = Pattern.compile("[-+]?[0-9]\\.[0-9]");   //("[-+]?\\d\\.\\d|\\d");
    Matcher m = p.matcher(str);
    
    StringBuilder sb = new StringBuilder();
    
    while (m.find()) {
      sb.append(m.group());
    }
    //System.out.println(sb.toString());
    return sb.toString();
  }
  
  public static String getName(String str){
    String number = getNumber(str);
 
    String[] arr = str.split(number);
    
    if(arr != null && arr.length > 1){
       return arr[1].trim();
    }
    
    return null;
  }
  
  public static String[] getNumberAndName(String str){
    String number = getNumber(str);
    String name = getName(str);
    
    String[] numberAndName = new String[2];
    
    if(number != null && number.trim().length() > 0) {
       numberAndName[0] = number;
       numberAndName[1] = name;
    }
    
    return numberAndName;
  }
  
  
  static class Data {
    String key;
    String value;

    List<Data> child;
    
    public Data(String[] kv){
      key = kv[0];
      value = kv[1];
      child = new ArrayList<Data>();
    }
    
    void addChild(List<Data> c){
      child.addAll(c);
    }
    
    boolean emptyChild(){
      return child == null || child.size() == 0;  
    }
    
    boolean isParent(String otherKey) {
      if(otherKey != null && key != null && key.startsWith(otherKey)){
        String s = StringUtils.replaceOnce(key, otherKey, "");
        if(s.length() == 2){
          return true;
        }
      }
      
      return false;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      result = prime * result + ((value == null) ? 0 : value.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Data other = (Data) obj;
      if (key == null) {
        if (other.key != null)
          return false;
      } else if (!key.equals(other.key))
        return false;
      if (value == null) {
        if (other.value != null)
          return false;
      } else if (!value.equals(other.value))
        return false;
      return true;
    }

    
    private String getChildJson(){
      StringBuilder str = new StringBuilder();
      
      int max = child.size();
      int i = 0; 
      for(Data data : child){
        str.append(data.toString());
        ++i;
        
        if(i >= 1 && i < max){
          str.append(",");
        }
      }
      
      return str.toString();
    }
    
    
     @Override
     public String toString(){
       String husbandAndWifeName = value;
       String husbandName = value;
       String wifeName = "";
       
       if(husbandAndWifeName != null){
         String[] couple = husbandAndWifeName.split("&");
         if(couple != null && couple.length == 2){
           husbandName = StringUtils.trim(couple[0]);
           wifeName = StringUtils.trim(couple[1]);
         }
       }
       
       String format = "{\"husband\": \"%s\", \"wife\": \"%s\", \"photo\": \"images/icon-person.png\", \"children\": [%s] }";
       return String.format(format, husbandName, wifeName, getChildJson());
     }
    
  }  
}


