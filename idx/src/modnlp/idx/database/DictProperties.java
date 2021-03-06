/**
 *  � 2006 S Luz <luzs@cs.tcd.ie>
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
package modnlp.idx.database;

import java.io.InputStream;
/**
 *  Load dictionary defaults
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: DictProperties.java,v 1.2 2006/05/22 17:26:02 amaral Exp $</font>
 * @see  
*/
public class DictProperties extends java.util.Properties{

  public static String PROP_FNAME = "dictionary.properties";
  String envHome = "/tmp/dbv2";      // a very unsafe default;
  String wPosTableName = "poindex.db";  // word.fileno -> [pos1, pos2, ...]
  String wFilTableName = "wfindex.db";  // word -> [fileno1, fileno2, ...]
  String caseTableName = "caindex.db";  // canonicalform -> [form1, form2, ...]
  String freqTableName = "fqtable.db";  // word -> noofoccurrences
  String fileTableName = "fitable.db";  // fileno -> fileuri

	public DictProperties () 
	{
    super();
    try {
      ClassLoader cl = this.getClass().getClassLoader();
      InputStream fis = ((cl.getResource(PROP_FNAME))
                         .openConnection()).getInputStream();
       //FileInputStream fis = new FileInputStream(new File("tecli.properties"));
      this.load(fis);
      envHome = getProperty("dictionaty.environment.home");
      wPosTableName = getProperty("wposition.table.name");
      wFilTableName = getProperty("wfile.table.name");
      caseTableName = getProperty("case.table.name");
      freqTableName = getProperty("frequency.table.name");
      fileTableName = getProperty("file.table.name");
    }
    catch (Exception e) {
	    System.err.println("Error reading property file "+PROP_FNAME+": "+e);
	    System.err.println("Using defaults in DictProperties.java");
		}
	}

  public String getEnvHome () {
    return envHome;
  }

  public String getWPosTableName () {
    return wPosTableName;
  }

  public String getWFilTableName () {
    return wFilTableName;
  }

  public String getCaseTableName () {
    return caseTableName;
  }

  public String getFreqTableName () {
    return freqTableName;
  }

  public String getFileTableName () {
    return fileTableName;
  }

}
