package modnlp.tc.dstruct;
import modnlp.tc.tsr.*;
import modnlp.tc.parser.*;
import java.util.HashSet;
/**
 *  Term set for text
 * @author  Saturnino Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: SetOfWords.java,v 1.1 2005/05/26 13:59:30 amaral Exp $</font>
 * @see  
*/
public class SetOfWords extends HashSet{


  private boolean ignoreCase = true;

  public SetOfWords (String text)
  {
    super();
    addTokens(text);
  }

  public SetOfWords (String text, StopWordList swlist)
  {
    super();
    addTokens(text, swlist);
  }

  public void addTokens (String text, StopWordList swlist)
  {
    Tokenizer tkzr = new Tokenizer(text);
    while (tkzr.hasMoreTokens()){
      String t = tkzr.nextToken();
      //System.err.println("Adding "+t);
      if (!swlist.contains(t))
        addToken(t);
    }
  }

  
  public void addTokens(String text){
    Tokenizer tkzr = new Tokenizer(text);
    while (tkzr.hasMoreTokens())
      addToken(tkzr.nextToken());
  }

  private boolean addToken (String type)
  {
    String key = isIgnoreCase() ? 
      Tokenizer.fixType(type.toLowerCase()) : Tokenizer.fixType(type);
    if (  key.equals("") )
      return false;
    return add(key);
  }

  public void removeStopWords (StopWordList swl)
  {
    removeAll(swl);
  }

  /**
   * Get the value of ignoreCase.
   * @return value of ignoreCase.
   */
  public boolean isIgnoreCase() {
    return ignoreCase;
  }
  

  /**
   * Set the value of ignoreCase.
   * @param v  Value to assign to ignoreCase.
   */
  public void setIgnoreCase(boolean  v) {
    this.ignoreCase = v;
  }
  

}
