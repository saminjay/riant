package com.riantservices.riant.activities;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
public class CallSoap
{
    private final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    private final String SOAP_ADDRESS = "http://www.theeatwellplate.com/ReadyGo.asmx";

    //Register user
    public String Call(String a,String b, String c, String d)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,"UserInsert");
        PropertyInfo pi=new PropertyInfo();
        pi.setName("usr");
        pi.setValue(a);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("mob_no");
        pi.setValue(b);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("email");
        pi.setValue(c);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("pwd");
        pi.setValue(d);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response;
        try
        {
            httpTransport.call("http://tempuri.org/UserInsert", envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }

    //Check User
    public int Call(String a,String b)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,"chkUser");
        PropertyInfo pi=new PropertyInfo();
        pi.setName("usr");
        pi.setValue(a);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("pwd");
        pi.setValue(b);
        pi.setType(String.class);
        request.addProperty(pi);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response;
        try
        {
            httpTransport.call("http://tempuri.org/chkUser", envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return (int) response;
    }

    //Local Price Estimation
    public float Call(String a,int b, String c, String d)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,"price_estimate");
        PropertyInfo pi=new PropertyInfo();
        pi.setName("type");
        pi.setValue(a);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("distance");
        pi.setValue(c);
        pi.setType(String.class);
        request.addProperty(pi);

        pi=new PropertyInfo();
        pi.setName("travel_time");
        Log.d("TIME",d);
        pi.setValue(d);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        String response;
        try
        {
            httpTransport.call("http://tempuri.org/price_estimate", envelope);
            response = envelope.getResponse().toString();
        }
        catch (Exception exception)
        {
            Log.d("DEVIL",exception.toString());
            return 0;
        }
        Log.d("DEVIL",response);
        return  Float.parseFloat(response);
    }
}