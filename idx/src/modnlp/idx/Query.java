/**
 *  © 2006 S Luz <luzs@cs.tcd.ie>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package modnlp.idx;

import modnlp.idx.database.Dictionary;
import modnlp.idx.query.WordQuery;


/**
 *  Print dictionary tables onto stdout (for testing purposes)
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: Query.java,v 1.1 2006/05/22 17:26:02 amaral Exp $</font>
 * @see  
*/
public class Query {
  private static boolean verbose = true;

  public static void main(String[] args) {
    Dictionary d = null;
    try {
      d = new Dictionary(false);
      d.printCorcordances(new WordQuery(args[0],d,true), 50, true, new java.io.PrintWriter(System.out));
      //d.dump();
      d.close();
    } // end try
    catch (Exception ex){
      if (d != null)
        d.close();
      System.err.println(ex);
      ex.printStackTrace();
      usage();
    }
  }



  public static void usage() {
    System.err.println("\nUSAGE: Query ");
    System.err.println("\tprint modnlp.idx.Dictionary tables to stdout");
    System.err.println("\tOptions:");
    System.err.println("\t\t-f       print frequency list");
    System.err.println("\t\t-q QUERY query dictionary");
  }
}
