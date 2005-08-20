package modnlp.tc.dstruct;
/**
 *  Record a 4-entry joint probability table for (Boolean) random vars
 *  term and category as well as the priors for p(term) and
 *  p(category)
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: Probabilities.java,v 1.1 2005/08/20 12:48:30 druid Exp $</font>
 * @see  
*/
public class Probabilities {
  public double tc;
  public double tnc;
  public double ntc;
  public double ntnc;
  public double t;
  public double c;

  public Probabilities(double pT,
                       double pC,
                       double pTAndC, 
                       double pTAnd_C, 
                       double p_TAndC, 
                       double p_TAnd_C
                       )
  {
    this.tc = pTAndC;
    this.tnc = pTAnd_C;
    this.ntc = p_TAndC;
    this.ntnc = p_TAnd_C;
    this.t = pT;
    this.c = pC;
  }

  public double getPTgivenC(){
    return  tc / c;
  }

  public double getPTgiven_C(){
    return  tnc / (1 - c);
  }  
}
