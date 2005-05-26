package modnlp.tc.tsr;
import modnlp.tc.parser.*;
import modnlp.tc.dstruct.*;
import modnlp.tc.util.*;
import java.io.*;
import java.util.Iterator;
import java.util.Set;
import java.util.Arrays;

/**
 * 
 * Term Space Reduction by Information Gain.  (see dimreduct-4up.pdf
 * and termextract-4up.pdf in the 4ict2 course web page)
 * 
 * @author  S. Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: InfoGain.java,v 1.1 2005/05/26 13:59:30 amaral Exp $</font>
 * @see  GenerateARFF
*/

public class InfoGain extends TermFilter
{

  public double computeLocalTermScore(String term, String cat){
    Probabilities p = pm.getProbabilities(term,cat);
    return
      Maths.xTimesLog2y(p.tc,   p.tc/(p.t * p.c)) +
      Maths.xTimesLog2y(p.ntc,  p.ntc/((1-p.t) * p.c)) +
      Maths.xTimesLog2y(p.tnc,  p.tnc/(p.t * (1-p.c))) +
      Maths.xTimesLog2y(p.ntnc, p.ntnc/((1-p.t) * (1-p.c)));
  }
}
      

