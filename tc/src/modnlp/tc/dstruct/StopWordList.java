package modnlp.tc.dstruct;
import java.io.*;
import java.util.Vector;
/**
 *  List of stop words to be removed
 *
 * @author  S. Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: StopWordList.java,v 1.1 2005/05/26 13:59:30 amaral Exp $</font>
 * @see  
*/
public class StopWordList extends  Vector
{

  public StopWordList (String flist) 
  {
    super();
    try {
      BufferedReader in
        = new BufferedReader(new FileReader(flist));
      String word = null;
      while ( (word = in.readLine()) != null )
        {
          this.add(word.toLowerCase());
        }
    }
    catch (IOException e){
      System.err.println("Error reading stopword list "+flist);
      e.printStackTrace();
    }
  }

  public boolean contains (String tw ){
    return super.contains(tw.toLowerCase());
  } 
}
