
package test.Module1.clienthtml;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import org.omg.CORBA.*;

import com.borland.orbutil.httpgateway.*;
import test.Module1.*;

/**
* Template File
*   GatewayExecute.java.template
* IDL Object
*   Module1
* Generation Date
*   1999년 7월 28일 수요일 04시24분50초 오후
* IDL Source File
*   C:/WORK/ant/source/test/untitled1.idl
* Abstract
*   The base class that the servlet is derived from.
*/

public abstract
class Module1GatewayBase extends GatewayBase {

    private org.omg.CORBA.ORB _orb;

    public void init(ServletConfig config)
    throws ServletException
    {
       String [] args = null;
       System.getProperties().put("ORBagentPort", "14000");

       _orb = org.omg.CORBA.ORB.init(args,System.getProperties());
       super.init(config);
    }


  protected void execute(int interfaceId,int methodId,com.borland.orbutil.httpgateway.IGatewayRequest gr)  throws Exception
  {
     switch(interfaceId) {
         case 0:
           Interface1_execute(methodId,gr);
           break;
     }
  }


  //
  // interface Module1.Interface1
  //
  protected Module1.Interface1 Interface1_getInstance(com.borland.orbutil.httpgateway.IGatewayRequest gr) throws Exception
  {
    Module1.Interface1 _interface1 = null;
    try {
      /*
        First look in the session cache
      */
      _interface1 = (Module1.Interface1) gr.getInterface("Module1.Interface1",-1);
    }
    catch (Throwable t) {
    }
    /*
      Now look in the orb for an instance of the interface
    */
    if (_interface1 == null ) {
      String name = "Interface1";
      _interface1 = Module1.Interface1Helper.bind(_orb, name);
    }
    if (_interface1 != null ) {
      gr.storeInterface("Module1.Interface1",_interface1);
    }
    return _interface1;
  }

  protected void Interface1_execute(int methodId,com.borland.orbutil.httpgateway.IGatewayRequest gr) throws Exception
  {
    Module1.Interface1 _interface1 = Interface1_getInstance(gr);

    switch(methodId) {
      case 0:
        Interface1_operation1(gr,_interface1);
        break;
    }
  }

  abstract protected void Interface1_operation1(com.borland.orbutil.httpgateway.IGatewayRequest gr,Module1.Interface1 iFace) throws Exception;

}

