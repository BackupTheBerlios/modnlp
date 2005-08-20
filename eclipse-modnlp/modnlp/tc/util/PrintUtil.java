package modnlp.tc.util;
/** 
 *  Print and display utilities
 *
 * @author  Saturnino Luz &#60;luzs@acm.org&#62;
 * @version <font size=-1>$Id: PrintUtil.java,v 1.1 2005/08/20 12:48:30 druid Exp $</font>
 * @see  
*/
public class PrintUtil {

  private static int prevsize = 0;

  public static void resetCounter ()
  {
    prevsize = 0;
  }

  public static void donePrinting(){
    System.err.println("...done");
    resetCounter();
  }

  public static void printNoMove(String header, int counter)
  {
    if ( prevsize == 0 )
      System.err.print(header+counter);
    else {
      for ( int i = 0; i < prevsize ; i++)
        System.err.print("\b");
      System.err.print(counter);
    }
    prevsize = (new String(counter+"")).length();
    //for (int i = 0 ; i < 100000; i++)
    //  System.out.print("");
  }
}
