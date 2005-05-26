package modnlp.tc.tsr;
import modnlp.tc.parser.*;
import modnlp.tc.dstruct.*;
import modnlp.tc.util.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Arrays;

/**
 * 
 * Term Space Reduction by GSS coefficient filtering (see
 * dimreduct-4up.pdf and termextract-4up.pdf [4ict2 web page])
 * 
 * @author  S. Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: GSScoefficient.java,v 1.1 2005/05/26 13:59:30 amaral Exp $</font>
 * @see  GenerateARFF
*/

public class GSScoefficient extends TermFilter
{


  public double computeLocalTermScore(String term, String cat){
    Probabilities p = pm.getProbabilities(term, cat);
    return (p.tc * p.ntnc) - (p.tnc * p.ntc);
  }

}
      
