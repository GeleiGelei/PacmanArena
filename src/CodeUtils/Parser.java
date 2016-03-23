
package CodeUtils;

/*
 * UTILITY CLASS CONTAINING ALL PARSING LOGIC TO HELP US ENCAPSULATE RELATED 
 * ACTIONS
 */

public class Parser {
    /* converts the class text into a more usable form
     * ex. 'class java.lang.String' -> 'String'
     */
    public static String parseForClassName(String c) {
        String ret = c.toString();
        ret = ret.substring(ret.lastIndexOf(".") + 1);
        
        return ret;
    }
}
