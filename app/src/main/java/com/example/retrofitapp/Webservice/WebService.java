package com.example.retrofitapp.Webservice;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class WebService {

    public WebService() {
    }

    public String getChecksum(String OrdId,String CustId,String Amount){
        String resultString="";
        SoapObject soapObject=new SoapObject("http://tempuri.org/","GetPaytmSignature");
        soapObject.addProperty("OrdId",OrdId);
        soapObject.addProperty("CustId",CustId);
        soapObject.addProperty("Amount",Amount);
        SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(soapObject);
        SSLConnection.allowAllSSL();
        HttpTransportSE httpTransportSE=new HttpTransportSE("http://macmanws.envigil.net/paytmChecksum.asmx?WSDL",1000000000);
        try {
            httpTransportSE.call("http://tempuri.org"+"/"+"GetPaytmSignature",envelope);
            SoapObject result = (SoapObject) envelope.bodyIn;
            resultString=result.getProperty(0).toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
