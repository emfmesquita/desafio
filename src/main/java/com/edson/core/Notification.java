package com.edson.core;

public class Notification {
	private String tipo;
	private String dataEnvio;
	private Parameters parametros;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(String dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public Parameters getParametros() {
		return parametros;
	}

	public void setParametros(Parameters parametros) {
		this.parametros = parametros;
	}

	public class Parameters {
		private String idProduto;
		private String idSku;

		public String getIdProduto() {
			return idProduto;
		}

		public void setIdProduto(String idProduto) {
			this.idProduto = idProduto;
		}

		public String getIdSku() {
			return idSku;
		}

		public void setIdSku(String idSku) {
			this.idSku = idSku;
		}
	}
}
