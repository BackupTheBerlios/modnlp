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
package modnlp.idx.inverted;
import modnlp.util.*;

import java.net.URL;
import java.io.*;

/**
 *  Tokenise a chunk of text and record the position of each token
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: Tokeniser.java,v 1.1 2006/05/22 16:55:04 amaral Exp $</font>
 * @see  
*/
public abstract class Tokeniser {

  protected boolean tagIndexing = false; 
  protected boolean verbose = false; 
  protected String originalText;
  protected TokenMap tokenMap;

  public Tokeniser (String text) {
    originalText = text;
    tokenMap = new TokenMap();
  }

  public Tokeniser (URL url) throws IOException {
    BufferedReader in = 
      new BufferedReader(new InputStreamReader(url.openStream()));
    StringBuffer sb = new StringBuffer(in.readLine()+" ");
    String line = null;
    while ((line = in.readLine()) != null) {
      sb.append(line);
      sb.append(" ");
    }
    originalText = sb.toString();
    tokenMap = new TokenMap();
  }

  public Tokeniser (File file) throws IOException {
    BufferedReader in = 
      new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    StringBuffer sb = new StringBuffer(in.readLine()+" ");
    String line = null;
    while ((line = in.readLine()) != null) {
      sb.append(line);
      sb.append(" ");
    }
    originalText = sb.toString();
    tokenMap = new TokenMap();
  }

  public boolean getTagIndexing() {
    return tagIndexing;
  }

  public void setTagIndexing(boolean v) {
    tagIndexing = v;
  }

  public boolean getVerbose() {
    return verbose;
  }

  public void setVerbose(boolean v) {
    verbose = v;
  }

  public TokenMap getTokenMap() {
    return tokenMap;
  }

  abstract void tokenise ();
  /*
   *  Very basic tokenisation; Proper tokenisers must override this method.

 {
    int ct = 0;
    StringTokenizer st = new StringTokenizer(originalText);
    while (st.hasMoreTokens()){
      tokenMap.putPos(st.nextToken(), ct++);
      if (verbose)
        PrintUtil.printNoMove("Tokenising ...",ct);
    }
    if (verbose)
      PrintUtil.donePrinting();
  }
   */ 



}
