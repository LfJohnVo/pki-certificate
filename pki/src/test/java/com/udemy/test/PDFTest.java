package com.udemy.test;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.udemy.pki.bean.CertificadoUdemy;
import com.udemy.pki.core.CertificateStore;
import com.udemy.pki.core.PadesFirma;
import com.udemy.pki.util.Constante;

public class PDFTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			CertificadoUdemy certificado= CertificateStore.getCertificateFromFile(Constante.CERTIFICADO, Constante.CLAVE);
			Path path = Paths.get(Constante.PDF);
			byte[] documento = Files.readAllBytes(path);
			//linea para generar pdf basico
			//documento = PadesFirma.firmaPdfBasico(documento, certificado);
			//linea para generar firma avanzada
			documento = PadesFirma.firmarPdfAvanzado(documento, certificado);
			
			FileOutputStream out = new FileOutputStream(Constante.PDF_FIRMADO);
			out.write(documento);
			out.close();
			System.out.println("PDF firmado");
			
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
