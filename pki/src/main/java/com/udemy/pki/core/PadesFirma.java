package com.udemy.pki.core;

import java.io.ByteArrayOutputStream;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
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
	
}
