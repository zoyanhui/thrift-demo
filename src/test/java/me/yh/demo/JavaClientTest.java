package me.yh.demo;

import junit.framework.TestCase;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import tutorial.Calculator;

/**
 * Created by Administrator on 2016/5/10 0010.
 */
public class JavaClientTest extends TestCase {

    private JavaClient javaClient;

    @Override
    public void setUp() throws Exception{
        javaClient = new JavaClient();
    }

    public void testPerform() throws Exception{
        String[] args = {"simple"};
        if (args.length != 1) {
            System.out.println("Please enter 'simple' or 'secure'");
            System.exit(0);
        }

        try {
            TTransport transport;
            if (args[0].contains("simple")) {
                transport = new TSocket("localhost", 9090);
                transport.open();
            }
            else {
        /*
         * Similar to the server, you can use the parameters to setup client parameters or
         * use the default settings. On the client side, you will need a TrustStore which
         * contains the trusted certificate along with the public key.
         * For this example it's a self-signed cert.
         */
                TSSLTransportFactory.TSSLTransportParameters params = new TSSLTransportFactory.TSSLTransportParameters();

                params.setTrustStore(".truststore", "thrift", "SunX509", "JKS");
        /*
         * Get a client transport instead of a server transport. The connection is opened on
         * invocation of the factory method, no need to specifically call open()
         */
                transport = TSSLTransportFactory.getClientSocket("localhost", 9091, 0, params);
            }

            TProtocol protocol = new TBinaryProtocol(transport);
            Calculator.Client client = new Calculator.Client(protocol);

            javaClient.perform(client);

            transport.close();
        } catch (TException x) {
            x.printStackTrace();
        }
    }


}