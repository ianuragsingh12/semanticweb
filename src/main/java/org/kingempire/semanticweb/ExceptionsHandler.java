package org.kingempire.semanticweb;

/**
 *
 * @author Anurag Singh
 */
public class ExceptionsHandler {
//    @ExceptionHandler(Exception.class)

    public void handleAllError(Exception ex) {
        System.out.println("Exception Handled!");
        System.out.println(ex);
    }
}
