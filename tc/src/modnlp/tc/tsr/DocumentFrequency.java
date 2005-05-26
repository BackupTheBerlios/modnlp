package modnlp.tc.tsr;
import modnlp.tc.parser.*;
import modnlp.tc.dstruct.*;
import modnlp.tc.util.PrintUtil;
import java.io.*;
import java.util.Arrays;

/**
 * 
 * A simple implementation of Term Space Reduction for Text
 * Categorisation based on frequency of documents in which a given
 * feature occurs...
 * 
 * @author  Saturnino Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: DocumentFrequency.java,v 1.1 2005/05/26 13:59:30 amaral Exp $</font>
 * @see  IndentationHandler
*/

public class DocumentFrequency extends TermFilter
{

  public double computeLocalTermScore(String term, String cat){
    Probabilities p = pm.getProbabilities(term, cat);
    return p.tc * ii.getCorpusSize();
  }

  public void computeGlobalDocFrequency() {
    ii.setFreqWordScoreArray(wsp);
  }

}
