package com.udemy.test;

import java.util.List;

import com.udemy.pki.bean.CertificadoUdemy;
import com.udemy.pki.core.CertificateStore;
import com.udemy.pki.util.Constante;

public class ServiceTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			//Con esto podemos obtener nuestro certificado digital desde archivo pfx o jks
			CertificadoUdemy certificado= CertificateStore.getCertificateFromFile(Constante.CERTIFICADO, Constante.CLAVE);
			System.out.println("/****************Obteniendo certificado de archivo pfx o jks****************/");
			System.out.println(certificado.getAlias());
			System.out.println("----------------------------------------------");
			System.out.println(certificado.getPrivateKey().getAlgorithm());
			System.out.println("----------------------------------------------");
			System.out.println(certificado.getPublicCertificate().toString());
			System.out.println("----------------------------------------------");
			
			//listar certificados en el almacen
			System.out.println("/****************Listando certificados registrados en el almacen de windows****************/");
			List<CertificadoUdemy> listCertificadoUdemy = CertificateStore.listCertificateFromStore();
			for (CertificadoUdemy certificadoUdemy : listCertificadoUdemy) {
				//imprime los alias
				System.out.println(certificadoUdemy.getAlias());
				//imprime quien emite el certificado
				//información del certificado publico
				System.out.println(certificadoUdemy.getPublicCertificate().getIssuerDN());
				System.out.println(certificadoUdemy.getPublicCertificate().getNotAfter());
				System.out.println("----------------------------------------------");
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
