package com.udemy.pki.core;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.udemy.pki.bean.CertificadoUdemy;

public class CertificateStore {
	//Con esto podemos obtener nuestro certificado digital desde archivo pfx o jks
	//codigo para extraer información de JKS
	public static CertificadoUdemy getCertificateFromFile(String path, String key) {
		
		CertificadoUdemy certificado = new CertificadoUdemy();
		try {
			//Abro archivo de keystore
			//se usa PKCS12 cuando se necesita la llave publica y privada
			KeyStore jks = KeyStore.getInstance("PKCS12");
			InputStream in = new FileInputStream(path);
			//extraigo el contenido y lo pongo en un array
			jks.load(in, key.toCharArray());
			in.close();
			//paso a una variable el contenido de aliases
			String aliasJks = jks.aliases().nextElement();
			//extrae llave privada
			PrivateKey pk = (PrivateKey) jks.getKey(aliasJks, key.toCharArray());
			//Se crea certificado para poder almacenar cadena de certificación
			Certificate[] chain = jks.getCertificateChain(aliasJks);
			//Obtener certificado principal (publico)
			X509Certificate oPublicCertificate = (X509Certificate) chain[0];
			//almacena toda la informacion que estamos procesando para usarlo en otro proceso
			certificado.setAlias(oPublicCertificate.getSubjectDN().getName());
			certificado.setPublicCertificate(oPublicCertificate);
			certificado.setPrivateKey(pk);
			certificado.setCertificateChain(chain);
			
		}catch (Exception e) {
			e.printStackTrace();
		}//catch
		
		return certificado;
		
	}//termina metodo
	
	//listar certificados en el almacen
	public static List<CertificadoUdemy> listCertificateFromStore() {
		List<CertificadoUdemy> listCertificadoUdemy = new ArrayList<>();
			try {
				//cargar instancia sin estandares de java, creando un almacen siendo el de windows
				KeyStore jks = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
				//carga
				jks.load(null, null);
				//recorrer todos los alias que se encuentren en windows
				Enumeration<String> en = jks.aliases();
				while (en.hasMoreElements()) {
					CertificadoUdemy certificado = new CertificadoUdemy();
					String aliasKey = (String) en.nextElement();
					
					//obtener llave privada
					PrivateKey pk = (PrivateKey) jks.getKey(aliasKey, null);
					//Se crea certificado para poder almacenar cadena de certificación
					Certificate[] chain = jks.getCertificateChain(aliasKey);
					//Obtener certificado principal (publico)
					X509Certificate oPublicCertificate = (X509Certificate) chain[0];
					
					certificado.setAlias(oPublicCertificate.getSubjectDN().getName());
					certificado.setPublicCertificate(oPublicCertificate);
					certificado.setPrivateKey(pk);
					certificado.setCertificateChain(chain);
					//metemos los certificados en un array
					listCertificadoUdemy.add(certificado);
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			return listCertificadoUdemy;
		}
	
}//termina class
