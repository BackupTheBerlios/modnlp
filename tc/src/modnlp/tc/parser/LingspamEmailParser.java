/** 
 * LingspamEmailParser
 * (S. Luz, luzs@cs.tcd.ie)
 **/ 
package modnlp.tc.parser;
import modnlp.tc.parser.*;
import modnlp.tc.dstruct.*;
import java.io.*;

/**
 * Parse a lingspam corpus files and store the results as a ParsedCorpus object.
 * (see I. Androutsopoulos, J. Koutsias, K.V. Chandrinos, George Paliouras, 
 * and C.D. Spyropoulos, "An Evaluation of Naive Bayesian Anti-Spam 
 * Filtering". In Potamias, G., Moustakis, V. and van Someren, M. (Eds.), 
 * Proceedings of the Workshop on Machine Learning in the New Information 
 * Age, 11th European Conference on Machine Learning (ECML 2000), 
 * Barcelona, Spain, pp. 9-17, 2000. for a description of the corpus;
 * The corpus is available at
 *      http://www.aueb.gr/users/ion/
 * 
 * @author  Saturnino Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: LingspamEmailParser.java,v 1.1 2005/05/26 13:59:30 amaral Exp $</font>
 * @see  
*/

public class LingspamEmailParser extends Parser
{

  /** 
   * parse: Set up parser object, perform parsing
   *
   * In lingspam, the category (spam or legit) is not stored with
   * the text but given by the file name ('spmmsg*.txt denote spam.)
   */
  public void  parse ()
  {
    parsedCorpus = new ParsedCorpus();
    ParsedDocument pni = new ParsedDocument();
    try {
      File f = new File(filename);
      String namenopath = f.getName();
      pni.setId(namenopath);
      pni.addCategory(getCategory(namenopath));
      BufferedReader in
        = new BufferedReader(new FileReader(filename));
      String line = null;
      while ( (line = in.readLine()) != null )
        {
          pni.addText(line);
        }
      parsedCorpus.addParsedDocument(pni);
      //System.out.println(parsedText);
    }
    catch (Exception e) 
      {
        System.err.println("Error parsing "+filename);
        e.printStackTrace();
      }
  }
  
  /**
   * In lingspam, the category (spam or legit) is not stored with
   * the text but given by the file name ('spmmsg*.txt denote spam.)
   */
  public static String getCategory(String fname){
    if (fname.startsWith("spmsg"))
      return "spam";
    else
      return "legit";
  }


  /**
   *  main method for test purposes.  
   *
   * Usage: LingspamEmailParser * FILENAME
   *
   * In lingspam, the category (spam or legit) is not stored with
   * the text but given by the file name ('spmmsg*.txt denote spam.)
   */
  public static void main(String[] args) {
    try {
      LingspamEmailParser f = new LingspamEmailParser();
      f.setFilename(args[0]);
      System.out.println(f.getParsedCorpus());
    }
    catch (Exception e){
      System.err.println("modnlp.tc.parser.LingspamEmailParser: ");
      System.err.println("Usage: LingspamEmailParser FILENAME");
      //e.printStackTrace();
    } 
  }  


}

