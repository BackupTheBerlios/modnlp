package modnlp.tc.evaluation;
/**
 *  Store maximum and minimum CSVs for a CSVTable
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: MaxMinCSV.java,v 1.1 2005/08/20 12:48:30 druid Exp $</font>
 * @see  
*/
public class MaxMinCSV {

  public double max;
  public double min;

  public MaxMinCSV(double mx, double mn){
    max = mx;
    min = mn;
  }
}
