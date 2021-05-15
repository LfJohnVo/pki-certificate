package com.udemy.test;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.udemy.pki.bean.CertificadoUdemy;
import com.udemy.pki.core.CertificateStore;
import com.udemy.pki.core.XadesFirma;
import com.udemy.pki.util.Constante;

public class XMLTest {

	public static void main(String[] args) {
		try {
			CertificadoUdemy certificado= CertificateStore.getCertificateFromFile(Constante.CERTIFICADO, Constante.CLAVE);
			Path path = Paths.get(Constante.XML);
			byte[] documento = Files.readAllBytes(path);
			documento = XadesFirma.firmaXmlBasico(documento, certificado);
			FileOutputStream out = new FileOutputStream(Constante.XML_FIRMADO);
			out.write(documento);
			out.close();
			System.out.println("xml firmado");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
