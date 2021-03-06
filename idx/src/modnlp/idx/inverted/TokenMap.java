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
package modnlp.idx.inverted;
import modnlp.idx.database.IntegerSet;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
/**
 *  In-memory hashmap of tokens (type as key) and their positions in a
 *  file
 *
 * @author  S Luz &#60;luzs@cs.tcd.ie&#62;
 * @version <font size=-1>$Id: TokenMap.java,v 1.2 2006/05/22 17:26:02 amaral Exp $</font>
 * @see  
*/

public class TokenMap extends HashMap {

  
  public void putPos (String type, int pos) {
    IntegerSet set = (IntegerSet) remove(type);
    if (set == null)
      set = new IntegerSet();
    set.add(new Integer(pos));
    put(type, set);
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    TreeMap tm = new TreeMap(this);
    for (Iterator e = tm.entrySet().iterator(); e.hasNext() ;)
			{
        Map.Entry kv = (Map.Entry) e.next();
        sb.append(kv.getKey()+" = ");
        IntegerSet set = (IntegerSet) kv.getValue();
        sb.append(set.toString());
        sb.append("\n");
      }
    return new String(sb);
  }

}
