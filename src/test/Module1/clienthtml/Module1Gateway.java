
package test.Module1.clienthtml;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import com.borland.orbutil.httpgateway.*;
import test.Module1.*;

/**
* Template File
*   Gateway.java.template
* IDL Object
*   Module1
* Generation Date
*   1999년 7월 28일 수요일 04시24분50초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   Implements a servlet that is invoked from the HTML clinet.
*/



public class Module1Gateway extends Module1GatewayBase {


    public void init(ServletConfig config)
    throws ServletException
    {
       super.init(config);
    }


   public void doGet (HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
       super.doGet(req,res);
    }



/**
*   By default the doPost method will process the posted variables, fire any operations
*   and resolve data back into the database.
*   Then it will redirect the browser back to the original URL to get the updated page.
*   To redirect to any other page call res.sendRedirect() before the call below.
*/
   public void doPost (HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException
    {
       super.doPost(req,res);

       String referer = req.getHeader("Referer");
       if (referer != null) {
          res.sendRedirect(referer);
       }
    }



    protected String errorHandler(Throwable t) {
      /*
        Place error handler code here
        return null to prevent display of java stack trace on the browser
      */
      CharArrayWriter out = new CharArrayWriter();
      PrintWriter pw = new PrintWriter(out,true);
      t.printStackTrace(pw);
      return out.toString();
    }


    private String getUserName()
    {
      String userName = "";
      return userName;
    }

    private String getPassword()
    {
      String password = "";
      return password;
    }





/**
*   The following three methods are used to copy a rowset to the HTML client.
*   The data will appear on the client either as a HTML table or in a set of
*   javascript arrays that can be navigated, The javascripted code to navigate
*   and edit the data is supplied by the base class GatewayBase.
*   startgetData is called once per sequence. It will initialise ISequence
*   GetRowsetCell is called for every field on each row of the table
*   The number of rows sent to the browser at a time can be changed by setting pageSize
*   endSetData is called after the last cell
*/

    protected String startGetData(ISequence isequence,int structIndex,IGatewayRequest gr) throws Exception {
       /*
         offset is the current position within the rowset retrieved from the web page.
         It will be 0 on the first get
       */
       int offset = isequence.getOffset();
       boolean ok = true;

       switch (structIndex) {
       }
       return null;
    }

    protected String getRowsetCell(ISequence isequence,int structIndex,IGatewayRequest gr,int fieldIndex,int rowIndex, String fieldKey)
    {
       switch (structIndex) {

       }
       return ("Unknown "+ structIndex + " " + fieldIndex);
    }



    protected int getRowsetCellStatus(ISequence isequence,int structIndex,IGatewayRequest gr,int fieldIndex,int rowIndex, String fieldKey)
    {
       switch (structIndex) {

       }
       return 0;
    }


    protected String endGetData(ISequence isequence,int structIndex,IGatewayRequest gr) {
  /**
  *   Called after the last call to getRowSetCell
  */
       return null;
    }


    protected void startSetData(ISequence isequence,int structIndex,IGatewayRequest gr,int size) throws Exception {

       switch (structIndex) {
       }
    }

    protected void setRowsetCell(ISequence isequence,int structIndex,IGatewayRequest gr,int rowIndex,int fieldIndex,String value,int op)
    {
    }

    protected void setRowsetCellStatus(ISequence isequence,int structIndex,IGatewayRequest gr,int rowIndex,int fieldIndex,int status)
    {
    }


    protected void endSetData( ISequence rowsetInfo,int index,IGatewayRequest gr) throws Exception
    {
    }



   /*
     All operation calls
   */

   protected void Interface1_operation1(com.borland.orbutil.httpgateway.IGatewayRequest gr,Module1.Interface1 iFace) throws Exception
   {
    
    try {
     String ret = iFace.operation1(Integer.valueOf(gr.getOperationParam(0)).intValue(),Float.valueOf(gr.getOperationParam(1)).floatValue());
     /*
       Place the parameter values back into the HTML variables
     */
     gr.setOperationParam(0,gr.getOperationParam(0));
     gr.setOperationParam(1,gr.getOperationParam(1));
     gr.setOperationReturnValue(""+ ret);
    } catch (Exception e) {
    }
   }

}

