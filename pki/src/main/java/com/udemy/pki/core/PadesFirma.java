package com.udemy.pki.core;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.udemy.pki.bean.CertificadoUdemy;

public class PadesFirma {
	//byte es el pdf a firmar y certificado el certificado
	public static byte[] firmaPdfBasico(byte[] data, CertificadoUdemy certificado) throws Exception {
		
		try {
			
			PdfReader reader = new PdfReader(data);
			ByteArrayOutputStream nuevoDocumento = new ByteArrayOutputStream();
			//Se inicia para crear la firma digital dentro del pdf
			PdfStamper stp = PdfStamper.createSignature(reader, nuevoDocumento, '\000', null, true);
			PdfSignatureAppearance sap = stp.getSignatureAppearance();
			
			//este proceso genera la firma por defecto
            //por lo tanto los datos no son tan especificos
			sap.setCrypto(certificado.getPrivateKey(), certificado.getCertificateChain(), null, PdfSignatureAppearance.WINCER_SIGNED);
			sap.setReason("Firma digital");
			sap.setLocation("Mexico");
			sap.setVisibleSignature(new Rectangle(100, 100, 350, 200), 1, "sig");
			stp.close();
			
			return nuevoDocumento.toByteArray();
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return null;
	}
	
	/*
	 * datadoc son los bits del documento
	 * Certificado es el certificado
	 * 
	 */
	public static byte[] firmarPdfAvanzado(byte[] dataDoc, CertificadoUdemy certificado) throws Exception {
		try {
			
			PdfReader reader = new PdfReader(dataDoc);
			ByteArrayOutputStream nuevoDocumento = new ByteArrayOutputStream();
			//Se inicia para crear la firma digital dentro del pdf
			PdfStamper stp = PdfStamper.createSignature(reader, nuevoDocumento, '\000', null, true);
			PdfSignatureAppearance sap = stp.getSignatureAppearance();
			//Se genera firma personalizada
			//aqui se usa adbe.pkcs7.detached porque no es necesario la clave privada internamente y nosotros lo podemos definir
			PdfSignature signature = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));
            signature.setReason("Firma Digital");
            signature.setLocation("M??xico");
			//definimos una fecha
            Date fechaFirma=new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaFirma);
            //a??adimos la fecha
            signature.setDate(new PdfDate(calendar));
            //setenamos los nuevos datos dentro de la firma
            sap.setSignDate(calendar);
            sap.setCryptoDictionary(signature);
            //definimos la firma visible y las propiedades personalizadas
            String firmado = "Firmado por " + PdfPKCS7.getSubjectFields(certificado.getPublicCertificate()).getField("CN");
            String razon = "Motivo: " + "Firma Digital";
            String lugar = "Lugar: " + "M??xico";
            String nombre = "Nombre: " + "Jonathan Vargas";
            String puesto = "Puesto: " + "Developer";
            SimpleDateFormat dateformatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");
            String fecha = "Fecha: " + dateformatter.format(fechaFirma);
            String firmaH = firmado + '\n' + razon + '\n' + lugar + '\n' + fecha + '\n' + nombre + '\n' + puesto;
            sap.setLayer2Text(firmaH);
            sap.setVisibleSignature(new Rectangle(100, 100, 350, 200), 1, null);
			//Aqui se genera el proceso de obtencion del hash
            int contentEstimated = 8192;
            System.out.println(reader.getFileLength());
            HashMap<PdfName, Integer> exc = new HashMap<>();
            exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
            sap.preClose(exc);
            //Aqui se hace el calculo para generar el algoritmo con sha-256
            InputStream data = sap.getRangeStream();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] buf = new byte[contentEstimated];
            int n;
            while ((n = data.read(buf)) > 0) {
                messageDigest.update(buf, 0, n);
            }
            
            //Aqui se obtiene hash despues de los calculos
            byte[] hash = messageDigest.digest();
            Calendar calendario = Calendar.getInstance();
            
            //Generamos la firma digital y se a??ade con el cifrado asimetrico
            PdfPKCS7 sgn = new PdfPKCS7(certificado.getPrivateKey(), certificado.getCertificateChain(), null, "SHA-256", null, false);
            byte[] sh = sgn.getAuthenticatedAttributeBytes(hash, calendario, null);
            sgn.update(sh, 0, sh.length);
            //se genera cadena firmada digitalmente
            byte[] encodedSig = sgn.getEncodedPKCS7(hash, calendario, null, null);
            
            //Se junta toda la informaci??n y se a??ade al pdf
            byte[] paddedSig = new byte[contentEstimated];
            System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
            PdfDictionary pdfDic = new PdfDictionary();
            pdfDic.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
            sap.close(pdfDic);
			
            reader.close();
            nuevoDocumento.flush();
            nuevoDocumento.close();
            
			return nuevoDocumento.toByteArray();

			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
