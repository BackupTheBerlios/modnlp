package modnlp.tc.tsr;
import modnlp.tc.parser.*;
import modnlp.tc.dstruct.*;
import modnlp.tc.util.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Arrays;

/**
 * 
 * Term Space Reduction by Odds Ratio filtering
 * (see dimreduct-4up.pdf and termextract-4up.pdf [4ict2 web page])
 * 
 * @author  S. Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: OddsRatio.java,v 1.1 2005/08/20 12:48:30 druid Exp $</font>
 * @see  GenerateARFF
*/

public class OddsRatio extends TermFilter
{

  public double computeLocalTermScore(String term, String cat){
    Probabilities p = pm.getProbabilities(term, cat);
    return Maths.safediv(p.getPTgivenC() * (1 - p.getPTgiven_C()),
                         p.getPTgiven_C() * (1 - p.getPTgivenC()));
  }
   

}
      
// System.err.println(wsp[i].getWord()+"\n p(t)    = "+p.t+"\n p(c)    = "+p.c+"\n p(tc)   = "+p.tc+"\n p(ntc)  = "+p.ntc+"\n p(tnc)  = "+p.tnc+"\n p(ntnc) = "+p.ntnc+"\n IG      = "+Maths.xTimesLog2y(p.tc,   p.tc/(p.t * p.c))+" + "+Maths.xTimesLog2y(p.ntc,  p.ntc/((1-p.t) * p.c))+" + "+Maths.xTimesLog2y(p.tnc,  p.tnc/(p.t * (1-p.c))) +" + "+Maths.xTimesLog2y(p.ntnc, p.ntnc/((1-p.t) * (1-p.c)))+" = "+ig); 
