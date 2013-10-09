
/*
 * Copyright 2013 Centiro
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package centiro.soapui.teststeps.io.writesocket;

import centiro.soapui.teststeps.base.TestStepBase;
import centiro.soapui.teststeps.IconFileNames;
import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.model.support.DefaultTestStepProperty;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;

import java.io.PrintWriter;
import java.net.*;

public class WriteSocketTestStep extends TestStepBase {

    public static final String IP_ADDRESS = "ipAddress";
    public static final String PORT_NUMBER = "portNumber";
    public static final String CONTENT = "content";
    public static final String USE_STXETX = "useSTXETX";

    protected WriteSocketTestStep(WsdlTestCase testCase, TestStepConfig config,  boolean forLoadTest) {
        super(testCase, config, true, forLoadTest);
    }

    @Override
    protected String getIconFileName() {
        return IconFileNames.WRITE_SOCKET;
    }

    protected String getFailedIconFileName()
    {
        return IconFileNames.WRITE_SOCKET_ERROR;
    }

    protected String getSucceededIconFileName()
    {
        return IconFileNames.WRITE_SOCKET_OK;
    }

    @Override
    protected void addCustomProperties() {
        addProperty(new DefaultTestStepProperty(IP_ADDRESS, false, this), true);
        addProperty(new DefaultTestStepProperty(PORT_NUMBER, false, this), true);
        addProperty(new DefaultTestStepProperty(CONTENT, false, this), true);
        addProperty(new DefaultTestStepProperty(USE_STXETX, false, this), true);

    }

    @Override
    protected void customRun(TestCaseRunner testCaseRunner, TestCaseRunContext context) throws Exception {
        Socket socket;
        PrintWriter writer;

        String host = expandPropertyValue(context, IP_ADDRESS);
        int port = Integer.parseInt(expandPropertyValue(context, PORT_NUMBER));
        SoapUI.log(String.format("Connecting to host %s, post %s", host, port));
        socket = new Socket(host, port );
        SoapUI.log(String.format("Connected to host %s, post %s", host, port));
        writer = new PrintWriter(socket.getOutputStream(), true);
        writeMessage(writer, expandPropertyValue(context, CONTENT),getPropertyValueAsBool(USE_STXETX));
        writer.close();
        socket.close();
        SoapUI.log(String.format("Socket closed."));
    }

    private void writeMessage(PrintWriter writer, String message, Boolean useStxEtx) throws Exception {

        if ( message==null || message.isEmpty())
            throw new Exception("Nothing to send!");

        if (useStxEtx)
        {
            writer.print(SocketMessageParts.rsStartOfTransmission);
            writer.flush();
        }

        writer.print(String.format("%s%s%s",
                useStxEtx ? SocketMessageParts.STX : "",
                message,
                useStxEtx ?  SocketMessageParts.ETX : ""));

        SoapUI.log(useStxEtx ? "Using STX/ETX" : "Sending raw message, without STX/ETX");
        SoapUI.log(String.format("Socket: Wrote %s", message));

        writer.flush();
    }
}
